package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.list.ListNil.cons;
import static com.sandbox.funcprog.list.ListNil.nil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import org.junit.Test;

public class ListNilTest {

	protected <T> ListNil<T> asListArray(T[] ts) {
		ListNil<T> list = nil();
		for (int i = ts.length - 1; i >= 0; --i) {
			list = cons(ts[i], list);
		}
		return list;
	}

	protected <T> ListNil<T> asList(@SuppressWarnings("unchecked") T... ts) {
		return asListArray(ts);
	}

	@Test
	public void testFoldl0() throws Exception {
		// arrange
		ListNil<Integer> list = asList();
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldl("0", bi);
		// assert
		assertThat(actual).isEqualTo("0");
	}

	@Test
	public void testFoldl1() throws Exception {
		// arrange
		ListNil<Integer> list = asList(1);
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldl("0", bi);
		// assert
		assertThat(actual).isEqualTo("(0+1)");
	}

	@Test
	public void testFoldl() throws Exception {
		// arrange
		ListNil<Integer> list = asList(1, 2, 3);
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldl("0", bi);
		// assert
		assertThat(actual).isEqualTo("(((0+1)+2)+3)");
	}

	@Test
	public void testTrace0() throws Exception {
		ListNil<String> list0 = asList();
		// act
		String actual0 = list0.trace();
		// assert
		assertThat(actual0).isEqualTo("[]");
	}

	@Test
	public void testTrace() throws Exception {
		// arrange
		ListNil<String> list1 = asList("3");
		ListNil<String> list = asList("1", "2", "3");
		// act
		String actual1 = list1.trace();
		String actual = list.trace();
		// assert
		assertThat(actual1).isEqualTo("[3]");
		assertThat(actual).isEqualTo("[1.2.3]");
	}

	@Test
	public void testReverse() throws Exception {
		// arrange
		ListNil<Integer> list = asList(1, 2, 3);
		// act
		ListNil<Integer> actual = list.reverse();
		// assert
		assertThat(actual.trace()).isEqualTo("[3.2.1]");
	}

	@Test
	public void testFoldlMass() throws Exception {
		// arrange
		int n = 30000;
		Integer[] array = IntStream.range(0, n).map(i -> 1 + i).boxed().toArray(Integer[]::new);
		ListNil<Integer> list = asListArray(array);
		BinaryOperator<Integer> bi = (a, b) -> a + b;
		// act
		Integer actual = list.foldl(0, bi);
		// assert
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	@Test
	public void testFoldr() throws Exception {
		// arrange
		ListNil<Integer> list = asList(1, 2, 3);
		BiFunction<Integer, String, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("(1+(2+(3+0)))");
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		ListNil<String> list = cons("a", cons("b", cons("c", nil())));
		// act
		ListNil<String> actuals = list.map(String::toUpperCase);
		// assert
		assertThat(actuals.trace()).isEqualTo("[A.B.C]");
	}

}
