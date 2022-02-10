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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.index.search.PlainSearchResult;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.search.ResultDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@WebService
@Authorized
@Path(ServiceSearch.PATH)
public class ServiceSearch extends AbstractRestWebService {

  private final String ALL_SPACES = "*";
  private final String ALL_COMPONENTS = "*";

  static final String PATH = "mobile/search";


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{query}")
  public List<ResultDTO> search(@PathParam("query") String query) throws Exception {
    List<ResultDTO> results = new ArrayList<ResultDTO>();

    QueryDescription q = new QueryDescription(query);
    q.setSearchingUser(getUser().getId());
    q.setRequestedLanguage(getUser().getUserPreferences().getLanguage());
    q.setAdminScope(getUser().isAccessAdmin());
    q.addComponent("Spaces");
    q.addComponent("Components");
    buildSpaceComponentAvailableForUser(q, ALL_SPACES, ALL_COMPONENTS);
    PlainSearchResult r = SearchEngineProvider.getSearchEngine().search(q);
    for (MatchingIndexEntry result : r.getEntries()) {

      if (result.getObjectType().equals(ContentsTypes.Photo.toString()) ||
          result.getObjectType().equals(ContentsTypes.Sound.toString()) ||
          result.getObjectType().equals(ContentsTypes.Video.toString()) ||
          result.getObjectType().equals(ContentsTypes.Streaming.toString()) ||
          result.getObjectType().equals(ContentsTypes.Publication.toString()) ||
          result.getObjectType().contains(ContentsTypes.Attachment.toString()) ||
          result.getObjectType().equals(ContentsTypes.Classified.toString()) ||
          result.getObjectType().equals(ContentsTypes.Node.toString()) ||
          result.getObjectType().equals(ContentsTypes.QuestionContainer.toString()) ||
          result.getObjectType().equals(ContentsTypes.Component.toString()) ||
          result.getObjectType().equals(ContentsTypes.Space.toString())) {
        String title = result.getTitle(getUser().getUserPreferences().getLanguage());
        if (title != null && title.contains("wysiwyg") == false) {
          ResultDTO entry = new ResultDTO();

          if (result.getObjectType().contains(ContentsTypes.Attachment.toString())) {
            entry.setType(ContentsTypes.Attachment.toString());
            String attachmentId = result.getObjectType().replace("Attachment", "");
            attachmentId =
                attachmentId.replace("_" + getUser().getUserPreferences().getLanguage(), "");
            entry.setAttachmentId(attachmentId);
          } else if (result.getObjectType().equals(ContentsTypes.Node.toString())) {
            if (result.getComponent().startsWith(Apps.kmelia.toString())) {
              entry.setType(ContentsTypes.Folder.name());
            } else if (result.getComponent().startsWith(Apps.gallery.toString())) {
              entry.setType(ContentsTypes.Album.name());
            }
          } else {
            entry.setType(result.getObjectType());
          }
          entry.setId(result.getObjectId());
          entry.setTitle(title);
          if (result.getObjectType().equals(ContentsTypes.Component.name())) {
            entry.setComponentId(result.getObjectId());
          } else {
            entry.setComponentId(result.getComponent());
          }
          results.add(entry);
        }
      }
    }

    return results;
  }

  private void buildSpaceComponentAvailableForUser(QueryDescription queryDescription,
      String spaceId, String componentId) throws Exception {

    if (spaceId == null || spaceId.length() == 0) {
      spaceId = ALL_SPACES;
    }
    if (componentId == null || componentId.length() == 0) {
      componentId = ALL_COMPONENTS;
    }

    if (spaceId.equals(ALL_SPACES)) {
      //No restriction on spaces.
      String[] allowedSpaceIds = getAdministration().getAllSpaceIds(getUser().getId());

      for (String allowedSpaceId : allowedSpaceIds) {
        buildSpaceComponentAvailableForUser(queryDescription, (String) allowedSpaceId,
            ALL_COMPONENTS);
      }
    } else {
      //The search is restricted to one given space
      if (componentId.equals(ALL_COMPONENTS)) {
        //No restriction on components of the selected space
        //First, we get all available components on this space
        String[] allowedComponentIds =
            getAdministration().getAvailCompoIds(spaceId, getUser().getId());
        for (String allowedComponentId : allowedComponentIds) {
          buildSpaceComponentAvailableForUser(queryDescription, spaceId, allowedComponentId);
        }

        //Second, we recurse on each sub space of this space
        String[] subSpaceIds =
            getAdministration().getAllowedSubSpaceIds(getUser().getId(), spaceId);
        if (subSpaceIds != null) {
          for (String subSpaceId : subSpaceIds) {
            buildSpaceComponentAvailableForUser(queryDescription, subSpaceId, ALL_COMPONENTS);
          }
        }
      } else {
        queryDescription.addComponent(componentId);
      }
    }
  }

  private Administration getAdministration() {
    return Administration.get();
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
  }
}
