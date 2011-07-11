<%@ attribute name="label" required="true" %>
<%@ attribute name="labelParameter" required="false" %>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="com.oosphere.silverpeasmobile.multilang.docclassifier"/>

<div id="titleDiv">
	<div class="cellBrowseBar">
		<div id="browseBar">
			<div id="breadCrumb"><fmt:message key="${label}"/><c:if test="${not empty labelParameter}"> ${labelParameter}</c:if></div>
		</div>
	</div>
</div>
