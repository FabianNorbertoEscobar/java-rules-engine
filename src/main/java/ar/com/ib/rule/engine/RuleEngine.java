package ar.com.ib.rule.engine;

import static ar.com.ib.rule.engine.log.LogManager.loggerWithCustomLayout;
import static java.lang.Boolean.FALSE;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import ar.com.ib.rule.engine.exception.RuleEngineException;

public class RuleEngine {

	private RuleEngine() {
	}

	public static RuleResult evaluate(Class<? extends IRule> ruleClass, Map<String, Object> params) {
		var rule = getRuleInstanceWithParameters(ruleClass, params);
		var result = executeCheck(rule);
		return setResultMessage(rule, result);
	}

	private static RuleResult setResultMessage(IRule rule, Boolean result) {
		loggerWithCustomLayout.debug("### Obteniendo mensaje de resultado de la regla {} ###",
				rule.getClass().getName());
		String message = (FALSE.equals(result)) ? rule.getErrorMessage() : rule.getSuccessMessage();
		return new RuleResult(result, message);
	}

	private static Boolean executeCheck(IRule rule) {
		loggerWithCustomLayout.debug("### Ejecutando check de la regla {} ###", rule.getClass().getName());
		return rule.check();
	}

	private static IRule getRuleInstanceWithParameters(Class<? extends IRule> classRule, Map<String, Object> params) {
		IRule rule = getRuleInstance(classRule);
		setParameters(rule, params);
		return rule;
	}

	private static IRule getRuleInstance(Class<? extends IRule> rule) {
		try {
			loggerWithCustomLayout.debug("### Creando instancia de la regla {} ###", rule.getName());
			return rule.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuleEngineException(e);
		}
	}

	private static void setParameters(IRule rule, Map<String, Object> params) {
		loggerWithCustomLayout.debug("### Seteando parametros a la regla {} ###", rule.getClass().getName());
		Arrays.stream(rule.getClass().getDeclaredFields()).collect(Collectors.toList()).forEach(elem -> {
			try {
				loggerWithCustomLayout.debug("### Seteando parametro {} ###", elem.getName());
				var value = params.get(elem.getName());
				elem.setAccessible(true);
				elem.set(rule, value);
			} catch (Exception e) {
				throw new RuleEngineException(String.format("No se pudo cargar el campo %s", elem.getName()), e);
			}
		});
	}
}
