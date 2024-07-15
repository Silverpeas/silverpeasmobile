/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.components.attachments;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.components.IframePage;

/**
 * @author svu
 */
public class AttachmentsManager {

  public static void viewDocument(String id, String lang, String title) {
    IframePage page = new IframePage("/silverpeas/services/media/viewer/embed/pdf?documentId=" + id + "" +
            "&documentType=attachment&language=" + lang + "&embedPlayer=true");
    page.setPageTitle(title);
    page.show();
  }

  public static void generateLink(String id, String instanceId, String lang, String title, Anchor link) {
    try {
      String url = UrlUtils.getAttachedFileLocation();
      url += "componentId/";
      url += instanceId;
      url += "/attachmentId/";
      url += id;
      url += "/lang/";
      url += lang;
      url += "/name/";
      url += title;

      link.setHref(url);
      if (MobilUtils.isIOS()) {
        //link.setTarget("_blank");
        link.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(final ClickEvent clickEvent) {
            String u =((Anchor)clickEvent.getSource()).getHref();
            Window.open(u,"_blank","fullscreen=yes");
          }
        });
      } else {
        link.setTarget("_self");
        link.getElement().setAttribute("download", title);
      }
    } catch (JavaScriptException e) {
      Notification.alert(e.getMessage());
    }
  }

}
