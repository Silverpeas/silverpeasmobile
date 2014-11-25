package com.silverpeas.mobile.server.services;

import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;
import com.silverpeas.mobile.shared.exceptions.AlmanachException;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.SearchException;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;
import com.silverpeas.mobile.shared.services.ServiceSearch;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.almanach.control.ejb.AlmanachBm;
import com.stratelia.webactiv.almanach.model.EventDetail;
import com.stratelia.webactiv.almanach.model.EventPK;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.silverpeas.search.PlainSearchResult;
import org.silverpeas.search.SearchEngineFactory;
import org.silverpeas.search.searchEngine.model.MatchingIndexEntry;
import org.silverpeas.search.searchEngine.model.ParseException;
import org.silverpeas.search.searchEngine.model.QueryDescription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ServiceSearchImpl extends AbstractAuthenticateService implements ServiceSearch {

  private static final long serialVersionUID = 1L;
  private AdminBusiness adminBm;
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
      PlainSearchResult r = SearchEngineFactory.getSearchEngine().search(q);
      for (MatchingIndexEntry result : r.getEntries()) {

        if (result.getObjectType().equals(ContentsTypes.Photo.toString()) || result.getObjectType().equals(ContentsTypes.Publication.toString()) || result.getObjectType().contains(ContentsTypes.Attachment.toString())) {
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
      List allowedSpaceIds = getAdminBm().getAvailableSpaceIds(getUserInSession().getId());

      for (int i = 0; i < allowedSpaceIds.size(); i++) {
        buildSpaceComponentAvailableForUser(queryDescription, (String) allowedSpaceIds.get(i),
            ALL_COMPONENTS);
      }
    } else {
      //The search is restricted to one given space
      if (componentId.equals(ALL_COMPONENTS)) {
        //No restriction on components of the selected space
        //First, we get all available components on this space
        List allowedComponentIds = getAdminBm().getAvailCompoIds(spaceId, getUserInSession().getId());
        for (int i = 0; i < allowedComponentIds.size(); i++) {
          buildSpaceComponentAvailableForUser(queryDescription, spaceId,
              (String) allowedComponentIds.get(i));
        }

        //Second, we recurse on each sub space of this space
        List subSpaceIds = getAdminBm().getAvailableSubSpaceIds(spaceId, getUserInSession().getId());
        if (subSpaceIds != null) {
          for (int i = 0; i < subSpaceIds.size(); i++) {
            buildSpaceComponentAvailableForUser(queryDescription, (String) subSpaceIds.get(i),
                ALL_COMPONENTS);
          }
        }
      } else {
        queryDescription.addSpaceComponentPair(spaceId, componentId);
      }
    }
  }

  private AdminBusiness getAdminBm() throws Exception {
    if (adminBm == null) {
      adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
    }
    return adminBm;
  }

}
