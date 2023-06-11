package com.org.samsung.service.manager.controller;

import com.org.samsung.service.manager.service.ServiceEndpoint;
import com.sun.jdi.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

import static com.org.samsung.service.manager.utils.Constants.EVENTS;

@RestController
@RequestMapping("/test")
public class ServiceController {
    static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);
    @Autowired
    private ServiceEndpoint serviceEndpoint;
    /**
     * @method fetchResults
     * @return Returns consolidated response for all registered endPoints
     * @parameter takes request map in order to identify endpoints and validate supported parameters
     */
    @PostMapping("/service-manager/invoke")
    public Mono<?> fetchResults(@RequestBody final Map<String, Object> request) throws IOException, InvocationException {
        LOG.info(EVENTS, "ServiceController", "fetchResults", request);
        return serviceEndpoint.invoke(request);
    }
    /**
     * @method getSupportedAttributes
     * @return Returns supported attribute list from the registry
     */
    @GetMapping("/service-manager/supported-attributes")
    public Mono<?> getSupportedAttributes() throws IOException, InvocationException {
        LOG.info(EVENTS, "ServiceController", "getSupportedAttributes", "supported-attributes");
        return serviceEndpoint.getSupportedParameters();
    }
    /**
     * @method getMaxConcurrency
     * @return Returns max concurrency for each external endpoints from the registry
     */
    @GetMapping("/service-manager/max-concurrency")
    public Mono<?> getMaxConcurrency() throws IOException, InvocationException {
        LOG.info(EVENTS, "ServiceController", "getMaxConcurrency", "max-concurrency");
        return serviceEndpoint.getMaxConcurrentInvocations();
    }
}
