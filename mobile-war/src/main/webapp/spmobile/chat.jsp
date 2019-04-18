<%@ page import="org.silverpeas.core.util.ResourceLocator" %>
<%@ page import="org.silverpeas.core.util.SettingBundle" %>
<%@ page import="org.silverpeas.core.admin.user.model.UserDetail" %><%--
  ~ Copyright (C) 2000 - 2019 Silverpeas
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
<!DOCTYPE HTML>
<html>

<%
  SettingBundle
      configChat = ResourceLocator.getSettingBundle("org.silverpeas.chat.settings.chat");
  UserDetail user = (UserDetail) session.getAttribute("user");
  String login = user.getLogin();
  int i = login.indexOf("@");
  if (i != -1) login.substring(i);
%>

<head>
  <title>Spmobile chat</title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="robots" content="noindex">

  <!-- require:dependencies -->
  <link href="jsxc/css/jquery-ui.min.css" media="all" rel="stylesheet" type="text/css" />
  <link href="jsxc/css/jsxc.css" media="all" rel="stylesheet" type="text/css" />
  <!--  endrequire -->

  <link href="chat.css" media="all" rel="stylesheet" type="text/css" />

  <!-- require:dependencies -->
  <script src="jsxc/lib/jquery.min.js"></script>
  <script src="jsxc/lib/jquery-ui.min.js"></script>
  <script src="jsxc/lib/jquery.slimscroll.js"></script>
  <script src="jsxc/lib/jquery.fullscreen.js"></script>
  <script src="jsxc/lib/jsxc.dep.min.js"></script>
  <!--  endrequire -->

  <script src="jsxc/lib/bootstrap.min.js"></script>

  <!-- jsxc library -->
  <script src="jsxc/jsxc.min.js"></script>

  <!-- init script -->
  <script src="chat.js"></script>
</head>

<body class="page-tchat">
<h1 class="tchat-header">Spmobile chat</h1>
<input type="hidden" id="xmpp-domain" name="xmpp-domain" class="form-control" value='<%=configChat.getString("chat.xmpp.domain.0")%>' />
<input type="hidden" id="bosh-url" name="bosh-url" class="form-control" value='<%=configChat.getString("chat.xmpp.httpBindUrl")%>' />
<input type="hidden" id="username" class="form-control" value='<%=login%>'/>
<input type="hidden" id="password" class="form-control" value='<%=user.getToken()%>'/>
</body>

</html>