package pong.main.util;

public class DualSingularSet<A, B> {

	private A aVal;
	private B bVal;

	public DualSingularSet() {
	}

	public DualSingularSet(A a, B b) {
		setKey(a);
		setValue(b);
	}

	public A getKey() {
		return aVal;
	}

	public B getValue() {
		return bVal;
	}

	public void setKey(A a) {
		if (a != null)
			aVal = a;
		else
			throw new NullPointerException("You placed a key which is null.");
	}

	public void setValue(B b) {
		if (b != null)
			bVal = b;
		else
			throw new NullPointerException("You placed a value which is null.");
	}

}
