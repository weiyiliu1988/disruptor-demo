package top.tst.stdsp.disruptor;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;

import top.tst.stdsp.disruptor.bean.TradeTransaction;
import top.tst.stdsp.disruptor.handler.TradeTransactionInDBHandler;

public class Demo1 {

	private String id = "test1";

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		int BUFFER_SIZE = 1024;
		int THREAD_NUMBERS = 4;

		final RingBuffer<TradeTransaction> ringBuffer = RingBuffer
				.createSingleProducer(new EventFactory<TradeTransaction>() {

					public TradeTransaction newInstance() {
						return new TradeTransaction();
					}

				}, BUFFER_SIZE, new YieldingWaitStrategy());

		// 创建线程池
		ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
		// 创建SequenceBarrier
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

		// 创建消息处理器(消费者)
		BatchEventProcessor<TradeTransaction> transProcessor = new BatchEventProcessor<TradeTransaction>(ringBuffer,
				sequenceBarrier, new TradeTransactionInDBHandler());

		// ringBuffer根据消费者状态
		ringBuffer.addGatingSequences(transProcessor.getSequence());

		// 把消息处理器提交到线程池
		executors.submit(transProcessor);

		Future<?> future = executors.submit(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				long seq;
				for (int i = 0; i < 1000; i++) {
					System.out.println("==ringBuffer指针===============>" + ringBuffer.getCursor());
					seq = ringBuffer.next();
					ringBuffer.get(seq).setPrice(i);
					ringBuffer.publish(seq);// 发布这个区块的数据使handler(consumer)可见
				}
				return null;
			}

		});

		future.get();
		Thread.sleep(1000);
		// 等上1秒，等消费都处理完成  
		transProcessor.halt();// 通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）  
		executors.shutdown();// 终止线程

	}

}
