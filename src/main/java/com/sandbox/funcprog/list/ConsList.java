package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Sum.left;
import static com.sandbox.funcprog.bifunctor.Sum.right;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.bifunctor.Sum;

public class ConsList<T> extends AbstractList<T> {

	private final Sum<Void, Prod<T, List<T>>> values;

	private ConsList(Sum<Void, Prod<T, List<T>>> values) {
		this.values = values;
	}

	public static <X> List<X> empty() {
		return new ConsList<>(left(null));
	}

	public static <X> List<X> cons(X x, List<X> xs) {
		return new ConsList<>(right(prod(x, xs)));
	}

	public static <X> List<X> one(X x) {
		return cons(x, empty());
	}

	public List<T> reverse() {
		return foldl(empty(), (ts, t) -> cons(t, ts));
	}

	@Override
	public <Z> List<Z> map(Function<T, Z> f) {
		return foldr(empty(), (t, zs) -> cons(f.apply(t), zs));
	}

	public List<T> concat(List<T> right) {
		return reverse().foldl(right, (ts, t) -> cons(t, ts));
	}

	public static <X> List<X> concat(List<X> left, List<X> right) {
		return left.concat(right);
	}

	@Override
	public <Z> List<Z> cumulr(Z e, BiFunction<T, Z, Z> bi) {
		return foldr(one(e), cumulrBiFunction(bi));
	}

	private <Z> BiFunction<T, List<Z>, List<Z>> cumulrBiFunction(BiFunction<T, Z, Z> bi) {
		return (t, zs) -> cons(bi.apply(t, zs.head()), zs);
	}

	@Override
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

	@Override
	public <Z> Z apply(Z z, BiFunction<T, List<T>, Z> bif) {
		return values.apply(v -> z, pr -> bif.apply(pr.left(), pr.right()));
	}

}
