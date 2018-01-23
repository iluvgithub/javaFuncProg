package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.stream.Anamorphism.iterate;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.sandbox.funcprog.bifunctor.Prod;

public class ConsListTest {

	private <A> ConsList<A> nil() {
		return ConsList.nil();
	}

	private <A> ConsList<A> cons(A a, ConsList<A> list) {
		return ConsList.cons(() -> a, () -> list);
	}

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
		Predicate<Integer> p = n -> n < 3;
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
	public void testZip() {
		// given
		ConsList<Integer> ints = cons(1, cons(2, nil()));
		ConsList<String> strings = cons("a", cons("b", cons("c", nil())));
		// when
		ConsList<Prod<String, Integer>> act0 = strings.zip(ints);
		ConsList<Prod<Integer, String>> act1 = ints.zip(strings);
		// then
		assertThat(act0.map(this::traceProd).trace()).isEqualTo("[a.1].[b.2]");
		assertThat(act1.map(this::traceProd).trace()).isEqualTo("[1.a].[2.b]");
	}

	private <X, Y> String traceProd(Prod<X, Y> xy) {
		return xy.apply((x, y) -> "[" + x + "." + y + "]");
	}

	@Test
	public void testTakeN() {
		// given
		ConsList<Integer> nil = nil();
		ConsList<Integer> list1 = cons(0, cons(1, cons(2, cons(3, nil))));
		int n = 3;
		// when
		ConsList<Integer> actual = nil.take(n);
		ConsList<Integer> actual1 = list1.take(n);
		ConsList<Integer> actual2 = list1.take(123);
		// then
		assertThat(actual.trace()).isEqualTo("");
		assertThat(actual1.trace()).isEqualTo("0.1.2");
		assertThat(actual2.trace()).isEqualTo("0.1.2.3");
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

	@Test
	public void testConcat() {
		// given
		ConsList<Integer> left = cons(0, cons(1, cons(2, cons(3, nil()))));
		ConsList<Integer> right = cons(4, cons(5, cons(6, nil())));
		// when
		ConsList<Integer> actual = left.concat(right);
		// then
		assertThat(actual.trace()).isEqualTo("0.1.2.3.4.5.6");
	}

	@Test
	public void testFlatten() {
		// given
		ConsList<Integer> left = cons(0, cons(1, cons(2, cons(3, nil()))));
		ConsList<Integer> right = cons(4, cons(5, cons(6, nil())));
		// when
		ConsList<Integer> actual = left.concat(right);
		// then
		assertThat(actual.trace()).isEqualTo("0.1.2.3.4.5.6");
	}

	@Test
	public void testFlatMap() {
		// given
		ConsList<Integer> left = cons(0, cons(1, cons(2, cons(3, nil()))));
		Function<Integer, ConsList<String>> f = n -> iterate(n.toString(), x -> x).take(n);
		// when
		ConsList<String> actual = left.flatMap(f);
		// then
		assertThat(actual.trace()).isEqualTo("1.2.2.3.3.3");
	}

	@Test
	public void testSort() {
		// given
		ConsList<Integer> list = cons(2, cons(1, cons(0, nil())));
		Comparator<Integer> comp = (a, b) -> a.compareTo(b);
		// when
		ConsList<Integer> actuals = list.sort(comp);
		// then
		assertThat(actuals.trace()).isEqualTo("0.1.2");
	}
 

}
