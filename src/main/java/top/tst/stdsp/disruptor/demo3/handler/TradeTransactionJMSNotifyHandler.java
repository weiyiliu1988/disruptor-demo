package top.tst.stdsp.disruptor.demo3.handler;

import com.lmax.disruptor.EventHandler;

import top.tst.stdsp.disruptor.bean.TradeTransaction;

public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {

	@Override
	public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
		// do send jms message
		System.out.println("Demo3 EventHandler!");
	}

}
