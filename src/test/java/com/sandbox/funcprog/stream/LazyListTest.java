package com.sandbox.funcprog.stream;

public class LazyListTest extends ConsListTest {

	@Override
	public <A> ConsList<A> nil() {
		return LazyList.nil();
	}

	@Override
	public <A> ConsList<A> cons(A a, ConsList<A> list) {
		return LazyList.cons(() -> a, () -> list);
	}

}
