package com.sandbox.funcprog.list;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class ListPlus<T> extends ListNil<T> {

	private final T t;

	private ListPlus(T t, ListNil<T> list) {
		super(list.values);
		this.t = t; 
	}

	public static final <X> ListPlus<X> wrap(X x) {
		return new ListPlus<>(x, ListNil.nil());
	}

	public static final <X> ListPlus<X> cons(X x, ListPlus<X> xs) {
		return new ListPlus<>(x, xs.asListNil());
	}

	private ListNil<T> asListNil() {
		return ListNil.cons(t, this);
	}

	public <Z> Z foldlPlus(Function<T, Z> f, BiFunction<Z, T, Z> bi) {
		return super.foldl(f.apply(t), bi);
	}

	@Override
	public String trace() {
		return withBrackets(foldlPlus(t-> new StringBuffer(t.toString()), ListNil::append));
	}

	public T reduce(BinaryOperator<T> bi) {
		return foldlPlus(x -> x, bi);
	}

	public <Z> Z foldrPlus(Function<T, Z> f, BiFunction<Z, T, Z> bi) {
		return null;
	}

}
