package ar.com.ib.rule.engine.exception;

import static ar.com.ib.rule.engine.log.LogManager.loggerWithCustomLayout;
import static ar.com.ib.rule.engine.log.LogManager.loggerWithJsonLayout;

public class RuleEngineException extends RuntimeException {

	private static final long serialVersionUID = 2639172653256790970L;

	public RuleEngineException(String message, Exception e) {
		super(message, e);
		loggerWithCustomLayout.error(message);
		loggerWithJsonLayout.error(e);
	}

	public RuleEngineException(Exception e) {
		super(e);
		loggerWithJsonLayout.error(e);
	}
}
