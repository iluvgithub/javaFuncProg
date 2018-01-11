package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.tailrecursion.Bouncer;

public class LazyList<A> implements ConsList<A> {

	private final Optional<Supplier<Prod<A, ConsList<A>>>> values;

	private LazyList(Optional<Supplier<Prod<A, ConsList<A>>>> values) {
		this.values = values;
	}

	public static <X> ConsList<X> nil() {
		return new LazyList<>(empty());
	}

	public static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return new LazyList<X>(of(() -> prod(x.get(), xs.get())));
	}

	@Override
	public Optional<Prod<A, ConsList<A>>> out() {
		return values.map(Supplier::get);
	}

	@Override
	public <B> B foldLeft(B id, BiFunction<B, A, B> accumulator) {
		return bounceFoldLeft(this, id, accumulator).call();
	}

	private static <U, V> Bouncer<V> bounceFoldLeft(ConsList<U> list, V out, BiFunction<V, U, V> bi) {
		return apply(list, resume(out), (hd, tl) -> suspend(() -> bounceFoldLeft(tl, bi.apply(out, hd), bi)));
	}

	public static <U, V> V apply(ConsList<U> list, V out, BiFunction<U, ConsList<U>, V> biFunction) {
		return list.out().map(headTail -> headTail.apply(biFunction)).orElse(out);
	}

}
