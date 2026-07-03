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

package org.silverpeas.mobile.shared.dto;

import com.google.gwt.core.client.GWT;
import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

public class MyLinkDTO implements Serializable {

    private String uri = "";

    private Double linkId = -1d;
    private Double position = -1d;

    private String name = "";
    private String description = "";
    private String url = "";

    private Boolean visible = true;
    private Boolean popup = false;

    private String userId = "";
    private String instanceId = "";
    private String objectId = "";

    private Double categoryId;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Double getLinkId() {
        return linkId;
    }

    public void setLinkId(Double linkId) {
        this.linkId = linkId;
    }

    public Double getPosition() {
        return position;
    }

    public void setPosition(Double position) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getPopup() {
        return popup;
    }

    public void setPopup(Boolean popup) {
        this.popup = popup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Double getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Double categoryId) {
        this.categoryId = categoryId;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        json.set("uri", uri);
        json.set("linkId", linkId);
        json.set("position", position);
        json.set("name", name);
        json.set("description", description);
        json.set("url", url);
        json.set("visible", visible);
        json.set("popup", popup);
        json.set("userId", userId);
        json.set("instanceId", instanceId);
        json.set("objectId", objectId);

        if (categoryId != null) {
            json.set("categoryId", categoryId);
        }

        return json;
    }

    public static MyLinkDTO fromJSON(JsPropertyMap<Object> json) {
        MyLinkDTO dto = new MyLinkDTO();

        try {
            dto.setUri((String) json.get("uri"));

            Object linkId = json.get("linkId");
            if (linkId instanceof Number) {
                dto.setLinkId(((Number) linkId).doubleValue());
            }

            Object position = json.get("position");
            if (position instanceof Number) {
                dto.setPosition(((Number) position).doubleValue());
            }

            dto.setName((String) json.get("name"));
            dto.setDescription((String) json.get("description"));
            dto.setUrl((String) json.get("url"));

            Object visible = json.get("visible");
            if (visible instanceof Boolean) {
                dto.setVisible((Boolean) visible);
            }

            Object popup = json.get("popup");
            if (popup instanceof Boolean) {
                dto.setPopup((Boolean) popup);
            }

            dto.setUserId((String) json.get("userId"));
            dto.setInstanceId((String) json.get("instanceId"));
            dto.setObjectId((String) json.get("objectId"));

            Object categoryId = json.get("categoryId");
            if (categoryId instanceof Number) {
                dto.setCategoryId(((Number) categoryId).doubleValue());
            }

        } catch (Throwable t) {
            GWT.log("ERREUR parsing MyLinkDTO", t);
        }

        return dto;
    }
}
