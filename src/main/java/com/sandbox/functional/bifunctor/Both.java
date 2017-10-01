package com.sandbox.functional.bifunctor;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 
 * Both represents a product of any two Objects.
 *
 * @param <LEFT>
 * @param <RIGHT>
 */
public class Both<LEFT, RIGHT>     {

	private final LEFT l;
	private final RIGHT r;

	private Both(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Both<L, R> both(L l, R r) {
		return new Both<>(l, r);
	}

	public LEFT left() {
		return l;
	}

	public RIGHT right() {
		return r;
	}
 
	public <Y1, Y2> Both<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return both(f.apply(l), g.apply(r));
	}

	// (a+b) -> (b+a)
	public Both<RIGHT, LEFT> swap() {
		return both(right(), left());
	}

	public static <A, B, C> Both<A, Both<B, C>> assocr(Both<Both<A, B>, C> input) {
		return both(input.left().left(), both(input.left().right(), input.right()));
	}

	public <Z> Z apply(BiFunction<LEFT, RIGHT, Z> bi) {
		return bi.apply(left(), right());
	}
}