package com.sergeystets.vladex.api;

import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

public class VladexApi {

    private final RestTemplate restTemplate;

    @Inject
    VladexApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
