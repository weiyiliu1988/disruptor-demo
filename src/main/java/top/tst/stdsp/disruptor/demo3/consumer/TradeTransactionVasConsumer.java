package top.tst.stdsp.disruptor.demo3.consumer;

import com.lmax.disruptor.EventHandler;

import top.tst.stdsp.disruptor.bean.TradeTransaction;

public class TradeTransactionVasConsumer implements EventHandler<TradeTransaction> {

	/**
	 * 消费者
	 * 
	 */
	@Override
	public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {

		System.out.println("===================>consumer");
		System.out.println("===================>consumer");

		System.out.println(event.getPrice());

	}

}
