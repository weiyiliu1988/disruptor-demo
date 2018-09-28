package top.tst.stdsp.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;

import top.tst.stdsp.disruptor.bean.TradeTransaction;
import top.tst.stdsp.disruptor.handler.TradeTransactionInDBHandler;

/**
 * 使用WorkerPool辅助创建消费者
 * 
 * @author Liuweiyi
 *
 */
public class Demo2 {

	public static void main(String[] args) throws InterruptedException {
		int BUFFER_SIZE = 1024;
		int THREAD_NUMBERS = 4;

		String id = "test";
		// 时间工厂 生成对象
		EventFactory<TradeTransaction> eventFactory = new EventFactory<TradeTransaction>() {

			@Override
			public TradeTransaction newInstance() {
				// 参数传入
				return new TradeTransaction(id, 1.1);
			}

		};

		RingBuffer<TradeTransaction> ringBuffer = RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);

		// 多生产者
		//RingBuffer<TradeTransaction> ringBuffer2 = RingBuffer.createMultiProducer(eventFactory, BUFFER_SIZE);

		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);
		WorkHandler<TradeTransaction> workHandlers = new TradeTransactionInDBHandler();

		WorkerPool<TradeTransaction> workerPool = new WorkerPool<TradeTransaction>(ringBuffer, sequenceBarrier,
				new IgnoreExceptionHandler(), workHandlers);

		workerPool.start(executor);

		for (int i = 0; i < 10500; i++) {
			long seq = ringBuffer.next();
			ringBuffer.get(seq).setPrice(i);
			ringBuffer.publish(seq);
		}

		Thread.sleep(1000);
		workerPool.halt();
		executor.shutdown();
	}
}
