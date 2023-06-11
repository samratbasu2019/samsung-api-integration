package com.org.samsung.service.manager.helper.impl;

import com.org.samsung.service.manager.helper.ValidationService;
import com.org.samsung.service.manager.model.Contract;
import com.org.samsung.service.manager.model.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.org.samsung.service.manager.utils.Constants.EVENTS;


@Component
public class ValidationServiceImpl implements ValidationService {
    static final Logger LOG = LoggerFactory.getLogger(ValidationServiceImpl.class);
    /**
     * @method isValid
     * @return true/false post validating incoming parameters with accepted params list in the registry for each endpoints
     * @parameter takes registry, request contract and type of request as its input
     */
    @Override
    public boolean isValid(final Registry registry, final Contract request, final String type) {
        boolean isValid = false;
        if (Objects.isNull(request) || Objects.isNull(request.getParameters())) {
            return isValid;
        }
        /*Generate registry and contract map of an input data stream*/
        Map<String, Object> registryMap = registry.getParameters().stream()
                .collect(Collectors.toMap(a -> a.getKey(), a -> a.getValue()));
        Map<String, Object> contractMap = request.getParameters().stream()
                .collect(Collectors.toMap(a -> a.getKey(), a -> a.getValue()));

        /*Compare registry and contract params and returns true/false post validation*/
        if (contractMap.size() > 0 ) {
            isValid = registryMap.keySet().containsAll(contractMap.keySet());
        }
        LOG.info(EVENTS, "isValid", isValid, type, contractMap, registryMap);


        return isValid;
    }
}
