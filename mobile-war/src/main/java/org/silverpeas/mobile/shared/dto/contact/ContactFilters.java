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

package org.silverpeas.mobile.shared.dto.contact;

import java.io.Serializable;

public class ContactFilters implements Serializable {

  private static final long serialVersionUID = -2505309587384249007L;

  public static final String ALL_EXT = "ALL_EXT&";
  public static final String ALL = "ALL&";
  public static final String MY = "MY&";


  private boolean hasContacts;
  private boolean hasPersonnalContacts;

  public boolean hasContacts() {
    return hasContacts;
  }

  public void setHasContacts(final boolean hasContacts) {
    this.hasContacts = hasContacts;
  }

  public boolean hasPersonnalContacts() {
    return hasPersonnalContacts;
  }

  public void setHasPersonnalContacts(final boolean hasPersonnalContacts) {
    this.hasPersonnalContacts = hasPersonnalContacts;
  }

}
