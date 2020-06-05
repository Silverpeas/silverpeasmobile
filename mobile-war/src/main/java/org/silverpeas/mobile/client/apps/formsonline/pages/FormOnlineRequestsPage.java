/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.apps.formsonline.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.AbstractFormsOnlinePagesEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormRequestStatusChangedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormSavedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlinePagesEventHandler;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineRequestValidatedEvent;
import org.silverpeas.mobile.client.apps.formsonline.pages.widgets.FormOnlineRequestItem;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;

import java.util.List;

public class FormOnlineRequestsPage extends PageContent implements FormsOnlinePagesEventHandler {

  private static FormsOnlineAsReceiverPageUiBinder uiBinder = GWT.create(FormsOnlineAsReceiverPageUiBinder.class);

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList requests;

  private List<FormRequestDTO> data;
  private boolean readOnly;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  @Override
  public void onFormsOnlineLoad(final FormsOnlineLoadedEvent event) {}

  @Override
  public void onFormLoaded(final FormLoadedEvent formLoadedEvent) {}

  @Override
  public void onFormSaved(final FormSavedEvent formSavedEvent) {}

  @Override
  public void onFormsOnlineRequestValidated(
      final FormsOnlineRequestValidatedEvent event) {
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).getId().equals(event.getData().getId())) {
        data.remove(i);
        break;
      }
    }
    display(data);
  }

  @Override
  public void onFormRequestStatusChange(
      final FormRequestStatusChangedEvent event) {
    for (FormRequestDTO r : data) {
      if (r.getId().equals(event.getData().getId())) {
        r.setState(event.getData().getState());
        r.setStateLabel(event.getData().getStateLabel());
      }
    }
    display(data);
  }

  interface FormsOnlineAsReceiverPageUiBinder extends UiBinder<Widget, FormOnlineRequestsPage> {
  }

  public FormOnlineRequestsPage() {
    msg = GWT.create(FormsOnlineMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
  }

  public void setData(List<FormRequestDTO> data, boolean readOnly) {
    this.readOnly = readOnly;
    this.data = data;
    display(data);
  }

  private void display(final List<FormRequestDTO> data) {
    requests.clear();
    for (FormRequestDTO r : data) {
      FormOnlineRequestItem item = new FormOnlineRequestItem();
      item.setData(r, readOnly);
      requests.add(item);
    }
  }
}