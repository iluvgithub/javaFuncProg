package com.func.list;

import static com.func.Curry.flipArgs;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.func.Curry;
import com.func.Prod;
import com.func.tailrec.Bouncer;

public class Hylomorphism {

	/**
	 * Provides a function from I(input) -> O(output), using an intermediary data
	 * structure (list of A)
	 * 
	 * @param isDone
	 *            end condition
	 * @param unfoldSeed
	 *            to build intermediary data
	 * @param output
	 *            default output
	 * @param foldSeed
	 *            to build output based on intermediary data
	 * 
	 * @return function I -> O
	 * 
	 */
	public static <I, A, O> Function<I, O> hylo(//
			Predicate<I> isDone, //
			Function<I, Prod<A, I>> unfoldSeed, //
			O output, //
			BiFunction<O, A, O> foldSeed) {
		return hylo(Curry.<I, Prod<A, I>> //
				optionalize(isDone, unfoldSeed), //
				output, //
				foldSeed);

	}

	/**
	 * @param unfoldSeed
	 *            to build optional intermediary data
	 * @param output
	 *            default output
	 * @param foldSeed
	 *            to build output based on intermediary data
	 * @return function I -> O
	 */
	public static <I, A, O> Function<I, O> hylo(//
			Function<I, Optional<Prod<A, I>>> unfoldSeed, //
			O output, //
			BiFunction<O, A, O> foldSeed) {

		return input -> hylo(unfoldSeed, foldSeed, output, input).eval();
	}

	public static <I, A, O> Bouncer<O> hylo(//
			Function<I, Optional<Prod<A, I>>> unfoldSeed, //
			BiFunction<O, A, O> foldSeed, //
			O output, //
			I input) {
		return unfoldSeed.apply(input).//
				map(recursiveStep(unfoldSeed, foldSeed, output)).//
				orElse(Bouncer.done(output));
	}

	private static <I, A, O> Function<Prod<A, I>, Bouncer<O>> recursiveStep(
			Function<I, Optional<Prod<A, I>>> unfoldSeed, //
			BiFunction<O, A, O> foldSeed, //
			O output) {
		return Prod.<A, I, Bouncer<O>>folder((a, i) -> () -> hylo(unfoldSeed, foldSeed, foldSeed.apply(output, a), i));
	}

	/**
	 * 
	 * Provides a function from I(input) -> O(output), using an intermediary data
	 * structure (list of A), except this time, order is reversed
	 * 
	 * @param isDone
	 *            end condition
	 * @param unfoldSeed
	 *            to build intermediary data
	 * @param output
	 *            default output
	 * @param foldSeed
	 *            to build output based on intermediary data (right fold in that
	 *            case)
	 * @return function I -> O
	 * 
	 */

	public static <I, A, O> Function<I, O> hylo(//
			Predicate<I> isDone, //
			Function<I, Prod<A, I>> unfoldSeed, //
			BiFunction<A, O, O> rightFoldSeed, //
			O output) {
		return hylo(Curry.<I, Prod<A, I>> //
				optionalize(isDone, unfoldSeed), rightFoldSeed, output);
	}

	/**
	 * Provides a function from I(input) -> O(output), using an intermediary
	 * 
	 * data structure (list of A), except this time, order is reversed
	 *
	 * @param isDone
	 *            end condition
	 * @param unfoldSeed
	 *            to build intermediary data
	 * @param output
	 *            default output
	 * @param foldSeed
	 *            to build output based on intermediary data (right fold in that
	 *            case)
	 * 
	 * @return function I -> O
	 * 
	 */

	public static <I, A, O> Function<I, O> hylo(//
			Function<I, Optional<Prod<A, I>>> unfoldSeed, //
			BiFunction<A, O, O> rightFoldSeed, //
			O output) {

		return hylo(unfoldSeed, List.<A>empty(), flipArgs(List::cons)).//
				andThen(hylo(List::out, output, flipArgs(rightFoldSeed)));
	}

	/**
	 * 
	 * filters a list
	 * 
	 * @param p
	 *            filtering predicate
	 * @return list containing only elements which abides by predicate
	 * 
	 */
	public static <X> Function<List<X>, List<X>> filter(Predicate<X> p) {
		return Hylomorphism.<List<X>, X, List<X>> //
				hylo(List::out, //
						filterSeed(p), //
						List.<X>empty());

	}

	private static <X> BiFunction<X, List<X>, List<X>> filterSeed(Predicate<X> p) {
		return (x, xs) -> Optional.of(x).filter(p).map(u -> List.cons(u, xs)).orElse(xs);
	}

	public static <U, V> Function<List<U>, List<V>> flatMapper(Function<U, List<V>> h) {
		return Anamorphism.mapper(h).andThen(Catamorphism.foldl(List.empty(), Anamorphism::concat));
	}
}
