package top.tst.stdsp.disruptor.bean;

/**
 * 假定为一条交易
 * 
 * @author Liuweiyi
 *
 */
public class TradeTransaction {

	private String id;
	private double price;// BigDecimal 亦可 本例测试

	public TradeTransaction() {
		super();
	}

	public TradeTransaction(String id, double price) {
		super();
		this.id = id;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
