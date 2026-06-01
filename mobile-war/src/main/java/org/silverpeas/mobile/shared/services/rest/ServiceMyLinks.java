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

import elemental2.core.Global;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.MyLinkCategoryDTO;
import org.silverpeas.mobile.shared.dto.MyLinkDTO;
import java.util.List;

/**
 * @author svu
 */
public class ServiceMyLinks extends AbstractService {

  public final static String PATH = "/silverpeas/services/mylinks";

  public void addLink(MyLinkDTO newLink, RestCallback<MyLinkDTO> callback) {
    post(PATH,
            Global.JSON.stringify(Js.asAny(newLink.toJSON())),
            result -> MyLinkDTO.fromJSON(
                    (JsPropertyMap<Object>) result
            ),
            callback
    );
  }

  public void getMyLinks(RestCallback<List<MyLinkDTO>> callback) {
    get(PATH,
            this::mapMyLinks,
            callback);
  }

  private List<MyLinkDTO> mapMyLinks(Object result) {
    return mapArray(result, MyLinkDTO::fromJSON);
  }

  public void deleteLink(String linkId, RestCallback<Void> callback) {
    delete(
            PATH + "/" + encode(linkId),
            null,
            text -> null,
            callback
    );
  }

  private List<MyLinkCategoryDTO> mapMyLinkCategories(Object result) {
    return mapArray(result, MyLinkCategoryDTO::fromJSON);
  }


  public void getMyCategories(RestCallback<List<MyLinkCategoryDTO>> callback) {
    get(PATH + "/categories",
            this::mapMyLinkCategories,
            callback);
  }
}
