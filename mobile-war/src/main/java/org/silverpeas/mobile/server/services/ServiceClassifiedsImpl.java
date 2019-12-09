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

import org.silverpeas.components.classifieds.model.ClassifiedDetail;
import org.silverpeas.components.classifieds.service.ClassifiedService;
import org.silverpeas.components.classifieds.service.ClassifiedServiceProvider;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.ClassifiedsException;
import org.silverpeas.mobile.shared.services.ServiceClassifieds;

import java.util.List;

/**
 * Service de gestion des news.
 * @author svu
 */
public class ServiceClassifiedsImpl extends AbstractAuthenticateService implements
    ServiceClassifieds {

  private static final long serialVersionUID = 1L;

  @Override
  public ClassifiedsDTO getClassifieds(String instanceId) throws ClassifiedsException, AuthenticationException {
    checkUserInSession();
    ClassifiedsDTO dto = new ClassifiedsDTO();

    //TODO
    //dto.setCategories();
    //dto.setTypes();

    ClassifiedService service = ClassifiedServiceProvider.getClassifiedService();
    List<ClassifiedDetail> classifiedDetails =  service.getAllValidClassifieds(instanceId);
    for (ClassifiedDetail classifiedDetail : classifiedDetails) {
      dto.getClassifieds().add(populate(classifiedDetail));
    }

    return dto;
  }

  private ClassifiedDTO populate(ClassifiedDetail classifiedDetail) {
    ClassifiedDTO dto = new ClassifiedDTO();
    dto.setTitle(classifiedDetail.getTitle());
    dto.setDescription(classifiedDetail.getDescription());
    //TODO
    return dto;
  }
}