package com.sandbox.funcprog.typefunctor.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Both;
import com.sandbox.funcprog.bifunctor.Either;
import com.sandbox.funcprog.functor.visitor.CataSeed;
import com.sandbox.funcprog.functor.visitor.CataSeedVisitor;

public class ListNilFunctor<T, X> implements CataSeed<T, X> {

	private final Either<Void, Both<T, X>> either;

	protected ListNilFunctor(Either<Void, Both<T, X>> either) {
		super();
		this.either = either;
	}

	public <Z> Z apply(Z z, BiFunction<T, X, Z> bi) {
		return either.apply(v -> z, both -> both.apply(bi));
	}

	@Override
	public <Y> ListNilFunctor<T, Y> map(Function<X, Y> f) {
		return new ListNilFunctor<>(either.map(l -> l, both -> both.map(t -> t, f)));
	}

	@Override
	public X accept(CataSeedVisitor<T, X> visitor) {
		return visitor.visit(this);
	}

}
