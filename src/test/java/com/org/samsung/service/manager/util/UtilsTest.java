package com.org.samsung.service.manager.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.org.samsung.service.manager.utils.Constants.PREDICT_NATIONALITY;

public class UtilsTest {
    private UtilsTest(){}
    public static Map<String, Object> getInvokePayload() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource staticDataResource = new ClassPathResource("invoke-request.json");
        String request = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        return mapper.readValue(request, new TypeReference<Map<String, Object>>(){});
    }
    public static String getExpectedInvokeAPIResponse() {
        ClassPathResource staticDataResource = new ClassPathResource("expected-invoke-api-response.json");
        String response = null;
        try {
            response = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
