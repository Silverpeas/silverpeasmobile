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

package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.services.helpers.UserHelper;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.WorkflowException;
import com.silverpeas.mobile.shared.services.ServiceWorkflow;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.ProfileInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.Field;
import org.silverpeas.core.contribution.content.form.FieldTemplate;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.RecordTemplate;
import org.silverpeas.core.contribution.content.form.record.GenericDataRecord;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.workflow.api.Workflow;
import org.silverpeas.core.workflow.api.event.TaskDoneEvent;
import org.silverpeas.core.workflow.api.instance.ProcessInstance;
import org.silverpeas.core.workflow.api.model.Action;
import org.silverpeas.core.workflow.api.model.Input;
import org.silverpeas.core.workflow.api.model.ProcessModel;
import org.silverpeas.core.workflow.api.model.Role;
import org.silverpeas.core.workflow.api.task.Task;
import org.silverpeas.core.workflow.api.user.User;
import org.silverpeas.core.workflow.engine.user.UserImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Service de gestion des workflows.
 * @author svuillet
 */
public class ServiceWorkflowImpl extends AbstractAuthenticateService implements ServiceWorkflow {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();

  static {
    SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
  }

  @Override
  public void processAction(List<WorkflowFieldDTO> data, String instanceId, String actionName, String role, String state, String processId) throws WorkflowException, AuthenticationException {
    checkUserInSession();
    try {
      ProcessModel model = Workflow.getProcessModelManager().getProcessModel(instanceId);
      String kind = "";
      if (actionName.equalsIgnoreCase("create")) {
        kind = model.getCreateAction(role).getKind();
      } else {
        kind = model.getAction(actionName).getKind();
      }

      Action action = null;
      TaskDoneEvent event = null;
      if (kind.equals("create")) {
        action = model.getCreateAction(role);
        GenericDataRecord record = getGenericDataRecord(data, role, model, action);
        event = getCreationTask(model, getUserInSession().getId(), role).buildTaskDoneEvent(action.getName(), record);
      } else {
        ProcessInstance processInstance = Workflow.getProcessInstanceManager().getProcessInstance(processId);
        //User user = new UserImpl(UserDetail.getById(getUserInSession().getId()));
        //processInstance.lock(model.getState(state), user);
        action = model.getAction(actionName);
        GenericDataRecord record = getGenericDataRecord(data, role, model, action);
        event = getTask(model, processInstance, getUserInSession().getId(), role, state).buildTaskDoneEvent(action.getName(), record);
      }
      Workflow.getWorkflowEngine().process(event, true); //TODO : how to persiste lock on instance
      Thread.sleep(1000); // Wait task creation

      } catch (Exception e) {
      throw  new WorkflowException(e);
    }

  }

