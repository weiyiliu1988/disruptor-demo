package top.tst.stdsp.disruptor.demo3.translator;

import com.lmax.disruptor.EventTranslator;

import top.tst.stdsp.disruptor.bean.TradeTransaction;

public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {

	private static int a = 0;

	// private Random random = new Random();

	@Override
	public void translateTo(TradeTransaction tradeTransaction, long sequence) {
		// 对生成的tradeTransaction进行处理
		this.generateTradeTransaction(tradeTransaction);

	}

	/**
	 * 逻辑处理方法
	 * 
	 * @param trade
	 * @return
	 */
	private TradeTransaction generateTradeTransaction(TradeTransaction trade) {
		trade.setPrice(a++);
		return trade;
	}

}
