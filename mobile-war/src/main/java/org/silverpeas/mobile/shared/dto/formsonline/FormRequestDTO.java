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

package org.silverpeas.mobile.shared.dto.formsonline;

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author svu
 */
public class FormRequestDTO implements Serializable {

  public static final int STATE_UNREAD = 1;
  public static final int STATE_READ = 2;
  public static final int STATE_VALIDATED = 3;
  public static final int STATE_REFUSED = 4;
  public static final int STATE_ARCHIVED = 5;
  public static final int STATE_CANCELED = 6;

  private String id;
  private String comments;
  private String title;
  private String description;
  private String creator;
  private String creationDate;
  private String formName;
  private int state;
  private String stateLabel;
  private String formId;
  private List<FormFieldDTO> data;
  private int validator;

  private FormLayerDTO htmlLayer;

  public FormLayerDTO getHtmlLayer() {
    return htmlLayer;
  }

  public void setHtmlLayer(FormLayerDTO htmlLayer) {
    this.htmlLayer = htmlLayer;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(final String comments) {
    this.comments = comments;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    if (title == null) {
      this.title = "";
    } else {
      this.title = title;
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    if (description == null) {
      this.description = "";
    } else {
      this.description = description;
    }
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(final String creator) {
    this.creator = creator;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public int getState() {
    return state;
  }

  public void setState(final int state) {
    this.state = state;
  }

  public List<FormFieldDTO> getData() {
    return data;
  }

  public void setData(final List<FormFieldDTO> data) {
    this.data = data;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(final String formId) {
    this.formId = formId;
  }

  public String getStateLabel() {
    return stateLabel;
  }

  public void setStateLabel(final String stateLabel) {
    this.stateLabel = stateLabel;
  }

  public boolean isReadOnly() {
    return (state == STATE_READ || state == STATE_UNREAD) && validator == 1;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public String getFormName() {
    return formName;
  }

    public int getValidator() {
        return validator;
    }

    public void setValidator(int validator) {
        this.validator = validator;
    }

  public static FormRequestDTO fromJSON(JsPropertyMap<Object> json) {
    FormRequestDTO dto = new FormRequestDTO();

    dto.setId(json.get("id") != null ? json.get("id").toString() : null);
    dto.setComments(json.get("comments") != null ? json.get("comments").toString() : null);
    dto.setTitle(json.get("title") != null ? json.get("title").toString() : null);
    dto.setDescription(json.get("description") != null ? json.get("description").toString() : null);
    dto.setCreator(json.get("creator") != null ? json.get("creator").toString() : null);
    dto.setCreationDate(json.get("creationDate") != null ? json.get("creationDate").toString() : null);
    dto.setFormName(json.get("formName") != null ? json.get("formName").toString() : null);
    dto.setFormId(json.get("formId") != null ? json.get("formId").toString() : null);
    dto.setStateLabel(json.get("stateLabel") != null ? json.get("stateLabel").toString() : null);

    // int state
    Object stateObj = json.get("state");
    if (stateObj != null) {
      try {
        dto.setState(Integer.parseInt(stateObj.toString()));
      } catch (NumberFormatException e) {
        dto.setState(0);
      }
    }

    // int validator
    Object validatorObj = json.get("validator");
    if (validatorObj != null) {
      try {
        dto.setValidator(Integer.parseInt(validatorObj.toString()));
      } catch (NumberFormatException e) {
        dto.setValidator(0);
      }
    }

    // data (List<FormFieldDTO>)
    Object dataObj = json.get("data");
    if (dataObj != null) {
      try {
        List<FormFieldDTO> list = new java.util.ArrayList<>();

        jsinterop.base.JsArrayLike<Object> array =
                jsinterop.base.Js.asArrayLike(dataObj);

        for (int i = 0; i < array.getLength(); i++) {
          Object item = array.getAt(i);
          if (item != null) {
            JsPropertyMap<Object> map = (JsPropertyMap<Object>) item;
            list.add(FormFieldDTO.fromJSON(map));
          }
        }

        dto.setData(list);

      } catch (Exception e) {
        dto.setData(null);
      }
    }

    // htmlLayer (FormLayerDTO)
    Object layerObj = json.get("htmlLayer");
    if (layerObj != null) {
      try {
        JsPropertyMap<Object> map = (JsPropertyMap<Object>) layerObj;
        dto.setHtmlLayer(FormLayerDTO.fromJSON(map));
      } catch (Exception e) {
        dto.setHtmlLayer(null);
      }
    }

    return dto;
  }
}
