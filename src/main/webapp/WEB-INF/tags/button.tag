<%@ attribute name="label" required="true" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="id" required="false" %>

<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.silverpeasmobile"/>

<table cellspacing="0" cellpadding="0" border="0" id="${id}">
	<tr>
		<td align="left"><img alt="" src="${pageContext.request.contextPath}/img/button/leftButton.png"></td>
		<td nowrap="nowrap" class="button"><a href="#" onclick="${action};" class="buttonLink"><fmt:message key="${label}"/></a></td>
		<td align="right"><img alt="" src="${pageContext.request.contextPath}/img/button/rightButton.png"></td>
	</tr>
</table>