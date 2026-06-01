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

package org.silverpeas.mobile.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

/**
 * @author svu
 */
public class MyLinkCategoryDTO implements Serializable {
    @JsonIgnore
    private Number catId;
    private String uri;

    @JsonIgnore
    private Number position;
    private String name;
    private String description;

    private String userId;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Number getCatId() {
        return catId;
    }

    public void setCatId(Number catId) {
        this.catId = catId;
    }

    public Number getPosition() {
        return position;
    }

    public void setPosition(Number position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static MyLinkCategoryDTO fromJSON(JsPropertyMap<Object> json) {
        MyLinkCategoryDTO dto = new MyLinkCategoryDTO();
        dto.setUri((String) json.get("uri"));
        dto.setCatId((Number) json.get("catId"));
        dto.setPosition((Number) json.get("position"));
        dto.setName((String) json.get("name"));
        dto.setDescription((String) json.get("description"));
        dto.setUserId((String) json.get("userId"));
        return dto;
    }
}
