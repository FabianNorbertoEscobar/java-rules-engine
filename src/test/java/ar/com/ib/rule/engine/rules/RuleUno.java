package ar.com.ib.rule.engine.rules;

import ar.com.ib.rule.engine.IRule;

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
