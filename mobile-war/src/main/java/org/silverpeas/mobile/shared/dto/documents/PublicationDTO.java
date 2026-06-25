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

package org.silverpeas.mobile.shared.dto.documents;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;

import java.util.ArrayList;
import java.util.List;

public class PublicationDTO extends DocumentDTO implements Comparable<PublicationDTO> {

    private String name;
    private String description;
    private String version;
    private String creator;
    private String creationDate;
    private String updater;
    private String updateDate;
    private int commentsNumber = 0;
    private String instanceId;
    private boolean content;

    private boolean draft;

    private boolean publishable;
    private List<PublicationDTO> linkedPublications;
    private String vignette;
    private int viewsNumber;

    private List<String> notAllowedDownloads;

    public PublicationDTO() {
        setClassName(this.getClass().getSimpleName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(PublicationDTO o) {
        return name.compareTo(o.getName());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(final int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(final String instanceId) {
        this.instanceId = instanceId;
    }

    public void setContent(boolean content) {
        this.content = content;
    }

    public boolean getContent() {
        return content;
    }

    public List<PublicationDTO> getLinkedPublications() {
        return linkedPublications;
    }

    public void setLinkedPublications(final List<PublicationDTO> linkedPublications) {
        this.linkedPublications = linkedPublications;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getVignette() {
        return vignette;
    }

    public void setVignette(final String vignette) {
        this.vignette = vignette;
    }

    public int getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(final int viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public void setNotAllowedDownloads(List<String> notAllowedDownloads) {
        this.notAllowedDownloads = notAllowedDownloads;
    }

    public List<String> getNotAllowedDownloads() {
        return notAllowedDownloads;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isPublishable() {
        return publishable;
    }

    public void setPublishable(boolean publishable) {
        this.publishable = publishable;
    }

    public static PublicationDTO fromJSON(JsPropertyMap<Object> json) {
        PublicationDTO dto = new PublicationDTO();

        if (json == null) {
            return dto;
        }

        dto.fromSuperJSON(json);

        dto.setName(json.get("name") != null ? json.get("name").toString() : "");
        dto.setDescription(json.get("description") != null ? json.get("description").toString() : "");
        dto.setVersion(json.get("version") != null ? json.get("version").toString() : "");
        dto.setCreator(json.get("creator") != null ? json.get("creator").toString() : "");
        dto.setUpdater(json.get("updater") != null ? json.get("updater").toString() : "");
        dto.setCreationDate(json.get("creationDate") != null ? json.get("creationDate").toString() : "");
        dto.setUpdateDate(json.get("updateDate") != null ? json.get("updateDate").toString() : "");
        dto.setInstanceId(json.get("instanceId") != null ? json.get("instanceId").toString() : "");
        dto.setVignette(json.get("vignette") != null ? json.get("vignette").toString() : "");

        dto.setCommentsNumber(json.get("commentsNumber") != null ? ((Number) json.get("commentsNumber")).intValue() : 0);
        dto.setViewsNumber(json.get("viewsNumber") != null ? ((Number) json.get("viewsNumber")).intValue() : 0);

        dto.setContent(json.get("content") != null ? Boolean.parseBoolean(json.get("content").toString()) : false);
        dto.setDraft(json.get("draft") != null ? Boolean.parseBoolean(json.get("draft").toString()) : false);
        dto.setPublishable(json.get("publishable") != null ? Boolean.parseBoolean(json.get("publishable").toString()) : false);

        // ===== linked publications =====
        if (json.get("linkedPublications") != null) {
            JsArray<Object> list = (JsArray<Object>) json.get("linkedPublications");

            List<PublicationDTO> result = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                result.add(PublicationDTO.fromJSON(map));
            }

            dto.setLinkedPublications(result);
        }

        // ===== not allowed downloads =====
        if (json.get("notAllowedDownloads") != null) {
            JsArray<Object> list = (JsArray<Object>) json.get("notAllowedDownloads");

            List<String> result = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                String na = (String) list.getAt(i);
                result.add(na);
            }

            dto.setNotAllowedDownloads(result);
        }

        return dto;
    }
}
