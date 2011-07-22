<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ include file="../parameters.jsp"%>

<jsp:useBean id="jsonNewsData" class="org.json.JSONArray"  scope ="request"/>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<!DOCTYPE html>
<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
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
			<h1>Contacts</h1>
			<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
		</div>
		
		<div id="content" data-role="content">
		
			<script type="text/javascript">
				function selectContact(contactId){
					var form = document.forms['contactForm'];
					form.contactId.value = contactId;
					form.submit();
				}
			</script>
			<form name="contactForm" method="post">
				<input type="hidden" name="action" value="contact"/>
				<input type="hidden" name="subAction" value="contactDetail"/>
				<input type="hidden" name="userId" value="${userId}"/>
				<input type="hidden" name="contactId" value=""/>
			</form>
		
			<ul data-role="listview" data-theme="g">
				<c:forEach items="${contacts}" var="contact">
					<li>
						<a href="javascript:selectContact('${contact.id}')">
							<img src="/silverpeas${contact.avatar}" />
							<p><h1> ${contact.lastName} ${contact.firstName} </h1></p>
						</a>
					</li>
				</c:forEach>		
			</ul>
			
		</div>
	
		<div data-role="footer" data-position="fixed">
			Copyright Silverpeas 1999-2011
		</div>
	</div>
</body>
</html>