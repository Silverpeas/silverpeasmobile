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

package org.silverpeas.mobile.client.apps.sharesbox.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.sharesbox.resources.ShareMessages;
import org.silverpeas.mobile.client.components.base.widgets.SelectableItem;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.Date;

public class ShareItem extends SelectableItem {

  private static ShareItemUiBinder uiBinder = GWT.create(ShareItemUiBinder.class);

  @UiField HTMLPanel container;

  @UiField
  SpanElement date, name;

  @UiField
  Anchor link;

  @UiField
  AnchorElement shareLink;

  @UiField(provided = true) protected ShareMessages msg = null;
  private TicketDTO data;

  interface ShareItemUiBinder extends UiBinder<Widget, ShareItem> {
  }

  public ShareItem() {
    msg = GWT.create(ShareMessages.class);
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
    if (isInSelectionMode()) {
      toogleDownload();
    } else {
      enableDownload();
    }
    endTouch(event, true, new Command() {
      @Override
      public void execute() {
        //TODO : display share informations
      }
    });
  }

  public void toogleDownload() {
    shareLink.toggleClassName("ui-disabled");
  }

  public void enableDownload() {
    shareLink.removeClassName("ui-disabled");
  }

  public void setData(TicketDTO data) {
    this.data = data;
    Date dt = new Date();
    dt.setTime(Long.parseLong(data.getCreationDate()));
    DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
    date.setInnerText(fmt.format(dt));
    name.setInnerText(data.getName());

    shareLink.setHref("/silverpeas/Ticket?Key=" + data.getToken());
    if (data.getSharedObjectType().equalsIgnoreCase("Attachment")) {
      shareLink.setAttribute("download", data.getName());
    } else {
      shareLink.setTarget("_blank");
    }
  }


  public TicketDTO getData() {
    return data;
  }
}
