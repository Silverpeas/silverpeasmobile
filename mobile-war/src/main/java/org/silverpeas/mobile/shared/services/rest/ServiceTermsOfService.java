/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

package org.silverpeas.mobile.shared.services.rest;

import jsinterop.base.Js;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;

/**
 * Service for handling requests related to the Terms and Conditions of Use (TCU).
 * @author svu
 */
public class ServiceTermsOfService extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/termsOfService";

  /**
   * Check whether the Terms and Conditions of Use need to be displayed.
   * @param callback Le callback pour gérer la réponse (booléen).
   */
  public void show(RestCallback<Boolean> callback) {
    String url = PATH + "/show";
    get(url, result -> Js.asBoolean(result), callback);
  }

  /**
   * Retrieve the content of the Terms and Conditions of Use.
   * @param callback Le callback pour gérer la réponse (texte brut).
   */
  public void getContent(RestCallback<String> callback) {
    String url = PATH + "/content";
    getText(url, result -> (String) result, callback);
  }
}