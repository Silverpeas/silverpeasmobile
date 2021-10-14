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

package org.silverpeas.mobile.client.apps.hyperlink;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.TextCallback;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.common.network.OfflineHelper;
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

      ServicesLocator.getServiceHyperLink().getUrl(instance.getId(), new TextCallback() {
        @Override
        public void onFailure(final Method method, final Throwable t) {
          EventBus.getInstance().fireEvent(new ErrorEvent(t));
        }

        @Override
        public void onSuccess(final Method method, final String url) {
          openLink(url);
        }
      });
    }
  }

  private void openLink(String url) {
    Notification.activityStop();
    OfflineHelper.hideOfflineIndicator();
    LinksManager.processLink(url);
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") &&
        event.getContent().getInstanceId().startsWith(Apps.hyperlink.name())) {
      ApplicationInstanceDTO app = new ApplicationInstanceDTO();
      app.setId(event.getContent().getInstanceId());
      app.setType(Apps.hyperlink.name());
      NavigationAppInstanceChangedEvent ev = new NavigationAppInstanceChangedEvent(app);
      appInstanceChanged(ev);
    }
  }
}
