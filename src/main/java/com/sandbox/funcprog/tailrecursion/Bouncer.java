package com.sandbox.funcprog.tailrecursion;

import java.util.stream.Stream;

@FunctionalInterface
public interface Bouncer<T> {

	public static <X> Bouncer<X> resume(X value) {
		return new Done<>(value);
	}

	public static <X> Bouncer<X> suspend(Bouncer<X> supplier) {
		return supplier;
	}

	Bouncer<T> apply();

	default boolean isComplete() {
		return false;
	}

	default T result() {
		throw new Error("not implemented");
	}

	default T call() {
		return Stream.iterate(this, Bouncer::apply).filter(Bouncer::isComplete).findFirst().get().result();
	}

	public static class Done<T> implements Bouncer<T> {

		private final T value;

		private Done(T value) {
			this.value = value;
		}

		@Override
		public boolean isComplete() {
			return true;
		}

		@Override
		public T result() {
			return value;
		}

		@Override
		public Done<T> apply() {
			throw new Error("not implemented.");
		}

	}

}
