package com.org.samsung.service.manager.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.samsung.service.manager.model.Contract;
import com.org.samsung.service.manager.model.Registry;
import reactor.core.publisher.Mono;
public interface ServiceEndpointsHelper {
    /**
     * @method getPredictedNationality
     * @return Predicts nationality for a given Name
     * @parameter takes registry and request contract as its input
     */
    Mono<?> getPredictedNationality(final Registry predictNationality, Contract request);
    /**
     * @Method getPredictedGender
     * @return Predicts Gender for a given Name
     * @parameter takes registry and request contract as its input
     */
    Mono<?> getPredictedGender(final Registry predictGender, Contract request);
    /**
     * @Method getObjects
     * @return post objects to an opensource platform
     * @parameter takes registry and request contract as its input
     */
    Mono<?> getObjects(final Registry postObjects, Contract request) throws JsonProcessingException;
}
