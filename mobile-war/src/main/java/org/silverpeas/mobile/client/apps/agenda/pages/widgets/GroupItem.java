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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.components.UnorderedList;

/**
 * @author svu
 */
public class GroupItem extends Composite {

  private int number;
  private TimeRange timeRange;
  private static GroupItem.GroupItemUiBinder uiBinder = GWT.create(GroupItem.GroupItemUiBinder.class);
  private int year;

  @UiField(provided = true) protected AgendaMessages msg = null;

  public void setYear(final int year) {
    this.year = year;
  }

  public int getYear() {
    return year;
  }

  interface GroupItemUiBinder extends UiBinder<Widget, GroupItem> {}

  @UiField
  SpanElement groupTitle, eventCount;

  @UiField
  UnorderedList events;

  @UiField
  HTMLPanel container;

  @UiField
  HTML group;

  public GroupItem() {
    msg = GWT.create(AgendaMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setTimeRange(final TimeRange timeRange) {
    this.timeRange = timeRange;
  }

  public void addEvent(EventItem item) {
    events.add(item);
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public long getNumber() {
    return number;
  }

  public void render() {
    if (timeRange.equals(TimeRange.weeks)) {
      groupTitle.setInnerText(msg.weekTitle(String.valueOf(number)) + " (" + year + ")");
    } else if (timeRange.equals(TimeRange.months)) {
      String mouth = "";
      mouth = getMouthLabel(mouth);
      groupTitle.setInnerText(mouth + " " + year);
    }
    eventCount.setInnerText(""+events.getCount());
  }

  private String getMouthLabel(String mouth) {
    switch (number) {
      case 1 :
        mouth = msg.january();
        break;
      case 2:
        mouth = msg.february();
        break;
      case 3:
        mouth = msg.march();
        break;
      case 4:
        mouth = msg.april();
        break;
      case 5:
        mouth = msg.may();
        break;
      case 6:
        mouth = msg.june();
        break;
      case 7 :
        mouth = msg.july();
        break;
      case 8:
        mouth = msg.august();
        break;
      case 9:
        mouth = msg.september();
        break;
      case 10:
        mouth = msg.october();
        break;
      case 11:
        mouth = msg.november();
        break;
      case 12:
        mouth = msg.december();
        break;
    }
    return mouth;
  }

  @UiHandler("group")
  void open(ClickEvent event) {
    container.getElement().toggleClassName("open");
  }

  public boolean isEmpty() {
    return (events.getCount() <= 0);
  }
}
