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

package org.silverpeas.mobile.shared.dto.media;

import jsinterop.base.JsPropertyMap;

public class PhotoDTO extends MediaDTO {
  private String dataPhoto;
  private String format;
  private int sizeH;
  private int sizeL;

  public PhotoDTO() {
    setClassName(this.getClass().getSimpleName());
  }

  public String getDataPhoto() {
    return dataPhoto;
  }
  public void setDataPhoto(String dataPhoto) {
    this.dataPhoto = dataPhoto;
  }
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  public void setSizeH(final int sizeH) {
    this.sizeH = sizeH;
  }

  public int getSizeH() {
    return sizeH;
  }

  public void setSizeL(final int sizeL) {
    this.sizeL = sizeL;
  }

  public int getSizeL() {
    return sizeL;
  }

  public static PhotoDTO fromJSON(JsPropertyMap<Object> json) {

    PhotoDTO dto = new PhotoDTO();
    if (json == null) {
      return dto;
    }

    dto.setClassName(dto.getClass().getSimpleName());
    MediaDTO.fromJSON(json, dto);

    dto.setDataPhoto(json.get("dataPhoto") != null ? json.get("dataPhoto").toString() : null);
    dto.setFormat(json.get("format") != null ? json.get("format").toString() : null);
    dto.setSizeH(json.get("sizeH") != null ? ((Number) json.get("sizeH")).intValue() : 0);
    dto.setSizeL(json.get("sizeL") != null ? ((Number) json.get("sizeL")).intValue() : 0);

    return dto;
  }

}
