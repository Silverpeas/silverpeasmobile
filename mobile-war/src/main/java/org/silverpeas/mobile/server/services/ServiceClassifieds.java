/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

import org.silverpeas.components.classifieds.model.ClassifiedDetail;
import org.silverpeas.components.classifieds.notification.ClassifiedOwnerNotification;
import org.silverpeas.components.classifieds.service.ClassifiedService;
import org.silverpeas.components.classifieds.service.ClassifiedServiceProvider;
import org.silverpeas.core.ResourceReference;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.AdministrationServiceProvider;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.attachment.util.SimpleDocumentList;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.FieldValuesTemplate;
import org.silverpeas.core.contribution.content.form.RecordSet;
import org.silverpeas.core.contribution.content.form.record.GenericFieldTemplate;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateException;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.notification.user.builder.helper.UserNotificationHelper;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.FormsHelper;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@WebService
@Authorized
@Path(ServiceClassifieds.PATH + "/{appId}")
public class ServiceClassifieds extends AbstractRestWebService {

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  static final String PATH = "mobile/classifieds";

  private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{Id}")
  public ClassifiedsDTO getClassified(@PathParam("Id") String id) {
    ClassifiedsDTO dto = getClassifieds();
    for (ClassifiedDTO c : dto.getClassifieds()) {
      if (c.getId().equals(id)) {
        dto.getClassifieds().clear();
        dto.getClassifieds().add(c);
        return dto;
      }
    }
    return dto;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{instanceId}/{message}")
  public void sendMessageToOwner(@PathParam("message") String message, ClassifiedDTO dto) {
    ClassifiedDetail classified = getClassifiedDetailById(dto.getId(), componentId);
    UserNotificationHelper.buildAndSend(new ClassifiedOwnerNotification(classified,
        getUser().getId(), message));
  }

  private ClassifiedDetail getClassifiedDetailById(String id, String instanceId) {
    ClassifiedService service = ClassifiedServiceProvider.getClassifiedService();
    List<ClassifiedDetail> l = service.getAllValidClassifieds(instanceId);
    for (ClassifiedDetail d : l) {
      if (d.getId().equals(id)) return d;
    }
    return null;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("all")
  public ClassifiedsDTO getClassifieds() {
    ClassifiedsDTO dto = new ClassifiedsDTO();

    try {
      String searchFields1 = getSearchField1(componentId, "searchFields1");
      String searchFields2 = getSearchField1(componentId, "searchFields2");
      dto.setCategories(createListField(componentId, searchFields1));
      dto.setTypes(createListField(componentId, searchFields2));
      dto.setHasComments(hasComments(componentId));
      ClassifiedService service = ClassifiedServiceProvider.getClassifiedService();
      List<ClassifiedDetail> classifiedDetails = service.getAllValidClassifieds(componentId);
      for (ClassifiedDetail classifiedDetail : classifiedDetails) {
        dto.getClassifieds().add(populate(classifiedDetail));
      }
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new WebApplicationException(e);
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

  private boolean hasComments(final String instanceId) {
    String value = AdministrationServiceProvider.getAdminService()
        .getComponentParameterValue(instanceId, "comments");
    return (value.equalsIgnoreCase("yes"));
  }

  private ClassifiedDTO populate(ClassifiedDetail classifiedDetail) throws Exception {
    ClassifiedDTO dto = new ClassifiedDTO();
    dto.setId(classifiedDetail.getId());
    dto.setTitle(classifiedDetail.getTitle());
    dto.setDescription(classifiedDetail.getDescription());
    dto.setPrice(String.valueOf(classifiedDetail.getPrice()));

    dto.setShowPrice(false);
    String showPrice =
            getMainSessionController().getComponentParameterValue(componentId, "usePrice");
    if (showPrice.equalsIgnoreCase("yes")) dto.setShowPrice(true);

    SimpleDocumentPK pk = new SimpleDocumentPK(classifiedDetail.getId(), classifiedDetail.getComponentInstanceId());
    List<String> pictures = new ArrayList<>();
    SimpleDocumentList<SimpleDocument> pics = AttachmentServiceProvider
        .getAttachmentService().listAllDocumentsByForeignKey(new ResourceReference(pk), getUser().getUserPreferences().getLanguage());
    for (SimpleDocument pic : pics) {
      String s = DataURLHelper
          .convertPictureToUrlData(pic.getAttachmentPath(), pic.getFilename(), "600x");
      pictures.add(s);
    }
    dto.setPictures(pictures);

    dto.setCreatorId(classifiedDetail.getCreatorId());
    dto.setCreatorName(Administration.get().getUserDetail(classifiedDetail.getCreatorId()).getDisplayedName());
    dto.setCreationDate(sdf.format(classifiedDetail.getCreationDate()));
    if (classifiedDetail.getUpdateDate() != null)dto.setUpdateDate(sdf.format(classifiedDetail.getUpdateDate()));

    PublicationTemplate pubTemplate = getPublicationTemplate(classifiedDetail.getComponentInstanceId());
    if (pubTemplate != null) {
      RecordSet recordSet = pubTemplate.getRecordSet();
      DataRecord data = recordSet.getRecord(classifiedDetail.getId());
      List<FormFieldDTO> fields = FormsHelper
          .getInstance().getViewFormFields(pubTemplate, data, getUser());
      dto.setFields(fields);
      dto.setCategory(data.getField(getSearchField1(classifiedDetail.getComponentInstanceId(), "searchFields1")).getValue());
      dto.setType(data.getField(getSearchField2(classifiedDetail.getComponentInstanceId(), "searchFields2")).getValue());
    }
    final ResourceReference ref = new ResourceReference(
        new PublicationPK(classifiedDetail.getId()));
    dto.setCommentsNumber(CommentServiceProvider
        .getCommentService().getCommentsCountOnResource(ClassifiedDetail.getResourceType(), ref));

    return dto;
  }

  private Map<String, String> createListField(String instanceId, String listName) throws Exception {
    Map<String, String> fields = Collections.synchronizedMap(new HashMap<>());
    if (StringUtil.isDefined(listName)) {
      PublicationTemplate pubTemplate = getPublicationTemplate(instanceId);
      if (pubTemplate != null) {
        GenericFieldTemplate field = (GenericFieldTemplate) pubTemplate.getRecordTemplate().getFieldTemplate(listName);
        field.getFieldValuesTemplate(getUser().getUserPreferences().getLanguage())
            .apply(v -> fields.put(v.getKey(), v.getLabel()));
      }
    }
    return fields;
  }

  private PublicationTemplate getPublicationTemplate(String instanceId) throws
                                                                        PublicationTemplateException {
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



  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return this.componentId;
  }

}
