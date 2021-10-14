/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.MarkAsReadNotificationsEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.NotificationsBoxPage;
import org.silverpeas.mobile.client.apps.notificationsbox.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionItem;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;

import java.util.List;

/**
 * @author: svu
 */
public class MarkAsReadButton extends ActionItem {

  interface NotReadButtonUiBinder extends UiBinder<HTMLPanel, MarkAsReadButton> {}

  private static NotReadButtonUiBinder uiBinder = GWT.create(NotReadButtonUiBinder.class);

  private NotificationsBoxPage parentPage;

  @UiField
  HTMLPanel container;
  @UiField
  Anchor read;

  @UiField(provided = true)
  protected NotificationsMessages msg = null;


  public MarkAsReadButton() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    setId("markAsRead");
  }

  @UiHandler("read")
  void read(ClickEvent event) {
    List<NotificationBoxDTO> selection = parentPage.getSelectedNotification();

    MarkAsReadNotificationsEvent notReadEvent = new MarkAsReadNotificationsEvent();
    notReadEvent.setSelection(selection);
    EventBus.getInstance().fireEvent(notReadEvent);

    // hide menu
    getElement().getParentElement().removeAttribute("style");
  }

  public void setParentPage(final NotificationsBoxPage parentPage) {
    this.parentPage = parentPage;
  }
}