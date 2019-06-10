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

import org.apache.commons.fileupload.FileItem;
import org.silverpeas.components.formsonline.model.FormDetail;
import org.silverpeas.components.formsonline.model.FormPK;
import org.silverpeas.components.formsonline.model.FormsOnlineDatabaseException;
import org.silverpeas.components.formsonline.model.FormsOnlineService;
import org.silverpeas.components.formsonline.model.RequestsByStatus;
import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.annotation.RequestScoped;
import org.silverpeas.core.annotation.Service;
import org.silverpeas.core.contribution.content.form.FieldTemplate;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.FormException;
import org.silverpeas.core.contribution.content.form.RecordSet;
import org.silverpeas.core.contribution.content.form.record.GenericFieldTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateException;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Service
@RequestScoped
@Path(ServiceFormsOnline.PATH)
@Authorized
public class ServiceFormsOnline extends RESTWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "formsOnline";


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("sendables/{appId}")
  public List<FormDTO> getSendablesForms(@PathParam("appId") String appId) {
      List<FormDTO> dtos = new ArrayList<>();
    try {
      List<FormDetail> forms = FormsOnlineService.get().getAllForms(appId, getUser().getId(), true);
      for (FormDetail form : forms) {
        FormDTO dto = new FormDTO();
        if (form.isSendable() && form.isPublished()) {
          dto.setId(String.valueOf(form.getId()));
          dto.setTitle(form.getTitle());
          dto.setDescription(form.getDescription());
          dto.setXmlFormName(form.getXmlFormName());
          dtos.add(dto);
        }
      }

    } catch (FormsOnlineDatabaseException e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dtos;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("myrequests/{appId}")
  public List<FormRequestDTO> getMyRequests(@PathParam("appId") String appId) {
    List<FormRequestDTO> requestDTOS = new ArrayList<>();

    try {
      RequestsByStatus reqs = FormsOnlineService.get().getAllUserRequests(appId, getUser().getId(), null);
      reqs.getAll();
      //TODO

    } catch (FormsOnlineDatabaseException e) {
      SilverLogger.getLogger(this).error(e);
    }

    return requestDTOS;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("saveForm")
  public Boolean saveForm() {



    String instanceId = "formsOnline16";
    String formId = "1";

    List<FileItem> items = this.getHttpRequest().getFileItems();
    int i = 0;
    for (FileItem item : items) {
      if (item.getFieldName().equalsIgnoreCase("instanceId")) {
        instanceId = item.getString();
        items.remove(i);
        break;
      }
      i++;
    }

    i = 0;
    for (FileItem item : items) {
      if (item.getFieldName().equalsIgnoreCase("formId")) {
        formId = item.getString();
        items.remove(i);
        break;
      }
      i++;
    }

    FormPK pk = new FormPK(formId, instanceId);
    try {
      FormsOnlineService.get().saveRequest(pk, getUser().getId(), items);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return Boolean.TRUE;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("form/{appId}/{formName}")
  public List<FormFieldDTO> getForm(@PathParam("appId") String appId, @PathParam("formName") String formName) {

    List<FormFieldDTO> fields = new ArrayList<>();

    try {
      PublicationTemplate template = getPublicationTemplate(formName, true);
      Form formUpdate = getEmptyForm(template);

      for(FieldTemplate field : formUpdate.getFieldTemplates()) {
        FormFieldDTO f = new FormFieldDTO();
        f.setDisplayerName(field.getDisplayerName());
        String label = field.getLabel(getUser().getUserPreferences().getLanguage());
        if (label == null || label.isEmpty()) label = field.getLabel();
        f.setLabel(label);
        f.setName(field.getFieldName());
        f.setType(field.getTypeName());
        f.setMandatory(field.isMandatory());
        f.setReadOnly(field.isReadOnly());
        f.setValues(((GenericFieldTemplate) field).getKeyValuePairs(getUserPreferences().getLanguage()));
        fields.add(f);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return fields;

  }

  private PublicationTemplate getPublicationTemplate(String xmlFormName, boolean registerIt)
      throws SilverpeasException {
    try {
      String xmlFormShortName = xmlFormName.substring(xmlFormName.indexOf('/') + 1, xmlFormName.indexOf('.'));
      if (registerIt) {
        getPublicationTemplateManager()
            .addDynamicPublicationTemplate(getComponentId() + ":" + xmlFormShortName, xmlFormName);
      }
      return getPublicationTemplateManager()
          .getPublicationTemplate(getComponentId()+":"+xmlFormShortName, xmlFormName);
    } catch (Exception e) {
      throw new SilverpeasException("Can't load form '"+xmlFormName+"'", e);
    }
  }

  private PublicationTemplateManager getPublicationTemplateManager() {
    return PublicationTemplateManager.getInstance();
  }

  private Form getEmptyForm(PublicationTemplate template) throws SilverpeasException {
    Form form;
    try {
      // form and DataRecord creation
      form = template.getUpdateForm();
      RecordSet recordSet = template.getRecordSet();
      form.setData(recordSet.getEmptyRecord());
    } catch (Exception e) {
      throw new SilverpeasException("Can't load form ", e);
    }
    return form;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }
}
