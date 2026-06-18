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
public class SurveyDetailDTO implements Serializable {
  private String id;
  private List<QuestionDTO> questions = new ArrayList<>();
  private String comments;
  private boolean anonymComment;
  private boolean canParticipate;
  private int nbParticipation;

  public List<QuestionDTO> getQuestions() {
    return questions;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setComments(final String text) {
    this.comments = text;
  }

  public String getComments() {
    return comments;
  }

  public boolean isAnonymComment() {
    return anonymComment;
  }

  public void setAnonymComment(final boolean anonymComment) {
    this.anonymComment = anonymComment;
  }

  public boolean isCanParticipate() {
    return canParticipate;
  }

  public void setCanParticipate(final boolean canParticipate) {
    this.canParticipate = canParticipate;
  }

  public int getNbParticipation() {
    return nbParticipation;
  }

  public void setNbParticipation(final int nbParticipation) {
    this.nbParticipation = nbParticipation;
  }

  public void setQuestions(final List<QuestionDTO> questions) {
    this.questions = questions;
  }

  public static SurveyDetailDTO fromJSON(JsPropertyMap<Object> json) {
    if (json == null) {
      return null;
    }

    SurveyDetailDTO dto = new SurveyDetailDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);

    dto.setComments(json.get("comments") != null ? json.get("comments").toString() : null);

    dto.setAnonymComment(json.get("anonymComment") != null &&
            Boolean.parseBoolean(json.get("anonymComment").toString()));

    dto.setCanParticipate(json.get("canParticipate") != null &&
            Boolean.parseBoolean(json.get("canParticipate").toString()));

    dto.setNbParticipation(json.get("nbParticipation") != null
            ? Integer.parseInt(json.get("nbParticipation").toString())
            : 0);
    if (json.get("questions") != null) {
      JsArray<Object> list = (JsArray<Object>) json.get("questions");
      List<QuestionDTO> result = new ArrayList<>();


        for (int i = 0; i < list.length; i++) {
          JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
          result.add(QuestionDTO.fromJSON(map));
        }
        dto.setQuestions(result);
    }

    return dto;
  }

  public JsPropertyMap<Object> toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();

    json.set("id", getId());
    json.set("comments", getComments());
    json.set("anonymComment", isAnonymComment());
    json.set("canParticipate", isCanParticipate());
    json.set("nbParticipation", getNbParticipation());

    if (getQuestions() != null) {
      List<JsPropertyMap<Object>> questionsJson = new ArrayList<>();

      for (QuestionDTO q : getQuestions()) {
        if (q != null) {
          questionsJson.add(q.toJSON());
        }
      }

      json.set("questions", questionsJson.toArray());
    }

    return json;
  }
}
