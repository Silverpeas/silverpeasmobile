/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.AccordionContent;
import com.gwtmobile.ui.client.widgets.AccordionStack;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.AbstractPublicationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.publication.PublicationNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.pages.widgets.AttachmentWidget;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;

public class PublicationPage extends Page implements View, PublicationNavigationPagesEventHandler {

  private static PublicationPageUiBinder uiBinder = GWT.create(PublicationPageUiBinder.class);

  private PublicationDTO publication;

  @UiField
  Label title, description, auteur, version;
  @UiField
  AccordionContent attachements;
  @UiField
  AccordionStack attachementsStack;

  @UiField(provided = true)
  protected DocumentsMessages msg = null;
  @UiField(provided = true)
  protected ApplicationMessages globalMsg = null;
  @UiField(provided = true)
  protected DocumentsResources ressources = null;

  interface PublicationPageUiBinder extends UiBinder<Widget, PublicationPage> {
  }

  public PublicationPage() {
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(DocumentsMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractPublicationPagesEvent.TYPE, this);
  }

  @Override
  public void goBack(Object returnValue) {
    stop();
    super.goBack(returnValue);
  }

  public void setPublicationId(String id) {
    // send event to controler for retrieve pub infos
    Notification.activityStart();
    EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(id));
  }

  @Override
  public void onLoadedPublication(PublicationLoadedEvent event) {
    Notification.activityStop();
    this.publication = event.getPublication();
    display();
  }

  /**
   * Refesh view informations.
   */
  private void display() {
    title.setText(publication.getName());
    description.setText(publication.getDescription());
    version.setText(publication.getVersion());
    auteur.setText(publication.getAuteur());

    attachements.clear();
    for (AttachmentDTO attachmentDTO : publication.getAttachments()) {
      AttachmentWidget att = new AttachmentWidget();
      att.setAttachment(attachmentDTO);
      attachements.add(att);
    }
    attachementsStack.expand();
  }
}
