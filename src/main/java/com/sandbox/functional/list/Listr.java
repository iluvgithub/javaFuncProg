package com.sandbox.functional.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.functor.Initial;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

public class Listr<T> implements Initial<T> {

	private final ListrFunctor<T, Listr<T>> values;

	private Listr(Either<Void, Both<T, Listr<T>>> input) {

		this.values = new ListrFunctor<>(input);

	}

	public static <X> Listr<X> nil() {

		return new Listr<>(Either.left(null));

	}

	public static <X> Listr<X> cons(X x, Listr<X> xs) {

		return new Listr<>(Either.right(Both.both(x, xs)));

	}

	@Override

	public ListrFunctor<T, Initial<T>> out() {

		return values.map(x -> x);

	}

	public <Z> Z foldl(Z z0, BiFunction<Z, T, Z> bi) {

		return null;

	}

	public <Z> Z foldr(Z z, BiFunction<T, Z, Z> bi) {

		return cata(seedFunctor -> seedFunctor.accept(foo(z, bi)));

	}

	private <Z> SeedFunctorVisitor<T, Z> foo(Z z, BiFunction<T, Z, Z> bi) {

		return new SeedFunctorVisitor<T, Z>() {

			@Override

			public Z visit(ListrFunctor<T, Z> target) {

				return target.apply(z, bi);

			}

		};

	}

	public Listr<T> reverse() {

		return null;

	}

	public String trace() {
		return surround(foldr("", Listr::append).toString());
	}

	private static <X> String append(X x, String s) {
		return x + (s.length() > 0 ? "." : "") + s;
	}

	private static String surround(String s) {
		return "[" + s + "]";
	}

	@Override

	public <Y> Listr<Y> map(Function<T, Y> f) {
		return null;
	}

}
