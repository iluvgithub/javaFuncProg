package com.sandbox.funcprog.oldstream;

import static com.sandbox.funcprog.bifunctor.Plus.asSwitch;
import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.tailrecursion.Bouncer;

public abstract class LazyStream<T> {

	public static <X> LazyStream<X> nil() {
		return new Empty<>();
	}

	public static <X> LazyStream<X> tau(X x) {
		return cons(() -> x, () -> nil());
	}

	public static <X> LazyStream<X> cons(Supplier<X> head, Supplier<LazyStream<X>> tail) {
		return new Cons<>(head, tail);
	}

	protected abstract Optional<Prod<T, LazyStream<T>>> headTail();

	public <Z> Z apply(Z defaultValue, BiFunction<T, LazyStream<T>, Z> biFunction) {
		return headTail().map(headTail -> headTail.apply(biFunction)).orElse(defaultValue);
	}

	public <Z> Z foldLeft(Z z, Function<Z, Function<T, Z>> f) {
		return foldLeft(z, (x, y) -> f.apply(x).apply(y));
	}

	public <Z> Z foldLeft(Z z, BiFunction<Z, T, Z> biFunction) {
		return bounceFoldLeft(z, biFunction).call();
	}

	private <Z> Bouncer<Z> bounceFoldLeft(Z z, BiFunction<Z, T, Z> bi) {
		return apply(resume(z), (head, tail) -> suspend(() -> tail.bounceFoldLeft(bi.apply(z, head), bi)));
	}

	public String trace() {
		return foldLeft("", (s, y) -> s + (s.equals("") ? "" : ".") + y);
	}

	public static <S, X> Function<S, LazyStream<X>> anamorphism(Predicate<S> p, Function<S, Prod<X, S>> g) {
		return anamorphism(s0 -> asSwitch(p).apply(s0).apply(s -> Optional.empty(), s -> Optional.of(g.apply(s))));
	}

	public static <S, X> Function<S, LazyStream<X>> anamorphism(Function<S, Optional<Prod<X, S>>> g) {
		return s0 -> g.apply(s0).map(prod -> prod.apply((x, s) -> cons(() -> x, () -> anamorphism(g).apply(s))))
				.orElse(nil());
	}

	public <Y> LazyStream<Y> map(Function<T, Y> f) {
		return null;
		/**
		LazyStream.<LazyStream<T>,Y>anamorphism( ts-> ts.apply( nil(), (t,ts) -> cons(
					() -> f.apply(t),
					() -> ts.map(f)
					)
				) ).apply(this);
				*/
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
