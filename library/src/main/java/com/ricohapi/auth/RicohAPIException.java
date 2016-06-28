/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth;

public class RicohAPIException extends Exception {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public RicohAPIException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public RicohAPIException(String message) {
        super(message);
    }

    public RicohAPIException(Throwable cause) {
        super(cause);
    }
}
