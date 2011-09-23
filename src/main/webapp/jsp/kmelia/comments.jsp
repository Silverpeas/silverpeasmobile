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
<c:set var="zeBackLabel" value="${(empty backLabel) ? 'Comments' : backLabel}"/>

<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.mobile-1.0b1.min.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
	<script type='text/javascript'>
        $(document).ready(function() {
            $('#comment').keyup(function() {
                var len = this.value.length;
                if (len >= 2000) {
                    this.value = this.value.substring(0, 2000);
                }
                $('#charLeft').text(2000 - len);
            });
        });
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
		<h1>${publication.name}</h1>
		<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
	</div>
	
	<div  data-role="content">
	
		<form name="form" action="${pageContext.request.contextPath}/index.html" method="post">
			<div data-role="fieldcontain">
				<%--<label for="comment">Add Comment</label>--%>
				<textarea cols="40" rows="8" name="comment" id="comment" style="width: 98%"></textarea>
				<span style="font-size:smaller;">&nbsp;<span id="charLeft">2000</span>  Characters left</span>
			</div>
			
			<input type="submit" value="Submit Comment" data-theme="a">  
			
			<input type="hidden" name="action" value="kmelia"/>
			<input type="hidden" name="subAction" value="addComment"/>
			<input type="hidden" name="userId" value="${userId}"/>
			<input type="hidden" name="componentId" value="${componentId}"/>
			<input type="hidden" name="publicationId" value="${publicationId}"/>
			<input type="hidden" name="attachmentId" value="${attachmentId}"/>
		</form>
		
		<ul data-role="listview" data-theme="a" data-inset="true">
		<c:forEach items="${comments}" var="comment">
			<li>
				<img src="/silverpeas${comment.ownerDetail.avatar}"/> 
				<p><b>${comment.owner}</b> - ${comment.creationDate}</p><p style="white-space: pre-line;">${comment.message}</p>
			</li>
		</c:forEach>
		</ul>
		
	</div>
	
	<div data-role="footer" data-position="fixed">
		Copyright Silverpeas 1999-2011
	</div>

</body>


</html>
