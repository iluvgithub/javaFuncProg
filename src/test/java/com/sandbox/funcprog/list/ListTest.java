package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.list.List.cons;
import static com.sandbox.funcprog.list.List.nil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListTest {

	@Test
	public void testSumRight() throws Exception {
		// arrange
		List<Integer> input = cons(1, cons(2, cons(3, nil())));
		// act
		Integer actual = input.foldRight(0, (a, b) -> a + b);
		// assert
		assertEquals(6, actual.intValue());
	}

	@Test
	public void testBigSum() throws Exception {
		// arrange
		int n = 100000;
		List<Integer> input = nil();
		for (int i = 0; i < n; ++i) {
			input = cons(i + 1, input);
		}
		// act
		Integer actual = input.foldLeft(0, (a, b) -> a + b);
		// assert
		assertEquals(n * (n + 1) / 2, actual.intValue());
	}

	@Test
	public void testEquals() throws Exception {
		// arrange
		List<Integer> input1 = cons(1, cons(2, cons(3, nil())));
		List<Integer> input2 = cons(1, cons(2, cons(3, nil())));
		List<Integer> input3 = cons(0, cons(1, cons(2, cons(3, nil()))));
		List<Integer> nil1 = nil();
		List<Integer> nil2 = nil();
		// assert
		assertTrue(input1.isEqualTo(input2));
		assertFalse(input1.isEqualTo(input3));
		assertTrue(nil1.isEqualTo(nil2));
		assertFalse(nil1.isEqualTo(input1));
		assertFalse(input1.isEqualTo(nil1));
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
}
