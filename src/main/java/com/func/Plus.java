package com.func;


import static com.func.Prod.prod;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Plus<LEFT, RIGHT> {

	public static <L, R> Plus<L, R> inLeft(L l) {
		return new Left<>(l);
	}

	public static <L, R> Plus<L, R> inRight(R r) {
		return new Right<>(r);
	}

	public static <A> Function<A, Plus<A, A>> fromPredicate(Predicate<A> p) {
		return a -> Optional.of(a).//
				filter(p).//
				map(x -> Plus.<A,A>inLeft(a)).//
				orElse(inRight(a));
	}

	/**
	 * 
	 * @param f: Z < -- L
	 * @param g: Z < -- R
	 * @return [f,g] Z < -- L+R
	 */
	public static <L, R, Z> Function<Plus<L, R>, Z> fold(Function<L, Z> f, Function<R, Z> g) {
		return Curry.<Plus<L, R>, Prod<Function<L, Z>, Function<R, Z>>, Z> //
				partialLeft(Plus::apply, prod(f, g));
	}

	private <Z> Z apply(Prod<Function<LEFT, Z>, Function<RIGHT, Z>> fg) {
		return apply(fg.left(), fg.right());
	}

	/**
	 * 
	 * @param f: L2 < -- L1
	 * @param g: R2 < -- R1
	 * @return f+g L2+R2 < -- L1+R1
	 */
	public static <L1, L2, R1, R2> Function<Plus<L1, R1>, Plus<L2, R2>> plus(Function<L1, L2> f, Function<R1, R2> g) {
		return fold(f.andThen(Plus::inLeft), g.andThen(Plus::inRight));
	}

	protected abstract <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g);

	protected String trace() {
		return apply(Object::toString, Object::toString);
	}

	private static class Left<LEFT, RIGHT> extends Plus<LEFT, RIGHT> {
		private final LEFT l;

		public Left(LEFT l) {
			this.l = l;
		}

		@Override
		protected <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g) {
			return f.apply(l);
		}
	}

	private static class Right<LEFT, RIGHT> extends Plus<LEFT, RIGHT> {
		private final RIGHT r;

		protected Right(RIGHT r) {
			this.r = r;
		}

		@Override
		protected <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g) {
			return g.apply(r);
		}

	}
}
