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

package org.silverpeas.mobile.shared.dto.survey;

import jsinterop.base.JsPropertyMap;

import java.io.Serializable;

/**
 * @author svu
 */
public class SurveyDTO implements Serializable {

  private String id;
  private String creator;
  private String creationDate;
  private String beginDate;
  private String endDate;
  private String nbVotes;
  private int nbMaxParticipations;
  private boolean anonymous;
  private String name;
  private String description;
  private int resultMode;
  private int resultView;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
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

  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(final String beginDate) {
    this.beginDate = beginDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public String getNbVotes() {
    return nbVotes;
  }

  public void setNbVotes(final String nbVotes) {
    this.nbVotes = nbVotes;
  }

  public int getNbMaxParticipations() {
    return nbMaxParticipations;
  }

  public void setNbMaxParticipations(final int nbMaxParticipations) {
    this.nbMaxParticipations = nbMaxParticipations;
  }

  public boolean isAnonymous() {
    return anonymous;
  }

  public void setAnonymous(final boolean anonymous) {
    this.anonymous = anonymous;
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

  public int getResultMode() {
    return resultMode;
  }

  public void setResultMode(final int resultMode) {
    this.resultMode = resultMode;
  }

  public int getResultView() {
    return resultView;
  }

  public void setResultView(final int resultView) {
    this.resultView = resultView;
  }

  public static SurveyDTO fromJSON(JsPropertyMap<Object> json) {
    if (json == null) {
      return null;
    }

    SurveyDTO dto = new SurveyDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setCreator(json.get("creator") != null ? json.get("creator").toString() : null);
    dto.setCreationDate(json.get("creationDate") != null ? json.get("creationDate").toString() : null);
    dto.setBeginDate(json.get("beginDate") != null ? json.get("beginDate").toString() : null);
    dto.setEndDate(json.get("endDate") != null ? json.get("endDate").toString() : null);
    dto.setNbVotes(json.get("nbVotes") != null ? json.get("nbVotes").toString() : null);

    dto.setNbMaxParticipations(json.get("nbMaxParticipations") != null ? Integer.parseInt(json.get("nbMaxParticipations").toString())  : 0);

    dto.setAnonymous(json.get("anonymous") != null && Boolean.parseBoolean(json.get("anonymous").toString()));

    dto.setName(json.get("name") != null ? json.get("name").toString() : null);
    dto.setDescription(json.get("description") != null ? json.get("description").toString() : null);

    dto.setResultMode(json.get("resultMode") != null ? Integer.parseInt(json.get("resultMode").toString()) : 0);

    dto.setResultView(json.get("resultView") != null ? Integer.parseInt(json.get("resultView").toString())  : 0);

    return dto;
  }
}
