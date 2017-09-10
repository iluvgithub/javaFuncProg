package com.sandbox.funcprog;

import java.util.function.Function;

public class Identity<X> implements Functor<X> {

	private final X x;

	private Identity(X x) {
		this.x = x;
	}

	@Override
	public <Y> Identity<Y> map(Function<X, Y> f) {
		return new Identity<>(f.apply(x));
	}
}
