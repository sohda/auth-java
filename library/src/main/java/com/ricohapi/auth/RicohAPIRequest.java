/*
 * Copyright (c) 2016 Ricoh Company, Ltd. All Rights Reserved.
 * See LICENSE for more information.
 */

package com.ricohapi.auth;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RicohAPIRequest {
    private HttpURLConnection connection;
    private URL url;
    private String responseBody = null;
    private String errorBody = null;

    public RicohAPIRequest(String urlString) throws IOException {
        this.url = new URL(urlString);
        connection = (HttpURLConnection)url.openConnection();
    }

    public void post(Map<String, String> header, Map<String, String> params) throws IOException {
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        for (String key : header.keySet()) {
            connection.setRequestProperty(key, header.get(key));
        }
        DataOutputStream os = new DataOutputStream(connection.getOutputStream());
        os.writeBytes(joinParameters(params));
    }

    public String getResponseBody() throws IOException {
        if (this.responseBody == null) {
            this.responseBody = getBody(connection.getInputStream());
        }
        return this.responseBody;
    }

    public String getErrorBody() throws IOException {
        if (this.errorBody == null) {
            this.errorBody = getBody(connection.getErrorStream());
        }
        return this.errorBody;
    }

    private static String getBody(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private static String joinParameters(Map<String, String> paramMap) {
        List<String> paramList = new ArrayList<>();
        for (String key : paramMap.keySet()) {
            String value = paramMap.get(key);
            paramList.add(StringUtils.join(new String[]{key, value}, "="));
        }
        return StringUtils.join(paramList, "&");
    }

    public boolean isSucceeded() throws IOException {
        int responseCode = connection.getResponseCode();
        return responseCode >= 200 && responseCode < 300;
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

}
