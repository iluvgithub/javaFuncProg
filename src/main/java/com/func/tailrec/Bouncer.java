package com.func.tailrec;

import java.util.stream.Stream;

@FunctionalInterface
public interface Bouncer<T> {

	default T eval() {
		return Stream.iterate(this, Bouncer::resume).//
				filter(Bouncer::isDone).//
				map(Bouncer::eval).//
				findFirst().//
				get();
	}

	Bouncer<T> resume();

	default Boolean isDone() {
		return false;
	}

	public static <X> Bouncer<X> done(X x) {
		return new Done<>(x);
	}

	public static class Done<T> implements Bouncer<T> {
		private final T t;

		private Done(T t) {
			this.t = t;
		}

		@Override
		public Boolean isDone() {
			return true;
		}

		@Override
		public T eval() {
			return t;
		}

		@Override
		public Bouncer<T> resume() {
			throw new RuntimeException("never call me!");
		}
	}
}
