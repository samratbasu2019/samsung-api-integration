package com.org.samsung.service.manager.service;

import com.org.samsung.service.manager.helper.ServiceEndpointsHelper;
import com.org.samsung.service.manager.util.UtilsTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ServiceManagerTest {
    @MockBean
    private ServiceEndpointsHelper serviceEndpointsHelper;
    @Autowired
    private ServiceEndpoint serviceEndpoint;
    /* Test invokeEmptyTest with empty mono objects */
    @Test
    public void invokeEmptyTest() throws Exception {
        given(serviceEndpointsHelper.getObjects(any(), any()))
                .willReturn(Mono.empty());
        given(serviceEndpointsHelper.getPredictedNationality(any(), any()))
                .willReturn(Mono.empty());
        given(serviceEndpointsHelper.getPredictedGender(any(), any()))
                .willReturn(Mono.empty());
        var res = serviceEndpoint.invoke(UtilsTest.getInvokePayload());

        StepVerifier.create(res)
                .expectNextCount(0)
                .expectNext()
                .verifyComplete();

    }

    /* Test getSupportedParameters method*/
    @Test
    public void getSupportedParametersTest() throws Exception {
        var res = serviceEndpoint.getSupportedParameters();
        StepVerifier.create(res)
                .expectNextCount(1)
                .expectNext()
                .verifyComplete();

    }

    /* Test getMaxConcurrentInvocations method*/
    @Test
    public void getMaxConcurrentInvocationsTest() throws Exception {
        var res = serviceEndpoint.getMaxConcurrentInvocations();
        StepVerifier.create(res)
                .expectNextCount(1)
                .expectNext()
                .verifyComplete();

    }
}
