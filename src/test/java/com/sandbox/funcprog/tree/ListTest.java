package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;
import static com.sandbox.funcprog.tree.List.single;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

public class ListTest {

	@Test
	public void testSum() throws Exception {
		testSum(0);
	}

	@Test
	public void testSum3() throws Exception {
		testSum(3);
	}

	@Test
	public void testSumBig() throws Exception {
		testSum(30000);
	}

	private void testSum(int n) throws Exception {
		// arrange
		List<Integer> list = empty();
		for (int i = n; i > 0; --i) {
			list = cons(i, list);
		}
		// act
		Integer act = list.reduce(0, (l, r) -> l + r);
		// assert
		assertThat(act).isEqualTo(n * (n + 1) / 2);
	}

	@Test
	public void testReverse() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		List<Integer> actual = list.reverse();
		// assert
		assertThat(4).isEqualTo(actual.head());
	}

	@Test
	public void testTrace() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		String actual = list.trace();
		// assert[ ]
		assertEquals("[1.2.3.4]", actual);
		assertEquals("[]", empty().trace());
		assertEquals("[5]", cons(5, empty()).trace());
		assertEquals("[5]", empty().concat(single(5)).trace());
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		List<String> list = cons("a", cons("b", cons("c", cons("d", empty()))));
		// act
		List<String> actual = list.map(String::toUpperCase);
		// assert
		assertEquals("[A.B.C.D]", actual.trace());
	}

	@Test
	public void testConcat() throws Exception {
		// arrange
		List<Integer> l = cons(1, cons(2, empty()));
		List<Integer> r = cons(3, cons(4, empty()));
		// act
		List<Integer> actual = l.concat(r);
		// assert[ ]
		assertEquals("[1.2.3.4]", actual.trace());
	}

	@Test
	public void testLeftCumulate() throws Exception {
		// arrange
		List<String> list = cons("1", cons("2", cons("3", cons("4", empty()))));
		// act
		List<String> actual = list.leftCumulate("0", (l, r) -> l + "+" + r);
		// assert
		assertEquals("[0.0+1.0+1+2.0+1+2+3.0+1+2+3+4]", actual.trace());
	}

	@Test
	public void testInits() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		List<List<Integer>> actual = list.inits();
		// assert
		assertEquals("[[].[1].[1.2].[1.2.3].[1.2.3.4]]", actual.map(xs -> xs.trace()).trace());
	}

	// @Test
	public void testTails() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		List<List<Integer>> actual = list.tails();
		// assert
		assertEquals("[[1.2.3.4].[2.3.4].[3.4].[4].[].]", actual.map(xs -> xs.trace()).trace());
	}

	@Test
	public void testMaximumSegmentLength() throws Exception {
		testMsl(7, cons(7, empty()));
		testMsl(7, cons(-1, cons(3, cons(3, cons(-5, cons(1, cons(3, cons(2, cons(-1, empty())))))))));
		testMsl(6, cons(-1, cons(3, cons(1, cons(-4, cons(-1, cons(4, cons(2, cons(-1, empty())))))))));
		testMsl(4, cons(-1, cons(3, cons(1, cons(-4, cons(-1, cons(1, cons(2, cons(-1, empty())))))))));
	}

	public void testMsl(Integer expected, List<Integer> input) throws Exception {
		Function<List<Integer>, Integer> msl = l -> l.leftCumulate(0, (a, b) -> Math.max(0, a + b)).fold(Integer.MIN_VALUE, (a, b) -> Math.max(a, b));
		assertThat(msl.apply(input)).isEqualTo(expected);
	}
}
