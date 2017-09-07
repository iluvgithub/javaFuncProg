package com.sandbox.funcprog.recursion;

import java.util.stream.Stream;

@FunctionalInterface
public interface Bouncer<T> {

	Bouncer<T> next();

	default Boolean finished() {
		return false;
	}

	default T result() {
		throw new Error("::result not implemented");
	}

	default T call() {
		return Stream.iterate(this, Bouncer::next).filter(Bouncer::finished).findFirst().get().result();
	}

	public static <T> Bouncer<T> suspend(final Bouncer<T> suspendedCall) {
		return suspendedCall;
	}

	public static <T> Bouncer<T> resume(T value) {
		return new Done<>(value);
	}

	public class Done<T> implements Bouncer<T> {
		private final T value;

		private Done(T value) {
			this.value = value;
		}
 

		@Override
		public Boolean finished() {
			return true;
		}

		@Override
		public T result() {
			return value;
		}


		@Override
		public Bouncer<T> next() {  
			throw new Error("::next not implemented");
		}
	}

}
