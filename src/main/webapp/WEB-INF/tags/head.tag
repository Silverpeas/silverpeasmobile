<%@ attribute name="page" required="true" %>
<%@ attribute name="datePicker" required="false" %>
<%@ attribute name="fileTree" required="false" %>
<%@ attribute name="dataTable" required="false" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<c:if test="${datePicker || dataTable}">
	<link type="text/css" href="/silverpeas/util/styleSheets/jquery/ui-lightness/jquery-ui-1.8.10.custom.css" rel="stylesheet"/>
</c:if>
<c:if test="${fileTree}">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqueryFileTree.css"/>
</c:if>
<c:if test="${dataTable}">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqueryDataTable.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.7.2.custom.css"/>
</c:if>
<link rel="stylesheet" type="text/css" href="/silverpeas/util/styleSheets/globalSP_SilverpeasV5.css"/>
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="/silverpeas/util/styleSheets/globalSP_SilverpeasV5-IE.css"/>
<![endif]-->
<link rel="stylesheet" type="text/css" href="/silverpeas/util/styleSheets/silverpeas_light_style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/${page}.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/${page}.js"></script>
<c:if test="${datePicker || dataTable}">
	<script type="text/javascript" src="/silverpeas/util/javaScript/jquery/jquery-1.5.min.js"></script>
	<script type="text/javascript" src="/silverpeas/util/javaScript/jquery/jquery-ui-1.8.10.custom.min.js"></script>
</c:if>
<c:if test="${datePicker}">
	<script type="text/javascript" src="/silverpeas/util/javaScript/jquery/jquery.ui.datepicker-fr.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/datePicker.js"></script>
</c:if>
<c:if test="${fileTree}">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqueryFileTree.js"></script>
</c:if>
<c:if test="${dataTable}">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqueryDataTableMin.js"></script>
</c:if>