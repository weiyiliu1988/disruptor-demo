package top.tst.stdsp.disruptor.demo3.translator;

import java.util.Random;

import com.lmax.disruptor.EventTranslator;

import top.tst.stdsp.disruptor.bean.TradeTransaction;

public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {

	private Random random = new Random();

	@Override
	public void translateTo(TradeTransaction event, long sequence) {
		// TODO Auto-generated method stub
		this.generateTradeTransaction(event);

	}

	// logic
	private TradeTransaction generateTradeTransaction(TradeTransaction trade) {
		trade.setPrice(random.nextDouble() * 9999);
		return trade;
	}

}
