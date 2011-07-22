<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sm"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ include file="../parameters.jsp"%>

<fmt:setLocale value="${lang}" />
<fmt:setBundle
	basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile" />

<!DOCTYPE html>
<html>
<head>
	<title><fmt:message key="pageTitle" />
	</title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
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
</head>
<body>
	

	<div data-role="page">
		<form name="formHome" action="${pageContext.request.contextPath}/index.html" method="post">
			<input type="hidden" name="action" value="login"/>
			<input type="hidden" name="subAction" value="home"/>
			<input type="hidden" name="userId" value="${userId}"/>
		</form>
		<div  data-role="header" data-position="fixed">
			<a href="#" data-icon="back" data-rel="back">Back</a>
			<h1>Status</h1>
			<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
		</div>
		
		<div data-role="content">
			
			<center><b>Current Status is '${status}'</b></center>
			
			<form action="${pageContext.request.contextPath}/index.html" method="post" data-ajax="false">
				<input type="hidden" name="action" value="profile"/>
				<input type="hidden" name="subAction" value="doChangeStatus"/>
				<input type="hidden" name="userId" value="${userId}"/>
				
				<div data-role="fieldcontain">
					<label for="status">New Status</label> 
					<input type="text" name="status" id="status" value="" />
				</div>
				
				<button type="submit" data-theme="a" name="submit" value="submit-value">Change</button>
			</form>
		</div>
		
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
	</div>
</body>
</html>