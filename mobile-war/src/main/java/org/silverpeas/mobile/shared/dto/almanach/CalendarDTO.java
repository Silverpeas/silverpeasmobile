/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.shared.dto.almanach;

import jsinterop.base.JsPropertyMap;

/**
 * @author svu
 */
public class CalendarDTO {
    private String id;
    private String title;
    private String zoneId;
    private String uri;

    private boolean main;
    private boolean userMainPersonal;
    private boolean userPersonal;
    private String ownerName;


    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(final String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(final boolean main) {
        this.main = main;
    }

    public boolean isUserMainPersonal() {
        return userMainPersonal;
    }

    public void setUserMainPersonal(final boolean userMainPersonal) {
        this.userMainPersonal = userMainPersonal;
    }

    public boolean isUserPersonal() {
        return userPersonal;
    }

    public void setUserPersonal(final boolean userPersonal) {
        this.userPersonal = userPersonal;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public static CalendarDTO fromJSON(JsPropertyMap<Object> json) {
        CalendarDTO dto = new CalendarDTO();

        dto.setId(json.get("id") != null ? json.get("id").toString() : null);
        dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
        dto.setZoneId(json.get("zoneId") != null ? json.get("zoneId").toString() : null);
        dto.setUri(json.get("uri") != null ? json.get("uri").toString() : null);

        dto.setMain(Boolean.parseBoolean(String.valueOf(json.get("main"))));
        dto.setUserMainPersonal(Boolean.parseBoolean(String.valueOf(json.get("userMainPersonal"))));
        dto.setUserPersonal(Boolean.parseBoolean(String.valueOf(json.get("userPersonal"))));

        dto.setOwnerName(json.get("ownerName") != null
                ? json.get("ownerName").toString()
                : null);

        return dto;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        json.set("id", getId());
        json.set("title", getTitle());
        json.set("zoneId", getZoneId());
        json.set("uri", getUri());

        json.set("main", isMain());
        json.set("userMainPersonal", isUserMainPersonal());
        json.set("userPersonal", isUserPersonal());

        json.set("ownerName", getOwnerName());

        return json;
    }
}
