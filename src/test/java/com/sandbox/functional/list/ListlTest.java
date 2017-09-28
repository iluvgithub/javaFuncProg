package com.sandbox.functional.list;

import static com.sandbox.functional.list.Listl.nil;
import static com.sandbox.functional.list.Listl.snoc;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import org.junit.Test;

public class ListlTest {

	@Test
	public void testTrace() throws Exception {
		// arrange
		Listl<Integer> list1 = snoc(nil(), 3);
		Listl<Integer> list = snoc(snoc(snoc(nil(), 1), 2), 3);
		// act
		String actual1 = list1.trace();
		String actual = list.trace();
		// assert
		assertThat(actual1).isEqualTo("[3]");
		assertThat(actual).isEqualTo("[1.2.3]");
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		Listl<String> list = snoc(snoc(snoc(nil(), "a"), "b"), "c");
		// act
		Listl<String> actuals = list.map(String::toUpperCase);
		// assert
		assertThat(actuals.trace()).isEqualTo("[A.B.C]");
	}

	@Test
	public void testFoldl() throws Exception {
		// arrange
		Listl<Integer> list = snoc(snoc(snoc(nil(), 1), 2), 3);
		BiFunction<String, Integer, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldl("0", bi);
		// assert
		assertThat(actual).isEqualTo("(((0+1)+2)+3)");
	}
}
