package com.example.rules.engine.exception;

import static com.example.rules.engine.log.LogManager.loggerWithCustomLayout;

public class RuleException extends RuntimeException {

	private static final long serialVersionUID = 9097361300319730360L;

	public RuleException(String message) {
		super(message);
		loggerWithCustomLayout.error("### Rule error: {} ###", message);
	}
}
