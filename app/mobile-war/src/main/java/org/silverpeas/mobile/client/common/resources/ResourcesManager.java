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

package org.silverpeas.mobile.client.common.resources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * @author: svu
 */
public class ResourcesManager {

  private static JSONObject labels = null;
  private static JSONObject params = null;

  public static String getLabel(String key) {
    String label = "";
    if (labels == null) {
      String json = JsonUtils.stringify(getLabels());
      labels = JSONParser.parseStrict(json).isObject();
    }
    if (labels.containsKey(key)) {
      label = labels.get(key).toString();
      label = label.substring(1, label.length()-1);
    }
    return label;
  }

  public static String getParam(String key) {
    String param = "";
    if (params == null) {
      String json = JsonUtils.stringify(getParams());
      params = JSONParser.parseStrict(json).isObject();
    }
    if (params.containsKey(key)) {
      param = params.get(key).toString();
      param = param.substring(1, param.length()-1);
    }
    return param;
  }

  public static native JavaScriptObject getLabels() /*-{
    return $wnd.labels;
  }-*/;

  public static native JavaScriptObject getParams() /*-{
    return $wnd.params;
  }-*/;
}
