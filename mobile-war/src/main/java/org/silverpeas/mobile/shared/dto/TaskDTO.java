/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.shared.dto;


public class TaskDTO extends BaseDTO {

  private static final long serialVersionUID = 2921606984249560882L;

  private int percentCompleted;
  private String priority;
  private String name = "";
  private String delegator = "";
  private String endDate = "";
  private String externalId;

  @Override
  public boolean equals(Object obj) {
    return ((TaskDTO) obj).getId().equals(getId());
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setPercentCompleted(final int percentCompleted) {
    this.percentCompleted = percentCompleted;
  }

  public void setPriority(final String priority) {
    this.priority = priority;
  }

  public void setDelegator(final String delegator) {
    this.delegator = delegator;
  }

  public String getDelegator() {
    return delegator;
  }

  public void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public int getPercentCompleted() {
    return percentCompleted;
  }

  public String getPriority() {
    return priority;
  }

  public void setExternalId(final String externalId) {
    this.externalId = externalId;
  }

  public String getExternalId() {
    return externalId;
  }
}
