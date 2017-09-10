package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
	public void testTrace() throws Exception {
		// arrange
		List<Integer> list = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		String actual = list.trace();
		// assert
		assertEquals("1.2.3.4", actual);
		assertEquals("", empty().trace());
		assertEquals("5", cons(5,empty()).trace());
	}

	@Test
	public void testCumulate() throws Exception {
		// arrange
		List<Integer> tree = cons(1, cons(2, cons(3, cons(4, empty()))));
		// act
		List<Integer> actual = tree.cumulate(0, (l, r) -> l + r);
		// assert
		assertEquals("0.1.3.6.10", actual.trace());
	}

}
