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

package org.silverpeas.mobile.shared.dto.classifieds;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class ClassifiedDTO {

    private String id;
    private String title;
    private String description;
    private String price;
    private String type;
    private String category;
    private List<String> pictures;
    private String creatorName;
    private String creatorId;
    private String creationDate;
    private String updateDate;
    private int commentsNumber;
    private List<FormFieldDTO> fields;
    private boolean showPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(final List<String> pictures) {
        this.pictures = pictures;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(final String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(final String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(final int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(final String creatorId) {
        this.creatorId = creatorId;
    }

    public List<FormFieldDTO> getFields() {
        return fields;
    }

    public void setFields(final List<FormFieldDTO> fields) {
        this.fields = fields;
    }

    public boolean getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(final boolean showPrice) {
        this.showPrice = showPrice;
    }

    public JsPropertyMap<Object> toJSON_IdOnly() {
        JsPropertyMap<Object> json = JsPropertyMap.of();
        json.set("id", getId());
        return json;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

            json.set("id", getId());
            json.set("title", getTitle());
            json.set("description", getDescription());
            json.set("price", getPrice());
            json.set("type", getType());
            json.set("category", getCategory());
            json.set("creatorName", getCreatorName());
            json.set("creatorId", getCreatorId());
            json.set("creationDate", getCreationDate());
            json.set("updateDate", getUpdateDate());
            json.set("commentsNumber", getCommentsNumber());
            json.set("showPrice", getShowPrice());

            if (getPictures() != null) {
                JsArray<Object> list = new JsArray<>();
                for (String f : getPictures()) {
                    list.push(f);
                }
                json.set("pictures", list);
            }


            if (getFields() != null) {
                JsArray<Object> list = new JsArray<>();
                for (FormFieldDTO f : getFields()) {
                    list.push(f.toJSON());
                }
                json.set("fields", list);
            }
        return json;
    }

    public static ClassifiedDTO fromJSON(JsPropertyMap<Object> json) {
        ClassifiedDTO dto = new ClassifiedDTO();

        dto.setId(json.get("id") != null ? json.get("id").toString() : null);
        dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
        dto.setDescription(json.get("description") != null ? json.get("description").toString() : null);
        dto.setPrice(json.get("price") != null ? json.get("price").toString() : null);
        dto.setType(json.get("type") != null ? json.get("type").toString() : null);
        dto.setCategory(json.get("category") != null ? json.get("category").toString() : null);
        dto.setCreatorName(json.get("creatorName") != null ? json.get("creatorName").toString() : null);
        dto.setCreatorId(json.get("creatorId") != null ? json.get("creatorId").toString() : null);
        dto.setCreationDate(json.get("creationDate") != null ? json.get("creationDate").toString() : null);
        dto.setUpdateDate(json.get("updateDate") != null ? json.get("updateDate").toString() : null);

        dto.setCommentsNumber(json.get("commentsNumber") != null ? Integer.parseInt(json.get("commentsNumber").toString()) : 0);
        dto.setShowPrice(json.get("showPrice") != null && Boolean.parseBoolean(json.get("showPrice").toString()));


        if (json.get("pictures") != null) {
            JsArray<Object> list = (JsArray<Object>) json.get("pictures");
            List<String> result = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                result.add((String) list.getAt(i));
            }
            dto.setPictures(result);
        }

        if (json.get("fields") != null) {
             List<FormFieldDTO> result = new ArrayList<>();
            JsArray<Object> list = (JsArray<Object>) json.get("fields");
            for (int i = 0; i < list.length; i++) {
                JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                result.add(FormFieldDTO.fromJSON(map));
            }
            dto.setFields(result);
        } else {
            dto.setFields(new ArrayList<>());
        }

        return dto;
    }
}
