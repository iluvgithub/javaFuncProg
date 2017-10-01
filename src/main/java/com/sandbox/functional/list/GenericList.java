package com.sandbox.functional.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;

public class GenericList<L, R1, R2> {

	private final Either<L, Both<R1, R2>> either;

	public GenericList(Either<L, Both<R1, R2>> either) {
		super();
		this.either = either;
	}

	protected <Y1, Y2> Either<L, Both<Y1, Y2>> preMap(Function<R1, Y1> f, Function<R2, Y2> g) {
		return either.map(x -> x, both -> both.map(f, g));
	}

	protected <Z> Z apply(Function<L, Z> f, BiFunction<R1, R2, Z> biFunction) {
		return either.apply(l -> f.apply(l), both -> both.apply(biFunction));
	}
}
