package top.tst.stdsp.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import top.tst.stdsp.disruptor.bean.TradeTransaction;
import top.tst.stdsp.disruptor.demo3.consumer.TradeTransactionVasConsumer;
import top.tst.stdsp.disruptor.demo3.handler.TradeTransactionJMSNotifyHandler;
import top.tst.stdsp.disruptor.demo3.publisher.TradeTransactionPublisher;
import top.tst.stdsp.disruptor.handler.TradeTransactionInDBHandler;

//https://blog.csdn.net/xiaoxufox/article/details/50133839
public class Demo3 {

	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();

		int bufferSize = 1024;
		ExecutorService executor = Executors.newFixedThreadPool(4);
		// ThreadFactory factory = ThreadFacto

		Disruptor<TradeTransaction> disruptor = new Disruptor<TradeTransaction>(new EventFactory<TradeTransaction>() {

			@Override
			public TradeTransaction newInstance() {
				return new TradeTransaction();
			}

		}, bufferSize, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}

		}, ProducerType.SINGLE, new BusySpinWaitStrategy());

		// 利用disruptor创建消费者组C1,C2

		EventHandlerGroup<TradeTransaction> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasConsumer(),
				new TradeTransactionInDBHandler());

		TradeTransactionJMSNotifyHandler jmsConsumer = new TradeTransactionJMSNotifyHandler();
		handlerGroup.then(jmsConsumer);

		disruptor.start();
		CountDownLatch latch = new CountDownLatch(1);

		// 生产者准备
		executor.submit(new TradeTransactionPublisher(latch, disruptor));
		try {
			latch.await(); // 等待消费者处理完毕
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 等待生产者完事.  

		disruptor.shutdown();
		executor.shutdown();

		System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
	}
}
