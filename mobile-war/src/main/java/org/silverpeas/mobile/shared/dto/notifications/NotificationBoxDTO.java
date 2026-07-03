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

package org.silverpeas.mobile.shared.dto.notifications;

import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.BaseDTO;

/**
 * @author: svu
 */
public class NotificationBoxDTO extends BaseDTO {

  private long idNotif;
  private boolean sended;

  public NotificationBoxDTO() {
    super();
  }

  public long getIdNotif() {
    return idNotif;
  }

  public void setIdNotif(final long idNotif) {
    this.idNotif = idNotif;
  }

  public boolean isSended() {
    return sended;
  }

  public void setSended(boolean sended) {
    this.sended = sended;
  }

  public Object toJSON() {
    JsPropertyMap<Object> json = JsPropertyMap.of();
    json.set("idNotif", idNotif);
    json.set("sended", Boolean.valueOf(sended));
    return json;
  }

  public static NotificationBoxDTO fromJSON(JsPropertyMap<Object> json, NotificationBoxDTO dto) {

    if (dto == null) {
      dto = new NotificationBoxDTO();
    }
    if (json == null) {
      return dto;
    }

    Object idNotif = json.get("idNotif");
    if (idNotif != null) {
      dto.setIdNotif((long) Js.asDouble(idNotif));
    }

    Object sended = json.get("sended");
    if (sended != null) {
      dto.setSended(Boolean.TRUE.equals(sended));
    }

    return dto;
  }
}
