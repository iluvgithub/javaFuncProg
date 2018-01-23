package com.sandbox.funcprog.bifunctor;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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

	protected Prod(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Prod<L, R> prod(L l, R r) {
		return new Prod<>(l, r);
	}

	public static <L, R> Function<R, Prod<L, R>> prodFromRight(L l) {
		return r -> prod(l, r);
	}

	public static <L, R> Function<L, Prod<L, R>> prodFromLeft(R r) {
		return l -> prod(l, r);
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

	public static <L, R, Z> Function<Prod<L, R>, Z> makeApply(BiFunction<L, R, Z> bi) {
		return pair -> pair.apply(bi);
	}

	public static <L, R> Predicate<Prod<L, R>> makePredicate(BiFunction<L, R, Boolean> bi) {
		return pair -> makeApply(bi).apply(pair);
	}

}
