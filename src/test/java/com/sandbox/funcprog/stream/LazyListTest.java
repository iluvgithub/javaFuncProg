package com.sandbox.funcprog.stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LazyListTest extends ConsListTest {

	@Override
	public <A> ConsList<A> nil() {
		return LazyList.nil();
	}

	@Override
	public <A> ConsList<A> cons(A a, ConsList<A> list) {
		return LazyList.cons(() -> a, () -> list);
	}

	@Test
	public void testMakeIntList() {
		// given
		ConsList<Integer> list = LazyList.makeIntList(0,3);
		// when
		String actual = list.trace();
		// then
		assertThat(actual).isEqualTo("0.1.2");
	}
}
