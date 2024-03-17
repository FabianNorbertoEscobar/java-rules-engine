package ar.com.ib.rule.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import ar.com.ib.rule.engine.exception.RuleException;
import ar.com.ib.rule.engine.rules.RuleDos;
import ar.com.ib.rule.engine.rules.RuleUno;

class RuleValidatorTest {

	@Test
	void evaluateResultTrueTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true));
		// Then
		assertTrue(result.getResult());
		assertEquals("OK", result.getMessage());
	}

	@Test
	void evaluateResultFalseTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false));
		// Then
		assertFalse(result.getResult());
		assertEquals("Error", result.getMessage());
	}

	@Test
	void evaluateResultTrueAndTrueTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).and(RuleUno.class,
				Map.of("result", true));
		// Then
		assertTrue(result.getResult());
	}

	@Test
	void evaluateResultTrueAndTrueFalseTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).and(RuleUno.class,
				Map.of("result", false));
		// Then
		assertFalse(result.getResult());
		assertEquals("Error", result.getMessage());
	}

	@Test
	void evaluateResultFalseAndTrueTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).and(RuleUno.class,
				Map.of("result", true));
		// Then
		assertFalse(result.getResult());
		assertEquals("Error", result.getMessage());
	}

	@Test
	void evaluateResultFalseAndFalseTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).and(RuleDos.class,
				Map.of("result", false));
		// Then
		assertFalse(result.getResult());
		assertEquals("Error", result.getMessage());
	}

	@Test
	void evaluateResultTrueOrTrueTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).or(RuleDos.class,
				Map.of("result", true));
		// Then
		assertTrue(result.getResult());
	}

	@Test
	void evaluateResultTrueOrFalseTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).or(RuleDos.class,
				Map.of("result", false));
		// Then
		assertTrue(result.getResult());
	}

	@Test
	void evaluateResultFalseOrTrueTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).or(RuleDos.class,
				Map.of("result", true));
		// Then
		assertTrue(result.getResult());
	}

	@Test
	void evaluateResultFalseOrFalseTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).or(RuleDos.class,
				Map.of("result", false));
		// Then
		assertFalse(result.getResult());
		assertEquals("ErrorDos", result.getMessage());
	}

	@Test
	void evaluateResultFalseOrFalseFireTest() {
		// Given - When
		Map<String, Object> paramsRuleOne = Map.of("result", false);
		Map<String, Object> paramsRuleTwo = Map.of("result", false);
		RuleResult ruleResult = RuleEngine.evaluate(RuleUno.class, paramsRuleOne).or(RuleDos.class, paramsRuleTwo);
		var exception = assertThrows(RuleException.class, () -> ruleResult.fire());
		// Then
		assertEquals("ErrorDos", exception.getMessage());
	}

	@Test
	void evaluateResultTrueIfThenFunctionTest() {
		// Given
		var num = 1;
		// When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).ifThen(num, (number) -> number + 1);
		// Then
		assertEquals(2, result);
	}

	@Test
	void evaluateResultTrueIfThenOrElseFunctionTest() {
		// Given
		var num = 1;
		// When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).ifThenOrElse(num,
				(number) -> number + 1, (number) -> number - 1);
		// Then
		assertEquals(0, result);
	}

	@Test
	void evaluateResultTrueIfThenCallableTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).ifThen(() -> 1 + 1);
		// Then
		assertEquals(2, result);
	}

	@Test
	void evaluateResultTrueIfThenOrElseCallableTest() {
		// Given - When
		var result = RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).ifThenOrElse(() -> 1 + 1, () -> 1 + 2);
		// Then
		assertEquals(3, result);
	}

	@Test
	void evaluateResultTrueIfThenConsumerTest() {
		// Given
		List<String> list = new ArrayList<>();
		list.add("1");
		Consumer<List<String>> consumerA = a -> a.add("2");
		assertEquals(1, list.size());
		// When
		RuleEngine.evaluate(RuleUno.class, Map.of("result", true)).ifThen(list, consumerA);
		// Then
		assertEquals(2, list.size());
	}

	@Test
	void evaluateResultTrueIfThenOrElseConsumerTest() {
		// Given
		List<String> list = new ArrayList<>();
		list.add("1");
		Consumer<List<String>> consumerA = a -> a.add("2");
		Consumer<List<String>> consumerB = a -> a.clear();
		assertEquals(1, list.size());
		// When
		RuleEngine.evaluate(RuleUno.class, Map.of("result", false)).ifThenOrElse(list, consumerA, consumerB);
		// Then
		assertEquals(0, list.size());
	}
}
