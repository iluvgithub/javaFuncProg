package com.func.tailrec;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Bouncer<T> extends Supplier<Bouncer<T>> {

	default T eval() {
		return Stream.iterate(this, Bouncer::get).//
				filter(Bouncer::isDone).//
				map(Bouncer::eval).//
				findFirst().//
				get();
	}

	default Boolean isDone() {
		return false;
	}

	public static <X> Bouncer<X> done(final X x) {
		return new Bouncer<X>() {
			@Override
			public Boolean isDone() {
				return true;
			}

			@Override
			public X eval() {
				return x;
			}

			@Override
			public Bouncer<X> get() {
				throw new RuntimeException("never call me!");
			}
		};
	}

	public static <X, Y, Z> Bouncer<Z> hylo(//
			Predicate<X> isDone, //
			Function<X, X> newState, //
			Function<X, Y> newElement, //
			BiFunction<Z, Y, Z> folder, //
			Z z, //
			X x //
	) {
		if (isDone.test(x)) {
			return done(z);
		} else {
			return () -> hylo(isDone, newState, newElement, folder, //
					folder.apply(z, newElement.apply(x)), //
					newState.apply(x)//
			);
		}
	}
}
