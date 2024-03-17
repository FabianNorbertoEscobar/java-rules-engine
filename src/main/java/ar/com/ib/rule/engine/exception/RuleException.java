package ar.com.ib.rule.engine.exception;

import static ar.com.ib.rule.engine.log.LogManager.loggerWithCustomLayout;

public class RuleException extends RuntimeException {

	private static final long serialVersionUID = 9097361300319730360L;

	public RuleException(String message) {
		super(message);
		loggerWithCustomLayout.error("### Rule error: {} ###", message);
	}
}
