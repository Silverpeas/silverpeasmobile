<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../parameters.jsp"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>
<c:set var="zeBackLabel" value="${(empty backLabel) ? 'Documents' : backLabel}"/>

<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
</head>

<body>
<div  data-role="page" >

	<form name="formHome" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="login"/>
		<input type="hidden" name="subAction" value="home"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	<div  data-role="header" data-position="fixed">
		<a href="#" data-icon="back" data-rel="back">Back</a>
		<h1>${publication.name}</h1>
		<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
	</div>
	
	<div  data-role="content">
		<div data-role="collapsible" data-collapsed="true">
			<h3>Ent&ecirc;te</h3>
			<p>
				<fmt:message key="fullDateFormat" var="datePattern"/>
				<b>Auteur</b> : ${publication.creatorName}<br/>
				<b>Date de cr&eacute;ation</b> : <fmt:formatDate pattern="${datePattern}" value="${publication.creationDate}"/><br/>
				<b>Description</b> : ${publication.description}<br/>
				<b>Derni&egrave;re mise a jour </b> : le <fmt:formatDate pattern="${datePattern}" value="${publication.updateDate}"/> par ${publication.updaterName}
			</p>
		</div>

		<div data-role="collapsible" data-collapsed="true">
			<h3>Pi&egrave;ces jointes</h3>
			<ul data-role="listview" data-inset="true" data-theme="c" data-dividertheme="b">
			<c:forEach items="${publication.attachments}" var="attachment">
				<li><a class="fileTitle" href="${pageContext.request.contextPath}/svp-fs/File?componentId=${componentId}&attachmentId=${attachment.fileId}" target="_blank"><img src="${attachment.fileIcon}" class="ui-li-icon"> ${attachment.name} <span class="ui-li-count">(${attachment.fileSize})</span></a>

				</td>
			</c:forEach>
		</div>
	</div>
	
	<script type="text/javascript">
		function goToServices() {
			var form = document.forms["formServices"];
			form.submit();
		}
		
		function goToProfile() {
			var form = document.forms["formProfile"];
			form.submit();
		}
	</script>
	<form name="formServices" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="kmelia"/>
		<input type="hidden" name="subAction" value="home"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	<form name="formProfile" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="profile"/>
		<input type="hidden" name="subAction" value="profile"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	<div data-role="footer" data-position="fixed">
		Copyright Silverpeas 1999-2011
	</div>

</body>


</html>
