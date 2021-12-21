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

package org.silverpeas.mobile.client.apps.resourcesManager.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.blog.events.app.BlogLoadEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.AbstractBlogPagesEvent;
import org.silverpeas.mobile.client.apps.blog.events.pages.BlogLoadedEvent;
import org.silverpeas.mobile.client.apps.blog.pages.widgets.BlogItem;
import org.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.AbstractResourcesManagerPagesEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.pages.ResourcesManagerPagesEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourcesManagerPage extends PageContent implements ResourcesManagerPagesEventHandler {

  private static ResourcesManagerPageUiBinder uiBinder = GWT.create(ResourcesManagerPageUiBinder.class);

  @UiField(provided = true) protected ResourcesManagerMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  interface ResourcesManagerPageUiBinder extends UiBinder<Widget, ResourcesManagerPage> {
  }

  public ResourcesManagerPage() {
    msg = GWT.create(ResourcesManagerMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
    //EventBus.getInstance().fireEvent(new ResourcesManagerLoadEvent("all"));
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractResourcesManagerPagesEvent.TYPE, this);
  }

}