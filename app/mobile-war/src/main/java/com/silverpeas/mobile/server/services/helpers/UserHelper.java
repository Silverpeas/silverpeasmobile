/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.server.services.helpers;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import org.apache.commons.codec.binary.Base64;
import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.io.file.ImageResizingProcessor;
import org.silverpeas.core.io.file.SilverpeasFileProcessor;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.web.look.PublicationUpdateDateComparator;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;

/**
 * @author: svu
 */
public class UserHelper {

  private static UserHelper instance;

  public static UserHelper getInstance() {
    if (instance == null) {
      instance = new UserHelper();
    }
    return instance;
  }

  public DetailUserDTO populate(UserDetail user) {
    DetailUserDTO dto= new DetailUserDTO();
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.seteMail(user.geteMail());
    dto.setStatus(user.getStatus());
    dto.setAvatar(user.getAvatar());
    dto.setLanguage(user.getUserPreferences().getLanguage());
    dto.setToken(user.getToken());
    return dto;
  }

  public UserDTO populateUserDTO(UserDetail user) {
    UserDTO dto= new UserDTO();
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.seteMail(user.geteMail());
    dto.setAvatar(user.getAvatar());
    return dto;
  }
}
