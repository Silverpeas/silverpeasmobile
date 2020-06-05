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

package org.silverpeas.mobile.client.common.navigation;

import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.contacts.events.app.ContactsFilteredLoadEvent;
import org.silverpeas.mobile.client.apps.favorites.events.app.GotoAppEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ShortCutRouter;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.IframePage;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.contact.ContactScope;

/**
 * @author svu
 */
public class LinksManager {

  private static Boolean iosShowIframe = Boolean.parseBoolean(ResourcesManager.getParam("ios.link.open.in.iframe"));

  public static void processLink(String url) {
    if(sameContext(url)) {
      String shortcutContentType = "";
      String shortcutAppId = null;
      String shortcutContentId = url.substring(url.lastIndexOf("/") + 1);
      if (url.contains("/Publication/")) {
        shortcutContentType = ContentsTypes.Publication.name();
      } else if (url.contains("/Media/")) {
        shortcutContentType = ContentsTypes.Media.name();
      } else if (url.contains("/Topic/")) {
        shortcutContentType = ContentsTypes.Folder.name();
        shortcutAppId = shortcutContentId.substring(shortcutContentId.lastIndexOf("=") + 1);
        shortcutContentId = shortcutContentId.substring(0, shortcutContentId.indexOf("?"));
      } else if (url.contains("/Rgallery/")) {
        shortcutContentType = ContentsTypes.Album.name();
        shortcutContentId = url.substring(url.lastIndexOf("=") + 1);
        shortcutAppId = url.substring("/Rgallery/".length(), url.lastIndexOf("/"));
      } else if (url.contains("/Space/")) {
        shortcutContentType = ContentsTypes.Space.name();
      } else if (url.contains("/Component/")) {
        GotoAppEvent eventGoApp = new GotoAppEvent();
        eventGoApp.setInstanceId(shortcutContentId);
        EventBus.getInstance().fireEvent(eventGoApp);
        return;
      } else if (url.contains("/silverpeas/Rdirectory/jsp/Main")) {
        String type = "";
        String filter = "";
        if (url.contains("GroupId=")) {
          type = ContactScope.group.name();
          filter = url.substring(url.indexOf("GroupId=") + "GroupId=".length());
        } else if (url.contains("DomainId=")) {
          type = ContactScope.domain.name();
          filter = url.substring(url.indexOf("DomainId=") + "DomainId=".length());
        }
        EventBus.getInstance().fireEvent(new ContactsFilteredLoadEvent(type, filter));
      } else {
        openExternalLink(url);
        return;
      }

      ShortCutRouter
          .route(SpMobil.getUser(), shortcutAppId, shortcutContentType, shortcutContentId, null, null);
      return;
    }
    openExternalLink(url);
  }

  private static boolean sameContext(String url) {
    String context = Window.Location.getHref();
    int i = context.indexOf("#");
    if (i >= 0) {
      context = context.substring(0, i);
    }
    context = context.replaceAll(Window.Location.getPath(), "") + "/silverpeas";

    return ((url.startsWith("/") || url.startsWith(context)));
  }

  public static void openExternalLink(String url) {
    if (MobilUtils.isIOS()) {
      if (iosShowIframe) {
        IframePage page = new IframePage(url);
        page.setPageTitle("");
        page.show();
      } else {
        //Window.Location.assign(url);
        Window.open(url, "_self", "");
      }
    } else {
      Window.open(url, "_blank", "");
    }
  }

}
