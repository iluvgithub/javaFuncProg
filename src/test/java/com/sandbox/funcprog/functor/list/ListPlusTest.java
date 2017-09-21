package com.sandbox.funcprog.functor.list;

import static com.sandbox.funcprog.functor.list.ListPlus.cons;
import static com.sandbox.funcprog.functor.list.ListPlus.wrap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import org.junit.Test;

public class ListPlusTest {

	protected <T> ListPlus<T> asListArray(T[] ts) {
		ListPlus<T> list = wrap(ts[ts.length - 1]);
		for (int i = ts.length - 2; i >= 0; --i) {
			list = cons(ts[i], list);
		}
		return list;
	}

	protected <T> ListPlus<T> asList(@SuppressWarnings("unchecked") T... ts) {
		return asListArray(ts);
	}

	@Test
	public void testFoldlPlus1() throws Exception {
		// arrange
		ListPlus<Integer> list = asList(1);
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldlPlus(i -> i + "", bi);
		// assert
		assertThat(actual).isEqualTo("1");
	}

	@Test
	public void testFoldlPlus() throws Exception {
		// arrange
		ListPlus<Integer> list = asList(1, 2, 3);
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldlPlus(i -> i.toString(), bi);
		// assert
		assertThat(actual).isEqualTo("((1+2)+3)");
	}

	@Test
	public void testFoldlMass() throws Exception {
		// arrange
		int n = 30000;
		Integer[] array = IntStream.range(0, n).map(i -> 1 + i).boxed().toArray(Integer[]::new);
		ListPlus<Integer> list = asListArray(array);
		BinaryOperator<Integer> bi = (a, b) -> a + b;
		// act
		Integer actual = list.reduce(bi);
		// assert
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	@Test
	public void testTrace() throws Exception {
		// arrange
		ListPlus<String> list1 = asList("3");
		ListPlus<String> list = asList("1", "2", "3");
		// act
		String actual1 = list1.trace();
		String actual = list.trace();
		// assert
		assertThat(actual).isEqualTo("[1.2.3]");
		assertThat(actual1).isEqualTo("[3]");
	}
}
