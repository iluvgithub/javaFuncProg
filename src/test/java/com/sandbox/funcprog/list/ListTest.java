package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.list.List.cons;
import static com.sandbox.funcprog.list.List.nil;
import static com.sandbox.funcprog.pair.Pair.pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.sandbox.funcprog.pair.Pair;

public class ListTest {

	@Test
	public void testSumRight() throws Exception {
		// arrange
		List<Integer> input = cons(1, cons(2, cons(3, nil())));
		// act
		Integer actual = input.foldLeft(0, (a, b) -> a + b);
		// assert
		assertEquals(6, actual.intValue());
	}

	@Test

	public void testBigSum() throws Exception {
		// arrange
		int n = 100000;
		List<Integer> input = makeBigList(n);
		// act
		Integer actual = input.foldLeft(0, (a, b) -> a + b);
		// assert
		assertEquals(n * (n + 1) / 2, actual.intValue());
	}

	private List<Integer> makeBigList(int n) {
		List<Integer> input = nil();
		for (int i = 0; i < n; ++i) {
			input = cons(i + 1, input);
		}
		return input;
	}

	@Test
	public void testSize() throws Exception {
		// arrange
		List<Integer> input = cons(1, cons(2, cons(3, nil())));
		// act
		Integer actual = input.size();
		// assert
		assertEquals(3, actual.intValue());

	}

	@Test
	public void testEquals() throws Exception {
		// arrange
		List<Integer> input1 = cons(1, cons(2, cons(3, nil())));
		List<Integer> input2 = cons(1, cons(2, cons(3, nil())));
		List<Integer> input3 = cons(0, cons(1, cons(2, cons(3, nil()))));
		List<Integer> input4 = cons(1, cons(-2, cons(3, nil())));
		List<Integer> nil1 = nil();
		List<Integer> nil2 = nil();
		// assert
		assertTrue(input1.isEqualTo(input2));
		assertFalse(input1.isEqualTo(input3));
		assertTrue(nil1.isEqualTo(nil2));
		assertFalse(nil1.isEqualTo(input1));
		assertFalse(input1.isEqualTo(nil1));
		assertFalse(input1.isEqualTo(input4));
	}

	@Test

	public void testEqualsMass() throws Exception {
		// arrange
		int n = 100000;
		List<Integer> input1 = makeBigList(n);
		List<Integer> input2 = makeBigList(n);
		// assert
		assertTrue(input1.isEqualTo(input2));
		// re-arrange
		input1 = cons(0, input1);
		input2 = cons(-1, input2);
		assertFalse(input1.isEqualTo(input2));
	}

	@Test
	public void testMap() throws Exception {
		// arrange
		List<Integer> input = cons(1, cons(2, cons(3, nil())));
		// act
		List<String> actual = input.map(i -> i.toString());
		// assert
		List<String> expected = cons("1", cons("2", cons("3", nil())));
		assertTrue(expected.isEqualTo(actual));
	}

	@Test

	public void testCat() throws Exception {
		// arrange
		List<Integer> input1 = cons(1, cons(2, cons(3, nil())));
		List<Integer> input2 = cons(4, cons(5, nil()));
		// act
		List<Integer> actual = input1.cat(input2);
		// assert
		List<Integer> expected = cons(1, cons(2, cons(3, cons(4, cons(5, nil())))));
		assertTrue(expected.isEqualTo(actual));
	}

	@Test
	public void testReverse() throws Exception {
		// arrange
		List<Integer> input = cons(1, cons(2, cons(3, cons(4, cons(5, nil())))));
		// act
		List<Integer> actual = input.reverse();
		// assert
		List<Integer> expected = cons(5, cons(4, cons(3, cons(2, cons(1, nil())))));
		assertTrue(expected.isEqualTo(actual));
	}

	@Test

	public void testUnfold() throws Exception {
		// arrange
		Predicate<Integer> keepGoing = n -> n < 4;
		Function<Integer, Integer> head = n -> n;
		Function<Integer, Integer> tail = n -> n + 1;
		Integer seed = 1;
		// act
		List<Integer> actual = List.unfold(keepGoing, head, tail, seed);
		// assert
		List<Integer> expected = cons(1, cons(2, cons(3, nil())));
		assertTrue(expected.isEqualTo(actual));
	}

	@Test

	public void testZip() throws Exception {
		// arrange
		List<Integer> left = cons(1, cons(2, cons(3, nil())));
		List<String> right = cons("1", cons("2", cons("3", nil())));
		// act
		List<Pair<Integer, String>> actual = nil().zip(pair(left, right));
		// assert
		List<Pair<Integer, String>> expected = cons(pair(1, "1"), cons(pair(2, "2"), cons(pair(3, "3"), nil())));
		assertTrue(expected.isEqualTo(actual));
	}

}
