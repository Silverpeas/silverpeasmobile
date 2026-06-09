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

package org.silverpeas.mobile.shared.dto.faq;


import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class QuestionDetailDTO implements Serializable {
    private long id;
    private String question;
    private String categoryId;
    private String description;
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static QuestionDetailDTO fromJSON(JsPropertyMap<Object> json) {
        QuestionDetailDTO dto = new QuestionDetailDTO();

        Object idObj = json.get("id");
        if (idObj != null) {
            if (idObj instanceof Number) {
                dto.setId(((Number) idObj).longValue());
            } else {
                dto.setId(Long.parseLong(idObj.toString()));
            }
        }

        dto.setQuestion((String) json.get("question"));
        dto.setQuestion((String) json.get("categoryId"));
        dto.setQuestion((String) json.get("description"));

        return dto;
    }

    public JsPropertyMap<Object> toJSON() {
        JsPropertyMap<Object> json = JsPropertyMap.of();

        json.set("id", getId());
        json.set("question", getQuestion());
        json.set("categoryId", getCategoryId());
        json.set("description", getDescription());

        return json;
    }

}
