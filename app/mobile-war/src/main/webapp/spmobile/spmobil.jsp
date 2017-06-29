<%@ page import="org.silverpeas.core.util.ResourceLocator" %>
<%@ page import="org.silverpeas.core.util.LocalizationBundle" %>
<%@ page import="org.silverpeas.core.util.URLUtil" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.silverpeas.mobile.server.helpers.ResourceBundleHelper" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://www.silverpeas.com/tld/viewGenerator" prefix="view" %>

<!doctype html>
<html manifest="spmobil.appcache">
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, target-densitydpi=device-dpi, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>

  <meta name="mobile-web-app-capable" content="yes">
  <meta name="HandheldFriendly" content="true">
  <meta name="apple-mobile-web-app-capable" content="yes"/>
  <meta name="apple-mobile-web-app-status-bar-style" content="default"/>

  <%
    String l = request.getHeader("Accept-Language");
    String appUrl = URLUtil.getApplicationURL();
    l = l.substring(0, 2);
    if (!l.equalsIgnoreCase("fr")) {
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

  <view:includePlugin name="tkn"/>

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
