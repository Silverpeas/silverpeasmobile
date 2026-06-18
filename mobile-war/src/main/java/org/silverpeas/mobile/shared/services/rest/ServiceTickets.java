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
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.List;

/**
 * Service for managing requests related to sharing tickets.
 * @author svu
 */
public class ServiceTickets extends AbstractService {

  private static final String PATH = "/silverpeas/services/mytickets";

  /**
   * Create a new ticket.
   * @param componentId
   * @param ticket
   * @param callback
   */
  public void createTicket(String componentId, TicketDTO ticket, RestCallback<TicketDTO> callback) {
    String url = PATH + "/" + encode(componentId) + "/";
    post(
            url,
            Global.JSON.stringify(Js.asAny(ticket.toJSON())),
            result -> TicketDTO.fromJSON((JsPropertyMap<Object>) result),
            callback
    );
  }

  /**
   * Retrieve the user's tickets with pagination.
   * @param page
   * @param callback
   */
  public void getMyTickets(String page, RestCallback<List<TicketDTO>> callback) {
    String url = PATH + "?page=" + encode(page);
    get(url, result -> mapArray(result, TicketDTO::fromJSON), callback);
  }
}