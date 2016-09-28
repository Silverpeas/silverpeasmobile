<%@ page import="org.silverpeas.core.util.ResourceLocator" %>
<%@ page import="org.silverpeas.core.util.LocalizationBundle" %>
<%@ page import="org.silverpeas.core.util.URLUtil" %>
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
    LocalizationBundle resource = ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.mobileSettings", l);
    String css = resource.getString("styleSheet");
    if (css != null && !css.isEmpty()) {
      out.println("<link rel='stylesheet' type='text/css' href='" + css + "'>");
    }
  %>

  <meta name="gwt:property" content="locale=<%=l%>">

  <link rel="apple-touch-icon" href="touch-icon-iphone.png"/>
  <link rel="apple-touch-icon" sizes="72x72" href="touch-icon-iphone.png"/>
  <link rel="apple-touch-icon" sizes="114x114" href="touch-icon-iphone4.png"/>
  <link rel="shortcut icon" sizes="196x196" href="nice-highres.png"/>
  <link rel="shortcut icon" sizes="128x128" href="niceicon.png"/>

  <title>Silverpeas Mobile</title>
  <script type="text/javascript" src="<%=appUrl%>/spmobil/spmobil.nocache.js"></script>
  <script>
    function resize() {
      var windowHeight = window.innerHeight;
      document.body.style.height = windowHeight + "px";
    }
  </script>

</head>
<body class="ui-panel-wrapper ui-page-theme-a csspinner traditional" onload="resize();"></body>

</html>
