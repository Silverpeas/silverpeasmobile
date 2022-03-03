/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.formsonline.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineLoadRequestEvent;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;

public class FormOnlineRequestItem extends Composite implements ClickHandler {

  private FormRequestDTO data;
  private boolean readOnly;
  private static FormOnlineItemUiBinder uiBinder = GWT.create(FormOnlineItemUiBinder.class);

  @UiField
  HTML content;
  @UiField
  HTMLPanel container;
  protected FormsOnlineMessages msg = null;

  interface FormOnlineItemUiBinder extends UiBinder<Widget, FormOnlineRequestItem> {}

  public FormOnlineRequestItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(FormsOnlineMessages.class);
  }

  public void setData(FormRequestDTO data, boolean readOnly) {
    this.data = data;
    this.readOnly = readOnly;
    if (data.getTitle().isEmpty()) data.setTitle("&nbsp;");
    String html = "<h2 class='title'><a href='#'>" + data.getTitle() + "</a></h2>";
    if (!data.getDescription().isEmpty()) html += "<span class='description'>" + data.getDescription() + "</span>";
    html += "<span class='state'>" + data.getStateLabel() + "</span>" + "<span class='author'>" + data.getCreationDate() + "&nbsp;" + data.getCreator() + "</span>";
    content.setHTML(html);
    content.addClickHandler(this);
  }

  @Override
  public void onClick(final ClickEvent event) {
    EventBus.getInstance().fireEvent(new FormsOnlineLoadRequestEvent(data, readOnly));
  }
}
