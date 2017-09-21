/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.silverpeas.mobile.client.apps.blog.events.app.AbstractBlogAppEvent;
import com.silverpeas.mobile.client.apps.blog.events.app.BlogAppEventHandler;
import com.silverpeas.mobile.client.apps.blog.events.app.BlogLoadEvent;
import com.silverpeas.mobile.client.apps.blog.events.pages.BlogLoadedEvent;
import com.silverpeas.mobile.client.apps.blog.pages.BlogPage;
import com.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external
    .NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.blog.PostDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class BlogApp extends App implements BlogAppEventHandler, NavigationEventHandler {

  private BlogMessages msg;
  private ApplicationInstanceDTO instance;

  public BlogApp(){
    super();
    msg = GWT.create(BlogMessages.class);
    EventBus.getInstance().addHandler(AbstractBlogAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start(){
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void loadBlog(final BlogLoadEvent event) {
    final String key = "posts_" + instance.getId();
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<PostDTO> result = LocalStorageHelper.load(key, List.class);
        if (result == null) {
          result = new ArrayList<PostDTO>();
        }
        EventBus.getInstance().fireEvent(new BlogLoadedEvent(result));
      }
    };

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<PostDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceBlog().getPosts(instance.getId(), this);
      }

      @Override
      public void onSuccess(List<PostDTO> result) {
        super.onSuccess(result);
        LocalStorageHelper.store(key, List.class, result);
        EventBus.getInstance().fireEvent(new BlogLoadedEvent(result));
      }
    };
    action.attempt();
  }

  public void updateDisplay() {
    loadBlog(new BlogLoadEvent());
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.blog.name())) {
      this.instance = event.getInstance();
      BlogPage page = new BlogPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }
}