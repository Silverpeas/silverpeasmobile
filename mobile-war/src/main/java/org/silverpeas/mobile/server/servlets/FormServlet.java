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

package org.silverpeas.mobile.server.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.attachment.AttachmentService;
import org.silverpeas.core.contribution.attachment.model.SimpleAttachment;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.Field;
import org.silverpeas.core.contribution.content.form.field.FileField;
import org.silverpeas.core.contribution.content.form.record.GenericDataRecord;
import org.silverpeas.core.util.Charsets;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.file.FileItem;
import org.silverpeas.core.util.file.FileUploadSizeLimitException;
import org.silverpeas.core.util.file.FileUploadUtil;
import org.silverpeas.core.workflow.api.ProcessInstanceManager;
import org.silverpeas.core.workflow.api.TaskManager;
import org.silverpeas.core.workflow.api.Workflow;
import org.silverpeas.core.workflow.api.WorkflowEngine;
import org.silverpeas.core.workflow.api.event.TaskDoneEvent;
import org.silverpeas.core.workflow.api.instance.ProcessInstance;
import org.silverpeas.core.workflow.api.model.Action;
import org.silverpeas.core.workflow.api.model.ProcessModel;
import org.silverpeas.core.workflow.api.task.Task;
import org.silverpeas.core.workflow.api.user.User;
import org.silverpeas.core.workflow.engine.user.UserImpl;
import org.silverpeas.mobile.server.helpers.MediaHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class FormServlet extends AbstractSilverpeasMobileServlet {

  @Inject
  private Administration admin;

  @Inject
  private AttachmentService attachmentService;

  @Inject
  WorkflowEngine workflowEngine;

  @Inject
  ProcessInstanceManager instanceManager;

  @Inject
  private TaskManager taskManager;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    try {
      String instanceId = null;
      String currentAction = null;
      String currentRole = null;
      String currentState = null;
      String processId = null;
      HashMap<String, Object> fields = new HashMap<>();
      String tempDir = MediaHelper.getTemporaryUploadMediaPath();

      // Parse the request
      List<FileItem> items = FileUploadUtil.parseRequest(request);

      // Process the uploaded items
      for (FileItem item : items) {
        if (item.isFormField()) {
          if (item.getFieldName().equals("instanceId")) {
            instanceId = item.getContent(Charsets.UTF_8);
          } else if (item.getFieldName().equals("currentAction")) {
            currentAction = item.getContent(Charsets.UTF_8);
          } else if (item.getFieldName().equals("currentRole")) {
            currentRole = item.getContent(Charsets.UTF_8);
          } else if (item.getFieldName().equals("currentState")) {
            currentState = item.getContent(Charsets.UTF_8);
          } else if (item.getFieldName().equals("processId")) {
            processId = item.getContent(Charsets.UTF_8);
          } else {
            fields.put(item.getFieldName(), item.getContent(Charsets.UTF_8));
          }
        } else {
          String fileName = item.getFileName();
          File file = new File(tempDir + File.separator + fileName);
          item.saveTo(file);
          fields.put(item.getFieldName(), file);
        }
      }
      processAction(request, response, fields, instanceId, currentAction, currentRole, currentState,
          processId);
    } catch (FileUploadSizeLimitException e) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void processAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> data,
      String instanceId, String actionName, String role, String state, String processId)
      throws Exception {
    checkUserInSession(request, response);

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
      GenericDataRecord record = getGenericDataRecord(request, data, role, model, action);
      event = getCreationTask(model, getUserInSession(request).getId(), role)
          .buildTaskDoneEvent(action.getName(), record);
    } else {
      ProcessInstance processInstance = instanceManager.getProcessInstance(processId);
      action = model.getAction(actionName);
      GenericDataRecord record = getGenericDataRecord(request, data, role, model, action);
      event = getTask(model, processInstance, getUserInSession(request).getId(), role, state)
          .buildTaskDoneEvent(action.getName(), record);
    }
    workflowEngine.process(event, true);
    Thread.sleep(1000); // Wait task creation


  }

  private Task getCreationTask(ProcessModel processModel, String userId, String currentRole)
      throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task creationTask = taskManager.getCreationTask(user, currentRole, processModel);
    return creationTask;
  }

  private Task getTask(ProcessModel processModel, ProcessInstance processInstance, String userId,
      String currentRole, String stateName) throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task[] tasks = taskManager.getTasks(user, currentRole, processInstance);
    for (final Task task : tasks) {
      if (task.getState().getName().equals(stateName)) {
        return task;
      }
    }
    return null;
  }

  private GenericDataRecord getGenericDataRecord(HttpServletRequest request,
      final Map<String, Object> data, final String role, final ProcessModel model,
      final Action action) throws Exception {
    GenericDataRecord record = (GenericDataRecord) model.getNewActionRecord(action.getName(), role,
        getUserInSession(request).getUserPreferences().getLanguage(), null);
    for (Map.Entry<String, Object> f : data.entrySet()) {
      Field field = record.getField(f.getKey());
      if (f.getValue() == null || f.getValue().equals("null")) {
        if (field.getTypeName().equalsIgnoreCase("file") && field.getValue() != null) {
          //TODO : make work delete file
          SimpleDocument doc = attachmentService.searchDocumentById(new SimpleDocumentPK(field.getValue()),
              getUserInSession(request).getUserPreferences().getLanguage());
          attachmentService.deleteAttachment(doc);
        }
        field.setNull();
      } else {
        if (field.getTypeName().equalsIgnoreCase("user")) {
          UserDetail u = admin.getUserDetail((String) f.getValue());
          field.setObjectValue(u);
        } else if (field.getTypeName().equalsIgnoreCase("group")) {
          GroupDetail g = admin.getGroup((String) f.getValue());
          field.setObjectValue(g);
        } else if (field.getTypeName().equalsIgnoreCase("multipleUser")) {
          StringTokenizer stk = new StringTokenizer((String) f.getValue(), ",");
          UserDetail[] users = new UserDetail[stk.countTokens()];
          int i = 0;
          while (stk.hasMoreTokens()) {
            String id = stk.nextToken().trim();
            UserDetail u = admin.getUserDetail(id);
            users[i] = u;
            i++;
          }
          field.setObjectValue(users);
        } else if (field.getTypeName().equalsIgnoreCase("date")) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date d = sdf.parse((String) f.getValue());
          field.setValue(DateUtil.formatDate(d));
        } else if (field.getTypeName().equalsIgnoreCase("wysiwyg")) {
          //TODO
          //WysiwygController.createFileAndAttachment();
          //WysiwygController.updateFileAndAttachment();
          //form.updateWysiwyg()
        } else if (field.getTypeName().equalsIgnoreCase("file")) {
          if (f.getValue() != null && !f.getValue().equals("null")) {
            File file = (File) f.getValue();
            SimpleDocumentPK simpleDocPk = new SimpleDocumentPK(null, model.getModelId());
            Path source = Paths.get(file.toURI());
            String mimeType = Files.probeContentType(source);
            String foreignId = model.getModelId();
            final UserDetail userInSession = getUserInSession(request);
            final String userLanguage = userInSession.getUserPreferences().getLanguage();
            final SimpleAttachment attachment =SimpleAttachment.builder(userLanguage)
                .setFilename(file.getName())
                .setTitle(file.getName())
                .setSize(file.length())
                .setContentType(mimeType)
                .setCreationData(userInSession.getId(), new Date())
                .build();
            SimpleDocument doc = new SimpleDocument(simpleDocPk, foreignId, 0, false,
                userInSession.getId(), attachment);
            doc = attachmentService.createAttachment(doc, file);
            ((FileField) field).setAttachmentId(doc.getId());
          }
        } else {
          field.setValue((String) f.getValue());
        }
      }
    }
    return record;
  }
}
