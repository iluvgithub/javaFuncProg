package com.func.tailrec;

import static com.func.tailrec.Bouncer.done;

import java.util.function.Function;

public class ComposedFunction<X, Y> implements Function<X, Y> {

	private Function<X, Bouncer<Y>> bf;

	private ComposedFunction(Function<X, Bouncer<Y>> bf) {
		this.bf = bf;
	}

	public static <A> Function<A, A> identity() {
		return new ComposedFunction<>(x -> done(x));
	}

	@Override
	public Y apply(X x) {
		return bf.apply(x).eval();

	}

	@Override
	public <U> Function<U, Y> compose(Function<? super U, ? extends X> before) {
		return new ComposedFunction<>(u -> () -> bf.apply(before.apply(u)));
	}

}
