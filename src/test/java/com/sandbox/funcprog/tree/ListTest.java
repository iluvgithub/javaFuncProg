package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.List.concat;
import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;
import static com.sandbox.funcprog.tree.List.one;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Function;

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

	@Test
	public void testHeadAndTailAndLast() throws Exception {
		// arrange
		List<String> list = cons("A", cons("B", one("C")));
		// assert
		assertThat(list.head()).isEqualTo("A");
		assertThat(list.tail().head()).isEqualTo("B");
		assertThat(list.tail().tail().head()).isEqualTo("C");
		assertThat(list.tail().tail().tail().head()).isNull();
		assertThat(list.last()).isEqualTo("C");
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		List<String> list = cons("a", cons("b", one("c")));
		// act
		List<String> actuals = list.map(String::toUpperCase);
		// assert
		assertThat(actuals.head()).isEqualTo("A");
		assertThat(actuals.tail().head()).isEqualTo("B");
		assertThat(actuals.tail().tail().head()).isEqualTo("C");
		assertThat(actuals.tail().tail().tail().head()).isNull();
	}

	@Test
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
		assertThat(actual)
				.isEqualTo(Arrays.asList("1", "2", "3").stream().reduce("0", (a, b) -> "(" + a + "+" + b + ")"));
	}

	@Test
	public void testFoldRightToLeftReduceMass() throws Exception {
		// arrange
		int n = 30000;
		List<Integer> list = makeList(n);
		// act
		Integer actual = list.reduceR(0, (a, b) -> a + b);
		// assert
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	private static List<Integer> makeList(int n) {
		List<Integer> list = empty();
		for (int i = 0; i < n; ++i) {
			list = cons(i + 1, list);
		}
		return list;
	}

	@Test
	public void testFoldLeftToRightReduceMass() throws Exception {
		int n = 30000;
		List<Integer> list = makeList(n);
		// act
		Integer actual = list.foldl(0, (a, b) -> a + b);
		// assert
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	@Test
	public void testConcat() throws Exception {
		// arrange
		List<Integer> l = cons(1, cons(2, one(3)));
		List<Integer> r = cons(4, cons(5, one(6)));
		// act
		List<Integer> actual = concat(l, r);
		// assert
		assertEquals("[1.2.3.4.5.6]", actual.trace());
	}

	@Test
	public void testCumulr() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, one(3)));
		// act
		List<String> actuals = list.cumulr("e", (i, s) -> i + "+" + s);
		// assert
		assertEquals("[1+2+3+e.2+3+e.3+e.e]", actuals.trace());
	}

	@Test
	public void testCumull() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, one(3)));
		// act
		List<String> actuals = list.cumull("e", (s, i) -> s + "+" + i);
		// assert
		assertEquals("[e.e+1.e+1+2.e+1+2+3]", actuals.trace());
	}

	@Test

	public void testInits() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, one(3)));
		// act
		List<List<Integer>> actual = list.inits();
		// assert
		assertThat(actual.map(x -> x.trace()).trace()).isEqualTo("[[].[1].[1.2].[1.2.3]]");
	}

	@Test
	public void testTails() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, one(3)));
		// act
		List<List<Integer>> actual = list.tails();
		// assert
		assertThat(actual.map(x -> x.trace()).trace()).isEqualTo("[[1.2.3].[2.3].[3].[]]");
	}

	@Test
	public void testMaxSegmentLength() throws Exception {
		// arrange
		List<Integer> list = cons(-1, cons(3, cons(2, cons(-1, cons(-2, cons(4, cons(2, one(-1))))))));
		// act
		Function<List<Integer>, Integer> max = is -> is.reduceR(0, Math::max);
		BinaryOperator<Integer> bi = (a, b) -> Math.max(0, a + b);
		Integer actual = max.apply(list.cumull(0, bi));
		// assert
		assertThat(actual).isEqualTo(8);
	}
}
