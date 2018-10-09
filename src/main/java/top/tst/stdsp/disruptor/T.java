package top.tst.stdsp.disruptor;

public class T {

	public static void main(String[] args) throws Exception {
		Exception e = new Exception();
		try {
			throw e;
		} catch (Exception e1) {
			throw e1;
		}
	}
}
