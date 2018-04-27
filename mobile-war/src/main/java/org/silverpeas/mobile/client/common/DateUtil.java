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

package org.silverpeas.mobile.client.common;

import java.util.Date;

/**
 * @author svu
 */
public class DateUtil {

  private static final int ISO_THURSDAY = 4;
  private static final int MAX_DAY_OF_WEEK = 6;
  private static final int DAYS_IN_WEEK = 7;
  private static final long MILLIS_DAY = 86400000;

  public static int getWeek(Date date) {
    final int dayOfWeek = 1 + (date.getDay() + MAX_DAY_OF_WEEK) % DAYS_IN_WEEK;
    final Date nearestThu = addDays(date, ISO_THURSDAY - dayOfWeek);
    final int year = nearestThu.getYear();
    final Date jan1 = new Date(year, 0, 1);
    return 1 + dayDiff(nearestThu, jan1) / DAYS_IN_WEEK;
  }

  public static Date addDays(final Date sourceDate, final long days) {
    return new Date(sourceDate.getTime() + (days * MILLIS_DAY));
  }

  public static int dayDiff(final Date firstDate, final Date secondDate) {
    return (int) ((firstDate.getTime() - secondDate.getTime()) / MILLIS_DAY);
  }

  public static boolean isSameDate(Date date0, Date date1) {
    assert date0 != null : "date0 cannot be null";
    assert date1 != null : "date1 cannot be null";
    return date0.getYear() == date1.getYear() && date0.getMonth() == date1.getMonth() &&
        date0.getDate() == date1.getDate();
  }
}
