/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth;

import com.ricohapi.auth.entity.AuthResult;
import com.ricohapi.auth.response.Discovery;
import com.ricohapi.auth.response.Token;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.TypeReference;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthClient {
    private static final String URL_AUTH_TOKEN = "https://auth.beta2.ucs.ricoh.com/auth/token";
    private static final String URL_AUTH_DISCOVERY = "https://auth.beta2.ucs.ricoh.com/auth/discovery";

    private String clientId;
    private String clientSecret;
    private String userId;
    private String userPass;
    private String accessToken = null;
    private String refreshToken;
    private Calendar expire;

    public AuthClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void setResourceOwnerCreds(String userId, String userPass) {
        this.userId = userId;
        this.userPass = userPass;
    }

    public void session(Scope scope, final CompletionHandler<AuthResult> handler) {
        try {
            Token token = token(scope);
            Discovery discovery = discovery(scope, token.getAccessToken());
            accessToken = discovery.getAccessToken();
            expire = Calendar.getInstance();
            expire.add(Calendar.SECOND, discovery.getExpiresIn());
            refreshToken = discovery.getRefreshToken();
            handler.onCompleted(new AuthResult(discovery.getAccessToken()));
        } catch (Exception e) {
            handler.onThrowable(e);
        }
    }

    public void getAccessToken(CompletionHandler<AuthResult> handler) {
        if (accessToken == null) {
            handler.onThrowable(new RicohAPIException("wrong usage: use the session method to get an access token."));
            return;
        }

        if (Calendar.getInstance().before(expire)) {
            handler.onCompleted(new AuthResult(accessToken));
            return;
        }

        try {
            Token token = refresh();
            accessToken = token.getAccessToken();
            expire = Calendar.getInstance();
            expire.add(Calendar.SECOND, token.getExpiresIn());
            refreshToken = token.getRefreshToken();
            handler.onCompleted(new AuthResult(accessToken));
        } catch (Exception e) {
            handler.onThrowable(e);
        }
    }

    private Token token(Scope scope) throws RicohAPIException, IOException {
        RicohAPIRequest request = new RicohAPIRequest(URL_AUTH_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/x-www-form-urlencoded");
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("username", userId);
        params.put("password", userPass);
        params.put("grant_type", "password");
        params.put("scope", joinScopes(Scope.AUTH, Scope.DISCOVERY, scope));
        request.post(header, params);

        if (request.isSucceeded()) {
            return JSON.decode(request.getResponseBody(), Token.class);
        } else {
            throw new RicohAPIException(request.getResponseCode(), request.getErrorBody());
        }
    }

    private Discovery discovery(Scope scope, String authToken) throws RicohAPIException, IOException {
        RicohAPIRequest request = new RicohAPIRequest(URL_AUTH_DISCOVERY);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + authToken);
        header.put("content-type", "application/x-www-form-urlencoded");
        Map<String, String> params = new HashMap<>();
        params.put("scope", joinScopes(scope));
        request.post(header, params);

        if (request.isSucceeded()) {
            Map<String, Discovery> respMap = JSON.decode(request.getResponseBody(), new TypeReference<Map<String, Discovery>>() {});
            return respMap.get(scope.getUrl());
        } else {
            throw new RicohAPIException(request.getResponseCode(), request.getErrorBody());
        }
    }
    private Token refresh() throws RicohAPIException, IOException {
        RicohAPIRequest request = new RicohAPIRequest(URL_AUTH_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/x-www-form-urlencoded");
        Map<String, String> params = new HashMap<>();
        params.put("refresh_token", refreshToken);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "refresh_token");
        request.post(header, params);

        if (request.isSucceeded()) {
            return JSON.decode(request.getResponseBody(), Token.class);
        } else {
            throw new RicohAPIException(request.getResponseCode(), request.getErrorBody());
        }
    }

    private static String joinScopes(Scope... scopes) {
        if (scopes == null || scopes.length == 0) {
            return "";
        }
        List<String> urlList = new ArrayList<>();
        for (Scope scope: scopes) {
            if (scope != null) {
                urlList.add(scope.getUrl());
            }
        }
        return StringUtils.join(urlList, " ");
    }
}
