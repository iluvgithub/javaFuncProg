package com.sandbox.functional.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.BiFunctor;
import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.visitor.SeedFunctor;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

//X ->  1 +  XxT
//f -> Id +  fxIdT
public class ListlFunctor<T, X> implements BiFunctor<T, X>, SeedFunctor<T, X> {

	private final Either<Void, Both<X, T>> either;

	protected ListlFunctor(Either<Void, Both<X, T>> either) {
		this.either = either;
	}

	@Override
	public <Y> ListlFunctor<T, Y> map(Function<X, Y> g) {
		return map(t -> t, g);
	}

	@Override
	public <Y1, Y2> ListlFunctor<Y1, Y2> map(Function<T, Y1> f, Function<X, Y2> g) {
		return new ListlFunctor<>(either.map(x -> x, both -> both.map(g, f)));
	}

	public <Z> Z apply(Z z, BiFunction<X, T, Z> biFunction) {
		return either.apply(v -> z, both -> both.apply(biFunction));
	}

	@Override
	public X accept(SeedFunctorVisitor<T, X> visitor) {
		return visitor.visit(this);
	}

}
