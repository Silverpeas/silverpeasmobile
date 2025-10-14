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

package org.silverpeas.mobile.server.services.helpers;


import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.FieldTemplate;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author: svu
 */
public class FormsHelper {

  private static FormsHelper instance;

  public static FormsHelper getInstance() {
    if (instance == null) {
      instance = new FormsHelper();
    }
    return instance;
  }

  public List<FormFieldDTO> getViewFormFields(PublicationTemplate pubTemplate, DataRecord data, User currentUser) throws Exception {
    List<FormFieldDTO> fields = new ArrayList<>();

    Form form = pubTemplate.getViewForm();
    for (FieldTemplate ft : form.getFieldTemplates()) {
      String label = ft.getLabel(currentUser.getUserPreferences().getLanguage());
      String value = data.getField(ft.getFieldName()).getStringValue();
      String id = "";
      String type = ft.getTypeName();
      String name = ft.getFieldName();
      String displayerName = ft.getDisplayerName();
      value = getDisplayValue(ft, value, currentUser, data.getResourceReference().getComponentInstanceId());

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
        while(stk.hasMoreTokens()) {
          String idUser = stk.nextToken();
          UserDetail u = Administration.get().getUserDetail(idUser);
          value += u.getDisplayedName() + ", ";
        }
        if (!value.isEmpty()) value = value.substring(0, value.length()-2);
      } else if(type.equalsIgnoreCase("group")) {
        GroupDetail g = Administration.get().getGroup(value);
        value = g.getName();
      } else if(type.equalsIgnoreCase("date")) {
        value = DateUtil.getInputDate(value, currentUser.getUserPreferences().getLanguage());
      } else if(type.equalsIgnoreCase("file")) {
        if (value != null) {
          SimpleDocument doc = AttachmentServiceProvider
              .getAttachmentService().searchDocumentById(new SimpleDocumentPK(value),
                  currentUser.getUserPreferences().getLanguage());
          value = doc.getTitle();
          if (value == null) value = doc.getFilename();
          id = doc.getId();
        }
      }
      FormFieldDTO dto = new FormFieldDTO();
      dto.setLabel(label);
      dto.setValue(value);
      dto.setName(name);
      dto.setType(type);
      dto.setId(id);
      dto.setDisplayerName(displayerName);
      dto.setInstanceId(data.getResourceReference().getComponentInstanceId());
      if (value != null) fields.add(dto);
    }

    return fields;
  }

  private String getDisplayValue(final FieldTemplate ft, String value, User currentUser, String instanceId) {
    Map<String, String> params = ft.getParameters(currentUser.getUserPreferences().getLanguage());
    String keys = params.get("keys");
    String values = params.get("values");
    if (keys != null && values != null) {
      String[] k = keys.split("##");
      String[] v = values.split("##");
      String[] vs = value.split("##");
      String result = "";
      for (int i = 0; i < k.length; i++) {
        for (int j = 0; j < vs.length; j++) {
          if (k[i].equals(vs[j])) {
            result += v[i] + " ";
          }
        }
      }
      if (!result.isEmpty()) value = result;
    }

    if (value != null && value.startsWith("xmlWysiwygField")) {
      String wysiwygFile = value.substring(value.indexOf('_') + 1);
      try {

        String path = FileRepositoryManager.getAbsolutePath(instanceId) +
            "xmlWysiwyg" + File.separator + wysiwygFile;
        value = new String(Files.readAllBytes(Paths.get(path)));
      } catch (Exception e) {
        SilverLogger.getLogger(this).error(e);
      }
    }

    return value;
  }

}
