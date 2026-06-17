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
import com.google.gwt.core.client.GWT;
import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

/**
 * @author svu
 */
public class MyLinkDTO implements Serializable {

    private String uri = "";
    @JsonIgnore //TODO remove after RestyGWT removal
    private Number linkId = -1;
    @JsonIgnore
    private Number position = -1;
    private String name = "";
    private String description = "";
    private String url = "";
    private Boolean visible = true;
    private Boolean popup = false;
    private String userId = "";
    private String instanceId = "";
    private String objectId = "";

    @JsonIgnore
    private Number categoryId;

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public Number getLinkId() {
        return linkId;
    }

    public void setLinkId(final Number linkId) {
        this.linkId = linkId;
    }

    public Number getPosition() {
        return position;
    }

    public void setPosition(final Number position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public Boolean getPopup() {
        return popup;
    }

    public void setPopup(final Boolean popup) {
        this.popup = popup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(final String instanceId) {
        this.instanceId = instanceId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public Number getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Number categoryId) {
        this.categoryId = categoryId;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        json.set("uri", getUri());
        json.set("linkId", getLinkId());
        json.set("position", getPosition());
        json.set("name", getName());
        json.set("description", getDescription());
        json.set("url", getUrl());
        json.set("visible", getVisible());
        json.set("popup", getPopup());
        json.set("userId", getUserId());
        json.set("instanceId", getInstanceId());
        json.set("objectId", getObjectId());
        json.set("categoryId", getCategoryId());

        return json;
    }

    public static MyLinkDTO fromJSON(JsPropertyMap<Object> json) {
        MyLinkDTO dto = new MyLinkDTO();
        try {
            dto.setUri((String) json.get("uri"));
            if (json.get("linkId") != null) dto.setLinkId(((Number) json.get("linkId")).intValue());
            if (json.get("position") != null) dto.setPosition(((Number) json.get("position")).intValue());
            dto.setName((String) json.get("name"));
            dto.setDescription((String) json.get("description"));
            dto.setUrl((String) json.get("url"));
            dto.setVisible((Boolean) json.get("visible"));
            dto.setPopup((Boolean) json.get("popup"));
            dto.setUserId((String) json.get("userId"));
            dto.setInstanceId((String) json.get("instanceId"));
            dto.setObjectId((String) json.get("objectId"));
            dto.setCategoryId((Number) json.get("categoryId"));
        } catch (Throwable t) {
            GWT.log("ERREUR", t);
        }
        return dto;
    }
}
