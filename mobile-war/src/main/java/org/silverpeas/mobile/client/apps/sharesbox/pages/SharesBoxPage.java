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

package org.silverpeas.mobile.client.apps.sharesbox.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.DeleteButton;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.widgets.MarkAsReadButton;
import org.silverpeas.mobile.client.apps.sharesbox.events.pages.AbstractSharesBoxPagesEvent;
import org.silverpeas.mobile.client.apps.sharesbox.events.pages.SharesBoxPagesEventHandler;
import org.silverpeas.mobile.client.apps.sharesbox.pages.widgets.ShareItem;
import org.silverpeas.mobile.client.apps.sharesbox.resources.ShareMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.List;

public class SharesBoxPage extends PageContent implements SharesBoxPagesEventHandler {

  private static SharesBoxPageUiBinder uiBinder = GWT.create(SharesBoxPageUiBinder.class);

  @UiField(provided = true) protected ShareMessages msg = null;
  @UiField
  UnorderedList shares;

  @UiField
  ActionsMenu actionsMenu;
  private List<TicketDTO> data;

  public void setData(List<TicketDTO> data) {
    this.data = data;

    ShareItem headerItem = new ShareItem();
    headerItem.setHeader(true);
    TicketDTO header = new TicketDTO();
    header.setCreationDate("date de création");
    header.setUri("Lien");
    header.setNbAccess("Nb accès");
    header.setNbAccessMax("Nb max accès");
    headerItem.setData(header);
    shares.add(headerItem);

    for (TicketDTO d : data) {
      ShareItem item = new ShareItem();
      item.setData(d);
      shares.add(item);
    }
  }

  interface SharesBoxPageUiBinder extends UiBinder<Widget, SharesBoxPage> {
  }

  public SharesBoxPage() {
    msg = GWT.create(ShareMessages.class);
    //setPageTitle(msg.notifications());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractSharesBoxPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractSharesBoxPagesEvent.TYPE, this);
  }
}