<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/silverpeas-menu.tld" prefix="silverpeas-menu"%>
<%@ taglib uri="/WEB-INF/silverpeas.tld" prefix="silverpeas"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="parameters.jsp"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.2.6.js"></script>
	<script type="text/javascript" src="/silverpeas/util/javaScript/jquery/jquery-1.5.min.js"></script>
	<script type="text/javascript" src="/silverpeas/util/javaScript/jquery/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			document.forms["form"].submit();
		});
	</script>
</head>

<body>
	<div id="main">
		<fmt:message key="loading"/>
		<silverpeas:site mode="${mode}" userId="${userId}" server="${server}" port="${port}" context="${context}" fileServer="${fileServer}"/>
		<form name="form" action="${pageContext.request.contextPath}/index.html" method="post">
			<input type="hidden" name="action" value="kmelia"/>
			<input type="hidden" name="subAction" value="home"/>
			<input type="hidden" name="userId" value="${userId}"/>
		</form>
	</div>
</body>
</html>