package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;
import static org.assertj.core.api.Assertions.assertThat;

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

}
