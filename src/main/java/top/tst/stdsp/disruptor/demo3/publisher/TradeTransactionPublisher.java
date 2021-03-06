package top.tst.stdsp.disruptor.demo3.publisher;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.dsl.Disruptor;

import top.tst.stdsp.disruptor.bean.TradeTransaction;
import top.tst.stdsp.disruptor.demo3.translator.TradeTransactionEventTranslator;

public class TradeTransactionPublisher implements Runnable {

	Disruptor<TradeTransaction> disruptor;
	private CountDownLatch latch;

	private static int LOOP = 1_000;

	public TradeTransactionPublisher(CountDownLatch latch, Disruptor<TradeTransaction> disruptor) {
		this.disruptor = disruptor;
		this.latch = latch;
	}

	@Override
	public void run() {
		TradeTransactionEventTranslator tradeTranslator = new TradeTransactionEventTranslator();
		for (int i = 0; i < LOOP; i++) {

			// 执行逻辑启动
			disruptor.publishEvent(tradeTranslator);
			System.out.println("-------------publishEvent------1------");
		}
		//
		latch.countDown();

	}
}
