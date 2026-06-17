/*
 * Copyright (C) 2000 - 2026 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.services.rest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import elemental2.core.Global;
import elemental2.core.JsArray;
import elemental2.dom.*;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.DEBUG;
import org.silverpeas.mobile.client.common.network.SpMobileRequestBuilder;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;

import static elemental2.dom.DomGlobal.fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AbstractService {

    private static String token = null;
    public String encode(String param) {
        if (param != null) {
            return URL.encodeQueryString(param);
        }
        return param;
    }

    private RequestInit initRequest(String method, String contentType) {
        AbortController controller = new AbortController();
        RequestInit init = RequestInit.create();
        init.setMethod(method);

        Headers headers = new Headers();
        headers.append("Content-Type", contentType);
        headers.append("Authorization", "Basic " + token);
        init.setHeaders(headers);

        init.setSignal(controller.signal);

        DomGlobal.setTimeout(__ -> {
            controller.abort();
        }, SpMobileRequestBuilder.TIMEOUT);

        return init;
    }

    public String escapeJson(String s) {
        if (s == null) return "";

        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    protected <T> void request(
            String method,
            String url,
            Object body,
            Function<Object, T> mapper,
            RestCallback<T> callback, String contentType) {

        if (isJsonResponse(contentType)) {
            requestJson(method, url, body, mapper, callback);
        } else {
            requestText(method, url, body, (RestCallback<String>) callback);
        }
    }

    protected <T> void requestJson(String method, String url, Object body, Function<Object, T> mapper,
            RestCallback<T> callback) {
        DEBUG.log(this, "requestJson " + method + " " + url);
        RequestInit init = initRequest(method, "application/json");

        if (body != null) {
            init.setBody(body.toString());
        }

        RestMethod restMethod = new RestMethod(init);

        fetch(url, init)
                .then(response -> {
                    restMethod.setStatusCode(response.status);
                    restMethod.setHeaders(response.headers);

                    if (!response.ok) {
                        throw new RuntimeException("HTTP " + response.status);
                    }

                    if (response.status == 204) {
                        return null;
                    }

                    return response.text();
                })
                .then(text -> {

                    if (text == null || text.trim().isEmpty()) {
                        callback.onSuccess(restMethod, null);
                        return null;
                    }

                    try {
                        Object json = Global.JSON.parse(text);
                        T result = mapper.apply(json);
                        callback.onSuccess(restMethod, result);
                    } catch (Exception e) {
                        callback.onFailure(restMethod, e);
                    }

                    return null;
                })
                .catch_(err -> {
                    callback.onFailure(restMethod,
                            new RuntimeException(String.valueOf(err)));
                    return null;
                });
    }

    protected void requestText(String method, String url, Object body, RestCallback<String> callback) {
        DEBUG.log(this, "requestText " + method + " " + url);
        RequestInit init = initRequest(method, "text/plain");

        if (body != null) {
            init.setBody(body.toString());
        }

        RestMethod restMethod = new RestMethod(init);

        fetch(url, init)
                .then(response -> {
                    restMethod.setStatusCode(response.status);
                    restMethod.setHeaders(response.headers);

                    if (!response.ok) {
                        throw new RuntimeException("HTTP " + response.status);
                    }

                    if (response.status == 204) {
                        callback.onSuccess(restMethod, null);
                        return null;
                    }

                    return response.text();
                })
                .then(text -> {
                    callback.onSuccess(restMethod, text);
                            //(text == null || text.isEmpty()) ? null : text);
                    return null;
                })
                .catch_(err -> {
                    callback.onFailure(restMethod,
                            new RuntimeException(String.valueOf(err)));
                    return null;
                });
    }

    private boolean isJsonResponse(String contentType) {
        return contentType != null && contentType.contains("application/json");
    }

    protected <T> void request(
            String method,
            String url,
            Object body,
            Function<Object, T> mapper,
            RestCallback<T> callback) {

        request(method, url, body, mapper,callback, "application/json");

    }

    protected <T> void get(
            String url,
            Function<Object, T> mapper,
            RestCallback<T> callback) {
        request("GET", url, null, mapper, callback);
    }

    protected <T> void getText(
            String url,
            Function<Object, T> mapper,
            RestCallback<T> callback) {
        request("GET", url, null, mapper, callback, "text/plain");
    }

    protected <T> void post(
            String url,
            String body,
            Function<Object, T> mapper,
            RestCallback<T> callback) {

        request("POST", url, body, mapper, callback);
    }

    protected <T> void put(
            String url,
            String body,
            Function<Object, T> mapper,
            RestCallback<T> callback) {

        request("PUT", url, body, mapper, callback);
    }

    protected <T> void delete(
            String url,
            Object body,
            Function<Object, T> mapper,
            RestCallback<T> callback) {

        request("DELETE", url, body, mapper, callback);
    }

    protected String toJsonArray(List<String> values) {

        return values.stream()
                .map(v -> "\"" + escapeJson(v) + "\"")
                .collect(java.util.stream.Collectors.joining(",", "[", "]"));
    }

    protected <T> List<T> mapArray(
            Object result,
            Function<JsPropertyMap<Object>, T> mapper) {

        JsArray<Object> array = (JsArray<Object>) result;

        List<T> values = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            values.add(
                    mapper.apply(
                            (JsPropertyMap<Object>) array.getAt(i)
                    )
            );
        }

        return values;
    }

    protected Boolean asBoolean(Object result) {
        return (Boolean) result;
    }

    public void initContext(final String login, final String password,
                            final String domainId) {
        String credentials = login + "@domain" + domainId + ":" + password;
        byte[] credentialsEncoded = Base64.encode(credentials.getBytes());
        this.token = convertByteArrayToString(credentialsEncoded);
    }

    private static String convertByteArrayToString(byte[] byteArray) {
        String s = "";
        for (int i = 0; i < byteArray.length; i++) {
            s += (char) (byteArray[i]);
        }
        return s;
    }
}
