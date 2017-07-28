package pong.main.util;

import java.util.ArrayList;

public class MultiMap<A, B> {

	private ArrayList<A> aVals;
	private ArrayList<B> bVals;

	public MultiMap() {
		aVals = new ArrayList<A>();
		bVals = new ArrayList<B>();
	}

	public MultiMap(int cap) {
		aVals = new ArrayList<A>() {

			private static final long serialVersionUID = -4377685711691182031L;

			@Override
			public boolean add(A e) {
				if (this.size() < cap)
					return super.add(e);
				else
					throw new IllegalArgumentException("More than " + cap + " elements in list.");
			}

		};
		bVals = new ArrayList<B>() {

			private static final long serialVersionUID = -4377685711691182031L;

			@Override
			public boolean add(B e) {
				if (this.size() < cap)
					return super.add(e);
				else
					throw new IllegalArgumentException("More than " + cap + " elements in list.");
			}

		};
		;
	}

	public MultiMap(A a, B b) {
		this(10);
		put(a, b);
	}

	public void put(A a, B b) {
		aVals.add(a);
		bVals.add(b);
		ensureMatch();
	}

	public void addAll(A[] aVals, B[] bVals) {
		if (aVals.length != bVals.length)
			throw new IllegalArgumentException("The lengths of the two arrays passed in do not match.");
		ensureMatch();
		for (int i = 0; i < aVals.length; i++)
			put(aVals[i], bVals[i]);

	}

	public int size() {
		ensureMatch();
		return aVals.size();
	}

	public void clear() {
		aVals.clear();
		bVals.clear();
	}

	public DualSingularSet<A, B> get(int index) {
		ensureMatch();
		return new DualSingularSet<A, B>(aVals.get(index), bVals.get(index));
	}

	private void ensureMatch() {
		if (aVals.size() != bVals.size())
			throw new RuntimeException("The size of two generic type array-list doesn't match.");
	}

	public boolean isEmpty() {
		ensureMatch();
		return aVals.isEmpty() && bVals.isEmpty();
	}

	public ArrayList<MultiMap<A, B>> splitFor(int i) {
		if (size() % i != 0)
			throw new IllegalArgumentException("The size of the multimap does not divide equally by " + i);
		ArrayList<MultiMap<A, B>> items = new ArrayList<MultiMap<A, B>>();
		MultiMap<A, B> split = new MultiMap<A, B>();
		for (int index = 0; index < size(); index++) {
			if (items.size() > 4) {
				items.add(split);
				split.clear();
			}
			DualSingularSet<A, B> set = get(index);
			split.put(set.getKey(), set.getValue());
		}
		items.add(split);
		return items;
	}
}
