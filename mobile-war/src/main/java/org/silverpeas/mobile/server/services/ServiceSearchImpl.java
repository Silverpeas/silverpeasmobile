/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.index.search.PlainSearchResult;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.search.ResultDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.SearchException;
import org.silverpeas.mobile.shared.services.ServiceSearch;

import java.util.ArrayList;
import java.util.List;

public class ServiceSearchImpl extends AbstractAuthenticateService implements ServiceSearch {

  private static final long serialVersionUID = 1L;
  private final String ALL_SPACES = "*";
  private final String ALL_COMPONENTS = "*";

  @Override
  public List<ResultDTO> search(final String query)
      throws SearchException, AuthenticationException {
    List<ResultDTO> results = new ArrayList<ResultDTO>();
    try {
      QueryDescription q = new QueryDescription(query);
      q.setSearchingUser(getUserInSession().getId());
      q.setRequestedLanguage(getUserInSession().getUserPreferences().getLanguage());
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
            result.getObjectType().equals(ContentsTypes.Node.toString())) {
          String title = result.getTitle(getUserInSession().getUserPreferences().getLanguage());
          if (title != null && title.contains("wysiwyg") == false) {
            ResultDTO entry = new ResultDTO();

            if (result.getObjectType().contains(ContentsTypes.Attachment.toString())) {
              entry.setType(ContentsTypes.Attachment.toString());
              String attachmentId = result.getObjectType().replace("Attachment", "");
              attachmentId = attachmentId
                  .replace("_" + getUserInSession().getUserPreferences().getLanguage(), "");
              entry.setAttachmentId(attachmentId);
            } else if(result.getObjectType().equals(ContentsTypes.Node.toString())) {
              if (result.getComponent().startsWith(Apps.kmelia.toString())) {
                entry.setType(ContentsTypes.Folder.name());
              }
            } else {
              entry.setType(result.getObjectType());
            }
            entry.setId(result.getObjectId());
            entry.setTitle(title);
            entry.setComponentId(result.getComponent());
            results.add(entry);
          }
        }
      }

    } catch (Exception e) {
      throw new SearchException(e);
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
      String[] allowedSpaceIds = getAdministration().getAllSpaceIds(getUserInSession().getId());

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
            getAdministration().getAvailCompoIds(spaceId, getUserInSession().getId());
        for (String allowedComponentId : allowedComponentIds) {
          buildSpaceComponentAvailableForUser(queryDescription, spaceId, allowedComponentId);
        }

        //Second, we recurse on each sub space of this space
        String[] subSpaceIds =
            getAdministration().getAllowedSubSpaceIds(getUserInSession().getId(), spaceId);
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

}
