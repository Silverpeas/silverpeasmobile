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

import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.ProfileInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.Field;
import org.silverpeas.core.contribution.content.form.FieldTemplate;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.RecordTemplate;
import org.silverpeas.core.contribution.content.form.field.MultipleUserField;
import org.silverpeas.core.contribution.content.form.record.GenericDataRecord;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.workflow.api.event.TaskDoneEvent;
import org.silverpeas.core.workflow.api.model.Action;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.core.workflow.api.Workflow;
import org.silverpeas.core.workflow.api.instance.ProcessInstance;
import org.silverpeas.core.workflow.api.model.Input;
import org.silverpeas.core.workflow.api.model.ProcessModel;
import org.silverpeas.core.workflow.api.model.Role;
import org.silverpeas.core.workflow.api.task.Task;
import org.silverpeas.core.workflow.api.user.User;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.workflow.FieldPresentationDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowDataDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Service de gestion des workflows.
 *
 * @author svuillet
 */
@WebService
@Authorized
@Path(ServiceWorkflow.PATH + "/{appId}")
public class ServiceWorkflow extends AbstractRestWebService {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();

  static final String PATH = "mobile/workflow";

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  private List<GroupDetail> getGroups(List<String> ids) throws Exception {
    ArrayList<GroupDetail> groups = new ArrayList<GroupDetail>();
    for (String id : ids) {
      groups.add(Administration.get().getGroup(id));
    }
    return groups;
  }

  private Map<String, String> getWorkflowUserRoles() throws Exception {
    String[] roles = getOrganisationController().getUserProfiles(getUser().getId(), getComponentId());
    TreeMap<String, String> userRoles = new TreeMap<String, String>();
    Role[] allRoles = Workflow.getProcessModelManager().getProcessModel(getComponentId()).getRoles();

    for (String role : roles) {
      for (Role r : allRoles) {
        if (r.getName().equals(role)){
          userRoles.put(r.getName(), r.getLabel(r.getName(), getUser().getUserPreferences().getLanguage()));
          break;
        }
      }
    }

    return userRoles;
  }

