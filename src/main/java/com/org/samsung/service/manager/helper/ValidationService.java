package com.org.samsung.service.manager.helper;

import com.org.samsung.service.manager.model.Contract;
import com.org.samsung.service.manager.model.Registry;

public interface ValidationService {
    /**
     * @method isValid
     * @return true/false post validating incoming parameters with accepted params list in the registry for each endpoints
     * @parameter takes registry, request contract and type of request as its input
     */
    boolean isValid(final Registry registry, final Contract request, final String type);
}
