package com.sandbox.funcprog.tree;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.bifunctor.Sum.left;
import static com.sandbox.funcprog.bifunctor.Sum.right;
import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.bifunctor.Sum;
import com.sandbox.funcprog.recursion.Bouncer;

public class List<T> {

	private Sum<Void, Prod<T, List<T>>> values;

	public List(Sum<Void, Prod<T, List<T>>> values) {
		this.values = values;
	}

	public static <X> List<X> empty() {
		return new List<>(left(null));
	}

	public static <X> List<X> cons(X x, List<X> xs) {
		return new List<>(right(prod(x, xs)));
	}

	public <Z> Z fold(Z out, BiFunction<T, Z, Z> bi) {
		return fold(this, out, bi).call();
	}

	private static <X, Z> Bouncer<Z> fold(List<X> xs, Z out, BiFunction<X, Z, Z> bi) {
		return xs.values.apply(//////////////////////////////////////////////////////////////////
				v -> resume(out), ///////////////////////////////////////////////////////////////
				pr -> suspend(///////////////////////////////////////////////////////////////////
						() -> fold(pr.right(), bi.apply(pr.left(), out), bi) ////////////////////
		)////////////////////////////////////////////////////////////////////////////////////////
		);

	}

	public <Z> List<Z> map(Function<T, Z> f) {
		return fold(empty(), (x, zs) -> cons(f.apply(x), zs));
	}

	public <Z> Z apply(Z z, Function<Prod<T, List<T>>, Z> g) {
		return values.apply(v->z, g);
	}

	public T reduce(T id, BinaryOperator<T> bi) {
		return fold(id, bi);
	}

	public T head() {
		return headOrTail(Prod::left);
	}

	public List<T> tail() {
		return headOrTail(Prod::right);
	}

	private <Z> Z headOrTail(Function<Prod<T, List<T>>, Z> f) {
		return values.apply(null, f);
	}

	protected <Z> Z headTwice(BiFunction<T, T, Z> bi) {
		return bi.apply(head(), tail().head());
	}

	protected List<T> tailTwice() {
		return tail().tail();
	}

}
