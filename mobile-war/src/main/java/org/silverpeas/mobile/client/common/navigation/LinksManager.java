/*
 * Copyright (C) 2000 - 2022 Silverpeas
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
import org.silverpeas.mobile.shared.dto.hyperlink.HyperLinkDTO;

/**
 * @author svu
 */
public class LinksManager {

  private static Boolean iosShowIframe = Boolean.parseBoolean(ResourcesManager.getParam("ios.link.open.in.iframe"));

  public static void navigateToPermalink(String url) {
    HyperLinkDTO dto = new HyperLinkDTO();
    dto.setUrl(url);
    dto.setInternalLink(true);
    dto.setOpenNewWindow(false);
    processLink(dto);
  }

  public static String createApplicationPermalink(String instanceId) {
    return "/silverpeas/Component/" + instanceId;
  }

  public static String createAlbumPermalink(String instanceId, String id) {
    return "/silverpeas/Topic/"+id+"?ComponentId=" + instanceId;
  }

  public static String createMediaPermalink(String id) {
    return "/silverpeas/Media/"+id;
  }
  public static void processLink(HyperLinkDTO hyperLinkDTO) {
    String url = hyperLinkDTO.getUrl();
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
        if (url.contains("GroupIds=")) {
          filter = url.substring(url.indexOf("GroupIds=") + "GroupIds=".length());
          int end = filter.indexOf("&");
          if (end != -1) {
            filter = filter.substring(0, end);
          }
          if (!filter.isEmpty()) {
            type = ContactScope.group.name();
          }
        } else if (url.contains("DomainIds=")) {
          filter = url.substring(url.indexOf("DomainIds=") + "DomainIds=".length());
          int end = filter.indexOf("&");
          if (end != -1) {
            filter = filter.substring(0, end);
          }
          if (!filter.isEmpty()) {
            type = ContactScope.domain.name();
          }
        }
        EventBus.getInstance().fireEvent(new ContactsFilteredLoadEvent(type, filter));
      } else {
        openExternalLink(url, hyperLinkDTO.getOpenNewWindow(), hyperLinkDTO.getInternalLink());
        return;
      }

      ShortCutRouter
              .route(SpMobil.getUser(), shortcutAppId, shortcutContentType, shortcutContentId, null, null);
      return;
    }
    openExternalLink(url, hyperLinkDTO.getOpenNewWindow(), hyperLinkDTO.getInternalLink());
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

  public static void openExternalLink(String url, boolean openNewWindow, boolean internalLink) {
    if (MobilUtils.isIOS()) {
      if (iosShowIframe) {
        openIframePage(url);
      } else {
        openExternalLinkStandard(url, openNewWindow, internalLink);
      }
    } else {
      openExternalLinkStandard(url, openNewWindow, internalLink);
    }
  }

  public static void openIframePage(String url) {
    IframePage page = new IframePage(url);
    page.setPageTitle("");
    page.show();
  }

  private static void openExternalLinkStandard(String url, boolean openNewWindow, boolean internalLink) {
    if (internalLink) {
      openIframePage(url);
    } else {
      if (openNewWindow) {
        Window.open(url, "_blank", "");
      } else {
        Window.open(url, "_self", "");
      }
    }
  }

}
