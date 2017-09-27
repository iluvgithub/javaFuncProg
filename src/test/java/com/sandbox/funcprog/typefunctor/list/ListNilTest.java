package com.sandbox.funcprog.typefunctor.list;

import static com.sandbox.funcprog.typefunctor.list.ListNil.cons;
import static com.sandbox.funcprog.typefunctor.list.ListNil.nil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import org.junit.Test;

import com.sandbox.funcprog.typefunctor.list.ListNil;
 

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
	public void testFoldr() throws Exception {
		// arrange
		ListNil<Integer> list = asList(1, 2, 3);
		BiFunction<Integer, String, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("(1+(2+(3+0)))");
	}
}
