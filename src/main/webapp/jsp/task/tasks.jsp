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
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.min.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
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
			<h1>Tasks</h1>
			<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
		</div>
		
		<div id="content" data-role="content">
		
			<script type="text/javascript">
				function selectTask(taskId){
					var form = document.forms['taskForm'];
					form.subAction.value = 'task';
					form.taskId.value = taskId;
					form.submit();
				}
				
				function newTask(){
					var form = document.forms['taskForm'];
					form.subAction.value = 'new';
					form.submit();
				}
			</script>
			<form name="taskForm" method="post">
				<input type="hidden" name="action" value="task"/>
				<input type="hidden" name="subAction" value="task"/>
				<input type="hidden" name="userId" value="${userId}"/>
				<input type="hidden" name="taskId" value=""/>
			</form>
		
			<ul data-role="listview" data-theme="a">
				<% String label = ""; %>
				<c:forEach items="${tasks}" var="task">
				
					<c:if test="${task.showListSeparator}">
						<li data-role="list-divider">
							<p><h1>${task.listSeparator}</h1></p>
					</li>
					</c:if>
					
					<li>
						<a href="javascript:selectTask('${task.id}')">
							<p><h1> ${task.name}</h1></p>
						</a>
					</li>
				</c:forEach>		
			</ul>
			
		</div>
		
		<div data-role="fieldcontain">
				<fieldset class="ui-grid-a">
					<a href="javascript:newTask();" data-role="button" data-theme="a">New</a>
				</fieldset>
		</div>
	
		<div data-role="footer" data-position="fixed">
			Copyright Silverpeas 1999-2011
		</div>
	</div>
</body>
</html>