/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.shared.dto.navigation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonSubTypes({
    @JsonSubTypes.Type(value = ApplicationInstanceDTO.class, name = "ApplicationInstanceDTO"),
    @JsonSubTypes.Type(value = SpaceDTO.class, name = "SpaceDTO")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")

public abstract class SilverpeasObjectDTO implements Serializable, Comparable<SilverpeasObjectDTO> {

  private String id;
  private String label;
  private Integer orderNum;


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(final Integer orderNum) {
    this.orderNum = orderNum;
  }

  @Override
  public int compareTo(SilverpeasObjectDTO o) {
    return orderNum.compareTo(o.orderNum);
  }
}
