<%--
  ~ Copyright (C) 2000 - 2024 Silverpeas
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

<%@ page import="org.silverpeas.core.chat.ChatLocalizationProvider" %>
<%@ page import="org.silverpeas.core.chat.servers.ChatServer" %>
<%@ page import="org.silverpeas.core.chat.ChatUser" %>
<%@ page import="org.silverpeas.core.util.file.FileServerUtils" %>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.silverpeas.com/tld/silverFunctions" prefix="silfn" %>
<%@ taglib uri="http://www.silverpeas.com/tld/viewGenerator" prefix="view" %>

<c:set var="chatUser" value="<%=ChatUser.getCurrentRequester()%>"/>
<c:set var="chatSettings" value="<%=ChatServer.getChatSettings()%>"/>
<c:set var="currentUser" value='<%=session.getAttribute("user")%>'/>
<jsp:useBean id="currentUser" type="org.silverpeas.core.admin.user.model.User"/>


<jsp:useBean id="chatSettings" type="org.silverpeas.core.chat.ChatSettings"/>
<c:set var="userLanguage" value="${currentUser.userPreferences.language}"/>
<jsp:useBean id="userLanguage" type="java.lang.String"/>
<c:set var="chatBundle" value="<%=ChatLocalizationProvider.getLocalizationBundle(userLanguage)%>"/>
<jsp:useBean id="chatBundle" type="org.silverpeas.kernel.bundle.LocalizationBundle"/>

<c:set var="chatUrl" value="${chatSettings.BOSHServiceUrl}"/>
<c:set var="chatWsUrl" value="${chatSettings.websocketServiceUrl}"/>
<c:set var="chatIceServer" value="${chatSettings.ICEServer}"/>
<c:set var="chatACL" value="${chatSettings.ACL}"/>
<c:set var="aclGroupsAllowedToCreate" value="${chatACL.aclOnGroupChat.groupsAllowedToCreate}"/>

<!DOCTYPE HTML>
<html lang="${userLanguage}">
<head>
  <title>Spmobile chat</title>
  <meta charset="utf-8">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <meta name="robots" content="noindex">
  <view:includePlugin name="minimalsilverpeas"/>
  <view:includePlugin name="chat"/>
  <script type="text/javascript">
    function init() {
      <c:choose>
      <c:when test="${sessionScope.get('Silverpeas.Chat') and chatUser.registered}">
        const chatOptions = {
          viewMode : 'mobile',
          url : '${chatUrl}',
          wsUrl : '${chatWsUrl}',
          jid : '${chatUser.chatLogin}@${chatUser.chatDomain}',
          vcard : {
            'fn' : '${silfn:escapeJs(chatUser.displayedName)}'
          },
          id : '${chatUser.chatLogin}',
          password : '${chatUser.chatPassword}',
          domain : '${chatUser.chatDomain}',
          <c:if test="${not empty chatIceServer}">
          ice : {
            server : '${chatIceServer}',
            auth : true
          },
          </c:if>
          acl : {
            groupchat : {
              creation : ${empty aclGroupsAllowedToCreate or chatUser.isAtLeastInOneGroup(aclGroupsAllowedToCreate)}
            }
          },
          language : '${userLanguage}',
          avatar : '/silverpeas/display/avatar/60x/',
          userAvatarUrl : '/silverpeas/<%=FileServerUtils.getImageURL(((ChatUser)pageContext.getAttribute("chatUser")).getAvatar(), "60x60")%>', notificationLogo : (window.SilverChatSettings ? window.SilverChatSettings.get('un.d.i.u') : ''),
          nbMsgMaxCachedPerRoom : ${chatSettings.maxCachedMsgThresholdPerRoom},
          clearMessagesOnReconnection : ${chatSettings.clearMessagesOnReconnection()},
          replyToEnabled : ${chatSettings.replyToEnabled},
          reactionToEnabled : ${chatSettings.reactionToEnabled},
          visioEnabled : ${chatSettings.visioEnabled},
          screencastEnabled : ${chatSettings.screencastEnabled},
          debug : false,
          selectUser : function(openChatWith) {
            $('#userId').off('change').on('change', function() {
              var id = $(this).val();
              if (id && id !== '${chatUser.id}') {
                User.get(id).then(function(user) {
                  if (user) {
                    openChatWith(user.chatId, user.fullName);
                  }
                });
              }
            });
          }
        };
        if (typeof SilverChat === 'undefined') {
          console.log('${silfn:escapeJs(chatBundle.getString("chat.client.notAvailable"))}');
          return;
        }
        SilverChat.init(chatOptions).start();
      </c:when>
      <c:otherwise>
      console.log('${silfn:escapeJs(chatBundle.getString("chat.server.notAvailable"))}');
      </c:otherwise>
      </c:choose>
    }
  </script>

  <link href="chat.css" media="all" rel="stylesheet" type="text/css" />
  <!-- init script -->
  <script src="chat.js"></script>
</head>
<body class="page-tchat" onload="init()">
<h1 class="tchat-header">Spmobile chat</h1>
<form id="chat_selected_user">
  <input type="hidden" name="userId" id="userId"/>
  <input type="hidden" name="userName" id="userName"/>
</form>
</body>
</html>