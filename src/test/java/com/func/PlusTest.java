package com.func;

import static com.func.Plus.inLeft;
import static com.func.Plus.*;
import static com.func.Plus.plus;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

public class PlusTest {

	@Test
	public void testBracket() {
		// given
		Plus<Integer, String> l = inLeft(1);
		Plus<Integer, String> r = inRight("a");
		// when
		Integer actl = l.apply(x -> x, x -> null);
		String actr = r.apply(x -> null, x -> x);
		// then
		assertThat(actl).isEqualTo(1);
		assertThat(actr).isEqualTo("a");
	}

	@Test
	public void testPlus() {
		// given
		Plus<Integer, String> l = inLeft(1);
		Plus<Integer, String> r = inRight("a");
		Function<Integer, Integer> f = i -> i + 1;
		Function<String, String> g = String::toUpperCase;
		// when
		Plus<Integer, String> actl = plus(f, g).apply(l);
		Plus<Integer, String> actr = plus(f, g).apply(r);
		// then
		assertThat(actl.trace()).isEqualTo("2");
		assertThat(actr.trace()).isEqualTo("A");
	}

	@Test
	public void testFromPredicate() {
		// given
		Predicate<Integer> p = i -> i > 0;
		Function<Integer,String>f= fromPredicate(p).andThen(fold(v->"+", v->"-"));
		// when
		String positive = f.apply(1);
		String negative = f.apply(-1);
		// then
		assertThat(positive).isEqualTo("+");
		assertThat(negative).isEqualTo("-");

	}

}
