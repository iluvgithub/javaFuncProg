package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.Tree.join;
import static com.sandbox.funcprog.tree.Tree.nil;
import static com.sandbox.funcprog.tree.Tree.tau;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TreeTest {

	@Test
	public void testSum0() throws Exception {
		testSum(0);
	}

	@Test
	public void testSum3() throws Exception {
		testSum(3);
	}

	@Test
	public void testSum30000() throws Exception {
		testSum(30000);
	}

	private void testSum(int n) throws Exception {
		// arrange
		Tree<Integer> tree = nil();
		Integer expected = n * (n + 1) / 2;
		while (n > 0) {
			tree = join(tau(n), tree);
			tree = join(tree, tau(0));
			tree = join(tau(0), tree);
			--n;

		}
		// act
		Integer actual = tree.reduce(0,(l, r) -> l + r);
		// assert
		assertThat(actual).isEqualTo(expected);

	}

}
