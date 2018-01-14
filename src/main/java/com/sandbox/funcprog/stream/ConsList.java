package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.stream.Anamorphism.unfold;
import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.tailrecursion.Bouncer;

public class ConsList<A> {

	public static <X> ConsList<X> nil() {
		return new ConsList<>(empty());
	}

	public static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return new ConsList<X>(of(() -> prod(x.get(), xs.get())));
	}

	private final Optional<Supplier<Prod<A, ConsList<A>>>> values;

	private ConsList(Optional<Supplier<Prod<A, ConsList<A>>>> values) {
		this.values = values;
	}

	private Optional<Prod<A, ConsList<A>>> out() {
		return values.map(Supplier::get);
	}

	public <B> B foldLeft(B id, BiFunction<B, A, B> biFunction) {
		return bounceFoldLeft(this, id, biFunction).call();
	}

	private static <U, V> Bouncer<V> bounceFoldLeft(ConsList<U> list, V out, BiFunction<V, U, V> bi) {
		return apply(list, resume(out), (hd, tl) -> suspend(() -> bounceFoldLeft(tl, bi.apply(out, hd), bi)));
	}

	private static <U, V> V apply(ConsList<U> list, V cumulated, BiFunction<U, ConsList<U>, V> biFunction) {
		return list.out().map(headTail -> headTail.apply(biFunction)).orElse(cumulated);
	}

	public Optional<A> head() {
		return out().map(Prod::left);
	}

	public Optional<ConsList<A>> tail() {
		return out().map(Prod::right);
	}

	public String trace() {
		return foldLeft("", (s, i) -> s + (s.length() == 0 ? "" : ".") + i);
	}

	public ConsList<A> reverse() {
		return foldLeft(nil(), (list, a) -> cons(() -> a, () -> list));
	}

	public <B> ConsList<B> map(Function<A, B> f) {
		return unfold(subMap(f)).apply(this);
	}

	private static <X, Y> Function<ConsList<X>, Optional<Prod<Y, ConsList<X>>>> subMap(Function<X, Y> f) {
		return xs -> xs.out().map(prod -> prod.mapLeft(f));
	}

	public <B> B foldRight(B id, BiFunction<A, B, B> biFunction) {
		return reverse().foldLeft(id, (b, a) -> biFunction.apply(a, b));
	}

	public ConsList<A> takeWhile(Predicate<A> p) {
		return apply((a, as) -> of(a).filter(p).map(x -> cons(() -> x, () -> as.takeWhile(p))).orElse(nil()));
	}

	private <B> ConsList<B> apply(BiFunction<A, ConsList<A>, ConsList<B>> biFunction) {
		return apply(this, nil(), biFunction);
	}

	public ConsList<A> dropWhile(Predicate<A> predicate) {
		return dropWhile(this, predicate).call();
	}

	private static <X> Bouncer<ConsList<X>> dropWhile(ConsList<X> acc, Predicate<X> p) {
		return apply(acc, resume(acc), (hd, tl) -> p.test(hd) ? suspend(() -> dropWhile(tl, p)) : resume(acc));
	}

}
