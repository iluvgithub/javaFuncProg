package com.sandbox.functional.list;

import static com.sandbox.functional.list.Listr.cons;
import static com.sandbox.functional.list.Listr.nil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;

import org.junit.Test;

public class ListrTest {

	@Test
	public void testFoldr() throws Exception {
		// arrange
		Listr<Integer> list = cons(1, cons(2, cons(3, nil())));
		BiFunction<Integer, String, String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("(1+(2+(3+0)))");
	}

	@Test
	public void testTrace() throws Exception {
		// arrange
		Listr<Integer> nil = nil();
		Listr<Integer> list1 = cons(3, nil());
		Listr<Integer> list = cons(1, cons(2, cons(3, nil())));
		// act
		String actual0 = nil.trace();
		String actual1 = list1.trace();
		String actual = list.trace();
		// assert
		assertThat(actual0).isEqualTo("[]");
		assertThat(actual1).isEqualTo("[3]");
		assertThat(actual).isEqualTo("[1.2.3]");
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		Listr<String> list = cons("a", cons("b", cons("c", nil())));
		// act
		Listr<String> actuals = list.map(String::toUpperCase);
		// assert
		assertThat(actuals.trace()).isEqualTo("[A.B.C]");
	}

	@Test
	public void testConverse() throws Exception {
		// arrange
		Listr<Integer> listr = cons(1, cons(2, cons(3, nil())));
		// act
		Listl<Integer> actual = listr.converse();
		// assert
		assertThat(actual.trace()).isEqualTo("[3.2.1]");
	}

}
