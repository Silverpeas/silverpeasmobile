/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
}
