/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.formsonline.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineAsReceiverLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.app.FormsOnlineLoadEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.AbstractFormsOnlinePagesEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormRequestStatusChangedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormSavedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineLoadedEvent;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlinePagesEventHandler;
import org.silverpeas.mobile.client.apps.formsonline.events.pages.FormsOnlineRequestValidatedEvent;
import org.silverpeas.mobile.client.apps.formsonline.pages.widgets.FormOnlineItem;
import org.silverpeas.mobile.client.apps.formsonline.pages.widgets.ViewMyRequestsButton;
import org.silverpeas.mobile.client.apps.formsonline.pages.widgets.ViewRequestsToValidateButton;
import org.silverpeas.mobile.client.apps.formsonline.resources.FormsOnlineMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;

public class FormsOnlinePage extends PageContent implements FormsOnlinePagesEventHandler {

  private static FormsOnlinePageUiBinder uiBinder = GWT.create(FormsOnlinePageUiBinder.class);

  @UiField(provided = true) protected FormsOnlineMessages msg = null;
  @UiField
  ActionsMenu actionsMenu;

  @UiField
  HTMLPanel container;

  @UiField
  UnorderedList forms;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private ViewMyRequestsButton myRequests = new ViewMyRequestsButton();
  private ViewRequestsToValidateButton requestsToValidate = new ViewRequestsToValidateButton();
  private boolean canReceive = false;

  @Override
  public void onFormsOnlineLoad(final FormsOnlineLoadedEvent event) {
    for (FormDTO form : event.getForms()) {
      FormOnlineItem item = new FormOnlineItem();
      item.setData(form);
      forms.add(item);
      if (form.isReceiver()) canReceive = true;
    }

    actionsMenu.addAction(favorite);
    favorite.init(getApp().getApplicationInstance().getId(), getApp().getApplicationInstance().getId(), ContentsTypes.Component.name(), getPageTitle());
    actionsMenu.addAction(myRequests);
    myRequests.init(getApp().getApplicationInstance().getId());
    if (canReceive) actionsMenu.addAction(requestsToValidate);
  }

  @Override
  public void onFormLoaded(final FormLoadedEvent formLoadedEvent) {}

  @Override
  public void onFormSaved(final FormSavedEvent formSavedEvent) {}

  @Override
  public void onFormsOnlineRequestValidated(
      final FormsOnlineRequestValidatedEvent formsOnlineRequestValidatedEvent) {
  }

  @Override
  public void onFormRequestStatusChange(
      final FormRequestStatusChangedEvent formRequestStatusChangedEvent) {
  }

  interface FormsOnlinePageUiBinder extends UiBinder<Widget, FormsOnlinePage> {
  }

  public FormsOnlinePage() {
    msg = GWT.create(FormsOnlineMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().getStyle().setHeight(12, Style.Unit.EM);
    EventBus.getInstance().addHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
    EventBus.getInstance().fireEvent(new FormsOnlineLoadEvent());
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractFormsOnlinePagesEvent.TYPE, this);
  }
}