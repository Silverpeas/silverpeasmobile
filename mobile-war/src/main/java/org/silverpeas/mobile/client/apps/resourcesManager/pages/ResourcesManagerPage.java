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

package org.silverpeas.mobile.client.apps.resourcesManager.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.AbstractResourcesManagerPagesEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.DeletedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.ResourcesManagerPagesEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.SavedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.widgets.AddReservationButton;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.widgets.ReservationItem;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;

import java.util.List;

public class ResourcesManagerPage extends PageContent implements ResourcesManagerPagesEventHandler {

  private static ResourcesManagerPageUiBinder uiBinder = GWT.create(ResourcesManagerPageUiBinder.class);

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;

  @UiField
  UnorderedList reservations;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private AddReservationButton addReservation = new AddReservationButton();
  private List<ReservationDTO> data;

  @Override
  public void deletedReservation(final DeletedReservationEvent deletedReservationEvent) {
    for (int j = 0; j < data.size(); j++) {
      ReservationDTO res = data.get(j);
      if (res.getId().equals(deletedReservationEvent.getData().getId())) {
        data.remove(j);
        break;
      }
    }
    displayList();
  }

  @Override
  public void savedReservation(final SavedReservationEvent savedReservationEvent) {
    data.add(savedReservationEvent.getData());
    displayList();
  }


  interface ResourcesManagerPageUiBinder extends UiBinder<Widget, ResourcesManagerPage> {
  }

  public ResourcesManagerPage() {
    msg = GWT.create(ResourcesManagerMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

  public void setData(final List<ReservationDTO> reservationsDTO) {
    this.data = reservationsDTO;
    displayList();

    addActionMenu(favorite);
    favorite.init(getApp().getApplicationInstance().getId(), getApp().getApplicationInstance().getId(), ContentsTypes.Component.name(), getPageTitle());
    addActionShortcut(addReservation);
  }

  private void displayList() {
    reservations.clear();
    for (ReservationDTO reservation : data) {
      ReservationItem item = new ReservationItem();
      item.setData(reservation);
      reservations.add(item);
    }
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

}