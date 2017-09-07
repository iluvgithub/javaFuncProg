package com.sandbox.funcprog.bifunctor;

import java.util.function.Function;

public abstract class Sum<LEFT, RIGHT> implements BiFunctor<LEFT, RIGHT> {

	public static <L, R> Sum<L, R> left(L l) {
		return new Left<>(l);
	}

	public static <L, R> Sum<L, R> right(R r) {
		return new Right<>(r);
	}

	public abstract <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g);

	@Override
	public <Y1, Y2> Sum<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return apply(l -> left(f.apply(l)), r -> right(g.apply(r)));
	}

	private static final class Left<LEFT, RIGHT> extends Sum<LEFT, RIGHT> {

		private final LEFT l;

		public Left(LEFT l) {
			this.l = l;
		}

		@Override
		public <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g) {
			return f.apply(l);
		}
	}

	private static final class Right<LEFT, RIGHT> extends Sum<LEFT, RIGHT> {

		private final RIGHT r;

		public Right(RIGHT r) {
			this.r = r;
		}

		@Override
		public <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g) {
			return g.apply(r);
		}
	}

}