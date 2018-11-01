package com.func;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Curry {

	public static <A, B> B eval(Function<A, B> f, A a) {
		return f.apply(a);
	}

	public static <A, B, Z> Function<A, Function<B, Z>> curry(BiFunction<A, B, Z> f) {
		return a -> b -> f.apply(a, b);
	}

	public static <A, B, Z> BiFunction<A, B, Z> uncurry(Function<A, Function<B, Z>> f) {
		return (a, b) -> f.apply(a).apply(b);
	}

	public static <A> BinaryOperator<A> uncurryop(Function<A, Function<A, A>> f) {
		return asBinaryOperator(uncurry(f));
	}

	public static <A> BinaryOperator<A> asBinaryOperator(BiFunction<A, A, A> f) {
		return (a0, a1) -> f.apply(a0, a1);
	}

	public static <A, B, U, V, Z> BiFunction<A, B, Z> compose(BiFunction<U, V, Z> bif, Function<A, U> f,
			Function<B, V> g) {
		return (a, b) -> bif.apply(f.apply(a), g.apply(b));
	}

	public static <A, B, Z> Function<A, Z> partialLeft(BiFunction<A, B, Z> bif, B b) {
		return a -> bif.apply(a, b);
	}

	public static <A, B, Z> Function<B, Z> partialRight(A a, BiFunction<A, B, Z> bif) {
		return b -> bif.apply(a, b);
	}
}
