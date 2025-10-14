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

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

/**
 * @author svu
 */
public class FormsHelper {

  public static native JavaScriptObject populateFormData(JavaScriptObject formData, String name, Element value) /*-{
    if (value == null) {
      formData.append(name, null);
    } else {
      formData.append(name, value.files[0]);
      formData.append(name+'$$id', '');
      formData.append(name+'Operation', 'ADD');
    }
    return formData;
  }-*/;

  public static native JavaScriptObject populateFormData(JavaScriptObject formData, String name, String value) /*-{
    formData.append(name, value);
    return formData;
  }-*/;

  public static native JavaScriptObject createFormData() /*-{
    var fd = new FormData();
    return fd;
  }-*/;

  public static boolean isStoreValueId(FormFieldDTO f) {
    if (f.getType().equalsIgnoreCase("user") || f.getType().equalsIgnoreCase("multipleUser") ||
            f.getType().equalsIgnoreCase("group")) return true;

    if (f.getDisplayerName() != null) {
      boolean r = f.getDisplayerName().equalsIgnoreCase("checkbox") ||
          f.getDisplayerName().equalsIgnoreCase("radio") ||
          f.getDisplayerName().equalsIgnoreCase("listbox");
      return r;
    }
    return false;
  }
}
