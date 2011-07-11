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

<c:choose>
	<c:when test="${not empty publications}">
		<fmt:message key="createdBy" var="createdByLabel"/>
		<fmt:message key="edit" var="editLabel"/>
		<fmt:message key="fullDateFormat" var="datePattern"/>
		<fmt:message key="onDate" var="onDateLabel"/>
		<fmt:message key="remove" var="removeLabel"/>
		<fmt:message key="updatedBy" var="updatedByLabel"/>
		<ul class="publications">
			<c:forEach items="${publications}" var="publication">
				<li class="publication">
					<div id="publication${publication.id}">
						<b>${publication.name}</b>
						<c:if test="${not empty publication.description}">
							<br/><span class="fileDescription">${publication.formattedDescription}</span>
						</c:if>
						<c:if test="${not empty publication.attachments}">
							<table width="100%">
								<c:forEach items="${publication.attachments}" var="attachment">
									<tr>
										<td class="fileImage"><img src="${attachment.fileIcon}" id="att_${attachment.fileId}"></td>
										<td><a class="fileTitle" href="${pageContext.request.contextPath}/svp-fs/File?componentId=${componentId}&attachmentId=${attachment.fileId}" target="_blank">${attachment.name}</a>
											<span class="fileSize">(${attachment.fileSize})</span>
										</td>
									</tr>
								</c:forEach>
							</table>
						</c:if>
					</div>
				</li>
			</c:forEach>
		</ul>
		<script type="text/javascript">setHighLight();</script>
	</c:when>
	<c:otherwise>
		<span class="noDocuments"><fmt:message key="noPublications"/></span>
	</c:otherwise>
</c:choose>