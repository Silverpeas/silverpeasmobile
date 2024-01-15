/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.agenda.events.app.ParticipationEvent;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.almanach.ParticipationStatusDTO;

/**
 * @author svu
 */
public class TentativeParticipationButton extends ActionItem {

  public static final String ID = "tentativeParticipation";

  interface AddReminderButtonUiBinder extends UiBinder<HTMLPanel, TentativeParticipationButton> {
  }

  private static AddReminderButtonUiBinder uiBinder = GWT.create(AddReminderButtonUiBinder.class);
  private CalendarEventDTO eventDTO;

  @UiField
  HTMLPanel container;
  @UiField
  Anchor action;

  @UiField(provided = true) protected AgendaMessages msg = null;

  public TentativeParticipationButton() {
    msg = GWT.create(AgendaMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    setId(ID);
  }

  @UiHandler("action")
  void tentative(ClickEvent event) {
    EventBus.getInstance().fireEvent(new ParticipationEvent(ParticipationStatusDTO.TENTATIVE, eventDTO));

    // hide menu
    ActionsMenu.close(getElement());

  }

  public void setEvent(final CalendarEventDTO eventDTO) {
    this.eventDTO = eventDTO;
  }
}
