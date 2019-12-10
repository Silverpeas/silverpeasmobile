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
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.service.AdministrationServiceProvider;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.attachment.util.SimpleDocumentList;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.RecordSet;
import org.silverpeas.core.contribution.content.form.record.GenericFieldTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateException;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.ClassifiedsException;
import org.silverpeas.mobile.shared.services.ServiceClassifieds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service de gestion des news.
 * @author svu
 */
public class ServiceClassifiedsImpl extends AbstractAuthenticateService implements
    ServiceClassifieds {

  private static final long serialVersionUID = 1L;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

  @Override
  public ClassifiedsDTO getClassifieds(String instanceId) throws ClassifiedsException, AuthenticationException {
    checkUserInSession();
    ClassifiedsDTO dto = new ClassifiedsDTO();

    try {
      String searchFields1 = getSearchField1(instanceId, "searchFields1");
      String searchFields2 = getSearchField1(instanceId, "searchFields2");
      dto.setCategories(createListField(instanceId, searchFields1));
      dto.setTypes(createListField(instanceId, searchFields2));
      ClassifiedService service = ClassifiedServiceProvider.getClassifiedService();
      List<ClassifiedDetail> classifiedDetails = service.getAllValidClassifieds(instanceId);
      for (ClassifiedDetail classifiedDetail : classifiedDetails) {
        dto.getClassifieds().add(populate(classifiedDetail));
      }
    } catch(Exception e) {
      throw new ClassifiedsException(e);
    }

    return dto;
  }

  private String getSearchField1(final String instanceId, final String searchFields1) {
    return AdministrationServiceProvider.getAdminService()
        .getComponentParameterValue(instanceId, searchFields1);
  }

  private String getSearchField2(final String instanceId, final String searchFields2) {
    return AdministrationServiceProvider.getAdminService()
        .getComponentParameterValue(instanceId, searchFields2);
  }

  private ClassifiedDTO populate(ClassifiedDetail classifiedDetail) throws Exception {
    ClassifiedDTO dto = new ClassifiedDTO();
    dto.setId(classifiedDetail.getId());
    dto.setTitle(classifiedDetail.getTitle());
    dto.setDescription(classifiedDetail.getDescription());
    dto.setPrice(String.valueOf(classifiedDetail.getPrice()));

    SimpleDocumentPK pk = new SimpleDocumentPK(classifiedDetail.getId(), classifiedDetail.getComponentInstanceId());
    List<String> pictures = new ArrayList<>();
    SimpleDocumentList<SimpleDocument> pics = AttachmentServiceProvider.getAttachmentService().listAllDocumentsByForeignKey(new ResourceReference(pk), getUserInSession().getUserPreferences().getLanguage());
    for (SimpleDocument pic : pics) {
      String s = DataURLHelper.convertPictureToUrlData(pic.getAttachmentPath(), pic.getFilename(), "200x");
      pictures.add(s);
    }
    dto.setPictures(pictures);

    dto.setCreatorName(classifiedDetail.getCreatorName());
    dto.setCreationDate(sdf.format(classifiedDetail.getCreationDate()));
    if (classifiedDetail.getUpdateDate() != null)dto.setUpdateDate(sdf.format(classifiedDetail.getUpdateDate()));

    PublicationTemplate pubTemplate = getPublicationTemplate(classifiedDetail.getComponentInstanceId());
    if (pubTemplate != null) {
      RecordSet recordSet = pubTemplate.getRecordSet();
      DataRecord data = recordSet.getRecord(classifiedDetail.getId());
      dto.setCategory(data.getField(getSearchField1(classifiedDetail.getComponentInstanceId(), "searchFields1")).getValue());
      dto.setType(data.getField(getSearchField2(classifiedDetail.getComponentInstanceId(), "searchFields2")).getValue());
    }
    return dto;
  }

  private Map<String, String> createListField(String instanceId, String listName) throws Exception {
    Map<String, String> fields = Collections.synchronizedMap(new HashMap<>());
    if (StringUtil.isDefined(listName)) {
        PublicationTemplate pubTemplate = getPublicationTemplate(instanceId);
        if (pubTemplate != null) {
          GenericFieldTemplate field = (GenericFieldTemplate) pubTemplate.getRecordTemplate().getFieldTemplate(listName);
          return field.getKeyValuePairs(getUserInSession().getUserPreferences().getLanguage());
        }
    } else { }
    return fields;
  }

  private PublicationTemplate getPublicationTemplate(String instanceId) throws PublicationTemplateException {
    PublicationTemplate pubTemplate = null;
    String xmlFormName = AdministrationServiceProvider.getAdminService().getComponentParameterValue(instanceId, "XMLFormName");
    if (StringUtil.isDefined(xmlFormName)) {
      String xmlFormShortName =
          xmlFormName.substring(xmlFormName.indexOf('/') + 1, xmlFormName.indexOf('.'));
      pubTemplate =
          PublicationTemplateManager.getInstance().getPublicationTemplate(
              instanceId + ":" + xmlFormShortName, xmlFormName);
    }
    return pubTemplate;
  }
}