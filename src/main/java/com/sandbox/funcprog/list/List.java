package com.sandbox.funcprog.list;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface List<T> {

	<Z> Z apply(Z z, BiFunction<T, List<T>, Z> bif);

	<Z> Z foldr(Z id, BiFunction<T, Z, Z> bi);

	T reduceR(T id, BinaryOperator<T> bi);

	<Z> Z foldl(Z z, BiFunction<Z, T, Z> bi);

	String trace();

	T head();
	List<T> tail();

	T last();

	List<T> reverse();

	<Z> List<Z> map(Function<T, Z> f);

	List<T> concat(List<T> right);

	<Z> List<Z> cumulr(Z e, BiFunction<T, Z, Z> bi);

	<Z> List<List<T>> tails();

	<Z> List<Z> cumull(Z e, BiFunction<Z, T, Z> bi);

	<Z> List<List<T>> inits();

}
