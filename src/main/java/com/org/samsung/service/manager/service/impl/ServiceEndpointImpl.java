package com.org.samsung.service.manager.service.impl;

import com.org.samsung.service.manager.helper.ServiceEndpointsHelper;
import com.org.samsung.service.manager.model.Contract;
import com.org.samsung.service.manager.model.Registry;
import com.org.samsung.service.manager.service.ServiceEndpoint;
import com.org.samsung.service.manager.utils.Utils;
import com.sun.jdi.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.org.samsung.service.manager.utils.Constants.PREDICT_NATIONALITY;
import static com.org.samsung.service.manager.utils.Constants.PREDICT_GENDER;
import static com.org.samsung.service.manager.utils.Constants.POST_OBJECTS;
import static com.org.samsung.service.manager.utils.Constants.EVENTS;

@Service
public class ServiceEndpointImpl<T> implements ServiceEndpoint<T> {
    static final Logger LOG = LoggerFactory.getLogger(ServiceEndpointImpl.class);

    @Autowired
    private ServiceEndpointsHelper serviceEndpointsHelper;
    /**
     * @return Map of concurrency limit supported by each endpoints
     * @parameter does not take any parameters because all supported parameters are available in registry
     */
    @Override
    public Mono<Map<String, Object>> getMaxConcurrentInvocations() throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        Registry predictNationality = Utils.getRegistryByKey(PREDICT_NATIONALITY);
        Registry predictGender = Utils.getRegistryByKey(PREDICT_GENDER);
        Registry postObjects = Utils.getRegistryByKey(POST_OBJECTS);

        responseMap.put(PREDICT_NATIONALITY, predictNationality.getMaxConcurrency());
        responseMap.put(PREDICT_GENDER, predictGender.getMaxConcurrency());
        responseMap.put(POST_OBJECTS, postObjects.getMaxConcurrency());
        return Mono.just(responseMap);
    }

    /**
     * @return Map of parameters supported by each endpoints
     * @parameter does not take any parameters because all supported parameters are available in registry
     */
    @Override
    public Mono<Map<String, Object>> getSupportedParameters() throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        Registry predictNationality = Utils.getRegistryByKey(PREDICT_NATIONALITY);
        Registry predictGender = Utils.getRegistryByKey(PREDICT_GENDER);
        Registry postObjects = Utils.getRegistryByKey(POST_OBJECTS);

        responseMap.put(PREDICT_NATIONALITY, predictNationality.getParameters());
        responseMap.put(PREDICT_GENDER, predictGender.getParameters());
        responseMap.put(POST_OBJECTS, postObjects.getParameters());
        LOG.info(EVENTS, "getSupportedParameters", "registry-values", predictNationality, predictGender, postObjects);

        return Mono.just(responseMap)
                .onErrorResume(error -> Mono.error(error.getCause()));
    }

    /**
     * @return Map of combined response post validating request parameters
     * @parameter takes request map that contains key and parameters/attributes. key should match with the registry
     */
    @Override
    public Mono<?> invoke(final Map<String, Object> request) throws InvocationException, IOException {
        /* Parse Registry in order to get the Registry details */
        Registry predictNationality = Utils.getRegistryByKey(PREDICT_NATIONALITY);
        Registry predictGender = Utils.getRegistryByKey(PREDICT_GENDER);
        Registry postObjects = Utils.getRegistryByKey(POST_OBJECTS);

        Map<String, Object> responseMap = new HashMap<>();

        /* Parse request in order to get the contract and its parameters */
        Contract predictNationalityRequest = Utils.getContractByKey(request, PREDICT_NATIONALITY);
        Contract predictGenderRequest = Utils.getContractByKey(request, PREDICT_GENDER);
        Contract postObjectRequest = Utils.getContractByKey(request, POST_OBJECTS);

        LOG.info(EVENTS, "invoke", "registry-values", predictNationality, predictGender, postObjects);
        LOG.info(EVENTS, "invoke", "inbound-request", predictNationalityRequest, predictGenderRequest, postObjectRequest);

        /* concurrently invoke all endpoints and integrate the result */
        return Mono.zip(serviceEndpointsHelper.getPredictedNationality(predictNationality, predictNationalityRequest),
                        serviceEndpointsHelper.getPredictedGender(predictGender, predictGenderRequest),
                        serviceEndpointsHelper.getObjects(postObjects, postObjectRequest))
                .flatMap(tuple -> {
                    responseMap.put(PREDICT_NATIONALITY, tuple.getT1());
                    responseMap.put(PREDICT_GENDER, tuple.getT2());
                    responseMap.put(POST_OBJECTS, tuple.getT3());
                    return Mono.just(responseMap);
                });

    }
}
