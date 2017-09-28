package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Both;
import com.sandbox.funcprog.bifunctor.Either;
import com.sandbox.funcprog.recursion.Bouncer;

public class ListNil<T> {
	protected final Either<Void, Both<T, ListNil<T>>> values;

	protected ListNil(Either<Void, Both<T, ListNil<T>>> values) {
		this.values = values;
	}

	public static final <X> ListNil<X> nil() {
		return new ListNil<>(Either.left(null));
	}

	public static final <X> ListNil<X> cons(X x, ListNil<X> xs) {
		return new ListNil<>(Either.right(Both.both(x, xs)));
	}

	public <Z> Z foldr(Z z0, BiFunction<T, Z, Z> bi) {
		return reverse().foldl(z0, (z, t) -> bi.apply(t, z));
	}

	public T reduce(T id, BinaryOperator<T> bi) {
		return null;
	}

	public <Z> Z foldl(Z z, BiFunction<Z, T, Z> bi) {
		return bounce(z, bi).call();
	}

	private <Z> Bouncer<Z> bounce(Z z, BiFunction<Z, T, Z> bi) {
		return values.apply(v -> resume(z), both -> suspend(() -> both.apply(nextStep(z, bi))));
	}

	private static <X, Z> BiFunction<X, ListNil<X>, Bouncer<Z>> nextStep(Z z, BiFunction<Z, X, Z> bi) {
		return (x, xs) -> xs.bounce(bi.apply(z, x), bi);
	}

	public String trace() {
		return withBrackets(foldl(new StringBuffer(), ListNil::append));
	}

	protected static String withBrackets(StringBuffer sb) {
		return "[" + sb + "]";
	}

	protected static <X> StringBuffer append(StringBuffer sb, X x) {
		return sb.append(sb.length() > 0 ? "." : "").append(x.toString());
	}

	public ListNil<T> reverse() {
		return foldl(nil(), (ts, t) -> cons(t, ts));
	}

	public <Y> ListNil<Y> map(Function<T, Y> f) {
		return foldr(nil(), (t, ys) -> cons(f.apply(t), ys));
	}

}
