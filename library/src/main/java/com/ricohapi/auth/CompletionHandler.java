/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth;

public interface CompletionHandler<T> {
    void onCompleted(T result);
    void onThrowable(Throwable t);
}
