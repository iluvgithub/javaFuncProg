package com.sandbox.functional.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.BiFunctor;
import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.visitor.SeedFunctor;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

//X ->  1 +   TxX
//f -> Id + IdTxf
public class ListrFunctor<T, X> implements SeedFunctor<T, X>, BiFunctor<T, X> {

	private final Either<Void, Both<T, X>> either;

	protected ListrFunctor(Either<Void, Both<T, X>> either) {
		this.either = either;
	}

	@Override
	public <Y> ListrFunctor<T, Y> map(Function<X, Y> f) {
		return map(t -> t, f);
	}

	@Override
	public <Y1, Y2> ListrFunctor<Y1, Y2> map(Function<T, Y1> f, Function<X, Y2> g) {
		return new ListrFunctor<>(either.map(x -> x, both -> both.map(f, g)));
	}

	public X accept(SeedFunctorVisitor<T, X> visitor) {
		return visitor.visit(this);
	}

	protected <Z> Z apply(Z z, BiFunction<T, X, Z> biFunction) {
		return either.apply(v -> z, both -> both.apply(biFunction));
	}

}
