package com.sandbox.functional.list;

import static com.sandbox.functional.bifunctor.Both.both;
import static com.sandbox.functional.bifunctor.Either.left;
import static com.sandbox.functional.bifunctor.Either.right;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.sandbox.functional.bifunctor.Both;
import com.sandbox.functional.bifunctor.Either;
import com.sandbox.functional.functor.Initial;
import com.sandbox.functional.visitor.SeedFunctorVisitor;

public class Listl<T> implements Initial<T> {

	private final ListlFunctor<T, Listl<T>> values;

	private Listl(Either<Void, Both<Listl<T>, T>> input) {
		this.values = new ListlFunctor<>(input);
	}

	public static <X> Listl<X> nil() {
		return new Listl<>(left(null));
	}

	public static <X> Listl<X> snoc(Listl<X> xs, X x) {
		return new Listl<>(right(both(xs, x)));
	}

	public Listl<T> snoc(T t) {
		return snoc(this, t);
	}

	@Override
	public ListlFunctor<T, Initial<T>> out() {
		return values.map(x -> x);
	}

	public <Z> Z foldl(Z z, BiFunction<Z, T, Z> bi) {
		return cata(seedFunctor -> seedFunctor.accept(visitor(z, bi)));

	}

	private <Z> SeedFunctorVisitor<T, Z> visitor(Z z, BiFunction<Z, T, Z> bi) {
		return new SeedFunctorVisitor<T, Z>() {
			@Override
			public Z visit(ListlFunctor<T, Z> target) {
				return target.apply(z, bi);
			}
		};
	}

	@Override
	public <Y> Listl<Y> map(Function<T, Y> f) {
		return foldl(nil(), (ts, t) -> snoc(ts, f.apply(t)));
	}

	public Listr<T> converse() {
		return foldl(Listr.nil(), (t, ts) -> Listr.cons(ts, t));

	}

	public String trace() {
		return surround(foldl("", Listl::append).toString());
	}

	private static <X> String append(String s, X x) {
		return s + (s.length() > 0 ? "." : "") + x;
	}

	private static String surround(String s) {
		return "[" + s + "]";
	}

}
