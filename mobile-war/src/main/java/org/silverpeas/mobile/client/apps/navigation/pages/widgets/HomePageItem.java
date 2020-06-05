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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.DateUtil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;

import java.util.Date;

public class HomePageItem extends Composite {

  private CalendarEventDTO eventData;
  private PublicationDTO data;
  private static HomePageItemUiBinder uiBinder = GWT.create(HomePageItemUiBinder.class);
  @UiField Anchor link;
  protected ApplicationMessages msg = null;


  interface HomePageItemUiBinder extends UiBinder<Widget, HomePageItem> {
  }

  public HomePageItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(ApplicationMessages.class);
  }

  public void setData(CalendarEventDTO data) {
    this.eventData = data;
    if (data.getStartDate().equals(data.getEndDate()) || (data.isOnAllDay() && isOnOneDay())) {
      link.setHTML(data.getTitle() + "&nbsp;<span class='date'>" + data.getStartDate() + "</span>");
    } else {
      link.setHTML(data.getTitle() + "&nbsp;<span class='date'>" + data.getStartDate() + " - " +
          data.getEndDate() + "</span>");
    }
    link.setStyleName("ui-btn ui-icon-carat-r");
  }

  private boolean isOnOneDay() {

    DateTimeFormat df = DateTimeFormat.getFormat("dd/MM/yyyy");
    DateTimeFormat df2 = DateTimeFormat.getFormat("MM/dd/yyyy");
    Date start = null;
    Date end = null;
    try {
      start = df2.parse(eventData.getStartDate());
      end = df2.parse(eventData.getEndDate());
    } catch(Exception e) {
      start = df.parse(eventData.getStartDate());
      end = df.parse(eventData.getEndDate());
    }

    end = DateUtil.addDays(end, -1);
    return (DateUtil.isSameDate(start, end));
  }

  public void setData(PublicationDTO data) {
    this.data = data;
    link.setHTML(data.getName() + "<span class='date'>"+data.getUpdateDate()+"</span>");
    link.setStyleName("ui-btn ui-icon-carat-r");
  }

  @UiHandler("link")
  protected void onClick(ClickEvent event) {
    if (data != null) {
      ContentDTO content = new ContentDTO();
      content.setId(data.getId());
      content.setType(ContentsTypes.Publication.name());
      content.setInstanceId(data.getInstanceId());
      EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    } else if (eventData != null) {
      ContentDTO content = new ContentDTO();
      content.setInstanceId(eventData.getCalendarId());
      content.setContributionId(eventData.getEventId());
      content.setType(ContentsTypes.Event.name());
      content.setInstanceId(eventData.getCalendarId());
      EventBus.getInstance().fireEvent(new NavigationShowContentEvent(content));
    }
  }
}
