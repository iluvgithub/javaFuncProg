package com.func.tree;

import static com.func.tree.BinTree.leaf;
import static com.func.tree.BinTree.nil;
import static com.func.tree.BinTree.tree;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BinTreeTest {

	@Test
	public void testTrace() {
		// given
		BinTree<Integer> zero = nil();
		BinTree<Integer> one = leaf(1);
		BinTree<Integer> l = tree(leaf(1), nil());
		BinTree<Integer> r = tree(nil(), leaf(1));
		BinTree<Integer> two = tree(leaf(1), leaf(2));
		BinTree<Integer> three = tree(leaf(1), tree(leaf(3), leaf(4)));
		// when & then
		assertThat(zero.trace()).isEqualTo("");
		assertThat(l.trace()).isEqualTo("<1,>");
		assertThat(r.trace()).isEqualTo("<,1>");
		assertThat(one.trace()).isEqualTo("1");
		assertThat(two.trace()).isEqualTo("<1,2>");
		assertThat(three.trace()).isEqualTo("<1,<3,4>>");
	}

	@Test
	public void testMassFold() {
		// given
		int n = 40000;
		BinTree<Integer> tree = leaf(0);
		for (int i = 1; i <= n; ++i) {
			tree = tree(tree(leaf(0), leaf(i)), tree(tree, leaf(0)));
		}
		// when
		Integer actual = tree.fold(0, x -> x, Integer::sum);
		// then
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

}