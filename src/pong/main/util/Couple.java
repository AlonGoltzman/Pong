package pong.main.util;

import java.util.ArrayList;
import static pong.main.util.Util.nullCheck;

public class Couple<T> extends ArrayList<T> implements Iterable<T> {

	private static final long serialVersionUID = 3154690803985149062L;
	private T t1;
	private T t2;

	public Couple() {

	}

	public Couple(T t1, T t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public boolean add(T t) {
		if (!isEmpty() && size() != 1) {
			if (t1 == null)
				t1 = t;
			t2 = t;
			return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return t1 == null && t2 == null;
	}

	@Override
	public int size() {
		return (isEmpty() ? 0 : (!nullCheck(t1) && !nullCheck(t2) ? 2 : 1));
	}

	@Override
	public T get(int itemNum) {
		if (itemNum > 1)
			throw new IllegalArgumentException("A couple consists of 2 items, getItem must be either 0 or 1.");
		return (itemNum == 0 ? t1 : t2);
	}

	@Override
	public T set(int itemNum, T t) {
		if (itemNum > 1)
			throw new IllegalArgumentException("A couple consists of 2 items, changeItem must be either 0 or 1.");
		if (itemNum == 0)
			return t1 = t;
		return t2 = t;
	}

}
