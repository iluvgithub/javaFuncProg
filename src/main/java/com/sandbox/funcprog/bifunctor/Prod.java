package com.sandbox.funcprog.bifunctor;

import java.util.function.Function;

public class Prod<LEFT, RIGHT> implements BiFunctor<LEFT, RIGHT>{

	private final LEFT l;
	private final RIGHT r;

	private Prod(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Prod<L, R> prod(L l, R r) {
		return new Prod<>(l, r);
	}

	public LEFT left() {
		return l;
	}

	public RIGHT right() {
		return r;
	}

	@Override
	public <Y1, Y2> BiFunctor<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return prod(f.apply(l),g.apply(r));
	}
}
