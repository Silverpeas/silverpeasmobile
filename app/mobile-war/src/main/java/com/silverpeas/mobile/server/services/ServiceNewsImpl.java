package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.comparator.DelegatedNewsBeginDateComparatorAsc;
import com.silverpeas.mobile.server.services.helpers.NewsHelper;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NewsException;
import com.silverpeas.mobile.shared.services.ServiceNews;
import org.apache.commons.codec.binary.Base64;
import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsService;
import org.silverpeas.components.delegatednews.service.DelegatedNewsServiceProvider;
import org.silverpeas.core.admin.ObjectType;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.logging.SilverLogger;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des news.
 * @author svu
 */
public class ServiceNewsImpl extends AbstractAuthenticateService implements ServiceNews {

  private static final long serialVersionUID = 1L;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private OrganizationController organizationController = OrganizationController.get();

  @Override
  public List<NewsDTO> getNews(String instanceId) throws NewsException, AuthenticationException {
    checkUserInSession();
    try {
      boolean managerAccess = Administration.get().isComponentManageable(instanceId, getUserInSession().getId());
      List<PublicationDetail> pubs = NewsHelper.getInstance().getNewsByComponentId(instanceId, managerAccess);
      List<NewsDTO> news = NewsHelper.getInstance().populate(pubs, managerAccess);
      return news;
    } catch (AdminException e) {
      throw new  NewsException(e);
    }
  }
}
