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

package org.silverpeas.mobile.client.apps.classifieds.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.classifieds.events.app.ClassifiedsSendMessageEvent;
import org.silverpeas.mobile.client.apps.classifieds.resources.ClassifiedsMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;

public class MessagePage extends PageContent {

  private static MessagePageUiBinder uiBinder = GWT.create(MessagePageUiBinder.class);

  @UiField(provided = true) protected ClassifiedsMessages msg = null;
  @UiField protected HTMLPanel container, title;
  @UiField protected TextArea message;
  @UiField protected Anchor send;

  private ClassifiedDTO data;

  interface MessagePageUiBinder extends UiBinder<Widget, MessagePage> {
  }

  public MessagePage() {
    msg = GWT.create(ClassifiedsMessages.class);
    setPageTitle(msg.contact());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("send-message");
    message.getElement().setAttribute("x-webkit-speech", "x-webkit-speech");
    message.getElement().setAttribute("speech", "speech");
    title.add(new HTML(msg.contact()));
  }

  @UiHandler("send")
  void send(ClickEvent event) {
    ClassifiedsSendMessageEvent ev = new ClassifiedsSendMessageEvent();
    ev.setMessage(message.getText());
    ev.setMessage(data);
    EventBus.getInstance().fireEvent(ev);
    back();
  }

  public void setData(final ClassifiedDTO data) {
    this.data = data;
  }
}