package com.func.tailrec;

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

}
