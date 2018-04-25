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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class GroupItem {

  private List<EventItem> items = new ArrayList<>();
  private HeaderItem header = new HeaderItem();
  private int number;
  private TimeRange timeRange;

  public GroupItem(final TimeRange timeRange) {
    this.timeRange = timeRange;
  }

  public List<EventItem> getItems() {
    return items;
  }

  public void addItem(EventItem item) {
    items.add(item);
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public long getNumber() {
    return number;
  }

  public HeaderItem getHeaderItem() {
    CalendarEventDTO dto = new CalendarEventDTO();

    if (timeRange.equals(TimeRange.weeks)) {
      dto.setTitle("Semaine " + number);
    } else if (timeRange.equals(TimeRange.months)) {
      dto.setTitle("Mois " + number);
    }
    header.setData(dto);
    return header;
  }
}
