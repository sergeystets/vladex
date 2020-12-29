package com.sergeystets.vladex.api;

import com.sergeystets.vladex.model.VerifyPhoneResponse;

import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

public class VladexApi {

    final RestTemplate restTemplate;

    @Inject
    VladexApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VerifyPhoneResponse verifyPhone(String serverUrl, String phoneNumber) {
        return restTemplate.getForObject(serverUrl + "/v3/ab2b0a3b-62d8-4bca-95f1-f4fff1c25cc6", VerifyPhoneResponse.class);
    }

}
