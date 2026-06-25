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

package org.silverpeas.mobile.shared.dto.media;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;

public class AlbumDTO extends BaseDTO implements Comparable<AlbumDTO> {

  private String name;
  private int countMedia;
  private boolean root = false;

  public AlbumDTO() {
    setClassName(this.getClass().getSimpleName());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int compareTo(AlbumDTO o) {
    return name.compareTo(o.getName());
  }

  public int getCountMedia() {
    return countMedia;
  }

  public void setCountMedia(final int countMedia) {
    this.countMedia = countMedia;
  }

  public boolean getRoot() {
    return root;
  }

  public void setRoot(final boolean root) {
    this.root = root;
  }


  public static AlbumDTO fromJSON(JsPropertyMap<Object> json) {
    return fromJSON(json, null);
  }
  public static AlbumDTO fromJSON(JsPropertyMap<Object> json, AlbumDTO dto) {

    if (dto == null) dto = new AlbumDTO();
    if (json == null) {
      return dto;
    }

    dto.fromSuperJSON(json);
    dto.setClassName(dto.getClass().getSimpleName());

    dto.setRoot(json.get("root") != null ? Boolean.parseBoolean(json.get("root").toString()) : false);
    dto.setName(json.get("name") != null ? json.get("name").toString() : null);
    dto.setCountMedia(json.get("countMedia") != null ? Integer.parseInt(json.get("countMedia").toString()) : 0);

    return dto;
  }
}
