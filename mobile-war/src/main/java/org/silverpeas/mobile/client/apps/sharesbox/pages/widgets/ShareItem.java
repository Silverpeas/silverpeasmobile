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

package org.silverpeas.mobile.client.apps.sharesbox.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import org.silverpeas.mobile.client.apps.sharesbox.resources.ShareMessages;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.Date;

public class ShareItem extends Composite implements ClickHandler {

  private static ShareItemUiBinder uiBinder = GWT.create(ShareItemUiBinder.class);

  @UiField HTMLPanel container;

  @UiField
  InlineHTML date, name;

  @UiField
  Anchor link;

  @UiField
  CheckBox select;

  @UiField(provided = true) protected ShareMessages msg = null;
  private TicketDTO data;

  @Override
  public void onClick(final ClickEvent clickEvent) {
    //TODO
  }

  interface ShareItemUiBinder extends UiBinder<Widget, ShareItem> {
  }

  public ShareItem() {
    msg = GWT.create(ShareMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    date.addClickHandler(this);
    name.addClickHandler(this);
  }

  public boolean isSelected() {
    return select.getValue();
  }

  public void setData(TicketDTO data) {
    this.data = data;
    Date dt = new Date();
    dt.setTime(Long.parseLong(data.getCreationDate()));
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
    date.setText(fmt.format(dt));
    name.setText(data.getName());

    link.setHref("/silverpeas/Ticket?Key=" + data.getToken());
    if (data.getSharedObjectType().equalsIgnoreCase("Attachment")) {
      link.getElement().setAttribute("download", data.getName());
    } else {
      link.setTarget("_blank");
    }
  }


  public TicketDTO getData() {
    return data;
  }
}
