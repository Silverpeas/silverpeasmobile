<%@ page isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<c:set var="mode" value="0"/>
<c:set var="server" value="localhost"/>
<c:set var="port" value="8000"/>
<c:set var="context" value="silverpeasmobile"/>
<c:set var="fileServer" value="svp-fs"/>

<c:set var="domainId" value="0"/>
<c:choose>
	<c:when test="${empty lang}">
		<c:set var="lang" value="en"/>
	</c:when>
	<c:otherwise>
		<c:set var="lang" value="${lang}"/>
	</c:otherwise>
</c:choose>