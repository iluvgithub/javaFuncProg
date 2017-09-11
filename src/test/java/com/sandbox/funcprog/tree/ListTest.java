package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;
import static com.sandbox.funcprog.tree.List.one;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BinaryOperator;

import org.junit.Test;

public class ListTest {

	@Test
	public void testFoldrEmpty() throws Exception {
		// arrange
		List<String> list = empty();
		BinaryOperator<String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("0");
	}

	@Test
	public void testFoldrOne() throws Exception {
		// arrange
		List<String> list = one("1");
		BinaryOperator<String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("(1+0)");
	}

	@Test
	public void testFoldr() throws Exception {
		// arrange
		List<String> list = cons("1", cons("2", one("3")));
		BinaryOperator<String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldr("0", bi);
		// assert
		assertThat(actual).isEqualTo("(1+(2+(3+0)))");
	}

	@Test
	public void testTrace() throws Exception {
		// arrange
		List<String> list = cons("1", cons("2", one("3")));
		// act
		String actual = list.trace();
		// assert
		assertThat(actual).isEqualTo("[1.2.3]");
		assertThat(empty().trace()).isEqualTo("[]");
		assertThat(one("1").trace()).isEqualTo("[1]");
	}

	//@Test
	public void testReverse() throws Exception {
		// arrange
		List<String> list = cons("1", cons("2", one("3")));
		// act
		List<String> actuals = list.reverse();
		// assert
		assertThat(actuals.trace()).isEqualTo("[3.2.1]");
	}

	@Test
	public void testFoldl() throws Exception {
		// arrange
		List<String> list = cons("1", cons("2", one("3")));
		BinaryOperator<String> bi = (a, b) -> "(" + a + "+" + b + ")";
		// act
		String actual = list.foldl("0", bi);
		// assert
		assertThat(actual).isEqualTo("(((0+1)+2)+3)");
	}

}
