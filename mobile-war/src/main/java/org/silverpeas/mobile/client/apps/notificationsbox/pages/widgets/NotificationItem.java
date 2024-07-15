/*
 * Copyright (C) 2000 - 2024 Silverpeas
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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationReadenEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.resources.NotificationsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;

public class NotificationItem extends SelectableItem {

  private static NotificationItemUiBinder uiBinder = GWT.create(NotificationItemUiBinder.class);

  @UiField HTMLPanel container;

  @UiField
  SpanElement date, title, source, author;

  @UiField
  Anchor link;

  @UiField(provided = true) protected NotificationsMessages msg = null;

  private NotificationBoxDTO data;

  interface NotificationItemUiBinder extends UiBinder<Widget, NotificationItem> {
  }

  public NotificationItem() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    setContainer(container);
  }

  @UiHandler("link")
  protected void startTouch(TouchStartEvent event) {
    startTouch(event, true);
  }

  @UiHandler("link")
  protected void moveTouch(TouchMoveEvent event) {
    super.moveTouch(event);
  }

  @UiHandler("link")
  protected void endTouch(TouchEndEvent event) {
    endTouch(event, true, new Command() {
      @Override
      public void execute() {
        if (data instanceof NotificationSendedDTO) {
          Window.Location.assign(((NotificationSendedDTO)data).getLink());
        } else {
          Window.Location.assign(((NotificationReceivedDTO)data).getLink());
          NotificationReadenEvent event = new NotificationReadenEvent((NotificationReceivedDTO) data);
          EventBus.getInstance().fireEvent(event);
        }
      }
    });
  }

  public void setData(NotificationSendedDTO data) {
    this.data = data;
    date.setInnerText(data.getDate());
    source.setInnerHTML(data.getSource());
    title.setInnerHTML(data.getTitle());
  }

  public void setData(NotificationReceivedDTO data) {
    this.data = data;
    date.setInnerText(data.getDate());
    author.setInnerText(data.getAuthor());
    source.setInnerText(data.getSource());
    title.setInnerHTML(data.getTitle());
    if (data.getReaden() == 0) {
      getElement().addClassName("not-read");
    }
  }

  public NotificationBoxDTO getData() {
    return data;
  }
}
