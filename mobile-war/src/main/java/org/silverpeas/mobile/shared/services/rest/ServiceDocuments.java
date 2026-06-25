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

import com.google.gwt.user.client.Window;
import elemental2.core.Global;
import elemental2.core.JsArray;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import org.silverpeas.mobile.shared.dto.documents.DocumentDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.documents.TopicDTO;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.List;

/**
 * Service to manage requests related to documents.
 * @author svu
 */
public class ServiceDocuments extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/documents";

  /**
   * Retrieves topics and publications for a given root topic.
   * @param appId The ID of the application.
   * @param rootTopicId The ID of the root topic.
   * @param callback The callback to handle the response (list of BaseDTO).
   */
  public void getTopicsAndPublications(String appId, String rootTopicId, RestCallback<List<BaseDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/topicsAndPublications/" + encode(rootTopicId);
    get(url, result -> DocumentDTO.fromJSON((JsArray<Object>) result), callback);
  }

  /**
   * Retrieves topics for a given root topic.
   * @param appId The ID of the application.
   * @param rootTopicId The ID of the root topic.
   * @param callback The callback to handle the response (list of TopicDTO).
   */
  public void getTopics(String appId, String rootTopicId, RestCallback<List<TopicDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/topics/" + encode(rootTopicId);
    get(url, result -> mapArray(result, TopicDTO::fromJSON), callback);
  }

  /**
   * Retrieves publications for a given topic.
   * @param appId The ID of the application.
   * @param topicId The ID of the topic.
   * @param callback The callback to handle the response (list of PublicationDTO).
   */
  public void getPublications(String appId, String topicId, RestCallback<List<PublicationDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/publications/" + encode(topicId);
    get(url, result -> mapArray(result, PublicationDTO::fromJSON), callback);
  }

  /**
   * Retrieves a specific publication by its ID.
   * @param appId The ID of the application.
   * @param id The ID of the publication.
   * @param contributionId The ID of the contribution.
   * @param type The type of the publication.
   * @param callback The callback to handle the response (PublicationDTO).
   */
  public void getPublication(String appId, String id, String contributionId, String type, RestCallback<PublicationDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/publication/" + encode(id) +
            "?contributionId=" + encode(contributionId) +
            "&type=" + encode(type);
    get(url, result -> PublicationDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves an attachment by its ID.
   * @param appId The ID of the application.
   * @param attachmentId The ID of the attachment.
   * @param callback The callback to handle the response (AttachmentDTO).
   */
  public void getAttachment(String appId, String attachmentId, RestCallback<AttachmentDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/attachment/" + encode(attachmentId);
    get(url, result -> AttachmentDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves tickets for a list of ticket IDs.
   * @param appId The ID of the application.
   * @param tickets The list of tickets to retrieve.
   * @param callback The callback to handle the response (list of TicketDTO).
   */
  public void getTickets(String appId, List<TicketDTO> tickets, RestCallback<List<TicketDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/tickets";
    post(
            url,
            Global.JSON.stringify(Js.asAny(mapListToJsonArray(tickets, TicketDTO::toJSON))),
            result -> mapArray(result, TicketDTO::fromJSON),
            callback
    );
  }

  /**
   * Deletes a list of tickets.
   * @param appId The ID of the application.
   * @param tickets The list of tickets to delete.
   * @param callback The callback to handle the response (list of TicketDTO).
   */
  public void deleteTickets(String appId, List<TicketDTO> tickets, RestCallback<List<TicketDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/deletetickets";
    post(
            url,
            Global.JSON.stringify(Js.asAny(mapListToJsonArray(tickets, TicketDTO::toJSON))),
            result -> mapArray(result, TicketDTO::fromJSON),
            callback
    );
  }

  /**
   * Retrieves the next or previous publication relative to a given publication.
   * @param appId The ID of the application.
   * @param id The ID of the current publication.
   * @param direction The direction ("next" or "previous").
   * @param callback The callback to handle the response (PublicationDTO).
   */
  public void getNextPublication(String appId, String id, String direction, RestCallback<PublicationDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/nextpublication/" + encode(id) + "/" + encode(direction);
    get(url, result -> PublicationDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Publishes a publication.
   * @param appId The ID of the application.
   * @param pubId The ID of the publication to publish.
   * @param callback The callback to handle the response (PublicationDTO).
   */
  public void publish(String appId, String pubId, RestCallback<PublicationDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/publish/" + encode(pubId);
    post(url, null, result -> PublicationDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  // Helper method to map a list of DTOs to a JSON array
  private JsPropertyMap<Object>[] mapListToJsonArray(List<TicketDTO> list, java.util.function.Function<TicketDTO, JsPropertyMap<Object>> mapper) {
    if (list == null) return new JsPropertyMap[0];
    JsPropertyMap<Object>[] array = new JsPropertyMap[list.size()];
    for (int i = 0; i < list.size(); i++) {
      array[i] = mapper.apply(list.get(i));
    }
    return array;
  }
}