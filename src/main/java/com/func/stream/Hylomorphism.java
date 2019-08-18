package com.func.stream;

import static com.func.Curry.flipArgs;
import static com.func.Curry.partialLeft;
import static com.func.Prod.folder;
import static com.func.Prod.leftMapper;
import static com.func.tailrec.Bouncer.done;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.func.Prod;
import com.func.tailrec.Bouncer;

public class Hylomorphism {

	public static <I, A, O> Function<I, O> hylo(//
			Predicate<I> isOver, //
			Function<I, Prod<A, I>> g, //
			O out, //
			BiFunction<A, O, O> f) {
		return hylo(i -> of(i).filter(isOver.negate()).map(g), out, f);
	}

	public static <I, A, O> Function<I, O> hylo(//
			Function<I, Optional<Prod<A, I>>> g, //
			O out, //
			BiFunction<A, O, O> f) {
		return i -> bouncer(g, f).apply(i, out).eval();
	}

	private static <I, A, O> BiFunction<I, O, Bouncer<O>> bouncer(//
			Function<I, Optional<Prod<A, I>>> g, //
			BiFunction<A, O, O> f) {
		return (i, o) -> () -> g.apply(i).//
				map(leftMapper(partialLeft(f, o))).//
				map(folder(flipArgs(bouncer(g, f)))).//
				orElse(done(o));

	}

}
