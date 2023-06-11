package com.org.samsung.service.manager.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static com.org.samsung.service.manager.utils.Constants.EVENTS;

@Component
public class WebServiceClient {
    Logger LOG = LoggerFactory.getLogger(WebServiceClient.class);

    private WebClient client;

    /**
     * @Method getClient
     * invokes external get endpoints with query string
     * @Returns Objects for a get endPoint
     */
    public Mono<?> getClient(final String host, final String uri,
                                  final Map<String, Object> queryParams){
        /*Generate query string to call external API*/
        String url = uri + "?" + queryParams.entrySet().stream().map(c->
                c.getKey() + "=" + c.getValue()).collect(Collectors.joining("&"));

        client = WebClient.create(host);
        LOG.info(EVENTS, "WebServiceClient", "getClient", "url", url);
        return client.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Object.class)
                .log("Get Client fetched the result")
                .onErrorResume(error -> Mono.error(error.getCause()));
    }
    /**
     * @Method postClient
     * invokes external post endpoints with request payload
     * @Returns Objects coming from the external API
     */
    public Mono<?> postClient(final String host, final String uri,
                                  final String body, final Class<?> clazz){
        client = WebClient.create(host);
        LOG.info(EVENTS, "WebServiceClient", "postClient", "url", uri, body);
        return client.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(body), String.class)
                .retrieve()
                .bodyToMono(clazz)
                .log("Post Client fetched the result")
                .onErrorResume(error -> Mono.error(error.getCause()));
    }
}
