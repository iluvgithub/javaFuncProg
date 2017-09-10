package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Sum.left;
import static com.sandbox.funcprog.bifunctor.Sum.right;
import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.bifunctor.Sum;
import com.sandbox.funcprog.recursion.Bouncer;

public class List<T> {

	private Sum<Void, Prod<T, List<T>>> values;

	public List(Sum<Void, Prod<T, List<T>>> values) {
		this.values = values;
	}

	public static <X> List<X> empty() {
		return new List<>(left(null));
	}

	public static <X> List<X> cons(X x, List<X> xs) {
		return new List<>(right(prod(x, xs)));
	}

	public static <X> List<X> single(X x) {
		return cons(x, empty());
	}

	public <Z> Z fold(Z out, BiFunction<T, Z, Z> bi) {
		return fold(this, out, bi).call();
	}

	private static <X, Z> Bouncer<Z> fold(List<X> xs, Z out, BiFunction<X, Z, Z> bi) {
		return xs.values.apply(//////////////////////////////////////////////////////////////////
				v -> resume(out), ///////////////////////////////////////////////////////////////
				pr -> suspend(() -> fold(pr.right(), bi.apply(pr.left(), out), bi) //////////////
		) ///////////////////////////////////////////////////////////////////////////////////////
		);
	}

	public List<T> reverse() {
		return reverse(this, empty()).call();
	}

	private static <T> Bouncer<List<T>> reverse(List<T> in, List<T> out) {
		return in.values.apply(//////////////////////////////////////////////////////////////////
				v -> resume(out), ///////////////////////////////////////////////////////////////
				pr -> suspend(() -> reverse(pr.right(), cons(pr.left(), out))) //////////////
		); ///////////////////////////////////////////////////////////////////////////////////////
	}

	public <Z> List<Z> map(Function<T, Z> f) {
		return fold(empty(), (x, zs) -> cons(f.apply(x), zs)).reverse();
	}

	public <Z> Z apply(Z z, Function<Prod<T, List<T>>, Z> g) {
		return values.apply(v -> z, g);
	}

	public T reduce(T id, BinaryOperator<T> bi) {
		return fold(id, bi);
	}

	public T head() {
		return headOrTail(Prod::left);
	}

	public List<T> tail() {
		return headOrTail(Prod::right);
	}

	private <Z> Z headOrTail(Function<Prod<T, List<T>>, Z> f) {
		return values.apply(null, f);
	}

	protected <Z> Z headTwice(BiFunction<T, T, Z> bi) {
		return bi.apply(head(), tail().head());
	}

	protected List<T> tailTwice() {
		return tail().tail();
	}

	public String trace() {
		return "[" + fold("", (x, z) -> (z.length() > 0) ? z + "." + x : x.toString()) + "]";
	}

	public List<T> concat(List<T> list) {
		return this.reverse().fold(list, (t, ts) -> cons(t, ts));
	}

	//TODO: make it tail recursive
	public <Z> List<Z> leftCumulate(Z e, BiFunction<Z, T, Z> bi) {
		return values.apply(///////////////////////////////
				v -> cons(e, empty()), ////////////////////
				pr -> cons(e, pr.right().leftCumulate(bi.apply(e, pr.left()), bi))//
		);
	}
 

	public List<List<T>> inits() {
		return map(x -> single(x)).leftCumulate(empty(), (l1, l2) -> l1.concat(l2));
	}

	public List<List<Integer>> tails() {
		return null;
	}

}
