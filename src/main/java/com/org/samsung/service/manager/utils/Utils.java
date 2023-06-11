package com.org.samsung.service.manager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.samsung.service.manager.model.Registry;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Utils {
    private Utils(){}
    /**
     * @Method getRegistryByKey
     * Reads registry from the resource folder and create registry objects
     * @Parameter Takes the key in order to parse the registry
     * @Returns registry object for a given key
     */
    public static Registry getRegistryByKey(final String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource staticDataResource = new ClassPathResource("registry.json");
        String registry = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        return mapper.convertValue(mapper.readValue(registry, Map.class).get(key), Registry.class);
    }
}
