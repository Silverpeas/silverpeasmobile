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
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListItem;
import com.gwtmobile.ui.client.widgets.ListPanel;
import com.silverpeas.mobile.client.apps.documents.events.app.internal.DocumentsStopEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadPublicationsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsLoadSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.controller.DocumentsSaveSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.AbstractDocumentsPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsLoadedSettingsEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.DocumentsPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.events.pages.NewInstanceLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.PublicationsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.AbstractTopicsNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicSelectedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsLoadedEvent;
import com.silverpeas.mobile.client.apps.documents.events.pages.navigation.TopicsNavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import com.silverpeas.mobile.client.apps.documents.resources.DocumentsResources;
import com.silverpeas.mobile.client.apps.navigation.Apps;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.View;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

/**
 * Documents mobile application.
 * @author svuillet
 */
public class DocumentsPage extends Page implements View, DocumentsPagesEventHandler,
    TopicsNavigationPagesEventHandler {

  private static DocumentsPageUiBinder uiBinder = GWT.create(DocumentsPageUiBinder.class);
  @UiField(provided = true)
  protected DocumentsMessages msg = null;
  @UiField(provided = true)
  protected ApplicationMessages globalMsg = null;
  @UiField(provided = true)
  protected DocumentsResources ressources = null;

  @UiField
  Label instance, topic;
  @UiField
  ListPanel pubs;

  private ApplicationInstanceDTO currentInstance = null;
  private TopicDTO currentTopic = null;

  interface DocumentsPageUiBinder extends UiBinder<Widget, DocumentsPage> {
  }

  public DocumentsPage() {
    ressources = GWT.create(DocumentsResources.class);
    ressources.css().ensureInjected();
    msg = GWT.create(DocumentsMessages.class);
    globalMsg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractDocumentsPagesEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractTopicsNavigationPagesEvent.TYPE, this);

    // load previous instance and topic selection
    EventBus.getInstance().fireEvent(new DocumentsLoadSettingsEvent());
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractDocumentsPagesEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractTopicsNavigationPagesEvent.TYPE, this);
  }

  @Override
  public void goBack(Object returnValue) {
    stop();
    EventBus.getInstance().fireEvent(new DocumentsStopEvent());
    super.goBack(returnValue);
  }

  @UiHandler("place")
  void browseAllAvailableECM(SelectionChangedEvent event) {
    if (event.getSelection() == 0) {
      NavigationApp app = new NavigationApp();
      app.setTypeApp(Apps.kmelia.name());
      app.setTitle(msg.titleECMBrowser());
      app.start(this);
    } else if (event.getSelection() == 1 && currentInstance != null) {
      TopicNavigationPage topicNav = new TopicNavigationPage();
      topicNav.setInstanceId(currentInstance.getId());
      topicNav.setTopicId(null);
      goTo(topicNav);
    }
  }

  @UiHandler("pubs")
  void viewPublication(SelectionChangedEvent event) {
    PublicationPage view = new PublicationPage();
    ListItem item = pubs.getItem(event.getSelection());
    view.setPublicationId(item.getElement().getId());
    goTo(view);
  }

  @Override
  public void onLoadedSettings(DocumentsLoadedSettingsEvent event) {
    currentInstance = event.getInstance();
    currentTopic = event.getTopic();
    displayPlace();

    // Send message to controller for retreive publications at root.
    EventBus.getInstance().fireEvent(
        new DocumentsLoadPublicationsEvent(currentInstance.getId(), currentTopic.getId()));
  }

  @Override
  public void onNewInstanceLoaded(NewInstanceLoadedEvent event) {
    // store instance document
    this.currentInstance = event.getInstance();
    this.currentTopic = null;
    displayPlace();
    // Send message to controller for save settings.
    EventBus.getInstance().fireEvent(new DocumentsSaveSettingsEvent(currentInstance, currentTopic));
  }

  private void displayPlace() {
    if (currentInstance != null) {
      instance.setText(currentInstance.getLabel());
    } else {
      instance.setText("-");
    }
    if (currentTopic != null) {
      topic.setText(currentTopic.getName());
    } else {
      topic.setText("-");
    }
  }

  @Override
  public void onLoadedTopics(TopicsLoadedEvent event) { /* Nothing to do in this page */
  }

  @Override
  public void onTopicSelected(TopicSelectedEvent event) {
    currentTopic = event.getTopic();
    displayPlace();

    // Send message to controller for save settings.
    EventBus.getInstance().fireEvent(new DocumentsSaveSettingsEvent(currentInstance, currentTopic));

    // Send message to controller for retreive publications.
    EventBus.getInstance().fireEvent(
        new DocumentsLoadPublicationsEvent(currentInstance.getId(), currentTopic.getId()));
  }

  @Override
  public void onLoadedPublications(PublicationsLoadedEvent event) {
    pubs.clear();
    for (PublicationDTO pub : event.getPublications()) {
      ListItem item = new ListItem();
      Label label = new Label(pub.getName());
      item.add(label);
      item.getElement().setId(pub.getId());
      pubs.add(item);
    }
  }
}