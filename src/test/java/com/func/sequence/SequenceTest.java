package com.func.sequence;

import static com.func.Prod.prod;
import static com.func.sequence.Sequence.cons;
import static com.func.sequence.Sequence.nil;
import static com.func.sequence.Sequence.unfold;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.func.Prod;

public class SequenceTest {

	@Test
	public void testHeadTail() {
		// given
		Sequence<Integer> nil = nil();
		Sequence<Integer> one = cons(1, nil());
		Sequence<Integer> two = cons(2, cons(1, nil()));
		// when && then
		assertThat(nil.headTail().isPresent()).isFalse();
		assertThat(one.headTail().isPresent()).isTrue();
		assertThat(one.head().get()).isEqualTo(1);
		assertThat(one.tail().get().headTail().isPresent()).isFalse();
		assertThat(two.head().get()).isEqualTo(2);
		assertThat(two.tail().get().head().get()).isEqualTo(1);
	}

	@Test
	public void testTrace() {
		// given
		Sequence<Integer> nil = nil();
		Sequence<Integer> one = cons(1, nil());
		Sequence<Integer> two = cons(2, cons(1, nil()));
		// when
		String act0 = nil.trace();
		String act1 = one.trace();
		String act2 = two.trace();
		// then
		assertThat(act0).isEqualTo("");
		assertThat(act1).isEqualTo("1");
		assertThat(act2).isEqualTo("2.1");
	}

	@Test
	public void testSize() {
		// given
		Sequence<Integer> nil = nil();
		Sequence<Integer> one = cons(1, nil());
		Sequence<Integer> two = cons(2, cons(1, nil()));
		// when
		Integer act0 = nil.size();
		Integer act1 = one.size();
		Integer act2 = two.size();
		// then
		assertThat(act0).isEqualTo(0);
		assertThat(act1).isEqualTo(1);
		assertThat(act2).isEqualTo(2);
	}

	@Test
	public void testIsEmpty() {
		// given
		Sequence<Integer> nil = nil();
		Sequence<Integer> one = cons(1, nil());
		Sequence<Integer> two = cons(2, cons(1, nil()));
		// when
		Boolean act0 = nil.isEmpty();
		Boolean act1 = one.isEmpty();
		Boolean act2 = two.isEmpty();
		// then
		assertThat(act0).isEqualTo(true);
		assertThat(act1).isEqualTo(false);
		assertThat(act2).isEqualTo(false);
	}

	@Test
	public void testFoldMass() {
		// given
		Long n = 400000L;
		Sequence<Long> input = nil();
		for (Long i = 0L; i < n; ++i) {
			input = cons(1 + i, input);
		}
		// when
		Long actual = input.fold(0L, (a, b) -> a + b);
		// then
		assertThat(actual).isEqualTo((n + 1) * n / 2);
	}

	@Test
	public void testReverse() {
		// given
		Sequence<Integer> one = cons(1, nil());
		Sequence<Integer> seq = cons(3, cons(2, cons(1, nil())));
		// when
		Sequence<Integer> actual = seq.reverse();
		// then
		assertThat(actual.trace()).isEqualTo("1.2.3");
		assertThat(one.reverse().trace()).isEqualTo(one.trace());
	}

	@Test
	public void testUnFoldMass() {
		// given
		int n = 40000;
		Predicate<Integer> p = x -> x == 0;
		Function<Integer, Prod<Integer, Integer>> g = i -> prod(i, i - 1);
		Sequence<Integer> input = unfold(p, g).apply(n);
		// when
		Integer actual = input.fold(0, (a, b) -> a + b);
		// then
		assertThat(actual).isEqualTo((n + 1) * n / 2);
	}

	@Test
	public void testMap() {
		// given
		Sequence<String> two = cons("a", cons("b", nil()));
		// when
		Sequence<String> actual = two.map(String::toUpperCase);
		// then
		assertThat(actual.trace()).isEqualTo("A.B");
	}

	@Test
	public void testFilter() {
		// given
		Sequence<Integer> two = cons(1, cons(2, cons(3, nil())));
		// when
		Sequence<Integer> actual0 = two.filter(x -> x % 2 == 0);
		Sequence<Integer> actual1 = two.filter(x -> x % 2 == 1);
		// then
		assertThat(actual0.trace()).isEqualTo("2");
		assertThat(actual1.trace()).isEqualTo("1.3");
	}

	@Test
	public void testCat() {
		// given
		Sequence<Integer> one = cons(0, cons(1, cons(2, nil())));
		Sequence<Integer> two = cons(3, cons(4, cons(5, nil())));
		// when
		Sequence<Integer> actuals = one.cat(two);
		// then
		assertThat(actuals.trace()).isEqualTo("0.1.2.3.4.5");
	}

	@Test
	public void testMassConcat() {
		// given
		int n = 500;
		String expected = "";
		Sequence<Sequence<Integer>> input = nil();
		Sequence<Integer> one = cons(0, cons(1, cons(2, nil())));
		for (int i = 0; i < n; ++i) {
			input = input.cat(cons(one, nil()));
			expected += (expected.length() == 0 ? "" : ".") + one.trace();
		}
		// when
		Sequence<Integer> actuals = Sequence.concat(input);
		// then
		assertThat(actuals.trace()).isEqualTo(expected);
	}

	@Test
	public void testFlatMap() {
		// given
		Function<Integer, Sequence<Integer>> f = Sequence.unfold(i -> i == 0, i -> prod(i, i - 1));
		Sequence<Integer> two = cons(3, cons(0, cons(5, nil())));
		// when
		Sequence<Integer> actuals = two.flatMap(f);
		// then
		assertThat(actuals.trace()).isEqualTo("3.2.1.5.4.3.2.1");
	}

}
