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
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
	<script type="text/javascript">
			function goToServices() {
				var form = document.forms["formServices"];
				form.submit();
			}
			
			function changeStatus() {
				var form = document.forms["formChangeStatus"];
				form.submit();
			}
			
			function infoThread() {
				var form = document.forms["formInfoThread"];
				form.submit();
			}
	</script>
</head>
<body>
	<form name="formServices" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="kmelia"/>
		<input type="hidden" name="subAction" value="home"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	<form name="formChangeStatus" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="profile"/>
		<input type="hidden" name="subAction" value="changeStatus"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	<form name="formInfoThread" action="${pageContext.request.contextPath}/index.html" method="post">
		<input type="hidden" name="action" value="profile"/>
		<input type="hidden" name="subAction" value="infoThread"/>
		<input type="hidden" name="userId" value="${userId}"/>
	</form>
	
	<div  data-role="page">
		<div  data-role="header" data-position="fixed">Profile</div>
		<div  data-role="content">
			<nav>
				<ul data-role="listview" data-inset="true" data-theme="c" data-dividertheme="b">
				    <li><a href="javascript:changeStatus()">Change Status</a></li>
				    <li><a href="javascript:infoThread()">Info Thread</a></li>
				</ul>
			</nav>
		</div>
	

		<div data-role="footer" data-position="fixed">
			<div data-role="navbar">
				<ul>
					<li><a href="javascript:goToServices()" >Services</a></li>
					<li><a href="">Profile</a></li>
				</ul>
			</div>
			<div datadetectors="off">Copyright Silverpeas 1999-2011</div>
		</div>
	</div>
</body>
</html>