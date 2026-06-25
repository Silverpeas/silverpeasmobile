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

package org.silverpeas.mobile.shared.dto.documents;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.media.MediaDTO;

import java.io.Serializable;
import java.util.Date;

public class AttachmentDTO {

    private String type = null;
    private long size;
    private String author = null;
    private String title = null;
    private int orderNum;
    private String id;
    private String instanceId;
    private String lang;
    private String userId;
    private boolean downloadAllowed;
    private String description;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDownloadAllowed(final boolean downloadAllowed) {
        this.downloadAllowed = downloadAllowed;
    }

    public boolean isDownloadAllowed() {
        return downloadAllowed;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AttachmentDTO fromJSON(JsPropertyMap<Object> json) {
        AttachmentDTO dto = new AttachmentDTO();
        if (json == null) {
            return dto;
        }

        dto.setType(json.get("type") != null ? json.get("type").toString() : null);
        dto.setSize(json.get("size") != null
                ? Long.parseLong(json.get("size").toString())
                : 0L);
        dto.setAuthor(json.get("author") != null ? json.get("author").toString() : null);
        dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
        dto.setOrderNum(json.get("orderNum") != null
                ? Integer.parseInt(json.get("orderNum").toString())
                : 0);
        dto.setId(json.get("id") != null ? json.get("id").toString() : null);
        dto.setInstanceId(json.get("instanceId") != null
                ? json.get("instanceId").toString()
                : null);
        dto.setLang(json.get("lang") != null ? json.get("lang").toString() : null);
        dto.setUserId(json.get("userId") != null ? json.get("userId").toString() : null);

        dto.setDownloadAllowed(json.get("downloadAllowed") != null
                && Boolean.parseBoolean(json.get("downloadAllowed").toString()));

        dto.setDescription(json.get("description") != null
                ? json.get("description").toString()
                : null);

        return dto;
    }
}
