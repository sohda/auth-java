/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth;

public enum Scope {
    AUTH("https://ucs.ricoh.com/scope/api/auth"),
    DISCOVERY("https://ucs.ricoh.com/scope/api/discovery"),
    MSTORAGE("https://ucs.ricoh.com/scope/api/udc2"),
    VSTREAM("https://ucs.ricoh.com/scope/api/udc2"),
    CAMERACTL("https://ucs.ricoh.com/scope/api/udc2");

    private String url;

    Scope(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
