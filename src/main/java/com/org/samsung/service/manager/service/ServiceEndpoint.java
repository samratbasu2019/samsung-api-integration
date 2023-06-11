package com.org.samsung.service.manager.service;

import com.sun.jdi.InvocationException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * A simple service interface that combines an {@link #invoke(java.util.Map)}
 * method to accept a map of named parameters and return zero or more results with
 * additional policy methods that specify {@link #getSupportedParameters() which
 * parameters are accepted} and {@link #getMaxConcurrentInvocations() the maximum
 * number of concurrent invocations allowed}.
 */
public interface ServiceEndpoint<T> {
    /**
     * @return the maximum number of concurrent invocations allowed, or {@code -1}
     * if unlimited.
     */
    Mono<Map<String, Object>> getMaxConcurrentInvocations() throws IOException;
    /**
     * @return the set of parameter names supported by this service
     */
    Mono<Map<String, Object>> getSupportedParameters() throws IOException;
    /**
     * Invoke the service with a map of named parameters (any subset of the supported
     * list of parameters) and return the results.
     *
     * @param request a map from parameter names to values
     * @return the results of
     */
    Mono<?> invoke(final Map<String, Object> request) throws InvocationException, IOException;
}