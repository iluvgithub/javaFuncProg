package com.sandbox.functional.list;

import java.util.function.Function;

import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

//X ->  1 +   TxX
//f -> Id + IdTxf
public class ListrFunctor<T, X> extends AbstractListFunctor<T, X, T, X> {

	protected ListrFunctor(Either<Void, Both<T, X>> either) {
		super(either);
	}

	@Override
	public <Y> ListrFunctor<T, Y> map(Function<X, Y> f) {
		return map(t -> t, f);
	}

	@Override
	public <Y1, Y2> ListrFunctor<Y1, Y2> map(Function<T, Y1> f, Function<X, Y2> g) {
		return new ListrFunctor<>(preMap(f, g));
	}

	@Override
	public X accept(SeedFunctorVisitor<T, X> visitor) {
		return visitor.visit(this);
	}
}
