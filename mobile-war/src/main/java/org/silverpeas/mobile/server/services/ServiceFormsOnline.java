/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.silverpeas.components.formsonline.FormsOnlineComponentSettings;
import org.silverpeas.components.formsonline.model.*;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.ProfileInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.*;
import org.silverpeas.core.contribution.content.form.form.HtmlForm;
import org.silverpeas.core.contribution.content.form.form.XmlForm;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateImpl;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.kernel.SilverpeasException;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormLayerDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;
import org.silverpeas.mobile.shared.dto.formsonline.ValidationRequestDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.emptyList;

@WebService
@Authorized
@Path(ServiceFormsOnline.PATH + "/{appId}")
public class ServiceFormsOnline extends AbstractRestWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "mobile/formsOnline";

  private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  private final SettingBundle formsOnlineBundle =
      ResourceLocator.getSettingBundle("org.silverpeas.formsonline.multilang.formsOnlineBundle");

  @PathParam("appId")
  private String componentId;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("loadRequest/{requestId}")
  public FormRequestDTO loadRequest(@PathParam("requestId") String requestId) {
    FormRequestDTO dto = null;
    try {
      RequestPK pk = new RequestPK(requestId, getComponentId());
      FormInstance req = FormsOnlineService.get().loadRequest(pk, getUser().getId());
      dto = populate(req);
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dto;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("processRequest/{requestId}")
  public void processRequest(@PathParam("requestId") String requestId, ValidationRequestDTO validation) {
    try {
      if (validation.getDecision().equalsIgnoreCase("cancel")) {
        FormsOnlineService.get().cancelRequest(new RequestPK(requestId, getComponentId()));
      } else {
        FormsOnlineService.get()
                .saveNextRequestValidationStep(new RequestPK(requestId, getComponentId()),
                        getUser().getId(), validation.getDecision(), validation.getComment(), false);
      }
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("receivables")
  public List<FormDTO> getReceivablesForms() {
    List<FormDTO> dtos = new ArrayList<>();
    try {
      Set<String> formIdsAsReceiver = FormsOnlineService.get()
          .getValidatorFormIdsWithValidationTypes(getComponentId(), getUser().getId(), emptyList())
          .keySet();
      for (String id : formIdsAsReceiver) {
        FormDetail form = FormsOnlineService.get().loadForm(new FormPK(id, getComponentId()));
        FormDTO dto = populate(formIdsAsReceiver, form);
        dtos.add(dto);
      }
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return dtos;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("sendables")
  public List<FormDTO> getSendablesForms() {
      List<FormDTO> dtos = new ArrayList<>();
    try {
      List<FormDetail> forms = FormsOnlineService.get().getAllForms(getComponentId(), getUser().getId(), true);
      Set<String> formIdsAsReceiver = FormsOnlineService.get()
          .getValidatorFormIdsWithValidationTypes(getComponentId(), getUser().getId(), emptyList())
          .keySet();
      for (FormDetail form : forms) {
        if (form.isSendable() && form.isPublished()) {
          FormDTO dto = populate(formIdsAsReceiver, form);
          dtos.add(dto);
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dtos;
  }

  private int countRequestsToValidate(int formId) throws Exception {
    int count = 0;
    RequestsByStatus r = FormsOnlineService.get().getValidatorRequests(getRequestsFilter(), getUser().getId(), null);
    for (FormInstance f : r.getToValidate()) {
      if (f.getFormId() == formId) {
        count++;
      }
    }
    return count;
  }

  private FormDTO populate(final Collection<String> formIdsAsReceiver, final FormDetail form) throws Exception {
    FormDTO dto = new FormDTO();
    dto.setId(String.valueOf(form.getId()));
    dto.setTitle(form.getTitle());
    dto.setDescription(form.getDescription());
    dto.setXmlFormName(form.getXmlFormName());
    dto.setNbRequests(countRequestsToValidate(form.getId()));
    if (formIdsAsReceiver.contains(String.valueOf(form.getId()))) {
      dto.setReceiver(true);
    }
    return dto;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("requests/{formId}")
  public List<FormRequestDTO> getRequests(@PathParam("formId") String formId) {
    List<FormRequestDTO> requestDTOS = new ArrayList<>();
    try {
      RequestsByStatus r = FormsOnlineService.get().getValidatorRequests(getRequestsFilter(), getUser().getId(), null);
      for (FormInstance f : r.getToValidate()) {
        if (f.getFormId() == Integer.parseInt(formId)) {
          f = FormsOnlineService.get().loadRequest(new RequestPK(f.getId(), getComponentId()), getUser().getId());
          FormRequestDTO dto = populate(f);
          requestDTOS.add(dto);
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return requestDTOS;
  }

  private FormRequestDTO populate(final FormInstance f)
      throws FormException {
    FormRequestDTO dto = new FormRequestDTO();
    dto.setId(f.getId());
    dto.setValidator(1);
    if (f.canBeCanceledBy(getUser())) dto.setValidator(0);
    dto.setComments(f.getComments());
    dto.setTitle(f.getTitle());
    dto.setDescription(f.getDescription());
    dto.setState(f.getState());
    dto.setCreator(f.getCreator().getDisplayedName());
    dto.setCreationDate(sdf.format(f.getCreationDate()));
    dto.setFormId(String.valueOf(f.getFormId()));
    dto.setFormName(f.getForm().getName()+".xml");
    switch (f.getState()) {
      case FormInstance.STATE_VALIDATED :
          dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateValidated"));
        break;
      case FormInstance.STATE_REFUSED:
        dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateRefused"));
        break;
      case FormInstance.STATE_ARCHIVED:
        dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateArchived"));
        break;
      case FormInstance.STATE_READ:
        dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateRead"));
        break;
      case FormInstance.STATE_UNREAD:
        dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateUnread"));
        break;
      case FormInstance.STATE_CANCELED:
        dto.setStateLabel(formsOnlineBundle.getString("formsOnline.stateCanceled"));
        break;
      default:
        break;
    }
    if (f.getFormWithData() instanceof XmlForm) {
      XmlForm formXml = ((XmlForm) f.getFormWithData());
      if (formXml != null) {
        DataRecord record = formXml.getData();
        List<FormFieldDTO> dataForm = new ArrayList<>();
        for (String name : record.getFieldNames()) {
          Field field = record.getField(name);
          FormFieldDTO fieldDTO = populateField(f, field);
          dataForm.add(fieldDTO);
        }
        dto.setData(dataForm);
      }
    } else {
      HtmlForm formHTML = ((HtmlForm) f.getFormWithData());
      DataRecord record = formHTML.getData();
      List<FormFieldDTO> dataForm = new ArrayList<>();
      for (String name : record.getFieldNames()) {
        Field field = record.getField(name);
        FormFieldDTO fieldDTO = populateField(f, field);
        dataForm.add(fieldDTO);
      }
      dto.setData(dataForm);
    }

    return dto;
  }

  private FormFieldDTO populateField(final FormInstance f, final Field field) {
    FormFieldDTO fieldDTO = new FormFieldDTO();
    fieldDTO.setLabel(getLabel(f, field.getName()));
    for (FieldTemplate fTemplate : f.getFormWithData().getFieldTemplates()) {
      if (fTemplate.getFieldName().equals(field.getName())) {
        Map<String, String> params = fTemplate.getParameters(getUser().getId());
        if (params.containsKey("keys")) {
          String [] keys = params.get("keys").split("##");
          String [] values = params.get("values").split("##");
          if (field.getValue() != null) {
            if (field.getValue().contains("##")) {
              String[] fiedValues = field.getValue().split("##");
              String fieldDisplayValue = "";
              for (String fieldValue : fiedValues) {
                int index = Arrays.asList(keys).indexOf(fieldValue);
                fieldDisplayValue += values[index] + " ";
              }
              fieldDTO.setValue(fieldDisplayValue);
              fieldDTO.setValueId(field.getValue());
            } else {
              int index = Arrays.asList(keys).indexOf(field.getValue());
              if (index != -1) {
                fieldDTO.setValue(values[index]);
                fieldDTO.setValueId(field.getValue());
              }
            }
          }
        } else {
          if (field.getTypeName().equalsIgnoreCase("multipleUser") || field.getTypeName().equalsIgnoreCase("user") || field.getTypeName().equalsIgnoreCase("group")) {
            fieldDTO.setValue(field.getValue());
          } else {
            if (field.getValue() != null && field.getValue().startsWith("xmlWysiwygField")) {
              String wysiwygFile = field.getValue().substring(field.getValue().indexOf('_') + 1);
              try {
                String path = FileRepositoryManager.getAbsolutePath(f.getComponentInstanceId()) +
                        "xmlWysiwyg" + File.separator + wysiwygFile;
                fieldDTO.setValue(new String(Files.readAllBytes(Paths.get(path))));
              } catch (Exception e) {
                SilverLogger.getLogger(this).error(e);
              }
            } else {
              fieldDTO.setValue(field.getStringValue());
            }
          }
        }
      }
    }
    fieldDTO.setType(field.getTypeName());
    fieldDTO.setName(field.getName());
    if (field.getTypeName().equalsIgnoreCase("file") && fieldDTO.getValue() != null) {
        fieldDTO.setValueId(fieldDTO.getValue());
        SimpleDocument doc = AttachmentServiceProvider.getAttachmentService().searchDocumentById(new SimpleDocumentPK(fieldDTO.getValueId(), componentId), null);
        fieldDTO.setValue(doc.getFilename());
      }


    return fieldDTO;
  }

  private String getLabel(final FormInstance f, final String name) {
    for (FieldTemplate fTemplate : f.getFormWithData().getFieldTemplates()) {
      if (fTemplate.getFieldName().equals(name)) {
        return fTemplate.getLabel(getUser().getUserPreferences().getLanguage());
      }
    }
    return "";
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("myrequests")
  public List<FormRequestDTO> getMyRequests() {
    List<FormRequestDTO> requestDTOS = new ArrayList<>();

    try {
      RequestsByStatus r = FormsOnlineService.get().getAllUserRequests(getComponentId(), getUser().getId(), null);
      for (FormInstance f : r.getAll()) {
        f = FormsOnlineService.get().loadRequest(new RequestPK(f.getId(), getComponentId()), getUser().getId());
        FormRequestDTO dto = populate(f);
        requestDTOS.add(dto);
      }

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return requestDTOS;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("saveForm/{formId}")
  public Boolean saveForm(@PathParam("formId") String formId) {
    String instanceId = getComponentId();
    List<FileItem> items = this.getHttpRequest().getFileItems();
    FormPK pk = new FormPK(formId, instanceId);
    try {
      FormsOnlineService.get().saveRequest(pk, getUser().getId(), items, false);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return Boolean.TRUE;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("formlayer/{formName}/{layerType}")
  public FormLayerDTO getFormLayer(@PathParam("formName") String formName, @PathParam("layerType") String layerType) {
    FormLayerDTO dto = new FormLayerDTO();
    String html = "";
    try {
      PublicationTemplate template = getPublicationTemplate(formName, true);
      String spHome = System.getenv("SILVERPEAS_HOME");
      if (layerType.equalsIgnoreCase("view")) {
        String file = ((PublicationTemplateImpl) template).getViewFileName();
        file = spHome + "/data/templateRepository/" + file;
        html = Files.readString(java.nio.file.Path.of(file));
        Form formUpdate = getEmptyForm(template);
        for (FieldTemplate field : formUpdate.getFieldTemplates()) {
          String fieldName = field.getFieldName();
          html = html.replaceFirst("<%="+fieldName+".label%>", field.getLabel(getMainSessionController()
                  .getCurrentUserDetail().getUserPreferences().getLanguage()));
        }
      } else {
        boolean layer = template.isUpdateLayerExist();
        if (layer) {
          String file = ((PublicationTemplateImpl) template).getUpdateFileName();
          file = spHome + "/data/templateRepository/" + file;
          html = Files.readString(java.nio.file.Path.of(file));
          Form formUpdate = getEmptyForm(template);
          for (FieldTemplate field : formUpdate.getFieldTemplates()) {
            String fieldName = field.getFieldName();
            String htmlField = getHtmlUpdateTagByDisplayerName(field);
            html = html.replaceFirst("<%=" + fieldName + "%>", htmlField);
            html = html.replaceFirst("<%=" + fieldName + ".label%>", field.getLabel(getMainSessionController()
                    .getCurrentUserDetail().getUserPreferences().getLanguage()));
          }
        }
      }

      ArrayList<String> scripts = new ArrayList<>();
      Elements els = Jsoup.parse(html).select("script");
      for (Element el : els) {
        String script = el.outerHtml();
        if (!script.contains("src=")) {
          script = el.html();
        }
        scripts.add(script);
      }
      dto.setHtml(html);
      dto.setScripts(scripts);
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dto;
  }
  private String getHtmlUpdateTagByDisplayerName(FieldTemplate field) {
    String htmlField = "";
    String displayerName = field.getDisplayerName();
    if (displayerName.equalsIgnoreCase("text") || displayerName.equalsIgnoreCase("simpletext")
            || displayerName.equalsIgnoreCase("map")) {
      htmlField = "<input type='text' id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\", \""+field.getFieldName()+"\");'></input>";
    } else if (displayerName.equalsIgnoreCase("textarea") || displayerName.equalsIgnoreCase("wysiwyg")) {
      htmlField = "<textarea id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\", \""+field.getFieldName()+"\");'></textarea>";
    } else if (displayerName.equalsIgnoreCase("radio")) {
      Map<String, String> params = field.getParameters(getUser().getId());
      String[] keys = params.get("keys").split("##");
      String[] values = params.get("values").split("##");
      for (int i = 0; i < values.length; i++) {
        String value = values[i];
        String key = keys[i];
        htmlField += "<input type='radio' id='" + field.getFieldName()+value + "' name='" + field.getFieldName()
                + "' value='"+key+"'"
                + "' onchange='javascript:updateModel(\""+field.getFieldName()+value+"\", \""+field.getFieldName()+"\");'>";
        htmlField += "<label for='"+field.getFieldName()+"'>"+value+"</label>";
      }
    } else if (displayerName.equalsIgnoreCase("checkbox")) {
      Map<String, String> params = field.getParameters(getUser().getId());
      String[] keys = params.get("keys").split("##");
      String[] values = params.get("values").split("##");
      for (int i = 0; i < values.length; i++) {
        String value = values[i];
        String key = keys[i];
        htmlField += "<input type='checkbox' id='" + field.getFieldName()+value + "' name='" + field.getFieldName()
                + "' value='" + key
                + "' onchange='javascript:updateModel(\""+field.getFieldName()+value+"\",\""+field.getFieldName()+"\");'>";
        htmlField += "<label for='"+field.getFieldName()+"'>"+value+"</label>";
      }
    } else if (displayerName.equalsIgnoreCase("listbox")) {
      Map<String, String> params = field.getParameters(getUser().getId());
      String[] keys = params.get("keys").split("##");
      String[] values = params.get("values").split("##");
      htmlField += "<select name='"+field.getFieldName()+"' id='"+field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\",\""+field.getFieldName()+"\");'>";
      for (int i = 0; i < values.length; i++) {
        String value = values[i];
        String key = keys[i];
        htmlField += "<option value='"+key+"'>" + value + "</option>";
      }
      htmlField += "</select>";
    } else if (displayerName.equalsIgnoreCase("file") || displayerName.equalsIgnoreCase("image")
                || displayerName.equalsIgnoreCase("video")) {
      htmlField = "<input type='file' id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\",\""+field.getFieldName()+"\");'></input>";
    }  else if (displayerName.equalsIgnoreCase("date")) {
      htmlField = "<input type='date' id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\", \""+field.getFieldName()+"\");'></input>";
    } else if (displayerName.equalsIgnoreCase("time")) {
      htmlField = "<input type='time' id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\", \""+field.getFieldName()+"\");'></input>";
    } else if (displayerName.equalsIgnoreCase("email")) {
      htmlField = "<input type='email' id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onchange='javascript:updateModel(\""+field.getFieldName()+"\", \""+field.getFieldName()+"\");'></input>";
    } else if (displayerName.equalsIgnoreCase("user") || displayerName.equalsIgnoreCase("multipleUser")
            || displayerName.equalsIgnoreCase("group")) {
      htmlField = "<textarea id='" + field.getFieldName() + "' name='" + field.getFieldName()
              + "' onclick='javascript:showUserSelection(\""+field.getFieldName()+"\",\""+field.getTypeName()+"\");'></textarea>";
    }
    return htmlField;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("form/{formName}")
  public List<FormFieldDTO> getForm(@PathParam("formName") String formName) {

    List<FormFieldDTO> fields = new ArrayList<>();

    try {
      if (NumberUtils.isDigits(formName)) {
        FormPK pk = new FormPK(formName, componentId);
        FormDetail fd = FormsOnlineService.get().loadForm(pk);
        formName = fd.getXmlFormName();
      }
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
        var values = new HashMap<String, String>();
        field.getFieldValuesTemplate(getUserPreferences().getLanguage())
                .apply(v -> values.put(v.getKey(), v.getLabel()));
        f.setValues(values);
        fields.add(f);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return fields;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("form/{formName}/{fieldName}")
  public List<BaseDTO> getUserField(@PathParam("formName") String formName, @PathParam("fieldName") String fieldName) {

    List<BaseDTO> result = new ArrayList<>();
    try {
      PublicationTemplate template = getPublicationTemplate(formName, true);
      List<FieldTemplate> fields = template.getUpdateForm().getFieldTemplates();
      for (FieldTemplate field : fields) {
        if (field.getFieldName().equals(fieldName)) {
          Map<String, String> parameters = field.getParameters(getUserPreferences().getLanguage());
          if (field.getTypeName().equalsIgnoreCase("user") || field.getTypeName().equalsIgnoreCase("multipleUser")) {
            List<String> users = new ArrayList<>();

            if (parameters.containsKey("usersOfInstanceOnly")) {
              List<ProfileInst> allProfilesInst = Administration.get().getComponentInst(getComponentId()).getAllProfilesInst();
              for (ProfileInst profileInst : allProfilesInst) {
                users.addAll(profileInst.getAllUsers());
                List<String> groupsIds = profileInst.getAllGroups();
                for (String groupId : groupsIds) {
                  users.addAll(Arrays.asList(Administration.get().getGroup(groupId).getUserIds()));
                }
              }
            } else if (parameters.containsKey("roles")) {
              List<String> roles = Arrays.asList(parameters.get("roles").split(","));
              List<ProfileInst> allProfilesInst = Administration.get().getComponentInst(getComponentId()).getAllProfilesInst();
              for (ProfileInst profileInst : allProfilesInst) {
                if (roles.contains(profileInst.getName())) {
                  users.addAll(profileInst.getAllUsers());
                  List<String> groupsIds = profileInst.getAllGroups();
                  for (String groupId : groupsIds) {
                    users.addAll(Arrays.asList(Administration.get().getGroup(groupId).getUserIds()));
                  }
                }
              }
            } else {
              users = Arrays.asList(Administration.get().getAllUsersIds());
            }

            // remove duplicate users
            users = new ArrayList<>(new HashSet<>(users));

            for (String usrId : users) {
              UserDetail usr = Administration.get().getUserDetail(usrId);
              UserDTO user = UserHelper.getInstance().populateUserDTO(usr);
              result.add(user);
            }

          } else if(field.getTypeName().equalsIgnoreCase("group")) {
            List<GroupDetail> groups = Administration.get().getAllGroups();
            for (GroupDetail gp : groups) {
              result.add(UserHelper.getInstance().populateGroupDTO(gp));
            }
          }
          break;
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return result;
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
    return this.componentId;
  }

  private RequestsFilter getRequestsFilter() {
    return new RequestsFilter(getComponentId(), isWorkgroupEnabled());
  }

  private boolean isWorkgroupEnabled() {
    return StringUtil.getBooleanValue(
        getComponentParameterValue(FormsOnlineComponentSettings.PARAM_WORKGROUP));
  }

  public String getComponentParameterValue(String parameterName) {
    return OrganizationController.get().getComponentParameterValue(getComponentId(), parameterName);
  }
}
