package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.makeApply;
import static com.sandbox.funcprog.bifunctor.Prod.makePredicate;
import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.stream.Anamorphism.from;
import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

	private Boolean isNotEmpty() {
		return out().isPresent();
	}

	private Optional<ConsList<A>> nonEmptyList() {
		return of(this).filter(ConsList::isNotEmpty);
	}

	public <B> B foldLeft(B init, BiFunction<B, A, B> biFunction) {
		return bounceFoldLeft(this, init, biFunction).call();
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

	public A hd() {
		return head().get();
	}

	public Optional<ConsList<A>> tail() {
		return out().map(Prod::right);
	}

	public ConsList<A> tl() {
		return tail().get();
	}

	public String trace() {
		return foldLeft("", (s, i) -> s + (s.length() == 0 ? "" : ".") + i);
	}

	public ConsList<A> reverse() {
		return foldLeft(nil(), (list, a) -> cons(() -> a, () -> list));
	}

	public <B> ConsList<B> map(Function<A, B> f) {
		return new Anamorphism<>(subMap(f)).unfold(this);
	}

	private static <X, Y> Function<ConsList<X>, Optional<Prod<Y, ConsList<X>>>> subMap(Function<X, Y> f) {
		return xs -> xs.out().map(prd -> prd.mapLeft(f));
	}

	public <B> B foldRight(B id, BiFunction<A, B, B> biFunction) {
		return reverse().foldLeft(id, (b, a) -> biFunction.apply(a, b));
	}

	public ConsList<A> takeWhile(Predicate<A> p) {
		return new Anamorphism<>(filteredOut(p)).unfold(this);
	}

	public <B> ConsList<Prod<A, B>> zip(ConsList<B> list) {
		return new Anamorphism<Prod<ConsList<A>, ConsList<B>>, Prod<A, B>>(makeApply((as, bs) -> as.optSubzip(bs)))
				.unfold(prod(this, list));
	}

	private <B> Optional<Prod<Prod<A, B>, Prod<ConsList<A>, ConsList<B>>>> optSubzip(ConsList<B> ys) {
		return ys.nonEmptyList().flatMap(bs -> nonEmptyList().map(as -> as.subzip(bs)));
	}

	private <B> Prod<Prod<A, B>, Prod<ConsList<A>, ConsList<B>>> subzip(ConsList<B> ys) {
		return prod(prod(this.hd(), ys.hd()), prod(this.tl(), ys.tl()));
	}

	private static <X> Function<ConsList<X>, Optional<Prod<X, ConsList<X>>>> filteredOut(Predicate<X> p) {
		return xs -> xs.out().filter(makePredicate((a, as) -> p.test(a)));
	}

	public ConsList<A> take(final Integer nbElementsToKeep) {
		return zip(from(0)).takeWhile(makePredicate((a, n) -> n < nbElementsToKeep)).map(Prod::left);
	}

	public ConsList<A> dropWhile(Predicate<A> predicate) {
		return dropWhile(this, predicate).call();
	}

	private static <X> Bouncer<ConsList<X>> dropWhile(ConsList<X> acc, Predicate<X> p) {
		return apply(acc, resume(acc), (hd, tl) -> p.test(hd) ? suspend(() -> dropWhile(tl, p)) : resume(acc));
	}

	public ConsList<A> concat(ConsList<A> right) {
		return this.foldRight(right, ConsList::cons);
	}

	private static <X> ConsList<X> cons(X x, ConsList<X> xs) {
		return cons(() -> x, () -> xs);
	}

	public static <X> ConsList<X> flatten(ConsList<ConsList<X>> listOfLists) {
		return listOfLists.foldLeft(nil(), (left, right) -> left.concat(right));
	}

	public <B> ConsList<B> flatMap(Function<A, ConsList<B>> f) {
		return flatten(foldRight(nil(), (a, bs) -> cons(f.apply(a), bs)));
	}

	public ConsList<A> sort(Comparator<A> comp) {
		return apply(this, nil(),
				(hd, tl) -> buildTree(comp).apply(hd, tl).apply((l, r) -> l.concat(prepend(hd).apply(r))));
	}

	private static <X> BiFunction<X, ConsList<X>, Prod<ConsList<X>, ConsList<X>>> buildTree(Comparator<X> comp) {
		return (x0, tl) -> tl.foldLeft(prod(nil(), nil()), subSort(comp, x0));
	}

	private static <X> BiFunction<Prod<ConsList<X>, ConsList<X>>, X, Prod<ConsList<X>, ConsList<X>>> subSort(
			Comparator<X> comp, X x0) {
		return (prd, x) -> comp.compare(x0, x) < 0 ? prd.mapRight(prepend(x)) : prd.mapLeft(prepend(x));
	}

	private static <X> UnaryOperator<ConsList<X>> prepend(X x) {
		return xs -> cons(x, xs);
	}

}
