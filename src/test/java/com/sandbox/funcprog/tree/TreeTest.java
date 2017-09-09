package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.tree.Tree.join;
import static com.sandbox.funcprog.tree.Tree.nil;
import static com.sandbox.funcprog.tree.Tree.tau;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
		Integer actual = tree.reduce(0, (l, r) -> l + r);
		// assert
		assertThat( expected).isEqualTo(actual);
	}

	@Test
	public void testHeadAndTail() throws Exception {
		// arrange
		Tree<Integer> tree = join(join(tau(1), tau(2)), join(tau(3), tau(4)));
		// act
		Integer h = tree.head();
		Integer l = tree.last();
		// assert
		assertThat(1).isEqualTo(h);
		assertThat(4).isEqualTo(l);
	}
	
	
	@Test
	public void testTrace() throws Exception {
		// arrange
		Tree<Integer> tree = join(join(tau(1), tau(2)), join(tau(3), tau(4)));
		// act
		String actual= tree.trace();
		// assert
		assertEquals("1.2.3.4", actual);
	}
	@Test
	public void testReverseTrace() throws Exception {
		// arrange
		Tree<Integer> tree = join(join(tau(1), tau(2)), join(tau(3), tau(4)));
		// act
		String actual= tree.reverseTrace();
		// assert
		assertEquals("4.3.2.1", actual);
	}
}
