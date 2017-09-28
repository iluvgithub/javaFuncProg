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
public class Prod<LEFT, RIGHT>     {

	private final LEFT l;
	private final RIGHT r;

	private Prod(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Prod<L, R> both(L l, R r) {
		return new Prod<>(l, r);
	}

	public LEFT left() {
		return l;
	}

	public RIGHT right() {
		return r;
	}
 
	public <Y1, Y2> Prod<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return both(f.apply(l), g.apply(r));
	}

	// (a+b) -> (b+a)
	public Prod<RIGHT, LEFT> swap() {
		return both(right(), left());
	}

	public static <A, B, C> Prod<A, Prod<B, C>> assocr(Prod<Prod<A, B>, C> input) {
		return both(input.left().left(), both(input.left().right(), input.right()));
	}

	public <Z> Z apply(BiFunction<LEFT, RIGHT, Z> bi) {
		return bi.apply(left(), right());
	}
}
