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

package org.silverpeas.mobile.shared.dto.media;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class MediaDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean download;
    private String title;
    private String name;
    private String updateDate;
    private String updater;
    private String creator;
    private String creationDate;
    private int commentsNumber;
    private String instance;
    private String mimeType;
    private long size;

    public boolean getDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(final String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(final String updater) {
        this.updater = updater;
    }

    public void setCommentsNumber(final int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(final String instance) {
        this.instance = instance;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public static MediaDTO fromJSON(JsPropertyMap<Object> json) {
        return fromJSON(json, null);
    }
    public static MediaDTO fromJSON(JsPropertyMap<Object> json, MediaDTO dto) {

        if (dto == null) dto = new MediaDTO();
        if (json == null) {
            return dto;
        }

        dto.fromSuperJSON(json);
        dto.setClassName(dto.getClass().getSimpleName());

        dto.setDownload(json.get("download") != null ? Boolean.parseBoolean(json.get("download").toString()) : false);
        dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
        dto.setName(json.get("name") != null ? json.get("name").toString() : null);
        dto.setUpdateDate(json.get("updateDate") != null ? json.get("updateDate").toString() : null);
        dto.setUpdater(json.get("updater") != null ? json.get("updater").toString() : null);
        dto.setCreator(json.get("creator") != null ? json.get("creator").toString() : null);
        dto.setCreationDate(json.get("creationDate") != null ? json.get("creationDate").toString() : null);
        dto.setInstance(json.get("instance") != null ? json.get("instance").toString() : null);
        dto.setMimeType(json.get("mimeType") != null ? json.get("mimeType").toString() : null);
        dto.setCommentsNumber(json.get("commentsNumber") != null ? Integer.parseInt(json.get("commentsNumber").toString()) : 0);
        dto.setSize(json.get("size") != null ? Long.parseLong(json.get("size").toString()) : 0L);

        return dto;
    }
}
