package com.func.monad;

import static com.func.Curry.uncurry;

import java.util.function.Function;

import com.func.Prod;
import com.func.vacuum.None;

@FunctionalInterface
public interface StateMonad<STATE, A> extends Function<STATE, Prod<STATE, A>> {

	public default Function<STATE, Prod<STATE, A>> asFunction() {
		return this;
	}

	public static <S, X> StateMonad<S, X> unit(X x) {
		return s -> Prod.prod(s, x);
	}

	public static <S, X> StateMonad<S, X> fromFunction(Function<S, Prod<S, X>> f) {
		return s -> f.apply(s);
	}

	public default <B> StateMonad<STATE, B> flatMap(Function<A, StateMonad<STATE, B>> h) {
		return fromFunction(this.andThen(Prod.folder(uncurry(h.andThen(StateMonad::asFunction))).compose(Prod::swap)));
	}

	public static <S> StateMonad<S, S> get() {
		return s -> Prod.prod(s, s);
	}

	public static <S> Function<S, StateMonad<S, None>> put() {
		return s -> s0 -> Prod.prod(s, None.NONE);
	}
}
