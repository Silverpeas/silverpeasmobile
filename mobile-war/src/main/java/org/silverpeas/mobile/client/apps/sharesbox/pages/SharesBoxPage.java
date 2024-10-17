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

package org.silverpeas.mobile.client.apps.sharesbox.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.sharesbox.events.app.DeleteSharesEvent;
import org.silverpeas.mobile.client.apps.sharesbox.events.pages.AbstractSharesBoxPagesEvent;
import org.silverpeas.mobile.client.apps.sharesbox.events.pages.SharesBoxPagesEventHandler;
import org.silverpeas.mobile.client.apps.sharesbox.events.pages.SharesDeletedEvent;
import org.silverpeas.mobile.client.apps.sharesbox.pages.widgets.ShareItem;
import org.silverpeas.mobile.client.apps.sharesbox.resources.ShareMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.widgets.DeleteButton;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SharesBoxPage extends PageContent implements SharesBoxPagesEventHandler {

  private static SharesBoxPageUiBinder uiBinder = GWT.create(SharesBoxPageUiBinder.class);

  @UiField(provided = true) protected ShareMessages msg = null;
  @UiField
  UnorderedList shares;
  private List<TicketDTO> data;

  private DeleteButton buttonDelete = new DeleteButton();

  public void setData(List<TicketDTO> data) {
    this.data = data;
    for (TicketDTO d : data) {
      ShareItem item = new ShareItem();
      item.setData(d);
      item.setParent(this);
      shares.add(item);
    }
  }

  public List<TicketDTO> getSelectedShares() {
    List<TicketDTO> selection = new ArrayList<>();
    for (int i = 0; i < shares.getCount(); i++) {
      ShareItem item = (ShareItem) shares.getWidget(i);
      if (item.isSelected()) {
        selection.add(item.getData());
      }
    }
    return selection;
  }

  @Override
  public void onSharesDeleted(SharesDeletedEvent event) {
    for (TicketDTO t : event.getShares()) {
      Iterator<Widget> it = shares.iterator();
      while (it.hasNext()) {
        ShareItem w = (ShareItem) it.next();
        if (w.getData().getToken().equalsIgnoreCase(t.getToken())) {
          w.removeFromParent();
        }
      }
    }
  }

  interface SharesBoxPageUiBinder extends UiBinder<Widget, SharesBoxPage> {
  }

  public SharesBoxPage() {
    msg = GWT.create(ShareMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractSharesBoxPagesEvent.TYPE, this);
    buttonDelete.setId("delete-share");
  }

  @Override
  public void setSelectionMode(boolean selectionMode) {
    super.setSelectionMode(selectionMode);
    if (selectionMode) {
      clearActions();
      buttonDelete.setCallback(new Command() {@Override public void execute() {deleteSelectedShares();}});
      addActionShortcut(buttonDelete);
    } else {
      clearActions();
    }
  }

  private void deleteSelectedShares() {
    Snackbar.showConfirmation(msg.deleteConfirmation(), new Command() {
      @Override
      public void execute() {
        List<TicketDTO> selection = getSelectedShares();
        DeleteSharesEvent deleteEvent = new DeleteSharesEvent();
        deleteEvent.setSelection(selection);
        if (!selection.isEmpty()) {
          removeActionShortcut(buttonDelete);
          EventBus.getInstance().fireEvent(deleteEvent);
        }
      }
    }, null);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractSharesBoxPagesEvent.TYPE, this);
  }
}