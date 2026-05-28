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

import com.google.gwt.http.client.URL;
import elemental2.core.JsArray;
import elemental2.dom.AbortController;
import elemental2.dom.DomGlobal;
import elemental2.dom.Headers;
import elemental2.dom.RequestInit;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.network.SpMobileRequestBuilder;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import static elemental2.dom.DomGlobal.fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AbstractService {
    public String encode(String param) {
        return URL.encodeQueryString(param);
    }

    public RequestInit initRequest (String method, String contentType) {
        AbortController controller = new AbortController();
        RequestInit init = RequestInit.create();
        init.setMethod(method);

        Headers headers = new Headers();
        headers.append("Content-Type", contentType);
        headers.append("Authorization", "Bearer " + AuthentificationManager.getInstance().getHeader(AuthentificationManager.XSTKN));
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
            RestCallback<T> callback) {

        RequestInit init = initRequest(method, "application/json");

        if (body != null) {
            init.setBody(body.toString());
        }

        RestMethod restMethod = new RestMethod(init);

        fetch(url, init)
                .then(response -> {
                    restMethod.setStatusCode(response.status);

                    if (!response.ok) {
                        throw new RuntimeException("HTTP " + response.status);
                    }

                    if (response.status == 204) {
                        return null;
                    }

                    return response.json();
                })
                .then(result -> {
                    callback.onSuccess(mapper.apply(result));
                    return null;
                })
                .catch_(err -> {
                    callback.onFailure(restMethod,
                            new RuntimeException(err.toString()));
                    return null;
                });
    }

    protected <T> void get(
            String url,
            Function<Object, T> mapper,
            RestCallback<T> callback) {

        request("GET", url, null, mapper, callback);
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

    protected  <T> List<T> mapArray(
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
}
