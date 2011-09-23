<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ include file="../parameters.jsp"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<!DOCTYPE html>
<html>

	<head>
		<title><fmt:message key="pageTitle"/></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.min.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
	</head>
	
	<body>
		
		<div  data-role="page">
	
			<form name="formHome" action="${pageContext.request.contextPath}/index.html" method="post">
				<input type="hidden" name="action" value="login"/>
				<input type="hidden" name="subAction" value="home"/>
				<input type="hidden" name="userId" value="${userId}"/>
			</form>
			<div  data-role="header" data-position="fixed">
				<a href="#" data-icon="back" data-rel="back">Back</a>
				<h1> ${contact.lastName} ${contact.firstName}</h1>
				<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
			</div>
			
			<div id="content" data-role="content">
			
				<img src="/silverpeas${contact.avatar}" />
				<br>
			
				<div class="ui-grid-a">
					<c:if test="${not empty contact.status}">
						<div class="ui-block-a"><strong>Current Status</strong></div>
						<div class="ui-block-b">${contact.status}</div>
						<div class="ui-block-a">&nbsp;</div>
						<div class="ui-block-b">&nbsp;</div>
					</c:if>
					<c:if test="${not empty contact.eMail}">
						<div class="ui-block-a"><strong>Email</strong></div>
						<div class="ui-block-b"><a class="ui-link-inherit" href="mailto:${contact.eMail}">${contact.eMail}</a></div>
						<div class="ui-block-a">&nbsp;</div>
						<div class="ui-block-b">&nbsp;</div>
					</c:if>
					
					<c:forEach items="${contactProperties}" var="property">
						<c:if test="${not empty property.value}">
							<div class="ui-block-a"><strong>${property.name} </strong></div>
							<div class="ui-block-b">${property.value} </div>
							<div class="ui-block-a">&nbsp;</div>
							<div class="ui-block-b">&nbsp;</div>
						</c:if>
					</c:forEach>		
					
				</div>
				
			</div>
		
			<div data-role="footer" data-position="fixed">
				Copyright Silverpeas 1999-2011
			</div>
		</div>
		
	</body>
</html>