package com.sandbox.functional.list;

import java.util.function.Function;

import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

//X ->  1 +  XxT
//f -> Id +  fxIdT
public class ListlFunctor<T, X> extends AbstractListFunctor<T, X, X, T> {

	protected ListlFunctor(Either<Void, Both<X, T>> either) {
		super(either);
	}

	@Override
	public <Y> ListlFunctor<T, Y> map(Function<X, Y> g) {
		return map(t -> t, g);
	}

	@Override
	public <Y1, Y2> ListlFunctor<Y1, Y2> map(Function<T, Y1> f, Function<X, Y2> g) {
		return new ListlFunctor<>(preMap(g, f));
	}

	@Override
	public X accept(SeedFunctorVisitor<T, X> visitor) {
		return visitor.visit(this);
	}

}
