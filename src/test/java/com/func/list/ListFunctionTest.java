package com.func.list;

import static com.func.list.Anamorphism.unfold;
import static com.func.list.Catamorphism.trace;
import static com.func.list.List.cons;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;

import com.func.Prod;

public class ListFunctionTest {

	@Test
	public void testFlatten() {
		// arrange
		Function<Integer, List<Integer>> f = unfold(n -> n == 0, n -> Prod.prod(n, n - 1));
		ListFunction<Integer> g = ListFunction.build(f).flatten(f);
		// act
		List<Integer> actuals = g.apply(4);
		String actual = Catamorphism.trace(actuals);
		// assert
		assertThat(actual).isEqualTo("4.3.2.1.3.2.1.2.1.1");
	}

	@Test
	public void testFlattenMass() {
		// arrange
		Function<Integer, List<Integer>> f = x -> cons(x + 1, List.empty());
		ListFunction<Integer> g = ListFunction.build(f);
		int n = 15000;
		for (int i = 0; i < n; ++i) {
			g = g.flatten(f);
		}
		// act
		List<Integer> actuals = g.apply(0);
		String actual = trace(actuals);
		// assert
		assertThat(actual).isEqualTo("" + (n + 1));
	}
}