  private GenericDataRecord getGenericDataRecord(final List<WorkflowFieldDTO> data, final String role, final ProcessModel model, final Action action)  throws Exception {
    Form form = model.getPublicationForm(action.getName(), role, getUserInSession().getUserPreferences().getLanguage());
    GenericDataRecord record = (GenericDataRecord) model.getNewActionRecord(action.getName(), role, getUserInSession().getUserPreferences().getLanguage(), null);
    for (WorkflowFieldDTO f : data) {
      Field field = record.getField(f.getName());
      if (f.getValue() == null) {
        field.setNull();
      } else {
        if (field.getTypeName().equalsIgnoreCase("user")) {
          UserDetail u = Administration.get().getUserDetail(f.getValue());
          field.setObjectValue(u);
        } else if (field.getTypeName().equalsIgnoreCase("group")) {
          GroupDetail g = Administration.get().getGroup(f.getValue());
          field.setObjectValue(g);
        } else if (field.getTypeName().equalsIgnoreCase("multipleUser")) {
          StringTokenizer stk = new StringTokenizer(f.getValue(), ",");
          UserDetail[] users = new UserDetail[stk.countTokens()];
          int i = 0;
          while (stk.hasMoreTokens()) {
            String id = stk.nextToken();
            UserDetail u = Administration.get().getUserDetail(id);
            users[i] = u;
            i++;
          }
          field.setObjectValue(users);
        } else if (field.getTypeName().equalsIgnoreCase("date")) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date d = sdf.parse(f.getValue());
          field.setValue(DateUtil.formatDate(d));
        } else {
          field.setValue(f.getValue());
        }
      }
    }
    return record;
  }

  private Task getCreationTask(ProcessModel processModel, String userId, String currentRole)  throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task creationTask = Workflow.getTaskManager().getCreationTask(user, currentRole, processModel);
    return creationTask;
  }

  private Task getTask(ProcessModel processModel, ProcessInstance processInstance, String userId, String currentRole, String stateName) throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task[] tasks = Workflow.getTaskManager().getTasks(user, currentRole, processInstance);
    for (final Task task : tasks) {
      if (task.getState().getName().equals(stateName)) {
        return task;
      }
    }
    return null;
  }

  private List<GroupDetail> getGroups(List<String> ids) throws Exception {
    ArrayList<GroupDetail> groups = new ArrayList<GroupDetail>();
    for (String id : ids) {
      groups.add(Administration.get().getGroup(id));
    }
    return groups;
  }

  @Override
  public List<BaseDTO> getUserField(String instanceId, String fieldName, String role) throws WorkflowException, AuthenticationException {
    checkUserInSession();
    HashSet<BaseDTO> result = new HashSet<BaseDTO>();
    try {
      boolean group = false;
      ProcessModel model = Workflow.getProcessModelManager().getProcessModel(instanceId);
      org.silverpeas.core.workflow.api.model.Form form = model.getCreateAction(role).getForm();
      Map<String,String> parameters = null;
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
      List<ProfileInst> profiles = Administration.get().getComponentInst(instanceId).getProfiles();

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
      throw  new WorkflowException(e);
    }

    return new ArrayList<BaseDTO>(result);
  }

  @Override
  public WorkflowInstancesDTO getInstances(String instanceId, String userRole) throws WorkflowException, AuthenticationException {
    checkUserInSession();
    WorkflowInstancesDTO data = new WorkflowInstancesDTO();
    try {
      ArrayList<WorkflowInstanceDTO> instances = new ArrayList<WorkflowInstanceDTO>();
      User user = Workflow.getUserManager().getUser(getUserInSession().getId());
      Role[] roles = Workflow.getProcessModelManager().getProcessModel(instanceId).getRoles();
      ProcessInstance[] processInstances = Workflow.getProcessInstanceManager().getProcessInstances(instanceId, user, roles[0].getName());

      if (userRole == null) {
        userRole = roles[0].getName();
      }

      for (ProcessInstance processInstance : processInstances) {
        instances.add(populate(processInstance, userRole));
      }
      data.setInstances(instances);

      TreeMap<String,String> r = new TreeMap<String, String>();
      for (Role role : roles) {
        r.put(role.getName(), role.getLabel(role.getName(), getUserInSession().getUserPreferences().getLanguage()));
      }
      data.setRoles(r);
      data.setRolesAllowedToCreate(new ArrayList(
          Arrays.asList(Workflow.getProcessModelManager().getProcessModel(instanceId).getCreationRoles())));
      data.setHeaderLabels(getHeaderLabels(instanceId, userRole));
    } catch (Exception e) {
      throw  new WorkflowException(e);
    }
    return data;
  }

  @Override
  public WorkflowInstancePresentationFormDTO getPresentationForm(String instanceId, String role) throws WorkflowException, AuthenticationException {
    checkUserInSession();
    WorkflowInstancePresentationFormDTO dto = new WorkflowInstancePresentationFormDTO();
    Map<String, String> map = new TreeMap<String, String>();
    Map<String, String> mapActions = new TreeMap<String, String>();
    try {
      ProcessInstance instance = Workflow.getProcessInstanceManager().getProcessInstance(instanceId);
      Form form = instance.getProcessModel().getPresentationForm("presentationForm", role, getUserInSession().getUserPreferences().getLanguage());
      DataRecord data = instance.getFormRecord("presentationForm", role, getUserInSession().getUserPreferences().getLanguage());

      Task[] tasks = Workflow.getTaskManager().getTasks(Workflow.getUserManager().getUser(getUserInSession().getId()),role, instance);

      for (Task task : tasks) {
        for(String actionName : task.getActionNames()) {
          String label = instance.getProcessModel().getAction(actionName).getLabel(role, getUserInSession().getUserPreferences().getLanguage());
          mapActions.put(actionName, label);
          dto.setState(task.getState().getName());
        }
      }
      for (FieldTemplate ft : form.getFieldTemplates()) {
        String label = ft.getLabel(getUserInSession().getUserPreferences().getLanguage());
        String value = data.getField(ft.getFieldName()).getStringValue();
        if (ft.getTypeName().equalsIgnoreCase("user")) {
          UserDetail u = Administration.get().getUserDetail(value);
          value = u.getDisplayedName();
        } else if (ft.getTypeName().equalsIgnoreCase("multipleUser")) {
          StringTokenizer stk = new StringTokenizer(value, ",");
          value = "";
          while(stk.hasMoreTokens()) {
            String id = stk.nextToken();
            UserDetail u = Administration.get().getUserDetail(id);
            value += u.getDisplayedName() + ", ";
          }
          if (!value.isEmpty()) value = value.substring(0, value.length()-2);
        } else if(ft.getTypeName().equalsIgnoreCase("group")) {
          GroupDetail g = Administration.get().getGroup(value);
          value = g.getName();
        }
        map.put(label, value);
      }
      dto.setTitle(form.getTitle());
    } catch (Exception e) {
      throw  new WorkflowException(e);
    }
    dto.setInstanceId(instanceId);
    dto.setActions(mapActions);
    dto.setFields(map);

    return dto;
  }

  @Override
  public WorkflowFormActionDTO getActionForm(String instanceId, String role, String action) throws WorkflowException, AuthenticationException {
    checkUserInSession();
    WorkflowFormActionDTO dto = new WorkflowFormActionDTO();
    try {
      org.silverpeas.core.workflow.api.model.Form form = null;
      if (action.equals("create")) {
        ProcessModel model = Workflow.getProcessModelManager().getProcessModel(instanceId);
        form = model.getCreateAction(role).getForm();
      } else {
        ProcessInstance instance = Workflow.getProcessInstanceManager().getProcessInstance(instanceId);
        form = instance.getProcessModel().getActionForm(action);
      }
      //DataRecord data = instance.getFolder();
      for (Input input : form.getInputs()) {
        WorkflowFieldDTO fdto = new WorkflowFieldDTO();
        fdto.setId(instanceId);
        fdto.setDisplayerName(input.getDisplayerName());
        fdto.setMandatory(input.isMandatory());
        fdto.setReadOnly(input.isReadonly());
        fdto.setName(input.getItem().getName());
        fdto.setLabel(input.getItem().getLabel(role, getUserInSession().getUserPreferences().getLanguage()));
        fdto.setValue(input.getValue());
        fdto.setType(input.getItem().getType());
        fdto.setValues(input.getItem().getKeyValuePairs());
        dto.addField(fdto);
      }
      dto.setTitle(form.getTitle(role, getUserInSession().getUserPreferences().getLanguage()));
      dto.setId(instanceId);

    } catch (Exception e) {
      throw  new WorkflowException(e);
    }
    return dto;
  }

  private List<String> getHeaderLabels(String modelId, String role) throws Exception {
    ArrayList<String> labels = new ArrayList<String>();
    RecordTemplate template = Workflow.getProcessModelManager().getProcessModel(modelId).getRowTemplate(role, getUserInSession().getUserPreferences().getLanguage());
    FieldTemplate[] headers = template.getFieldTemplates();
    for (FieldTemplate header : headers) {
      labels.add(header.getLabel());
    }
    return labels;
  }

  private WorkflowInstanceDTO populate(ProcessInstance processInstance, String role) throws Exception {
    WorkflowInstanceDTO dto = new WorkflowInstanceDTO();
    dto.setId(processInstance.getInstanceId());
    String title = processInstance.getTitle(role, getUserInSession().getUserPreferences().getLanguage());
    dto.setTitle(title);
    dto.setState(processInstance.getActiveStates().toString());

    RecordTemplate template = Workflow.getProcessModelManager().getProcessModel(processInstance.getModelId()).getRowTemplate(role, getUserInSession().getUserPreferences().getLanguage());
    FieldTemplate[] headers = template.getFieldTemplates();

    for (FieldTemplate header : headers) {
      String value = processInstance
          .getRowDataRecord(role, getUserInSession().getUserPreferences().getLanguage()).getField(header.getFieldName()).getStringValue();
      dto.addHeaderField(value);
    }

    return dto;
  }

}
