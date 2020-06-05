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

package org.silverpeas.mobile.client.apps.documents.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadGedItemsEvent;
import org.silverpeas.mobile.client.apps.documents.events.app.DocumentsLoadPublicationEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation
    .AbstractGedNavigationPagesEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemClickEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation.GedItemsLoadedEvent;
import org.silverpeas.mobile.client.apps.documents.events.pages.navigation
    .GedNavigationPagesEventHandler;
import org.silverpeas.mobile.client.apps.documents.pages.widgets.GedItem;
import org.silverpeas.mobile.client.apps.documents.resources.DocumentsMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;

import java.util.List;

public class GedNavigationPage extends PageContent implements View, GedNavigationPagesEventHandler {

  private DocumentsMessages msg;

  @UiField UnorderedList list;
  @UiField ActionsMenu actionsMenu;

  private TopicDTO root;
  private String rootTopicId, instanceId;
  private boolean dataLoaded = false;
  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  private static GedNavigationPageUiBinder uiBinder = GWT.create(GedNavigationPageUiBinder.class);

  interface GedNavigationPageUiBinder extends UiBinder<Widget, GedNavigationPage> {
  }

  public GedNavigationPage() {
    msg = GWT.create(DocumentsMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractGedNavigationPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractGedNavigationPagesEvent.TYPE, this);
  }

  public void setTopicId(String rootTopicId) {
    Notification.activityStart();
    this.setRootTopicId(rootTopicId);
    EventBus.getInstance().fireEvent(new DocumentsLoadGedItemsEvent(instanceId, rootTopicId));
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  @Override
  public void onLoadedTopics(GedItemsLoadedEvent event) {
    if (isVisible() && dataLoaded == false) {
      Notification.activityStart();
      list.clear();
      List<BaseDTO> dataItems = event.getTopicsAndPublications();
      for (Object dataItem : dataItems) {
        if (dataItem instanceof TopicDTO && ((TopicDTO) dataItem).isRoot()) {
          setPageTitle(((TopicDTO) dataItem).getName());
          root = (TopicDTO) dataItem;
        } else {
          GedItem item = new GedItem();
          item.setData(dataItem);
          list.add(item);
        }
      }
      dataLoaded = true;

      actionsMenu.addAction(favorite);

      if (root.getId() == null) {
        favorite.init(instanceId, instanceId, ContentsTypes.Component.name(), root.getName());
      } else {
        favorite.init(instanceId, root.getId(), ContentsTypes.Folder.name(), root.getName());
      }
      Notification.activityStop();
    }

  }

  @Override
  public void onGedItemClicked(GedItemClickEvent event) {
    if (isVisible()) {
      if (event.getGedItem() instanceof TopicDTO) {
        GedNavigationPage page = new GedNavigationPage();
        page.setInstanceId(instanceId);
        page.setTopicId(((TopicDTO)event.getGedItem()).getId());
        page.show();
      } else if (event.getGedItem() instanceof PublicationDTO) {
        PublicationPage page = new PublicationPage();
        page.setPageTitle(msg.publicationTitle());
        page.show();
        EventBus.getInstance().fireEvent(new DocumentsLoadPublicationEvent(((PublicationDTO) event.getGedItem()).getId(), ContentsTypes.Publication.toString()));
      }
    }
  }

  public String getRootTopicId() {
    return rootTopicId;
  }

  public void setRootTopicId(String rootTopicId) {
    this.rootTopicId = rootTopicId;
  }
}
