<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="org.silverpeas.core.util.LocalizationBundle" %>
<%@ page import="org.silverpeas.core.util.ResourceLocator" %>
<%@ page import="org.silverpeas.core.util.URLUtil" %>
<%@ page import="org.silverpeas.mobile.server.helpers.ResourceBundleHelper" %>
<%@ page import="java.util.Map" %>

<%--
  ~ Copyright (C) 2000 - 2018 Silverpeas
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU Affero General Public License as
  ~ published by the Free Software Foundation, either version 3 of the
  ~ License, or (at your option) any later version.
  ~
  ~ As a special exception to the terms and conditions of version 3.0 of
  ~ the GPL, you may redistribute this Program in connection with Free/Libre
  ~ Open Source Software ("FLOSS") applications as described in Silverpeas's
  ~ FLOSS exception.  You should have received a copy of the text describing
  ~ the FLOSS exception, and it is also available here:
  ~ "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU Affero General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Affero General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --%>

<!doctype html>
<html manifest="spmobil.appcache">
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
  <meta name="robots" content="noindex">
  <meta name="viewport" content="width=device-width, target-densitydpi=device-dpi, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>

  <meta name="mobile-web-app-capable" content="yes">
  <meta name="HandheldFriendly" content="true">
  <meta name="apple-mobile-web-app-capable" content="yes"/>
  <meta name="apple-mobile-web-app-status-bar-style" content="default"/>

  <%
    String l = request.getHeader("Accept-Language");
    String appUrl = URLUtil.getApplicationURL();
    if (l != null && !l.isEmpty()) {

      l = l.substring(0, 2);
      if (!l.equalsIgnoreCase("fr")) {
        l = "en";
      }
    } else {
      l = "en";
    }

    LocalizationBundle resourceLabels = ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.multilang.mobileBundle", l);
    Map<String, String> map = ResourceBundleHelper.convertResourceBundleToMap(resourceLabels);
    String jsonLabels = new ObjectMapper().writeValueAsString(map);

    LocalizationBundle resource = ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.mobileSettings", l);
    Map<String, String> mapConfig = ResourceBundleHelper.convertResourceBundleToMap(resource);
    String jsonParams = new ObjectMapper().writeValueAsString(mapConfig);
  %>

  <meta name="gwt:property" content="locale=<%=l%>">

  <link rel="apple-touch-icon" href="<%= resource.getString("apple-touch-icon")%>"/>
  <link rel="apple-touch-icon" sizes="72x72" href="<%= resource.getString("apple-touch-icon72x72")%>"/>
  <link rel="apple-touch-icon" sizes="114x114" href="<%= resource.getString("apple-touch-icon114x114")%>"/>
  <link rel="shortcut icon" sizes="196x196" href="<%= resource.getString("shortcut_icon196x196")%>"/>
  <link rel="shortcut icon" sizes="128x128" href="<%= resource.getString("shortcut_icon128x128")%>"/>

  <title><%= resourceLabels.getString("shortcut_title")%></title>
  <script type="text/javascript" src="<%=appUrl%>/spmobile/spmobile.nocache.js"></script>
  <script>
    var labels = <% out.println(jsonLabels); %>;
    var params = <% out.println(jsonParams); %>;
    function resize() {
      var windowHeight = window.innerHeight;
      document.body.style.height = windowHeight + "px";
    }
  </script>

  <%
    String css = resource.getString("styleSheet");
    if (css != null && !css.isEmpty()) {
      out.println("<link rel='stylesheet' type='text/css' href='" + css + "'>");
    }
  %>


    </head>
<body class="ui-panel-wrapper ui-page-theme-a csspinner traditional" onload="resize();">
  <div id="oneinch" style="position: absolute;padding: 0;visibility: hidden;width: 1in;height: 1in;"></div>
</body>

</html>
