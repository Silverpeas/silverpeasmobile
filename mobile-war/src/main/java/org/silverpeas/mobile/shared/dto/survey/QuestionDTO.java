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

package org.silverpeas.mobile.shared.dto.survey;

import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class QuestionDTO implements Serializable {
  private String id;
  private String type;
  private String label;
  private List<AnswerDTO> answers = new ArrayList<>();
  private List<ResponseDTO> responses = new ArrayList<>();



    public void setAnswers(final List<AnswerDTO> answers) {
    this.answers = answers;
  }

  public void setResponses(final List<ResponseDTO> responses) {
    this.responses = responses;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public List<AnswerDTO> getAnswers() {
    return answers;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public List<ResponseDTO> getResponses() {
    return responses;
  }

  public static QuestionDTO fromJSON(JsPropertyMap<Object> json) {
    if (json == null) {
      return null;
    }

    QuestionDTO dto = new QuestionDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setType(json.get("type") != null ? json.get("type").toString() : null);
    dto.setLabel(json.get("label") != null ? json.get("label").toString() : null);

    // answers
    if (json.get("answers") != null) {
      JsArray<Object> list = (JsArray<Object>) json.get("answers");

      List<AnswerDTO> result = new ArrayList<>();
      for (int i = 0; i < list.length; i++) {
        JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
        result.add(AnswerDTO.fromJSON(map));
      }

      dto.setAnswers(result);
    }

    // responses
    if (json.get("responses") != null) {
      JsArray<Object> list = (JsArray<Object>) json.get("responses");

      List<ResponseDTO> result = new ArrayList<>();
      for (int i = 0; i < list.length; i++) {
        JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
        result.add(ResponseDTO.fromJSON(map));
      }

      dto.setResponses(result);
    }

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();

    json.set("id", getId());
    json.set("type", getType());
    json.set("label", getLabel());

    if (getAnswers() != null) {
      List<JsPropertyMap<Object>> answersJson = new ArrayList<>();

      for (AnswerDTO a : getAnswers()) {
        if (a != null) {
          answersJson.add(a.toJSON());
        }
      }

      json.set("answers", answersJson.toArray());
    }

    if (getResponses() != null) {
      List<JsPropertyMap<Object>> responsesJson = new ArrayList<>();

      for (ResponseDTO r : getResponses()) {
        if (r != null) {
          responsesJson.add(r.toJSON());
        }
      }

      json.set("responses", responsesJson.toArray());
    }

    return json;
  }
}
