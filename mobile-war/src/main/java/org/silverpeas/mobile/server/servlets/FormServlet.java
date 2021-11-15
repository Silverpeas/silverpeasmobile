/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleAttachment;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.Field;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.field.FileField;
import org.silverpeas.core.contribution.content.form.record.GenericDataRecord;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.workflow.api.Workflow;
import org.silverpeas.core.workflow.api.event.TaskDoneEvent;
import org.silverpeas.core.workflow.api.instance.ProcessInstance;
import org.silverpeas.core.workflow.api.model.Action;
import org.silverpeas.core.workflow.api.model.ProcessModel;
import org.silverpeas.core.workflow.api.task.Task;
import org.silverpeas.core.workflow.api.user.User;
import org.silverpeas.core.workflow.engine.user.UserImpl;
import org.silverpeas.mobile.server.helpers.MediaHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class FormServlet extends AbstractSilverpeasMobileServlet {

  private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
  private static long MAX_FILE_SIZE = 1024 * 1024 * 100; // 100MB
  private static long MAX_REQUEST_SIZE = 1024 * 1024 * 110; // 110MB

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    MAX_FILE_SIZE = FileRepositoryManager.getUploadMaximumFileSize();
    MAX_REQUEST_SIZE = (long) (MAX_FILE_SIZE * 1.1);
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    try {
      String instanceId = null;
      String currentAction = null;
      String currentRole = null;
      String currentState = null;
      String processId = null;
      HashMap<String, Object> fields = new HashMap<String, Object>();
      String charset = "UTF-8";
      String tempDir = MediaHelper.getTemporaryUploadMediaPath();

      // configures upload settings
      DiskFileItemFactory factory = new DiskFileItemFactory();
      // sets memory threshold - beyond which files are stored in disk
      factory.setSizeThreshold(MEMORY_THRESHOLD);
      // sets temporary location to store files
      factory.setRepository(new File(tempDir));

      ServletFileUpload upload = new ServletFileUpload(factory);

      // sets maximum size of upload file
      upload.setFileSizeMax(MAX_FILE_SIZE);

      // sets maximum size of request (include file + form data)
      upload.setSizeMax(MAX_REQUEST_SIZE);

      // Parse the request
      @SuppressWarnings("unchecked") List<FileItem> items = null;
      items = upload.parseRequest(request);

      // Process the uploaded items
      Iterator iter = items.iterator();
      while (iter.hasNext()) {
        FileItem item = (FileItem) iter.next();
        if (item.isFormField()) {
          if (item.getFieldName().equals("instanceId")) {
            instanceId = item.getString(charset);
          } else if (item.getFieldName().equals("currentAction")) {
            currentAction = item.getString(charset);
          } else if (item.getFieldName().equals("currentRole")) {
            currentRole = item.getString(charset);
          } else if (item.getFieldName().equals("currentState")) {
            currentState = item.getString(charset);
          } else if (item.getFieldName().equals("processId")) {
            processId = item.getString(charset);
          } else {
            fields.put(item.getFieldName(), item.getString(charset));
          }
        } else {
          String fileName = item.getName();
          File file = new File(tempDir + File.separator + fileName);
          item.write(file);
          fields.put(item.getFieldName(), file);
        }
      }
      processAction(request, response, fields, instanceId, currentAction, currentRole, currentState,
          processId);
    } catch (FileUploadBase.FileSizeLimitExceededException eu) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
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
      ProcessInstance processInstance =
          Workflow.getProcessInstanceManager().getProcessInstance(processId);
      action = model.getAction(actionName);
      GenericDataRecord record = getGenericDataRecord(request, data, role, model, action);
      event = getTask(model, processInstance, getUserInSession(request).getId(), role, state)
          .buildTaskDoneEvent(action.getName(), record);
    }
    Workflow.getWorkflowEngine().process(event, true);
    Thread.sleep(1000); // Wait task creation


  }

  private Task getCreationTask(ProcessModel processModel, String userId, String currentRole)
      throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task creationTask = Workflow.getTaskManager().getCreationTask(user, currentRole, processModel);
    return creationTask;
  }

  private Task getTask(ProcessModel processModel, ProcessInstance processInstance, String userId,
      String currentRole, String stateName) throws Exception {
    User user = new UserImpl(UserDetail.getById(userId));
    Task[] tasks = Workflow.getTaskManager().getTasks(user, currentRole, processInstance);
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
    Form form = model.getPublicationForm(action.getName(), role,
        getUserInSession(request).getUserPreferences().getLanguage());
    GenericDataRecord record = (GenericDataRecord) model.getNewActionRecord(action.getName(), role,
        getUserInSession(request).getUserPreferences().getLanguage(), null);
    for (Map.Entry<String, Object> f : data.entrySet()) {
      Field field = record.getField(f.getKey());
      if (f.getValue() == null || f.getValue().equals("null")) {
        if (field.getTypeName().equalsIgnoreCase("file") && field.getValue() != null) {
          //TODO : make work delete file
          SimpleDocument doc = AttachmentServiceProvider.getAttachmentService().searchDocumentById(new SimpleDocumentPK(field.getValue()),
              getUserInSession(request).getUserPreferences().getLanguage());
          AttachmentServiceProvider.getAttachmentService().deleteAttachment(doc);
        }
        field.setNull();
      } else {
        if (field.getTypeName().equalsIgnoreCase("user")) {
          UserDetail u = Administration.get().getUserDetail((String) f.getValue());
          field.setObjectValue(u);
        } else if (field.getTypeName().equalsIgnoreCase("group")) {
          GroupDetail g = Administration.get().getGroup((String) f.getValue());
          field.setObjectValue(g);
        } else if (field.getTypeName().equalsIgnoreCase("multipleUser")) {
          StringTokenizer stk = new StringTokenizer((String) f.getValue(), ",");
          UserDetail[] users = new UserDetail[stk.countTokens()];
          int i = 0;
          while (stk.hasMoreTokens()) {
            String id = stk.nextToken().trim();
            UserDetail u = Administration.get().getUserDetail(id);
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
            doc = AttachmentServiceProvider.getAttachmentService().createAttachment(doc, file);
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
