<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ include file="parameters.jsp"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<!DOCTYPE html>
<html>
	<head>
	<title><fmt:message key="pageTitle"/></title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
	
	<script type="text/javascript">
		
		function goTo(action, subAction) {
			var form = document.forms["form"];
			form.action.value = action;
			form.subAction.value = subAction;
			form.submit();
		}
	</script>
</head>

<body>

<form name="form" action="${pageContext.request.contextPath}/index.html" method="post">
	<input type="hidden" name="action" value=""/>
	<input type="hidden" name="subAction" value=""/>
	<input type="hidden" name="userId" value="${userId}"/>
</form>

<div  data-role="page">
	<div  data-role="header" data-position="fixed"><h1>Home</h1></div>
	<div  data-role="content">

			<div class="ui-grid-a">
				<div class="ui-block-a"><a href="javascript:goTo('kmelia', 'home')" data-role="button" data-theme="a">Documents</a></div>
				<div class="ui-block-b"><a href="javascript:goTo('profile', 'infoThread')" data-role="button" data-theme="a">Dashboard</a></div>
				<div class="ui-block-a"><a href="javascript:goTo('profile', 'changeStatus')" data-role="button" data-theme="a">Status</a></div>
				<div class="ui-block-b"><a href="javascript:goTo('contact', 'contacts')" data-role="button" data-theme="a">Contacts</a></div>
			</div>

	</div>
	<div data-role="footer" data-position="fixed">
		Copyright Silverpeas 1999-2011
	</div>
</div>

</body>
</html>
