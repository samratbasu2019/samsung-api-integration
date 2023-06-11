package com.org.samsung.service.manager.helper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.samsung.service.manager.helper.ServiceEndpointsHelper;
import com.org.samsung.service.manager.helper.ValidationService;
import com.org.samsung.service.manager.model.*;
import com.org.samsung.service.manager.webclient.WebServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.org.samsung.service.manager.utils.Constants.PREDICT_NATIONALITY;
import static com.org.samsung.service.manager.utils.Constants.PREDICT_GENDER;
import static com.org.samsung.service.manager.utils.Constants.POST_OBJECTS;
import static com.org.samsung.service.manager.utils.Constants.EVENTS;
import static com.org.samsung.service.manager.utils.Constants.NAME;
import static com.org.samsung.service.manager.utils.Constants.YEAR;
import static com.org.samsung.service.manager.utils.Constants.PRICE;

@Component
public class ServiceEndpointsHelperImpl implements ServiceEndpointsHelper {
    static final Logger LOG = LoggerFactory.getLogger(ServiceEndpointsHelperImpl.class);
    @Autowired
    private WebServiceClient webServiceClient;

    @Autowired
    private ValidationService validationService;
    /**
     * @method getPredictedNationality
     * @return Predicts nationality for a given Name
     * @parameter takes registry and request contract as its input
     */
    @Override
    public Mono<?> getPredictedNationality(final Registry predictNationality, final Contract request) {
        /*Validates if requested parameters in the request contract exist in the registry or not.
        * @return empty object in case of validation failures
        * */
        if(!validationService.isValid(predictNationality, request, PREDICT_NATIONALITY)) {
            return Mono.just("");
        }
        /*Generate query params based on request payload in case of a valid input data stream*/
        Map<String, Object>  queryParam = request.getParameters().stream()
                .collect(Collectors.toMap(a-> a.getKey(), a-> a.getValue()));

        LOG.info(EVENTS, "getPredictedNationality", "ServiceEndpointsHelperImpl", predictNationality.getHost(), queryParam);
        /*invoking external service*/
        return webServiceClient.getClient(predictNationality.getHost(),"", queryParam)
                .flatMap(Mono::just)
                .onErrorResume(error -> Mono.error(error.getCause()));
    }
    /**
     * @Method getPredictedGender
     * @return Predicts Gender for a given Name
     * @parameter takes registry and request contract as its input
     */
    @Override
    public Mono<?> getPredictedGender(final Registry predictGender, final Contract request) {
        /*Validates if requested parameters in the request contract exist in the registry or not.
         * @return empty object in case of validation failures
         * */
        if(!validationService.isValid(predictGender, request, PREDICT_GENDER)) {
            return Mono.just("");
        }
        Map<String, Object>  queryParam = request.getParameters().stream()
                .collect(Collectors.toMap(a-> a.getKey(), a-> a.getValue()));
        LOG.info(EVENTS, "getPredictedGender", "ServiceEndpointsHelperImpl", predictGender.getHost(), queryParam);
        /*invoking external service*/
        return webServiceClient.getClient(predictGender.getHost(),"", queryParam)
                .flatMap(Mono::just)
                .onErrorResume(error -> Mono.error(error.getCause()));
    }
    /**
     * @Method getObjects
     * @return post objects to an opensource platform
     * @parameter takes registry and request contract as its input
     */
    @Override
    public Mono<?> getObjects(final Registry postObjects, final Contract request) throws JsonProcessingException {
        /*Validates if requested parameters in the request contract exist in the registry or not.
         * @return empty object in case of validation failures
         * */
        if(!validationService.isValid(postObjects, request, POST_OBJECTS)) {
            return Mono.just("");
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Parameters> params = new ArrayList<>(request.getParameters());
        postObjects.setParameters(params);
        PostPayload payload = new PostPayload();
        DataObject data = new DataObject();
        /*construct request payload for POST call based on request contract*/
        request.getParameters().forEach(c -> {
            if (NAME.equalsIgnoreCase(c.getKey())) {
                payload.setName(c.getValue());
            } else if(YEAR.equalsIgnoreCase(c.getKey())) {
                data.setYear(Integer.valueOf(c.getValue()));
            } else if(PRICE.equalsIgnoreCase(c.getKey())) {
                data.setPrice(Double.valueOf(c.getValue()));
            }
        });
        payload.setData(data);
        String body = mapper.writer().writeValueAsString(payload);

        LOG.info(EVENTS, "getObjects", body, postObjects.getHost(), postObjects.getUri());
        /*invoking external service*/
        return webServiceClient.postClient(postObjects.getHost(),postObjects.getUri(), body, String.class)
                .onErrorResume(error -> Mono.error(error.getCause()));
    }
}
