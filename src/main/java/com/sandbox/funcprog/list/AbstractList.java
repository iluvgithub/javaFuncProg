package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import com.sandbox.funcprog.recursion.Bouncer;

public abstract class AbstractList<T> implements List<T> {

	@Override
	public String trace() {
		return "[" + asStringBuffer() + "]";
	}

	private StringBuffer asStringBuffer() {
		return foldl(new StringBuffer(), (a, b) -> a.append(a.length() > 0 ? "." : "").append(b));
	}

	/**
	 * right to left reduction
	 * 
	 * @param z
	 * @param bi
	 * @return
	 * 
	 */
	@Override
	public <Z> Z foldr(Z id, BiFunction<T, Z, Z> bi) {
		return reverse().foldl(id, (z, t) -> bi.apply(t, z));
	}

	public T reduceR(T id, BinaryOperator<T> bi) {
		return foldr(id, bi);
	}

	@Override
	public <Z> Z foldl(Z z, BiFunction<Z, T, Z> bi) {
		return bounce(this, z, bi).call();
	}

	protected static <X, Z> Bouncer<Z> bounce(List<X> list, Z z, BiFunction<Z, X, Z> bi) {
		return list.apply(resume(z), (x, xs) -> suspend(() -> bounce(xs, bi.apply(z, x), bi)));
	}

	@Override
	public T head() {
		return applyOnNonEmpty((t, ts) -> t);
	}

	@Override
	public List<T> tail() {
		return applyOnNonEmpty((t, ts) -> ts);
	}

	private <Z> Z applyOnNonEmpty(BiFunction<T, List<T>, Z> bif) {
		return apply(null, bif);
	}

	@Override
	public T last() {
		return reverse().head();
	}
}