  private Map<String, List<String>> getHeaderLabels(Map<String, String> roles) throws Exception {

    Map<String, List<String>> headerLabels = new TreeMap<>();
    for (Map.Entry<String, String> role : roles.entrySet()) {
      headerLabels.put(role.getKey(), getHeaderLabels(getComponentId(), role.getKey()));
    }
    return headerLabels;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("userField/{instanceId}/{actionName}/{fieldName}/{role}")
  public List<BaseDTO> getUserField(@PathParam("instanceId") String instanceId, @PathParam("actionName") String actionName,
      @PathParam("fieldName") String fieldName, @PathParam("role") String role) throws Exception {
    HashSet<BaseDTO> result = new HashSet<BaseDTO>();
    try {
      boolean group = false;
      ProcessModel model = Workflow.getProcessModelManager().getProcessModel(instanceId);

      org.silverpeas.core.workflow.api.model.Form form = null;
      if (actionName.equals("create")) {
        form = model.getCreateAction(role).getForm();
      } else {
        form = model.getAction(actionName).getForm();
      }
      Map<String, String> parameters = null;
      for (Input input : form.getInputs()) {
        if (input.getItem().getName().equals(fieldName)) {
          parameters = input.getItem().getKeyValuePairs();
          group = input.getItem().getType().equalsIgnoreCase("group");
          break;
        }
      }
      ArrayList<String> users = new ArrayList<String>();
      ArrayList<GroupDetail> groups = new ArrayList<GroupDetail>();
      String roles = parameters.get("roles");
      List<ProfileInst> profiles =
          Administration.get().getComponentInst(getComponentId()).getProfiles();

      if (group) {
        if (roles == null || roles.isEmpty()) {
          groups.addAll(Administration.get().getAllGroups());
        } else {
          StringTokenizer stk = new StringTokenizer(roles, ",");
          while (stk.hasMoreTokens()) {
            String r = stk.nextToken();
            for (ProfileInst profil : profiles) {
              if (profil.getName().equals(r)) {
                groups.addAll(getGroups(profil.getAllGroups()));
              }
            }
          }
        }

        //populate groups
        for (GroupDetail gp : groups) {
          result.add(UserHelper.getInstance().populateGroupDTO(gp));
        }

      } else {
        if (roles == null || roles.isEmpty()) {
          users.addAll(Arrays.asList(Administration.get().getAllUsersIds()));
        } else {
          StringTokenizer stk = new StringTokenizer(roles, ",");
          while (stk.hasMoreTokens()) {
            String r = stk.nextToken();
            for (ProfileInst profil : profiles) {
              if (profil.getName().equals(r)) {
                users.addAll(profil.getAllUsers());
              }
            }
          }
        }

        // populate users
        for (String id : users) {
          UserDetail u = Administration.get().getUserDetail(id);
          result.add(UserHelper.getInstance().populateUserDTO(u));
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }

    return new ArrayList<BaseDTO>(result);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("instances/{role}/{callNumber}")
  public StreamingList<WorkflowInstanceDTO> getWorkflowInstances(@PathParam("role") String userRole, @PathParam("callNumber") int callNumber) throws Exception {
    int callSize = 25;

    User user = Workflow.getUserManager().getUser(getUser().getId());

    List<ProcessInstance> list = (List<ProcessInstance>) request.getSession().getAttribute("Cache_processInstances_"+userRole);
    if (list == null) {
      list =
          Workflow.getProcessInstanceManager().getProcessInstances(getComponentId(), user, userRole);
      request.getSession().setAttribute("Cache_processInstances_"+userRole, list);
    }

    Role[] roles = Workflow.getProcessModelManager().getProcessModel(getComponentId()).getRoles();
    if (userRole == null || userRole.isEmpty()) {
      userRole = roles[0].getName();
    }


    int calledSize = 0;
    boolean moreElements = true;
    if (callNumber > 0) calledSize = callSize * callNumber;

    if ((calledSize + callSize) >= list.size()) {
      moreElements = false;
      callSize = list.size() - calledSize;
    }

    ArrayList<WorkflowInstanceDTO> instances = new ArrayList<WorkflowInstanceDTO>();
    for (ProcessInstance processInstance : list.subList(calledSize, calledSize + callSize)) {
      instances.add(populate(processInstance, userRole));
    }

    StreamingList<WorkflowInstanceDTO> streamingList = new StreamingList<WorkflowInstanceDTO>(instances, moreElements);
    if (!streamingList.getMoreElement()) getHttpRequest().getSession().removeAttribute("Cache_processInstances_"+userRole);

    return streamingList;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("datainstances/{role}")
  public WorkflowDataDTO getDataInstances(@PathParam("role") String userRole) throws Exception {
    WorkflowDataDTO data = new WorkflowDataDTO();
    try {
      if (userRole.equals("null")) userRole = null;
      Role[] roles = Workflow.getProcessModelManager().getProcessModel(getComponentId()).getRoles();
      if (userRole == null || userRole.isEmpty()) {
        userRole = roles[0].getName();
      }

      data.setRoles(getWorkflowUserRoles());
      data.setRolesAllowedToCreate(new ArrayList(Arrays.asList(
          Workflow.getProcessModelManager().getProcessModel(getComponentId()).getCreationRoles())));
      data.setHeaderLabels(getHeaderLabels(data.getRoles()));

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return data;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("presentationForm/{instanceId}/{role}")
  public WorkflowInstancePresentationFormDTO getPresentationForm(@PathParam("instanceId") String instanceId, @PathParam("role") String role)
      throws Exception {
    WorkflowInstancePresentationFormDTO dto = new WorkflowInstancePresentationFormDTO();
    List<FieldPresentationDTO> fields = new ArrayList<FieldPresentationDTO>();
    Map<String, String> mapActions = new TreeMap<String, String>();
    try {
      ProcessInstance instance =
          Workflow.getProcessInstanceManager().getProcessInstance(instanceId);
      Form form = instance.getProcessModel().getPresentationForm("presentationForm", role,
          getUser().getUserPreferences().getLanguage());
      dto.setId(instance.getInstanceId());
      DataRecord data = instance.getFormRecord("presentationForm", role,
          getUser().getUserPreferences().getLanguage());

      Task[] tasks = Workflow.getTaskManager()
          .getTasks(Workflow.getUserManager().getUser(getUser().getId()), role, instance);

      for (Task task : tasks) {
        for (String actionName : task.getActionNames()) {
          String label = instance.getProcessModel().getAction(actionName)
              .getLabel(role, getUser().getUserPreferences().getLanguage());
          mapActions.put(actionName, label);
          dto.setState(task.getState().getName());
        }
      }
      for (FieldTemplate ft : form.getFieldTemplates()) {
        String label = ft.getLabel(getUser().getUserPreferences().getLanguage());
        String value = data.getField(ft.getFieldName()).getStringValue();
        String id = "";
        String type = ft.getTypeName();
        String displayerName = ft.getDisplayerName();
        value = getDisplayValue(ft, value, instance);

        if (type.equalsIgnoreCase("user")) {
          if (value.isEmpty()) {
            value = null;
          } else {
            UserDetail u = Administration.get().getUserDetail(value);
            value = u.getDisplayedName();
          }
        } else if (type.equalsIgnoreCase("multipleUser")) {
          StringTokenizer stk = new StringTokenizer(value, ",");
          value = "";
          while (stk.hasMoreTokens()) {
            String idUser = stk.nextToken();
            UserDetail u = Administration.get().getUserDetail(idUser);
            value += u.getDisplayedName() + ", ";
          }
          if (!value.isEmpty()) {
            value = value.substring(0, value.length() - 2);
          }
        } else if (type.equalsIgnoreCase("group")) {
          GroupDetail g = Administration.get().getGroup(value);
          value = g.getName();
        } else if (type.equalsIgnoreCase("date")) {
          value =
              DateUtil.getInputDate(value, getUser().getUserPreferences().getLanguage());
        } else if (type.equalsIgnoreCase("file")) {
          if (value != null) {
            SimpleDocument doc = AttachmentServiceProvider.getAttachmentService()
                .searchDocumentById(new SimpleDocumentPK(value),
                    getUser().getUserPreferences().getLanguage());
            value = doc.getTitle();
            id = doc.getId();
          }
        }
        if (value != null) {
          fields.add(new FieldPresentationDTO(label, value, id, type, displayerName));
        }
      }
      dto.setTitle(form.getTitle());
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    dto.setInstanceId(getComponentId());
    dto.setActions(mapActions);
    dto.setFields(fields);

    return dto;
  }

  private String getDisplayValue(final FieldTemplate ft, String value, ProcessInstance instance) {
    Map<String, String> params =
        ft.getParameters(getUser().getUserPreferences().getLanguage());
    String keys = params.get("keys");
    String values = params.get("values");
    if (keys != null && values != null) {
      String[] k = keys.split("##");
      String[] v = values.split("##");
      for (int i = 0; i < k.length; i++) {
        if (k[i].equals(value)) {
          return v[i];
        }
      }
    }

    if (value != null && value.startsWith("xmlWysiwygField")) {
      String wysiwygFile = value.substring(value.indexOf('_') + 1);
      try {
        String path = FileRepositoryManager.getAbsolutePath(instance.getModelId()) + "xmlWysiwyg" +
            File.separator + wysiwygFile;
        value = new String(Files.readAllBytes(Paths.get(path)));
      } catch (Exception e) {
        SilverLogger.getLogger(this).error(e);
      }
    }

    return value;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("actionForm/{instanceId}/{role}/{action}")
  public WorkflowFormActionDTO getActionForm(@PathParam("instanceId") String instanceId, @PathParam("role") String role, @PathParam("action") String action)
      throws Exception {
    WorkflowFormActionDTO dto = new WorkflowFormActionDTO();
    try {
      org.silverpeas.core.workflow.api.model.Form form = null;
      DataRecord data = null;
      if (action.equals("create")) {
        ProcessModel model = Workflow.getProcessModelManager().getProcessModel(getComponentId());
        form = model.getCreateAction(role).getForm();
        dto.setId(model.getModelId());
      } else {
        ProcessInstance instance =
            Workflow.getProcessInstanceManager().getProcessInstance(instanceId);
        form = instance.getProcessModel().getActionForm(action);
        data = instance.getFolder();
        dto.setId(instance.getInstanceId());
      }
      if (form != null) {
        for (Input input : form.getInputs()) {
          WorkflowFieldDTO fdto = new WorkflowFieldDTO();
          fdto.setId(getComponentId());
          fdto.setDisplayerName(input.getDisplayerName());
          fdto.setMandatory(input.isMandatory());
          fdto.setReadOnly(input.isReadonly());
          fdto.setName(input.getItem().getName());
          fdto.setActionName(action);
          fdto.setLabel(
                  input.getItem().getLabel(role, getUser().getUserPreferences().getLanguage()));
          fdto.setValue(input.getValue());
          if (!action.equals("create")) {
            Field f = data.getField(input.getItem().getName());
            if (f.getValue() != null && !f.getValue().isEmpty()) {
              if (input.getItem().getType().equalsIgnoreCase("date")) {
                fdto.setValue(f.getValue().replaceAll("/", "-"));
                //TODO : user, multipleUser, group data
              } else if (input.getItem().getType().equalsIgnoreCase("user")) {
                UserDetail u = (UserDetail) f.getObjectValue();
                fdto.setValueId(u.getId());
                fdto.setValue(f.getValue());
              } else if (input.getItem().getType().equalsIgnoreCase("multipleUser")) {
                String[] usersId = ((MultipleUserField) f).getUserIds();
                if (usersId.length > 0) {
                  String ids = Arrays.toString(usersId);
                  ids = ids.substring(1, ids.length() - 1);
                  fdto.setValueId(ids);
                  String value = "";
                  for (String id : usersId) {
                    value += Administration.get().getUserDetail(id).getDisplayedName() + ",";
                  }
                  value = value.substring(0, value.length() - 1);
                  fdto.setValue(value);
                }
              } else if (input.getItem().getType().equalsIgnoreCase("group")) {
                GroupDetail g = (GroupDetail) f.getObjectValue();
                fdto.setValueId(g.getId());
                fdto.setValue(f.getValue());
              } else if (input.getItem().getType().equalsIgnoreCase("file")) {
                SimpleDocument doc = AttachmentServiceProvider.getAttachmentService()
                        .searchDocumentById(new SimpleDocumentPK(f.getValue()),
                                getUser().getUserPreferences().getLanguage());
                fdto.setValue(doc.getTitle());
                fdto.setValueId(doc.getId());
              } else {
                fdto.setValue(f.getValue());
              }
            }
          }
          fdto.setType(input.getItem().getType());
          fdto.setValues(input.getItem().getKeyValuePairs());
          dto.addField(fdto);
        }
        dto.setTitle(form.getTitle(role, getUser().getUserPreferences().getLanguage()));
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }
    return dto;
  }

  private List<String> getHeaderLabels(String modelId, String role) throws Exception {
    ArrayList<String> labels = new ArrayList<String>();
    RecordTemplate template = Workflow.getProcessModelManager().getProcessModel(modelId)
        .getRowTemplate(role, getUser().getUserPreferences().getLanguage());
    FieldTemplate[] headers = template.getFieldTemplates();
    for (FieldTemplate header : headers) {
      labels.add(header.getLabel());
    }
    return labels;
  }

  private WorkflowInstanceDTO populate(ProcessInstance processInstance, String role)
      throws Exception {
    WorkflowInstanceDTO dto = new WorkflowInstanceDTO();
    dto.setId(processInstance.getInstanceId());
    String title =
        processInstance.getTitle(role, getUser().getUserPreferences().getLanguage());
    dto.setTitle(title);
    dto.setState(processInstance.getActiveStates().toString());

    RecordTemplate template =
        Workflow.getProcessModelManager().getProcessModel(processInstance.getModelId())
            .getRowTemplate(role, getUser().getUserPreferences().getLanguage());
    FieldTemplate[] headers = template.getFieldTemplates();

    for (FieldTemplate header : headers) {
      Field fd = processInstance.getRowDataRecord(role,
          getUser().getUserPreferences().getLanguage()).getField(header.getFieldName());
      String value = getDisplayValue(header, fd.getStringValue(), processInstance);
      dto.addHeaderField(value);
    }

    return dto;
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
