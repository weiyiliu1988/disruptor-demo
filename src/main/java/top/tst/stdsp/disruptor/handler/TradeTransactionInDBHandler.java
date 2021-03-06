package top.tst.stdsp.disruptor.handler;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import top.tst.stdsp.disruptor.bean.TradeTransaction;

public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction>, WorkHandler<TradeTransaction> {

	/**
	 * WorkHandler 接口
	 * 
	 */
	@Override
	public void onEvent(TradeTransaction event) throws Exception {
		event.setId(UUID.randomUUID().toString());
		System.out.println("==========price============>" + event.getPrice());
	}

	/**
	 * EventHandler 接口
	 * 
	 */
	@Override
	public void onEvent(TradeTransaction event, long arg1, boolean arg2) throws Exception {

		this.onEvent(event);
	}

}
