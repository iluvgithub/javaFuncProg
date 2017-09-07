package com.sandbox.funcprog.bifunctor;

public class Prod<LEFT, RIGHT> {

	private final LEFT l;
	private final RIGHT r;

	private Prod(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Prod<L, R> pord(L l, R r) {
		return new Prod<>(l, r);
	}

	public LEFT left() {
		return l;
	}

	public RIGHT right() {
		return r;
	}
}
