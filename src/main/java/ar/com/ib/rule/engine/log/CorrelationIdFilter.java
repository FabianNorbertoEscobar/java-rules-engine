package ar.com.ib.rule.engine.log;
import static ar.com.ib.rule.engine.log.LogManager.loggerWithCustomLayout;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorrelationIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Context
	HttpServerRequest request;

	private static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
	private static final String CORRELATION_ID = "correlationId";

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		var correlationId = request.getHeader(HEADER_CORRELATION_ID) != null
				&& !request.getHeader(HEADER_CORRELATION_ID).isEmpty() ? request.getHeader(HEADER_CORRELATION_ID)
						: generateCorrelationId();

		MDC.put(CORRELATION_ID, correlationId);
		ThreadContext.put(CORRELATION_ID, correlationId);
	}

	private String generateCorrelationId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext,
			ContainerResponseContext containerResponseContext) throws IOException {
		MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
		headers.add(HEADER_CORRELATION_ID, MDC.get(CORRELATION_ID));

		logResponseWithCorrelationId(containerResponseContext.getStatus());

		MDC.remove(CORRELATION_ID);
		ThreadContext.remove(CORRELATION_ID);
	}

	private static void logResponseWithCorrelationId(int status) {
		loggerWithCustomLayout.info("RESPONSE STATUS {}", status);
	}

}
