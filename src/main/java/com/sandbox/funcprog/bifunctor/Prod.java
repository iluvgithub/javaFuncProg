package com.sandbox.funcprog.bifunctor;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 
 * Both represents a product of any two Objects.
 *
 * @param <LEFT>
 * @param <RIGHT>
 */
public class Prod<LEFT, RIGHT> {

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

	public <Y1, Y2> Prod<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return prod(f.apply(l), g.apply(r));
	}

	public <Y> Prod<Y, RIGHT> mapLeft(Function<LEFT, Y> f) {
		return map(f, r -> r);
	}

	public <Z> Prod<LEFT, Z> mapRight(Function<RIGHT, Z> g) {
		return map(l -> l, g);
	}

	public <Z> Z apply(BiFunction<LEFT, RIGHT, Z> bi) {
		return bi.apply(left(), right());
	}
}
