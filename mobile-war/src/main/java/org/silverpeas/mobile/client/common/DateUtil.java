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

package org.silverpeas.mobile.client.common;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import java.util.Date;

/**
 * @author svu
 */
public class DateUtil {

  private static final int ISO_THURSDAY = 4;
  private static final int MAX_DAY_OF_WEEK = 6;
  private static final int DAYS_IN_WEEK = 7;
  private static final long MILLIS_DAY = 86400000;

  @SuppressWarnings("deprecation")
  public static int getWeek(Date date) {
    int dayOfWeek = 1 + (date.getDay() + MAX_DAY_OF_WEEK) % DAYS_IN_WEEK;
    Date nearestThu = addDays(date, ISO_THURSDAY - dayOfWeek);
    Date jan1 = getFirstDayOfTheYear(getYear(nearestThu));
    return 1 + dayDiff(nearestThu, jan1) / DAYS_IN_WEEK;
  }

  private static Date getFirstDayOfTheYear(int year) {
    DateTimeFormat format=DateTimeFormat.getFormat("yyyy-MM-dd");
    return format.parse(year + "-01-01");
  }

  public static Date addDays(final Date sourceDate, final long days) {
    return new Date(sourceDate.getTime() + (days * MILLIS_DAY));
  }

  public static int dayDiff(final Date firstDate, final Date secondDate) {
    return (int) ((firstDate.getTime() - secondDate.getTime()) / MILLIS_DAY);
  }

  public static boolean isSameDate(Date date0, Date date1) {
    return CalendarUtil.isSameDate(date0, date1);
  }

  public static int getYear(Date date) {
    DateTimeFormat format=DateTimeFormat.getFormat("yyyy");
    return Integer.parseInt(format.format(date));
  }

  public static int getMonth(Date date) {
    DateTimeFormat format=DateTimeFormat.getFormat("MM");
    return Integer.parseInt(format.format(date));
  }
}
