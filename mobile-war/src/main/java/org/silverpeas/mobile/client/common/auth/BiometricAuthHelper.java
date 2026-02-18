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
package org.silverpeas.mobile.client.common.auth;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.common.auth.resources.BiometricMessages;

public class BiometricAuthHelper {

    private static final BiometricMessages MESSAGES = GWT.create(BiometricMessages.class);

    public static native void loginLocal(AuthCallback callback) /*-{

        function base64ToBuffer(base64) {
            var str = atob(base64);
            var buf = new ArrayBuffer(str.length);
            var bufView = new Uint8Array(buf);
            for (var i = 0; i < str.length; i++) {
                bufView[i] = str.charCodeAt(i);
            }
            return buf;
        }

        var stored = JSON.parse($wnd.localStorage.getItem("localCredential"));
        if (!stored) {
            callback.@org.silverpeas.mobile.client.common.auth.AuthCallback::onResult(Z)(false);
            return;
        }

        var publicKey = {
            challenge: new Uint8Array(32),
            allowCredentials: [{
                id: base64ToBuffer(stored.rawId),
                type: "public-key"
            }],
            userVerification: "required",
            timeout: 60000
        };

        $wnd.crypto.getRandomValues(publicKey.challenge);

        $wnd.navigator.credentials.get({ publicKey: publicKey })
            .then(function(assertion) {
                callback.@org.silverpeas.mobile.client.common.auth.AuthCallback::onResult(Z)(true);
            }, function(err) {
                console.error(err);
                callback.@org.silverpeas.mobile.client.common.auth.AuthCallback::onResult(Z)(false);
            });

    }-*/;

    public static native void registerLocal(
            String appName,
            String successMessage,
            String errorMessage
    ) /*-{

        function bufferToBase64(buf) {
            return btoa(String.fromCharCode.apply(null, new Uint8Array(buf)));
        }

        var publicKey = {
            challenge: new Uint8Array(32),
            rp: { name: appName },
            user: {
                id: new Uint8Array(16),
                name: "user_local",
                displayName: "Utilisateur Local"
            },
            pubKeyCredParams: [{ type: "public-key", alg: -7 }],
            authenticatorSelection: { userVerification: "required" },
            timeout: 60000
        };

        $wnd.crypto.getRandomValues(publicKey.challenge);
        $wnd.crypto.getRandomValues(publicKey.user.id);

        $wnd.navigator.credentials.create({ publicKey: publicKey })
            .then(function(credential) {

                var stored = {
                    id: credential.id,
                    rawId: bufferToBase64(credential.rawId),
                    response: {
                        clientDataJSON: bufferToBase64(credential.response.clientDataJSON),
                        attestationObject: bufferToBase64(credential.response.attestationObject)
                    }
                };

                $wnd.localStorage.setItem("localCredential", JSON.stringify(stored));
                $wnd.alert(successMessage);
            }, function(err) {
                console.error(err);
                $wnd.alert(errorMessage);
        });

    }-*/;

    public static void register() {
        registerLocal(
                MESSAGES.appName(),
                MESSAGES.successMessage(),
                MESSAGES.errorMessage()
        );
    }

    public static native boolean isRegistered() /*-{
        var stored = $wnd.localStorage.getItem("localCredential");
        return stored != null;
    }-*/;

}
