package com.func;

import static com.func.Curry.compose;
import static java.util.function.Function.identity;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 
 * Both represents a product of any two Objects.
 *
 * @param <LEFT>
 * @param <RIGHT>
 */
public class Prod<LEFT, RIGHT> {

	private final LEFT l;
	private final RIGHT r;

	private Prod(LEFT l, RIGHT r) {
		super();
		this.l = l;
		this.r = r;
	}

	public static <L, R> Prod<L, R> prod(L l, R r) {
		return new Prod<>(l, r);
	}

	public static <X> Prod<X, X> delta(X x) {
		return prod(x, x);
	}

	public LEFT left() {
		return l;
	}

	public RIGHT right() {
		return r;
	}

	/**
	 * @param f:
	 *            L < -- X
	 * @param g:
	 *            R < -- X
	 * @return <f,g> LxR <-- X
	 */
	public static <X, L, R> Function<X, Prod<L, R>> bracket(Function<X, L> f, Function<X, R> g) {
		return cross(f, g).compose(Prod::delta);
	}

	/**
	 * @param f:
	 *            L2 < -- L1
	 * @param g:
	 *            R2 < -- R1
	 * @return fxg L2xR2 <-- L1xR1
	 */
	public static <L1, L2, R1, R2> Function<Prod<L1, R1>, Prod<L2, R2>> cross(Function<L1, L2> f, Function<R1, R2> g) {
		return Curry.<Prod<L1, R1>, BiFunction<L1, R1, Prod<L2, R2>>, Prod<L2, R2>> //
				partialLeft(Prod::apply, compose(Prod::prod, f, g));
	}

	public static <L, R, Z> Function<Prod<L, R>, Z> folder(BiFunction<L, R, Z> bi) {
		return prd -> bi.apply(prd.left(), prd.right());
	}

	public static <Z, L, R> Function<Prod<L, R>, Z> folder(Function<L, Function<R, Z>> f) {
		return folder((x, y) -> f.apply(x).apply(y));
	}

	protected <Z> Z apply(BiFunction<LEFT, RIGHT, Z> f) {
		return f.apply(left(), right());
	}

	public Prod<RIGHT, LEFT> swap() {
		return prod(right(), left());
	}

	public static <L, R> Function<Prod<L, R>, Prod<R, L>> swapper() {
		return prd -> prd.swap();
	}

	public static <LX, LY, R> Function<Prod<LX, R>, Prod<LY, R>> leftMapper(Function<LX, LY> f) {
		return Prod.<LX, LY, R, R>cross(f, identity());
	}

	public static <L, RX, RY> Function<Prod<L, RX>, Prod<L, RY>> rightMapper(Function<RX, RY> f) {
		return Prod.<L, L, RX, RY>cross(identity(), f);
	}

}
