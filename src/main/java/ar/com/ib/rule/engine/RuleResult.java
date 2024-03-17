package ar.com.ib.rule.engine;

import static ar.com.ib.rule.engine.RuleEngine.evaluate;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static ar.com.ib.rule.engine.log.LogManager.loggerWithCustomLayout;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import ar.com.ib.rule.engine.exception.RuleEngineException;
import ar.com.ib.rule.engine.exception.RuleException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class RuleResult {

	private Boolean result;

	private String message;

	public RuleResult and(Class<? extends IRule> rule, Map<String, Object> params) {
		loggerWithCustomLayout.debug("### Ejecutando método AND ###");
		return (TRUE.equals(getResult())) ? evaluate(rule, params) : this;
	}

	public RuleResult or(Class<? extends IRule> rule, Map<String, Object> params) {
		loggerWithCustomLayout.debug("### Ejecutando método OR ###");
		return (TRUE.equals(getResult())) ? this : evaluate(rule, params);
	}

	public void fire() {
		if (FALSE.equals(getResult())) {
			throw new RuleException(getMessage());
		}
	}

	public <T> void ifThen(T t, Consumer<T> consumer) {
		fire();
		consumer.accept(t);
	}

	public <T> void ifThenOrElse(T t, Consumer<T> consumerThen, Consumer<T> consumerElse) {
		try {
			ifThen(t, consumerThen);
		} catch (RuleException e) {
			consumerElse.accept(t);
		}
	}

	public <T, R> R ifThen(T t, Function<T, R> function) {
		fire();
		return function.apply(t);
	}

	public <T, R> R ifThenOrElse(T t, Function<T, R> functionThen, Function<T, R> functionElse) {
		try {
			return ifThen(t, functionThen);
		} catch (RuleException e) {
			return functionElse.apply(t);
		}
	}

	public <T> T ifThen(Callable<T> callable) {
		fire();
		return callCalleable(callable);
	}

	public <T> T ifThenOrElse(Callable<T> callableThen, Callable<T> callableElse) {
		try {
			return ifThen(callableThen);
		} catch (RuleException e) {
			return callCalleable(callableElse);
		}
	}

	private <T> T callCalleable(Callable<T> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			throw new RuleEngineException(e);
		}
	}
}
