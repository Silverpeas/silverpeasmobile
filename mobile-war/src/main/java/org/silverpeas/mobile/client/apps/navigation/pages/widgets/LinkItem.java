/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.client.apps.navigation.pages.widgets;

import com.google.gwt.user.client.ui.Composite;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.favorites.events.app.GotoAppEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ShortCutRouter;
import org.silverpeas.mobile.shared.dto.ContentsTypes;

/**
 * @author svu
 */
public abstract class LinkItem extends Composite {

  protected void processLink(String url) {
    if(url.startsWith("/")) {
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
      }
      ShortCutRouter
          .route(SpMobil.getUser(), shortcutAppId, shortcutContentType, shortcutContentId, null);
    }
    
  }
  
}
