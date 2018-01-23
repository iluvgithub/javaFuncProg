package com.sandbox.funcprog.sample;

import static com.sandbox.funcprog.bifunctor.Prod.makeApply;
import static com.sandbox.funcprog.bifunctor.Prod.makePredicate;
import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Prod.prodFromRight;
import static com.sandbox.funcprog.stream.ConsList.flatten;

import java.util.function.Function;
import java.util.function.Predicate;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.stream.Anamorphism;
import com.sandbox.funcprog.stream.ConsList;

public class PrimeDecomposer {

	public static ConsList<Integer> decompose(Integer n) {
		return flatten(new Anamorphism<>(makePredicate(PrimeDecomposer::isOver), makeApply(PrimeDecomposer::step))
				.unfold(prod(2, n)));
	}

	private static Prod<ConsList<Integer>, Prod<Integer, Integer>> step(Integer d, Integer n) {
		return new Anamorphism<>(isNotDivisible(d), divide(d)).unfoldWithState(n).mapRight(prodFromRight(d + 1));
	}

	private static Boolean isOver(Integer d, Integer n) {
		return n == 1;
	}

	private static Predicate<Integer> isNotDivisible(Integer d) {
		return n -> n % d != 0;
	}

	private static Function<Integer, Prod<Integer, Integer>> divide(Integer d) {
		return n -> prod(d, n / d);
	}

}
