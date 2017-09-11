package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Sum.left;
import static com.sandbox.funcprog.bifunctor.Sum.right;
import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;

import java.util.function.BiFunction;

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

	/**
	 * right to left reduction
	 * 
	 * @param z
	 * @param bi
	 * @return
	 * 
	 */
	public <Z> Z foldr(Z id, BiFunction<T, Z, Z> bi) {
		return values.apply(v -> id, pr -> bi.apply(pr.left(), pr.right().foldr(id, bi))

		);
	}

	public String trace() {
		return "[" + foldr("", (a, b) -> a + (b.length() > 0 ? ("." + b) : "")) + "]";
	}

	public List<String> reverse() {
		return null;
	}

	public <Z> Z foldl(Z z, BiFunction<Z, T, Z> bi) {
		return bounce(this, z, bi).call();
	}

	protected static <X, Z> Bouncer<Z> bounce(List<X> xs, Z z, BiFunction<Z, X, Z> bi) {
		return xs.values.apply(v -> resume(z), pr -> suspend(() -> bounce(pr.right(), bi.apply(z, pr.left()), bi)));
	}
}
