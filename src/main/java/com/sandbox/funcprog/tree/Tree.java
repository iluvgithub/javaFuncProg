package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Prod.swap;
import static com.sandbox.funcprog.bifunctor.Sum.left;
import static com.sandbox.funcprog.bifunctor.Sum.right;
import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;
import static com.sandbox.funcprog.tree.List.cons;
import static com.sandbox.funcprog.tree.List.empty;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.bifunctor.Sum;
import com.sandbox.funcprog.recursion.Bouncer;

public class Tree<T> {

	private final Sum<Sum<Void, T>, Prod<Tree<T>, Tree<T>>> values;

	private Tree(Sum<Sum<Void, T>, Prod<Tree<T>, Tree<T>>> values) {
		this.values = values;
	}

	public static <X> Tree<X> nil() {
		return new Tree<>(left(left(null)));
	}

	public static <X> Tree<X> tau(X x) {
		return new Tree<>(left(right(x)));
	}

	public static <X> Tree<X> join(Tree<X> l, Tree<X> r) {
		return new Tree<>(right(prod(l, r)));
	}

	public <Z> Z fold(Z id, Function<T, Z> f, BinaryOperator<Z> bi) {
		return fold(prod(asList(this), empty()), id, f, bi, x -> x).call().head();
	}

	public <Z> Z reverseFold(Z id, Function<T, Z> f, BinaryOperator<Z> bi) {
		return fold(prod(asList(this), empty()), id, f, bi, x -> swap(x)).call().head();
	}

	public T reduce(T id, BinaryOperator<T> bi) {
		return fold(id, x -> x, bi);
	}

	private static <X> List<Tree<X>> asList(Tree<X> tree) {
		return cons(tree, empty());
	}

	private static <X, Z> Bouncer<List<Z>> fold(Prod<List<Tree<X>>, List<Z>> inOuts, Z id, Function<X, Z> f,
			BinaryOperator<Z> bi, UnaryOperator<Prod<Tree<X>, Tree<X>>> swap) {
		return inOuts.left().apply(////////////////////////////////////////
				resume(inOuts.right()), ////////////////////////////////////////
				pr -> suspend(() -> fold(newArgs(pr, inOuts.right(), id, f, bi, swap), id, f, bi, swap)));
	}

	private static <X, Z> Prod<List<Tree<X>>, List<Z>> newArgs(Prod<Tree<X>, List<Tree<X>>> pr, List<Z> outs, Z id,
			Function<X, Z> f, BinaryOperator<Z> bi, UnaryOperator<Prod<Tree<X>, Tree<X>>> swap) {
		if (null == pr.left()) {
			return prod(pr.right(), outsFromNonLeafElement(outs, bi));
		} else {
			return pr.left().values.apply(/////////////////////////////////
					sum -> prod(pr.right(), outsFromLeafElement(sum, outs, id, f)),
					prd -> prod(insFromNonLeafElement(swap.apply(prd), pr.right()), outs)
			///////////////////////////////////////////////////////////////
			);
		}
	}

	private static <Z> List<Z> outsFromNonLeafElement(List<Z> outs, BinaryOperator<Z> bi) {
		return cons(outs.headTwice(bi), outs.tailTwice());
	}

	private static <X, Z> List<Z> outsFromLeafElement(Sum<Void, X> sum, List<Z> outs, Z id, Function<X, Z> f) {
		return cons(sum.apply(v -> id, t -> f.apply(t)), outs);
	}

	private static <X> List<Tree<X>> insFromNonLeafElement(Prod<Tree<X>, Tree<X>> prd, List<Tree<X>> tail) {
		return cons(prd.right(), cons(prd.left(), cons(null, tail)));
	}

	public T head() {
		return reduce(null, (a, b) -> a);
	}

	public T last() {
		return reduce(null, (a, b) -> b);
	}

	public String trace() {
		return fold("", Object::toString, (a, b) -> a + "." + b);
	}

	public String reverseTrace() {
		return reverseFold("", Object::toString, (a, b) -> a + "." + b);
	}


}
