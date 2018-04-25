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

package org.silverpeas.mobile.client.apps.agenda.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

/**
 * @author svu
 */
public class EventPage  extends PageContent {
  private static EventPageUiBinder uiBinder = GWT.create(EventPageUiBinder.class);

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  interface EventPageUiBinder extends UiBinder<Widget, EventPage> {
  }

  public EventPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.titleEvent());
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(CalendarEventDTO event) {
    event.getTitle();
    event.getDescription();
    //TODO
  }
}