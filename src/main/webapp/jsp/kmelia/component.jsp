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
<c:set var="zeBackLabel" value="${(empty backLabel) ? 'Services' : backLabel}"/>

<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.min.css" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>

	<script type="text/javascript">
		function selectNode(nodeId) {
			var form = document.forms["form"];
			form.elements["nodeId"].value = nodeId;
			form.submit();
		}

		function selectPubli(pubId) {
			var form = document.forms["form"];
			form.elements["subAction"].value = "publication";
			form.elements["pubId"].value = pubId;
			form.submit();
		}
	</script>

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
			<h1>${component.name}</h1>
			<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
		</div>
		
		<div  data-role="content">
			<nav>
				<ul data-role="listview" data-inset="true" data-theme="c" data-dividertheme="b">
					<li data-role="list-divider">Dossiers</li>
					<c:forEach items="${nodes}" var="node">
						<c:if test='${node.id != "1" && node.id != "2"}'>
						<li><a href="javascript:selectNode('${node.id}')"><img src="img/fileTree/folder.jpeg" class="ui-li-icon"> ${node.name} <span class="ui-li-count">${node.nbObjects}</span></a></li>
						</c:if>
							</c:forEach>
					<c:if test="${empty nodes}">
						<li>Aucun sous dossier</li>
					</c:if>
			
					<li data-role="list-divider">Publications</li>
					<c:forEach items="${publis}" var="publi">
						<li><a href="javascript:selectPubli('${publi.id}')"><img src="img/fileTree/doc.jpg" class="ui-li-icon"> ${publi.name}</a></li>
							</c:forEach>
					<c:if test="${empty publis}">
						<li>Aucune publication</li>
					</c:if>
				</ul>
			</nav>

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
			
			

			<form name="form" action="${pageContext.request.contextPath}/index.html" method="post">
							<input type="hidden" name="action" value="kmelia"/>
							<input type="hidden" name="subAction" value="component"/>
							<input type="hidden" name="userId" value="${userId}"/>
							<input type="hidden" name="spaceId" value="${spaceId}"/>
							<input type="hidden" name="componentId" value="${component.id}"/>
							<input type="hidden" name="pubId" value=""/>
							<input type="hidden" name="nodeId" value=""/>
			</form>
		</div>
		
		<div data-role="footer" data-position="fixed">
			Copyright Silverpeas 1999-2011
		</div>
	</div>
</body>


</html>
