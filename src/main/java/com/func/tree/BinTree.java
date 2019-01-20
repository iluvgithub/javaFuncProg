package com.func.tree;

import static com.func.Plus.inLeft;
import static com.func.Plus.inRight;
import static com.func.sequence.Sequence.cons;
import static com.func.sequence.Sequence.tau;
import static com.func.tailrec.Bouncer.done;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import com.func.Plus;
import com.func.Prod;
import com.func.sequence.Sequence;
import com.func.tailrec.Bouncer;
import com.func.vacuum.None;

/**
 * 
 */
public class BinTree<A> {

	private final Supplier<Optional<Plus<A, Prod<BinTree<A>, BinTree<A>>>>> values;

	private BinTree(Supplier<Optional<Plus<A, Prod<BinTree<A>, BinTree<A>>>>> values) {
		this.values = values;
	}

	public static final <X> BinTree<X> nil() {
		return new BinTree<>(() -> empty());
	}

	public static final <X> BinTree<X> leaf(X x) {
		return new BinTree<>(() -> of(Plus.inLeft(x)));
	}

	public static final <X> BinTree<X> tree(BinTree<X> l, BinTree<X> r) {
		return new BinTree<>(() -> of(Plus.inRight(Prod.prod(l, r))));
	}

	protected Optional<Plus<A, Prod<BinTree<A>, BinTree<A>>>> getValues() {
		return values.get();
	}

	public String trace() {
		return fold("", Object::toString, BinTree::traceOp);
	}

	private static String traceOp(String l, String r) {
		return "<" + l + "," + r + ">";
	}

	public <Z> Z fold(Z z, Function<A, Z> f, BinaryOperator<Z> op) {
		return bounceFold(z, f, op, //
				tau(inLeft(this)), tau(z)//
		).eval().head().get();
	}

	// https://stackoverflow.com/questions/41440313/tail-recursive-fold-on-a-binary-tree-in-scala
	private <X, Z> Bouncer<Sequence<Z>> bounceFold(Z z, Function<X, Z> f, BinaryOperator<Z> op, //
			Sequence<Plus<BinTree<X>, None>> toVisit, Sequence<Z> acc) {

		if (toVisit.isEmpty()) {
			return done(acc);
		}

		Function<BinTree<X>, Bouncer<Sequence<Z>>> foo = bt -> {
			Function<Optional<X>, Bouncer<Sequence<Z>>> a = opt0 -> {// leaf
				Sequence<Z> acc0 = cons(opt0.map(f).orElse(z), acc);
				return () -> bounceFold(z, f, op, toVisit.tail().get(), acc0);
			};
			Function<Prod<BinTree<X>, BinTree<X>>, Bouncer<Sequence<Z>>> b = prd -> {// branch
				BinTree<X> l = prd.left();
				BinTree<X> r = prd.right();
				Sequence<Plus<BinTree<X>, None>> in0 = //
						cons(inLeft(r), //
								cons(inLeft(l), //
										cons(inRight(None.INSTANCE), toVisit.tail().get())));
				return () -> bounceFold(z, f, op, in0, acc);
			};
			return Plus.fold(a, b).apply(bt.getValues().//
			map(Plus.plus(Optional::of, w -> w)).//
			orElse(Plus.inLeft(Optional.empty()))
			//
			);
		};

		Function<None, Bouncer<Sequence<Z>>> bar = v -> { // branchStub
			Z l = acc.head().get();
			Z r = acc.tail().get().head().get();
			Sequence<Z> acc0 = cons(op.apply(l, r), acc.tail().get().tail().get());
			return () -> bounceFold(z, f, op, toVisit.tail().get(), acc0);
		};

		return Plus.fold(foo, bar).apply(toVisit.head().get());
	}

}
