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

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.DomainDTO;

import java.util.List;

public class ServiceConnection extends AbstractService {
    public final static String PATH = "/silverpeas/services/mobile/connection";

    public void generateSecurityCode(String login, String domainId, RestCallback<Void> callback) {
        get(PATH + "/securityCode/" + encode(login) + "/" + encode(domainId),
                result -> null,
                callback);
    }

    public void checkSecurityCode(String login, String domainId, String code, RestCallback<Boolean> callback) {
        get(PATH + "/securityCode/check/" + encode(login) + "/" + encode(domainId) + "/" + encode(code),
                this::asBoolean,
                callback);
    }

    public void getDomains(RestCallback<List<DomainDTO>> callback) {
        get(PATH + "/domains/",
                this::mapDomains,
                callback);
    }

    private List<DomainDTO> mapDomains(Object result) {
        return mapArray(result, DomainDTO::fromJSON);
    }

    public void setTabletMode(RestCallback<Boolean> callback) {
        put(PATH + "/setTabletMode/",
                null,
                this::asBoolean,
                callback);
    }

    public void userExist(String login, String domainId, RestCallback<Boolean> callback) {
        get(PATH + "/userExist/" + encode(login) + "/" + encode(domainId),
                this::asBoolean,
                callback);
    }

    public void userAcceptsTermsOfService(RestCallback<Void> callback) {
        put(PATH + "/userAcceptsTermsOfService/",
                null,
                result -> null,
                callback);
    }

    public void changePwd(String newPwd, RestCallback<Void> callback) {
        put(PATH + "/changePwd/",
                newPwd,
                result -> null,
                callback);
    }

    public void login(List<String> ids, RestCallback<DetailUserDTO> callback) {
        post(PATH + "/login/",
                toJsonArray(ids),
                result -> DetailUserDTO.fromJSON(
                        (JsPropertyMap<Object>) result
                ),
                callback
        );
    }

}
