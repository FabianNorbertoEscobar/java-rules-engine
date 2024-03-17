package ar.com.ib.rule.engine.log;

import org.apache.logging.log4j.Logger;

public class LogManager {

	private LogManager() { }

	private static final String LOGGER_WITH_CUSTOM_LAYOUT = "LOGGER_WITH_CUSTOM_LAYOUT";

	public static final Logger loggerWithCustomLayout = org.apache.logging.log4j.LogManager
			.getLogger(LOGGER_WITH_CUSTOM_LAYOUT);

	private static final String LOGGER_WITH_JSON_LAYOUT = "LOGGER_WITH_JSON_LAYOUT";

	public static final Logger loggerWithJsonLayout = org.apache.logging.log4j.LogManager
			.getLogger(LOGGER_WITH_JSON_LAYOUT);

}
