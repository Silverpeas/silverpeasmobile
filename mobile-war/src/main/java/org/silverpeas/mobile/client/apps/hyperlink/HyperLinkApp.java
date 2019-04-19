/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.apps.hyperlink;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

public class HyperLinkApp extends App implements NavigationEventHandler {

  private ApplicationInstanceDTO instance;

  public HyperLinkApp(){
    super();
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
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.hyperlink.name())) {
      this.instance = event.getInstance();
      final String key = "hyperlink" + "_" + event.getInstance().getId();
      AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<String>(getOfflineAction(event, key)) {
        @Override
        public void onSuccess(String result) {
          super.onSuccess(result);
          LocalStorageHelper.store(key, String.class, result);
          openLink(result);
        }

        @Override
        public void attempt() {
          ServicesLocator.getServiceHyperLink().getUrl(instance.getId(), this);
        }
      };
      action.attempt();
    }
  }

  private void openLink(String url) {
    Notification.activityStop();

    if (MobilUtils.isIOS()) {
      //Window.Location.assign(url);
      Window.open(url, "_system", "");
    } else {
      Window.open(url, "_blank", "");
    }
  }

  private Command getOfflineAction(final NavigationAppInstanceChangedEvent event, final String key) {
    Command offlineAction = new Command() {

      @Override
      public void execute() {
        String result = LocalStorageHelper.load(key, String.class);
        if (result == null) {
          result = "";
        }
        openLink(result);
      }
    };

    return offlineAction;
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
  }
}
