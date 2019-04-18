/*
 * Copyright (C) 2000 - 2019 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.dto.reminder;

import org.silverpeas.mobile.shared.dto.almanach.TimeUnitDTO;

import java.io.Serializable;

/**
 * @author svu
 */
public class ReminderDTO implements Serializable {
  private String id;
  private String cId;
  private String cProperty;
  private String userId;
  private String dateTime;
  private Integer duration;
  private TimeUnitDTO timeUnit;
  private String text;
  private boolean canBeModified;
  private boolean canBeDeleted;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getcId() {
    return cId;
  }

  public void setcId(final String cId) {
    this.cId = cId;
  }

  public String getcProperty() {
    return cProperty;
  }

  public void setcProperty(final String cProperty) {
    this.cProperty = cProperty;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(final String dateTime) {
    this.dateTime = dateTime;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(final Integer duration) {
    this.duration = duration;
  }

  public TimeUnitDTO getTimeUnit() {
    return timeUnit;
  }

  public void setTimeUnit(final TimeUnitDTO timeUnit) {
    this.timeUnit = timeUnit;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public boolean isCanBeModified() {
    return canBeModified;
  }

  public void setCanBeModified(final boolean canBeModified) {
    this.canBeModified = canBeModified;
  }

  public boolean isCanBeDeleted() {
    return canBeDeleted;
  }

  public void setCanBeDeleted(final boolean canBeDeleted) {
    this.canBeDeleted = canBeDeleted;
  }
}
