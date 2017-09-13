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

	private final Sum<Void, Prod<T, List<T>>> values;

	public List(Sum<Void, Prod<T, List<T>>> values) {
		this.values = values;
	}

	public static <X> List<X> empty() {
		return new List<>(left(null));
	}

	public static <X> List<X> cons(X x, List<X> xs) {
		return new List<>(right(prod(x, xs)));
	}

	public static <X> List<X> one(X x) {
		return cons(x, empty());
	}

	public T head() {
		return applyOnNonEmpty((t, ts) -> t);
	}

	public List<T> tail() {
		return applyOnNonEmpty((t, ts) -> ts);
	}

	public T last() {
		return reverse().head();
	}

	private <Z> Z apply(Z z, BiFunction<T, List<T>, Z> bif) {
		return values.apply(v -> z, pr -> bif.apply(pr.left(), pr.right()));
	}

	private <Z> Z applyOnNonEmpty(BiFunction<T, List<T>, Z> bif) {
		return apply(null, bif);
	}

	/**
	 * right to left reduction
	 * 
	 * @param z
	 * @param bi
	 * @return
	 * 
	 */
	public <Z> Z foldr(Z id, BiFunction<T, Z, Z> bi) {
		return reverse().foldl(id, (z, t) -> bi.apply(t, z));
		// values.apply(v -> id, pr -> bi.apply(pr.left(), pr.right().foldr(id,
		// bi)));
	}

	public T reduceR(T id, BinaryOperator<T> bi) {
		return foldr(id, bi);
	}

	public String trace() {
		return "[" + foldr("", (a, b) -> a + (b.length() > 0 ? ("." + b) : "")) + "]";
	}

	public List<T> reverse() {
		return foldl(empty(), (ts, t) -> cons(t, ts));
	}

	public <Z> Z foldl(Z z, BiFunction<Z, T, Z> bi) {
		return bounce(this, z, bi).call();
	}

	protected static <X, Z> Bouncer<Z> bounce(List<X> list, Z z, BiFunction<Z, X, Z> bi) {
		return list.apply(resume(z), (x, xs) -> suspend(() -> bounce(xs, bi.apply(z, x), bi)));
	}

	public <Z> List<Z> map(Function<T, Z> f) {
		return foldr(empty(), (t, zs) -> cons(f.apply(t), zs));
	}

	public List<T> concat(List<T> right) {
		return reverse().foldl(right, (ts, t) -> cons(t, ts));
		// foldr(right, List::cons);
	}

	public static <X> List<X> concat(List<X> left, List<X> right) {
		return left.concat(right);
	}

	public <Z> List<Z> cumulr(Z e, BiFunction<T, Z, Z> bi) {
		return foldr(one(e), cumulrBiFunction(bi));
	}

	private <Z> BiFunction<T, List<Z>, List<Z>> cumulrBiFunction(BiFunction<T, Z, Z> bi) {
		return (t, zs) -> cons(bi.apply(t, zs.head()), zs);
	}

	public <Z> List<Z> cumull(Z e, BiFunction<Z, T, Z> bi) {
		return foldl(one(e), cumullBiFunction(bi));
	}

	private <Z> BiFunction<List<Z>, T, List<Z>> cumullBiFunction(BiFunction<Z, T, Z> bi) {
		return (zs, t) -> zs.concat(one(bi.apply(zs.last(), t)));
	}

	public List<List<T>> inits() {
		return cumull(empty(), (ts, t) -> ts.concat(one(t)));
	}

	public List<List<T>> tails() {
		return cumulr(empty(), (t, ts) -> cons(t, ts));
	}

}
