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

package org.silverpeas.mobile.server.comparator;

import org.silverpeas.components.delegatednews.model.DelegatedNews;

import java.util.Comparator;

/**
 * @author: svu
 */
public class DelegatedNewsBeginDateComparatorAsc implements Comparator<DelegatedNews> {
  static public DelegatedNewsBeginDateComparatorAsc comparator = new DelegatedNewsBeginDateComparatorAsc();

  public int compare(DelegatedNews p1, DelegatedNews p2) {
    int compareResult = p1.getBeginDate().compareTo(p2.getBeginDate());
    if (compareResult == 0) {
      // both objects have same begin date
      // first one is the one which has been validated after the other
      compareResult = 0 - p1.getValidationDate().compareTo(p2.getValidationDate());
    }

    return compareResult;
  }
}