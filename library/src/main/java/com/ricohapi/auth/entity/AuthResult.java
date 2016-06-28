/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth.entity;

public class AuthResult {
    private String accessToken;

    public AuthResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
