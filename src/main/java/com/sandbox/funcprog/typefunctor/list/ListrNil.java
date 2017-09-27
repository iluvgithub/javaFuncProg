package com.sandbox.funcprog.typefunctor.list;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Both;
import com.sandbox.funcprog.bifunctor.Either;
import com.sandbox.funcprog.functor.Functor;
import com.sandbox.funcprog.functor.visitor.CataSeed;
import com.sandbox.funcprog.functor.visitor.CataSeedVisitor;
import com.sandbox.funcprog.typefunctor.TypeFunctor;

public class ListNil<T> extends TypeFunctor<T> {

	private final ListNilFunctor<T, TypeFunctor<T>> values;

	private ListNil(Either<Void, Both<T, TypeFunctor<T>>> either) {
		this.values = new ListNilFunctor<>(either);
	}

	public static final <X> ListNil<X> nil() {
		return new ListNil<>(Either.left(null));
	}

	public static final <X> ListNil<X> cons(X x, ListNil<X> xs) {
		return new ListNil<>(Either.right(Both.both(x, xs)));
	}

	@Override
	protected CataSeed<T, TypeFunctor<T>> out() {
		return values;
	}

	public <Z> Z foldr(Z z, BiFunction<T, Z, Z> bi) {
		return cata(cataSeed -> cataSeed.accept(new ListNilVisitor<>(z, bi)));
	}

	private static class ListNilVisitor<T, Z> implements CataSeedVisitor<T, Z> {
		private final Z z;
		private final BiFunction<T, Z, Z> bi;

		public ListNilVisitor(Z z, BiFunction<T, Z, Z> bi) {
			super();
			this.z = z;
			this.bi = bi;
		}

		@Override
		public Z visit(ListNilFunctor<T, Z> cataSeed) {
			return cataSeed.apply(z, bi);
		}
	};

	public String trace() {
		return withBrackets(foldr(new StringBuffer(), ListNil::append).reverse());
	}

	protected static <X> StringBuffer append(X x, StringBuffer sb) {
		return sb.append(sb.length() > 0 ? "." : "").append(x.toString());
	}

	protected static String withBrackets(StringBuffer sb) {
		return "[" + sb + "]";
	}

	@Override
	public <Y> Functor<Y> map(Function<T, Y> f) {
		return null;
	}

}
