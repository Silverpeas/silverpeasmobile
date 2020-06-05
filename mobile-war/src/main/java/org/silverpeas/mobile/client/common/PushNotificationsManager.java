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

package org.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;

/**
 * @author svu
 */
public class PushNotificationsManager {

  private static PushNotificationsManager instance;

  public static PushNotificationsManager getInstance() {
    if (instance == null) {
      instance = new PushNotificationsManager();
    }
    return instance;
  }

  private static native void exportJavaMethods() /*-{
    $wnd.storeToken = $entry(@org.silverpeas.mobile.client.common.PushNotificationsManager::storeToken(*));
  }-*/;

  public void inject() {
    exportJavaMethods();
    Element head = Document.get().getElementsByTagName("head").getItem(0);
    ScriptElement sce = Document.get().createScriptElement();
    sce.setType("text/javascript");
    sce.setSrc("/silverpeas/spmobile/app-init.js");
    head.appendChild(sce);
  }

  public static void storeToken(String token) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceNavigation().storeTokenMessaging(token, this);
      }
    };
    action.attempt();
  }
}
