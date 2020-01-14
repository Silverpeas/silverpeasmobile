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

package org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.resources.NotificationsMessages;
import org.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import org.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;
import org.silverpeas.mobile.shared.dto.TaskDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;

public class NotificationItem extends Composite implements ClickHandler {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;

  @UiField
  InlineHTML date, title, source, author;

  @UiField(provided = true) protected NotificationsMessages msg = null;

  private NotificationReceivedDTO data;

  @Override
  public void onClick(final ClickEvent clickEvent) {
    //TODO : test
    Window.Location.assign(data.getLink());
  }

  interface ContactItemUiBinder extends UiBinder<Widget, NotificationItem> {
  }

  public NotificationItem() {
    msg = GWT.create(NotificationsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    date.addClickHandler(this);
    author.addClickHandler(this);
    source.addClickHandler(this);
    title.addClickHandler(this);
  }

  public void setData(NotificationReceivedDTO data) {
    this.data = data;
    date.setText(data.getDate());
    author.setText(data.getAuthor());
    source.setText(data.getSource());
    title.setText(data.getTitle());
  }
}
