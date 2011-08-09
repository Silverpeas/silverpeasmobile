<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="parameters.jsp"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
</head>

<body>

<div  data-role="page" >
<div  data-role="header"><h1><fmt:message key="pageTitle"/></h1></div>
<div  data-role="content">

		<form name="form" action="${pageContext.request.contextPath}/index.html" method="post" data-ajax="false">

			<center>
				<img src="http://www.silverpeas.com/images/header-logo.gif"/>
			</center>

			<div data-role="fieldcontain">
			    <label for="login"><fmt:message key="login"/></label>
			    <input type="text" name="login" id="login" value="${login}"  />
			</div>

			<div data-role="fieldcontain">
			    <label for="password"><fmt:message key="password"/></label>
			    <input type="password" name="password" id="password" value=""  />
			</div>
			
			<c:if test="${fn:length(domains)==1}">
				<input type="hidden" name="domainId" value="${domains[0].id}">
			</c:if>
			<c:if test="${fn:length(domains)>1}">
				<div data-role="fieldcontain">
					<label for="domainId" class="select">Domain</label>
					<select name="domainId" id="domainId">
					<c:forEach items="${domains}" var="domain">
						<c:if test="${domain.id == domainIdFromUrl}">
							<option value="${domain.id}" selected>${domain.name}</option>
						</c:if>
						<c:if test="${domain.id != domainIdFromUrl}">
							<option value="${domain.id}" >${domain.name}</option>
						</c:if>
					</c:forEach>
					</select>
				</div>
			</c:if>
			
			<c:if test="${not empty error}">
				<h4><fmt:message key="error.${error}"/></h4>
			</c:if>

			<button type="submit" data-theme="a" name="submit" value="submit-value">Go</button>

			<input type="hidden" name="action" value="login">
			<input type="hidden" name="subAction" value="login">
			<input type="hidden" name="lang" value="${lang}">
		</form>
</div>
	<div  data-role="footer">Copyright Silverpeas 1999-2011</div>
</div>

</body>
</html>
