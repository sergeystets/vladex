package com.sergeystets.vladex.model;

import lombok.Data;

@Data
public class VerifyPhoneResponse {

    private long otpExpirationSeconds;
}
