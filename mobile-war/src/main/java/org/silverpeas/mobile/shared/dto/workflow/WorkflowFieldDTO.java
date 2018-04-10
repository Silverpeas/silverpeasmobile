/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.shared.dto.workflow;

import com.google.gwt.dom.client.Element;
import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;
import java.util.Map;

public class WorkflowFieldDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;
  private boolean readOnly;
  private boolean mandatory;
  private String displayerName;
  private String name;
  private String label;
  private String value;
  private String valueId;
  private String type;
  private String actionName;
  private Map<String, String> values;
  private transient Element objectValue = null;

  public WorkflowFieldDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    return ((WorkflowFieldDTO) obj).getId().equals(getId());
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(final boolean readOnly) {
    this.readOnly = readOnly;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(final boolean mandatory) {
    this.mandatory = mandatory;
  }

  public String getDisplayerName() {
    return displayerName;
  }

  public void setDisplayerName(final String displayerName) {
    this.displayerName = displayerName;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setValues(final Map<String, String> values) {
    this.values = values;
  }

  public Map<String, String> getValues() {
    return values;
  }

  public String getValueId() {
    return valueId;
  }

  public void setValueId(final String valueId) {
    this.valueId = valueId;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(final String actionName) {
    this.actionName = actionName;
  }

  public void setObjectValue(final Element objectValue) {
    this.objectValue = objectValue;
  }

  public Element getObjectValue() {
    return objectValue;
  }
}
