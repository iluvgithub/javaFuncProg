package com.sandbox.functional.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.BiFunctor;
import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.visitor.SeedFunctor;

public abstract class AbstractListFunctor<T, X, U, V> implements BiFunctor<T, X>, SeedFunctor<T, X> {

	private final Either<Void, Both<U, V>> either;

	protected AbstractListFunctor(Either<Void, Both<U, V>> either) {
		this.either = either;
	}

	protected <Z> Z apply(Z z, BiFunction<U, V, Z> biFunction) {
		return either.apply(v -> z, both -> both.apply(biFunction));
	}

	protected <Y1, Y2> Either<Void, Both<Y1, Y2>> preMap(Function<U, Y1> f, Function<V, Y2> g) {
		return either.map(x -> x, both -> both.map(f, g));
	}

}
