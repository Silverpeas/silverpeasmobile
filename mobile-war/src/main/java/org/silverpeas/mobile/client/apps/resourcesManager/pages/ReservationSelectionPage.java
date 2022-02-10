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

package org.silverpeas.mobile.client.apps.resourcesManager.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.SaveReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.AbstractResourcesManagerPagesEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.DeletedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.ResourcesManagerPagesEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.SavedReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.widgets.ResourceItem;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.Popin;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReservationSelectionPage extends PageContent implements ResourcesManagerPagesEventHandler {

  private static ResourcesManagerPageUiBinder uiBinder = GWT.create(ResourcesManagerPageUiBinder.class);

  private ReservationDTO reservation;
  private List<String> categoriesIds;

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;

  @UiField
  UnorderedList resources;

  @UiField
  Anchor validate;

  public void setReservation(final ReservationDTO data) {
    this.reservation = data;
  }

  public void setResources(final List<ResourceDTO> resources) {
    if (resources.isEmpty()) {
      validate.setVisible(false);
    }

    categoriesIds = new ArrayList<>();
    for (ResourceDTO res : resources) {
      if (!categoriesIds.contains(res.getCategoryId())) {
        categoriesIds.add(res.getCategoryId());
      }
    }

    for (String catId : categoriesIds) {
      ResourceItem itemCat = new ResourceItem();
      this.resources.add(itemCat);
      for (ResourceDTO res : resources) {
        if (res.getCategoryId().equals(catId)) {
          itemCat.setData(res, true);

          ResourceItem item = new ResourceItem();
          item.setData(res, false);
          this.resources.add(item);
        }
      }
    }
  }

  @Override
  public void deletedReservation(final DeletedReservationEvent deletedReservationEvent) {}

  @Override
  public void savedReservation(final SavedReservationEvent savedReservationEvent) {}

  interface ResourcesManagerPageUiBinder extends UiBinder<Widget, ReservationSelectionPage> {
  }

  public ReservationSelectionPage() {
    msg = GWT.create(ResourcesManagerMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractResourcesManagerPagesEvent.TYPE, this);

  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

  @UiHandler("validate")
  protected void validate(ClickEvent event) {
    List<ResourceDTO> selection = new ArrayList<>();
    Iterator<Widget> it = resources.iterator();
    while (it.hasNext()) {
      ResourceItem item = (ResourceItem) it.next();
      if (item.isSelected()) {
        selection.add(item.getData());
      }
    }

    if (selection.isEmpty()) {
      new Popin(msg.errorNoResource()).show();

    } else {
      reservation.setResources(selection);
      SaveReservationEvent saveEvent = new SaveReservationEvent();
      saveEvent.setData(reservation);
      EventBus.getInstance().fireEvent(saveEvent);
    }
  }

}