package com.sandbox.funcprog.sample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.sandbox.funcprog.stream.ConsList;

public class PrimeDecomposerTest {

	private ConsList<Integer> list(Integer... ints) {
		return ConsList.fromArray(ints);
	}

	@Test
	public void testOne() throws Exception {
		assertThat(generate(1).trace()).isEqualTo(list().trace());
	}

	@Test
	public void testTwo() throws Exception {
		assertThat(generate(2).trace()).isEqualTo(list(2).trace());
	}

	@Test
	public void testThree() throws Exception {
		assertThat(generate(3).trace()).isEqualTo(list(3).trace());
	}

	@Test
	public void testFour() throws Exception {
		assertThat(generate(4).trace()).isEqualTo(list(2, 2).trace());
	}

	@Test
	public void testSix() throws Exception {
		assertThat(generate(6).trace()).isEqualTo(list(2, 3).trace());
	}

	@Test
	public void testEight() throws Exception {
		assertThat(generate(8).trace()).isEqualTo(list(2, 2, 2).trace());
	}

	@Test
	public void testNine() throws Exception {
		assertThat(generate(9).trace()).isEqualTo(list(3, 3).trace());
	}

	private ConsList<Integer> generate(Integer n) {
		return PrimeDecomposer.decompose(n);
	}

}
