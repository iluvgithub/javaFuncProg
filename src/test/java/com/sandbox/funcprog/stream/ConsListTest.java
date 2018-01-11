package com.sandbox.funcprog.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import org.junit.Test;

public abstract class ConsListTest     {

	abstract <A> ConsList<A> nil();

	abstract <A> ConsList<A> cons(A a, ConsList<A> list);
	
	@Test
	public void testFoldLeft() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> list = cons(0, cons(1, cons(2, nil)));
		BiFunction<String, Integer, String> f = (s, i) -> s + (s.length() == 0 ? "" : ".") + i;
		// when
		String actual0 = nil.foldLeft("e", f);
		String actual = list.foldLeft("e", f);
		// then
		assertThat(actual0).isEqualTo("e");
		assertThat(actual).isEqualTo("e.0.1.2");
	}

	@Test
	public void testTrace() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> one = cons(0, nil);
		ConsList<Integer> list = cons(0, cons(1, cons(2, nil)));
		// when
		String actual0 = nil.trace();
		String actual1 = one.trace();
		String actual = list.trace();
		// then
		assertThat(actual0).isEqualTo("");
		assertThat(actual1).isEqualTo("0");
		assertThat(actual).isEqualTo("0.1.2");
	}
}
