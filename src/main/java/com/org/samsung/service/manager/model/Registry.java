package com.org.samsung.service.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Registry {
    private String host;
    private String method;
    private String headers;
    private String uri;
    @JsonProperty("max-concurrency")
    private String maxConcurrency;
    private List<Parameters> parameters;

}
