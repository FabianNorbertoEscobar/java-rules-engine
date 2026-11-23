package com.example.rules.engine.rules;

import com.example.rules.engine.IRule;

public class RuleUno implements IRule {

	private Boolean result;

	@Override
	public Boolean check() {
		return result;
	}

	@Override
	public String getSuccessMessage() {
		return "OK";
	}

	@Override
	public String getErrorMessage() {
		return "Error";
	}
}
