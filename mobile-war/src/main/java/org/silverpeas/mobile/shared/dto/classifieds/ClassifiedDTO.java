/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.shared.dto.classifieds;

import java.io.Serializable;

/**
 * @author svu
 */
public class ClassifiedDTO implements Serializable {
  private String title;
  private String description;
  private String price;
  private String type;
  private String category;
  private String picture1;
  private String picture2;
  private String picture3;
  private String picture4;


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

  public String getPicture1() {
    return picture1;
  }

  public void setPicture1(final String picture1) {
    this.picture1 = picture1;
  }

  public String getPicture2() {
    return picture2;
  }

  public void setPicture2(final String picture2) {
    this.picture2 = picture2;
  }

  public String getPicture3() {
    return picture3;
  }

  public void setPicture3(final String picture3) {
    this.picture3 = picture3;
  }

  public String getPicture4() {
    return picture4;
  }

  public void setPicture4(final String picture4) {
    this.picture4 = picture4;
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
}
