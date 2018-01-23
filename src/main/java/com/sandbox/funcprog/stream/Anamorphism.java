package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.makeApply;
import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.stream.ConsList.unzip;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.sandbox.funcprog.bifunctor.Prod;

/**
 *
 * @param <S>
 *            state
 * @param <X>
 *            output will be list of type X
 * 
 */
public class Anamorphism<S, X> {

	private final Function<S, Optional<Prod<X, S>>> generation;

	public Anamorphism(Function<S, Optional<Prod<X, S>>> generation) {
		this.generation = generation;
	}

	/**
	 * 
	 * @param stop
	 *            stopping condition
	 * @param g
	 *            one step function
	 */
	public Anamorphism(Predicate<S> stop, Function<S, Prod<X, S>> g) {
		this(s0 -> of(s0).filter(stop.negate()).map(g));
	}

	public ConsList<X> unfold(S s) {
		return generation.andThen(opt -> opt.map(unfoldStep()).orElse(nil())).apply(s);
	}

	private Function<Prod<X, S>, ConsList<X>> unfoldStep() {
		return prod -> prod.apply((x, s) -> cons(() -> x, () -> unfold(s)));
	}

	private static <X> ConsList<X> nil() {
		return ConsList.nil();
	}

	private static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return ConsList.cons(x, xs);
	}

	public Prod<ConsList<X>, S> unfoldWithState(S s0) {
		return unzip(withState().unfold(s0)).mapRight(ConsList::last).mapRight(optS -> optS.orElse(s0));
	}

	private Anamorphism<S, Prod<X, S>> withState() {
		return new Anamorphism<>(s -> generation.apply(s).map(makeApply((x, z) -> prod(prod(x, z), z))));
	}

	public static ConsList<Integer> from(Integer n) {
		return iterate(n, x -> x + 1);
	}

	public static ConsList<Integer> fromTo(Integer fromIncluded, Integer toExcluded) {
		return from(fromIncluded).takeWhile(n -> n < toExcluded);
	}

	public static <Z> ConsList<Z> iterate(Z from, UnaryOperator<Z> op) {
		return new Anamorphism<>(z -> false, iterateStep(op)).unfold(from);
	}

	private static <Z> Function<Z, Prod<Z, Z>> iterateStep(UnaryOperator<Z> op) {
		return z -> prod(z, op.apply(z));
	}

}
