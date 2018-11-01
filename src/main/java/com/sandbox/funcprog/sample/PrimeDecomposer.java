package com.sandbox.funcprog.sample;

import static com.sandbox.funcprog.bifunctor.Prod.makeApply;
import static com.sandbox.funcprog.bifunctor.Prod.makePredicate;
import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.stream.Anamorphism;
import com.sandbox.funcprog.stream.ConsList;

public class PrimeDecomposer {

	public static ConsList<Integer> decompose(Integer input) {
		Predicate<Integer> stop = n -> n == 1;
		BiFunction<Integer, Integer, Boolean> subStop = (d, n) -> n % d != 0;
		BiFunction<Integer, Integer, Prod<Integer, Integer>> step = (d, n) -> prod(d, n / d);
		UnaryOperator<Integer> op = d -> d + 1;
		return meta(stop, subStop, step, op).apply(2).apply(input);
	}

	protected static <D, N, X> Function<D, Function<N, ConsList<X>>> meta(////////////////////
			Predicate<N> stop, /////////////////////////////////////////////////////////////
			BiFunction<D, N, Boolean> subStop, /////////////////////////////////////////////
			BiFunction<D, N, Prod<X, N>> step, /////////////////////////////////////////////
			UnaryOperator<D> op ////////////////////////////////////////////////////////////
	) {
		return d -> n -> new Anamorphism<>(subPredicate(stop), subLoop(subStop, step, op)).unfold(prod(d, n))
				.filter(Optional::isPresent).map(Optional::get);
	}

	private static <D, N, X> Predicate<Prod<D, N>> subPredicate(Predicate<N> stop) {
		return makePredicate((dd, nn) -> stop.test(nn));
	}

	private static <D, N, X> Function<Prod<D, N>, Prod<Optional<X>, Prod<D, N>>> subLoop(
			BiFunction<D, N, Boolean> subStop, /////////////////////////////////////////////
			BiFunction<D, N, Prod<X, N>> step, /////////////////////////////////////////////
			UnaryOperator<D> op ////////////////////////////////////////////////////////////
	) {
		return dn -> of(dn).filter(makePredicate(subStop).negate()).////////////////////////
				map(mapper(step).apply(dn.left()))./////////////////
				orElse(prod(empty(), dn.mapLeft(op)));
	}

	private static <D, N, X> Function<D, Function<Prod<D, N>, Prod<Optional<X>, Prod<D, N>>>> mapper(
			BiFunction<D, N, Prod<X, N>> step) {
		return d -> makeApply(step).andThen(makeApply(reassemble(d)));
	}

	private static <D, N, X> BiFunction<X, N, Prod<Optional<X>, Prod<D, N>>> reassemble(D d) {
		return (x, n) -> prod(of(x), prod(d, n));
	}
}
