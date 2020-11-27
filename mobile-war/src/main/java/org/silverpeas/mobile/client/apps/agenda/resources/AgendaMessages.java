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

package org.silverpeas.mobile.client.apps.agenda.resources;

import com.google.gwt.i18n.client.Messages;

public interface AgendaMessages extends Messages {
  String title();
  String titleEvent();

  String to();
  String toDay();
  String from();

  String content();
  String delete();
  String add();

  String DAY();
  String WEEK();
  String MONTH();
  String YEAR();

  String NEVER();


  String AWAITING();
  String ACCEPTED();
  String DECLINED();
  String TENTATIVE();

  String ATTIME();

  String HOUR();
  String HOURS(String number);
  String MINUTES(String number);
  String OneDAY();
  String TwoDay();
  String OneWeek();

  String addReminder();
  String acceptParticipation();
  String rejectParticipation();
  String tentativeParticipation();

  String weekFilter();
  String mouthFilter();

  String weekTitle(String number);

  String january();
  String february();
  String march();
  String april();
  String may();
  String june();
  String july();
  String august();
  String september();
  String october();
  String november();
  String december();

  String monday();
  String tuesday();
  String wednesday();
  String thursday();
  String friday();
  String saturday();
  String sunday();

  String noEvent();

  String the();
}
