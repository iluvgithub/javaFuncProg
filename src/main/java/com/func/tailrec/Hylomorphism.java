package com.func.tailrec;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.func.Prod;

public class Hylomorphism {

	public static <X, Y, Z> Function<X, Z> hylo(//
			Predicate<X> isDone, //
			Function<X, X> newState, //
			Function<X, Y> newElement, //
			BiFunction<Z, Y, Z> folder, //
			Z z) {
		return x -> hylo(isDone, newState, newElement, folder, z, x).eval();
	}

	public static <X, Y, Z> Bouncer<Z> hylo(//
			Predicate<X> isDone, //
			Function<X, X> newState, //
			Function<X, Y> newElement, //
			BiFunction<Z, Y, Z> folder, //
			Z z, //
			X x //
	) {
		return hylo(s -> Optional.of(s).//
				filter(isDone.negate()).//
				map(Prod.bracket(newElement, newState)), folder, z, x);
	}

	public static <X, Y, Z> Bouncer<Z> hylo(//
			Function<X, Optional<Prod<Y, X>>> g, //
			BiFunction<Z, Y, Z> c, //
			Z z, X x0) {
		return () -> g.apply(x0).//
				map(Prod.folder((y, x) -> hylo(g, c, c.apply(z, y), x))).//
				orElse(Bouncer.done(z));
	}

	public static <X, Y, Z> Function<X, Z> hylo(//
			Function<X, Optional<Prod<Y, X>>> g, //
			Z z, //
			BiFunction<Z, Y, Z> c //
	) {
		return x -> hylo(g, c, z, x).eval();
	}
}
