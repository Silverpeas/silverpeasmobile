package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.SearchException;
import com.silverpeas.mobile.shared.services.ServiceSearch;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.index.search.PlainSearchResult;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.QueryDescription;

import java.util.ArrayList;
import java.util.List;

public class ServiceSearchImpl extends AbstractAuthenticateService implements ServiceSearch {

  private static final long serialVersionUID = 1L;
  private final String ALL_SPACES = "*";
  private final String ALL_COMPONENTS = "*";

  @Override
  public List<ResultDTO> search(final String query) throws SearchException, AuthenticationException {
    List<ResultDTO> results = new ArrayList<ResultDTO>();
    try {
      QueryDescription q = new QueryDescription(query);
      q.setSearchingUser(getUserInSession().getId());
      q.setRequestedLanguage(getUserInSession().getUserPreferences().getLanguage());
      buildSpaceComponentAvailableForUser(q, ALL_SPACES, ALL_COMPONENTS);
      PlainSearchResult r = SearchEngineProvider.getSearchEngine().search(q);
      for (MatchingIndexEntry result : r.getEntries()) {

        if (result.getObjectType().equals(ContentsTypes.Photo.toString()) || result.getObjectType().equals(ContentsTypes.Sound.toString()) || result.getObjectType().equals(ContentsTypes.Video.toString()) || result.getObjectType().equals(ContentsTypes.Streaming.toString()) || result.getObjectType().equals(ContentsTypes.Publication.toString()) || result.getObjectType().contains(
            ContentsTypes.Attachment.toString())) {
          String title = result.getTitle(getUserInSession().getUserPreferences().getLanguage());
          if (title != null && title.contains("wysiwyg") == false) {
            ResultDTO entry = new ResultDTO();

            if (result.getObjectType().contains(ContentsTypes.Attachment.toString())) {
              entry.setType(ContentsTypes.Attachment.toString());
              String attachmentId = result.getObjectType().replace("Attachment", "");
              attachmentId = attachmentId
                  .replace("_" + getUserInSession().getUserPreferences().getLanguage(), "");
              entry.setAttachmentId(attachmentId);
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

  private void buildSpaceComponentAvailableForUser(QueryDescription queryDescription, String spaceId, String componentId) throws Exception {

    if (spaceId == null || spaceId.length() == 0) {
      spaceId = ALL_SPACES;
    }
    if (componentId == null || componentId.length() == 0) {
      componentId = ALL_COMPONENTS;
    }

    if (spaceId.equals(ALL_SPACES)) {
      //No restriction on spaces.
      String [] allowedSpaceIds = getAdministration().getAllSpaceIds(getUserInSession().getId());

      for (String allowedSpaceId : allowedSpaceIds) {
        buildSpaceComponentAvailableForUser(queryDescription, (String) allowedSpaceId, ALL_COMPONENTS);
      }
    } else {
      //The search is restricted to one given space
      if (componentId.equals(ALL_COMPONENTS)) {
        //No restriction on components of the selected space
        //First, we get all available components on this space
        String [] allowedComponentIds = getAdministration().getAvailCompoIds(spaceId, getUserInSession().getId());
        for (String allowedComponentId : allowedComponentIds) {
          buildSpaceComponentAvailableForUser(queryDescription, spaceId, allowedComponentId);
        }

        //Second, we recurse on each sub space of this space
        String [] subSpaceIds = getAdministration().getAllowedSubSpaceIds(getUserInSession().getId(), spaceId);
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
