package com.sandbox.funcprog.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

public abstract class ConsListTest {

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

	@Test
	public void testReverse() {
		// given
		ConsList<Integer> list = cons(0, cons(1, cons(2, nil())));
		// when
		ConsList<Integer> actual = list.reverse();
		// then
		assertThat(actual.trace()).isEqualTo("2.1.0");
	}

	@Test
	public void testMap() {
		// given
		ConsList<String> list = cons("a", cons("b", cons("c", nil())));
		// when
		ConsList<String> actuals = list.map(String::toUpperCase);
		// then
		assertThat(actuals.trace()).isEqualTo("A.B.C");
	}
	
	@Test
	public void testFoldRight() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> list = cons(0, cons(1, cons(2, nil)));
		BiFunction<Integer, String, String> f = (i, s) -> i + (s.length() == 0 ? "" : ".") + s;
		// when
		String actual0 = nil.foldRight("e", f);
		String actual = list.foldRight("e", f);
		// then
		assertThat(actual0).isEqualTo("e");
		assertThat(actual).isEqualTo("0.1.2.e");
	}

	@Test
	public void testTakeWhile() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> list0 = cons(3, nil);
		ConsList<Integer> list1 = cons(0, cons(1, cons(2, nil)));
		Predicate<Integer> p = n -> n <3;
		// when
		ConsList<Integer> actual = nil.takeWhile(p);
		ConsList<Integer> actual0 = list0.takeWhile(p);
		ConsList<Integer> actual1 = list1.takeWhile(p);
		// then
		assertThat(actual.trace()).isEqualTo("");
		assertThat(actual0.trace()).isEqualTo("");
		assertThat(actual1.trace()).isEqualTo("0.1.2");
	}

	@Test
	public void testDropWhile() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> list0 = cons(0, nil);
		ConsList<Integer> list1 = cons(0, cons(1, cons(2, cons(3, nil))));
		Predicate<Integer> p = n -> n < 2;
		// when
		ConsList<Integer> actual = nil.dropWhile(p);
		ConsList<Integer> actual0 = list0.dropWhile(p);
		ConsList<Integer> actual1 = list1.dropWhile(p);
		// then
		assertThat(actual.trace()).isEqualTo("");
		assertThat(actual0.trace()).isEqualTo("");
		assertThat(actual1.trace()).isEqualTo("2.3");
	}

}
