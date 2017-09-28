package com.sandbox.funcprog.bifunctor;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * Either represents an alternative between two Objects.
 *
 * @param <LEFT>
 * @param <RIGHT>
 */
public abstract class Plus<LEFT, RIGHT> {

	public static <L, R> Plus<L, R> left(L l) {
		return new Left<>(l);
	}

	public static <L, R> Plus<L, R> right(R r) {
		return new Right<>(r);
	}

	public abstract <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g);

	public static <T> Function<T, Plus<T, T>> asSwitch(Predicate<T> p) {
		return t -> p.test(t) ? left(t) : right(t);
	}

	public <Y1, Y2> Plus<Y1, Y2> map(Function<LEFT, Y1> f, Function<RIGHT, Y2> g) {
		return apply(l -> left(f.apply(l)), r -> right(g.apply(r)));
	}

	// (a+b) -> (b+a)
	public Plus<RIGHT, LEFT> swap() {
		return apply(a -> right(a), b -> left(b));
	}

	// (a+b)+c -> a+(b+c)
	public static <A, B, C> Plus<A, Plus<B, C>> assocr(Plus<Plus<A, B>, C> input) {
		return input.apply(ab -> ab.apply(a -> left(a), b -> right(new Left<>(b))), c -> right(new Right<>(c)));
	}

	private static final class Left<LEFT, RIGHT> extends Plus<LEFT, RIGHT> {

		private final LEFT l;

		public Left(LEFT l) {
			this.l = l;
		}

		@Override
		public <Z> Z apply(Function<LEFT, Z> f, Function<RIGHT, Z> g) {
			return f.apply(l);
		}
	}

	private static final class Right<LEFT, RIGHT> extends Plus<LEFT, RIGHT> {

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