package com.sandbox.funcprog.stream;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;
import static com.sandbox.funcprog.bifunctor.Prod.*;

public abstract class LazyStream<T> {

	public static <X> LazyStream<X> nil() {
		return new Empty<>();
	}

	public static <X> LazyStream<X> cons(Supplier<X> head, Supplier<LazyStream<X>> tail) {
		return new Cons<>(head, tail);
	}

	protected abstract Optional<Prod<T, LazyStream<T>>> headTail();

	public <Z> Z apply(BiFunction<T, LazyStream<T>, Z> biFunction) {
		return null;
	}

	private static class Empty<T> extends LazyStream<T> {

		@Override
		protected Optional<Prod<T, LazyStream<T>>> headTail() {
			return Optional.empty();
		}
	}

	private static class Cons<T> extends LazyStream<T> {
		private final Supplier<T> head;
		private final Supplier<LazyStream<T>> tail;

		private Cons(Supplier<T> head, Supplier<LazyStream<T>> tail) {
			super();
			this.head = head;
			this.tail = tail;
		}

		@Override
		protected Optional<Prod<T, LazyStream<T>>> headTail() {
			return Optional.of(prod(head.get(), tail.get()));
		}

	}

}
