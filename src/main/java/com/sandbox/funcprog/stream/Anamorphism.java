package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.sandbox.funcprog.bifunctor.Prod;

public class Anamorphism {

	private Anamorphism() {
	}

	public static <S, X> Function<S, ConsList<X>> unfold(Predicate<S> stoppingCondition, Function<S, Prod<X, S>> g) {
		return unfold(s0 -> of(s0).filter(stoppingCondition.negate()).map(g));
	}

	public static <S, X> Function<S, ConsList<X>> unfold(Function<S, Optional<Prod<X, S>>> g) {
		return g.andThen(opt -> opt.map(unfoldStep(g)).orElse(nil()));
	}

	private static <S, X> Function<Prod<X, S>, ConsList<X>> unfoldStep(Function<S, Optional<Prod<X, S>>> g) {
		return prod -> prod.apply((x, s) -> cons(() -> x, () -> unfold(g).apply(s)));
	}

	private static <X> ConsList<X> nil() {
		return LazyList.nil();
	}

	private static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return LazyList.cons(x, xs);
	}

	public static ConsList<Integer> from(Integer n) {
		return iterate(n, x -> x + 1);
	}

	public static ConsList<Integer> fromTo(Integer fromIncluded, Integer toExcluded) {
		return from(fromIncluded).takeWhile(n -> n < toExcluded);
	}

	public static <Z> ConsList<Z> iterate(Z from, UnaryOperator<Z> op) {
		return unfold(z -> false, iterateStep(op)).apply(from);
	}

	private static <Z> Function<Z, Prod<Z, Z>> iterateStep(UnaryOperator<Z> op) {
		return z -> prod(z, op.apply(z));
	}

}
