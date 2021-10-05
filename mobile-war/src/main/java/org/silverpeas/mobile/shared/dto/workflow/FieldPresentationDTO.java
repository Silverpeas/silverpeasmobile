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

package org.silverpeas.mobile.shared.dto.workflow;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class FieldPresentationDTO extends BaseDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;

  private String label;
  private String value;
  private String type;
  private String displayerName;
  private String instanceId;

  public FieldPresentationDTO() {
  }

  public FieldPresentationDTO(final String key, final String value, final String id, final String type, String displayerName) {
    this.label = key;
    this.value = value;
    setId(id);
    this.type = type;
    this.displayerName = displayerName;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FieldPresentationDTO other = (FieldPresentationDTO) obj;
    if (getId() == null) {
      return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return Integer.parseInt(getId());
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getDisplayerName() {
    return displayerName;
  }

  public void setDisplayerName(final String displayerName) {
    this.displayerName = displayerName;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }
}
