package com.sandbox.funcprog.stream;

import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;

public class Anamorphism {

	private Anamorphism() {
	}

	public static <S, X> Function<S, ConsList<X>> unfold(Predicate<S> p, Function<S, Prod<X, S>> g) {
		return unfold(s0 -> of(s0).filter(p.negate()).map(g));
	}

	public static <S, X> Function<S, ConsList<X>> unfold(Function<S, Optional<Prod<X, S>>> g) {
		return g.andThen(opt -> opt.map(unfoldStep(g)).orElse(nil()));
	}

	private static <S, X> Function<Prod<X, S>, ConsList<X>> unfoldStep(Function<S, Optional<Prod<X, S>>> g) {
		return prod -> prod.apply((x, s) -> cons(() -> x, () -> unfold(g).apply(s)));
	}

	public static <X> ConsList<X> nil() {
		return LazyList.nil();
	}

	public static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return LazyList.cons(x, xs);
	}

}
