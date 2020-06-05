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

package org.silverpeas.mobile.client.apps.classifieds;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.client.apps.classifieds.events.app.AbstractClassifiedsAppEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.app.ClassifiedsAppEventHandler;
import org.silverpeas.mobile.client.apps.classifieds.events.app.ClassifiedsLoadEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.app.ClassifiedsSendMessageEvent;
import org.silverpeas.mobile.client.apps.classifieds.events.pages.ClassifiedsLoadedEvent;
import org.silverpeas.mobile.client.apps.classifieds.pages.ClassifiedPage;
import org.silverpeas.mobile.client.apps.classifieds.pages.ClassifiedsPage;
import org.silverpeas.mobile.client.apps.classifieds.resources.ClassifiedsMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

public class ClassifiedsApp extends App implements ClassifiedsAppEventHandler, NavigationEventHandler {

  private ClassifiedsMessages msg;

  public ClassifiedsApp(){
    super();
    msg = GWT.create(ClassifiedsMessages.class);
    EventBus.getInstance().addHandler(AbstractClassifiedsAppEvent.TYPE, this);
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
  public void loadClassifieds(final ClassifiedsLoadEvent event) {
    final String key = "classifieds_" + getApplicationInstance().getId();
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        ClassifiedsDTO result = LocalStorageHelper.load(key, ClassifiedsDTO.class);
        if (result == null) {
          result = new ClassifiedsDTO();
        }
        EventBus.getInstance().fireEvent(new ClassifiedsLoadedEvent(result));
      }
    };

    AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<ClassifiedsDTO>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceClassifieds().getClassifieds(getApplicationInstance().getId(), this);
      }

      @Override
      public void onSuccess(ClassifiedsDTO result) {
        super.onSuccess(result);
        LocalStorageHelper.store(key, ClassifiedsDTO.class, result);
        EventBus.getInstance().fireEvent(new ClassifiedsLoadedEvent(result));
      }
    };
    action.attempt();
  }

  @Override
  public void sendMessage(final ClassifiedsSendMessageEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceClassifieds().sendMessageToOwner(event.getMessage(),
            event.getData(), getApplicationInstance().getId(), this);
      }
    };
    action.attempt();
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.classifieds.name())) {
      setApplicationInstance(event.getInstance());
      ClassifiedsPage page = new ClassifiedsPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.setApp(this);
      page.show();
    }
  }

  @Override
  public void startWithContent(final ContentDTO content) {
    ApplicationInstanceDTO appDTO = new ApplicationInstanceDTO();
    appDTO.setId(content.getInstanceId());
    setApplicationInstance(appDTO);
    if (content.getType().equals(ContentsTypes.Classified.toString())) {
      ServicesLocator.getServiceClassifieds().getClassified(content.getInstanceId(), content.getId(), new AsyncCallback<ClassifiedsDTO>() {
        @Override
        public void onFailure(final Throwable throwable) {
          EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
        }

        @Override
        public void onSuccess(final ClassifiedsDTO data) {
          ClassifiedDTO c = data.getClassifieds().get(0);
          ClassifiedPage page = new ClassifiedPage();
          page.setComments(data.hasComments());
          page.setCategory(c.getCategory());
          page.setType(c.getType());
          page.setApp(ClassifiedsApp.this);
          page.setData(c);
          Notification.activityStop();
          page.show();
        }
      });
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.classifieds.name())) {
      super.showContent(event);
    } else {
      startWithContent(event.getContent());
    }
  }
}
