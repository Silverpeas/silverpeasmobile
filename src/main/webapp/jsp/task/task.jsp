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
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.mobile.datebox.min.css" /> 
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquerymobileoverride.css">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name = "format-detection" content = "telephone=no">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.1.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile-1.0b1.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.mobile.datebox.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/taskDatesConsistency.js"></script>
		
		<script type="text/javascript">
			function updateTask() {
				var datesConsistency = checkDatesConsistency();
				if(datesConsistency){
					var form = document.forms["formTask"];
					form.subAction.value="update";
					form.submit();
				}
			}
	
			function closeTask() {
				var form = document.forms["formTask"];
				form.subAction.value="close";
				form.submit();
			}
			
			function createTask() {
				var datesConsistency = checkDatesConsistency();
				if(datesConsistency){
					var form = document.forms["formTask"];
					form.subAction.value="create";
					form.submit();
				}
			}
			
			function manageStart(selectedValue){
				if("specific"==selectedValue){
					var element = document.getElementById("startDateBlock");
					element.style.visibility='visible';
					element.style.display='';
				} else {
					var element = document.getElementById("startDateBlock");
					element.style.visibility='hidden';
					element.style.display='none';
				}
			}
			
			function manageEnd(selectedValue){
				if("specific"==selectedValue){
					var element = document.getElementById("endDateBlock");
					element.style.visibility='visible';
					element.style.display='';
				} else {
					var element = document.getElementById("endDateBlock");
					element.style.visibility='hidden';
					element.style.display='none';
				}
			}
			
			function manageDateOptions(){
				manageStart(document.getElementById("start").value);
				manageEnd(document.getElementById("end").value);
			}
			
			function roundCompleted(){
				var value = document.getElementById("completed").value;
				value = Math.round(value/10);
				document.getElementById("completed").value = value*10;
			}
		</script>
			
		
		<script type='text/javascript'>
	        $(document).ready(function() {
	            $('#description').keyup(function() {
	                var len = this.value.length;
	                if (len >= 4000) {
	                    this.value = this.value.substring(0, 4000);
	                }
	                $('#charLeft').text(4000 - len);
	            });
	        });
	    </script>
	</head>
	
	<body onLoad="javascript:manageDateOptions()">
		
		<div  data-role="page">
	
			<form name="formHome" action="${pageContext.request.contextPath}/index.html" method="post">
				<input type="hidden" name="action" value="login"/>
				<input type="hidden" name="subAction" value="home"/>
				<input type="hidden" name="userId" value="${userId}"/>
			</form>
			<div  data-role="header" data-position="fixed">
				<a href="#" data-icon="back" data-rel="back">Back</a>
				<h1> ${task.name}</h1>
				<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
			</div>
			
			<div id="content" data-role="content">
				<form name="formTask" action="${pageContext.request.contextPath}/index.html" method="post">
					<input type="hidden" name="action" value="task"/>
					<input type="hidden" name="subAction" value=""/>
					<input type="hidden" name="userId" value="${userId}"/>
					<input type="hidden" name="taskId" value="${task.id}"/>
					<input type="hidden" name="datesModified" value="false"/>
					
					<input type="hidden" id="daysBeforeEndOfWeek" name="daysBeforeEndOfWeek" value="${daysBeforeEndOfWeek}"/>
					<input type="hidden" id="daysBeforeEndOfNextWeek" name="daysBeforeEndOfNextWeek" value="${daysBeforeEndOfNextWeek}"/>
					
					<div data-role="fieldcontain">
						<label for="name">Name</label> 
						<input type="text" name="name" id="name" value="${task.name}" />
					</div>
					
					<div data-role="fieldcontain">
						<label for="description">Description</label> 
						<textarea cols="40" rows="8" name="description" id="description" style="width: 98%">${task.description}</textarea>
						<span style="font-size:smaller;">&nbsp;<span id="charLeft">${4000 - fn:length(task.description)}</span>  Characters left</span>
						<%-- <input type="text" name="description" id="description" value="${task.description}" /> --%>
					</div>
					
					<div id="datesError" style="color: #ff0000; font-weight:bold;">
					</div>
					
					<div data-role="collapsible" data-collapsed="true" id="startBlock">
						<h3>Start Date ${task.startDateLabel}</h3>
						<div data-role="fieldcontain">
							<label for="start">Start</label>
							<select name="startDateOption" id="start" onChange="javascript:manageStart(this.value); document.formTask.datesModified.value='true';" onClick="javascript:hideDateError()">
								<c:if test="${task.startDateOption == 'today'}">
									<option value="today" selected>Today</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'today'}">
									<option value="today">Today</option> 
								</c:if>
								<c:if test="${task.startDateOption == 'tomorrow'}">
									<option value=tomorrow selected>Tomorrow</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'tomorrow'}">
									<option value="tomorrow">Tomorrow</option> 
								</c:if>
								<c:if test="${task.startDateOption == 'thisweek'}">
									<option value=thisweek selected>This Week</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'thisweek'}">
									<option value="thisweek">This Week</option> 
								</c:if>
								<c:if test="${task.startDateOption == 'nextweek'}">
									<option value=nextweek selected>Next Week</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'nextweek'}">
									<option value="nextweek">Next Week</option> 
								</c:if>
								<c:if test="${task.startDateOption == 'later'}">
									<option value=later selected>Later</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'later'}">
									<option value="later">Later</option> 
								</c:if>
								<c:if test="${task.startDateOption == 'specific'}">
									<option value=specific selected>Specific Date/Time</option> 
								</c:if>
								<c:if test="${task.startDateOption != 'specific'}">
									<option value="specific">Specific Date/Time</option> 
								</c:if>
							</select>
							
							<div id="startDateBlock">
								<label for="startDate" id="startDateLabel">Start Date</label>
								 <input name="startDate" id="startDate" type="date" data-role="datebox" data-options='{"mode":"calbox", "dateFormat":"DD-MM-YYYY", "noButtonFocusMode":true, "useDialogForceFalse":true, "closeCallback":"document.formTask.datesModified.value=\"true\";"}' value="${task.startDate}" onClick="javascript:hideDateError()">
								<%--<input name="startDate" id="startDate" type="date" value="${task.startDate}" onClick="javascript:hideDateError()">--%>
								
								<label for="startTime" id="startTimeLabel">Start Time</label>
								<input name="startTime" id="startTime" type="date" data-role="datebox" data-options='{"mode":"timebox", "noButtonFocusMode":true, "useDialogForceFalse":true, "closeCallback":"document.formTask.datesModified.value=\"true\";"}' value="${task.startTime}" onClick="javascript:hideDateError()">
	   						</div>
						</div>
					</div>
					
					<div data-role="collapsible" data-collapsed="true"  id="endBlock">
						<h3>End Date  ${task.endDateLabel}</h3>
						<div data-role="fieldcontain">
							<label for="end">End</label>
							<select name="endDateOption" id="end" onChange="javascript:manageEnd(this.value); document.formTask.datesModified.value='true';" onClick="javascript:hideDateError()">
								<c:if test="${task.endDateOption == 'today'}">
									<option value="today" selected>Today</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'today'}">
									<option value="today">Today</option> 
								</c:if>
								<c:if test="${task.endDateOption == 'tomorrow'}">
									<option value=tomorrow selected>Tomorrow</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'tomorrow'}">
									<option value="tomorrow">Tomorrow</option> 
								</c:if>
								<c:if test="${task.endDateOption == 'thisweek'}">
									<option value=thisweek selected>This Week</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'thisweek'}">
									<option value="thisweek">This Week</option> 
								</c:if>
								<c:if test="${task.endDateOption == 'nextweek'}">
									<option value=nextweek selected>Next Week</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'nextweek'}">
									<option value="nextweek">Next Week</option> 
								</c:if>
								<c:if test="${task.endDateOption == 'later'}">
									<option value=later selected>Later</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'later'}">
									<option value="later">Later</option> 
								</c:if>
								<c:if test="${task.endDateOption == 'specific'}">
									<option value=specific selected>Specific Date/Time</option> 
								</c:if>
								<c:if test="${task.endDateOption != 'specific'}">
									<option value="specific">Specific Date/Time</option> 
								</c:if>
							</select>
							
							<div id="endDateBlock">
								<label for="endDate" id="endDateLabel">End Date</label>
								<input name="endDate" id="endDate" type="date" data-role="datebox" data-options='{"mode":"calbox", "dateFormat":"DD-MM-YYYY", "noButtonFocusMode":true, "useDialogForceFalse":true, "closeCallback":"document.formTask.datesModified.value=\"true\";"}' value="${task.endDate}" onClick="javascript:hideDateError()">
								<%-- <input name="endDate" id="endDate" type="date" value="${task.endDate}" onClick="javascript:hideDateError()">--%>
								
								<label for="endTime" id="endTimeLabel">End Time</label>
								<input name="endTime" id="endTime" type="date" data-role="datebox" data-options='{"mode":"timebox", "noButtonFocusMode":true, "useDialogForceFalse":true, "closeCallback":"document.formTask.datesModified.value=\"true\";"}' value="${task.endTime}" onClick="javascript:hideDateError()">
							</div>
						</div>
					</div>
					
					<c:if test='${fn:length(task.attendees) != 0}'>
						<div data-role="fieldcontain">	
							
							<label for="attendeeId">Task Attendee:</label>
							<c:forEach items="${listUsers}" var="attendee">
								<c:if test="${attendee.id == task.attendeeId}">
									${attendee.firstName} ${attendee.lastName}
								</c:if> 
							</c:forEach>
						</div>
					</c:if>
					<c:if test='${fn:length(task.attendees) == 0}'>
						<div data-role="fieldcontain">	
							
							<label for="attendeeId">Task Attendee:</label>
							<select name="attendeeId" id="attendee">
							<c:forEach items="${listUsers}" var="attendee">
									<option value="${attendee.id}">${attendee.firstName} ${attendee.lastName}</option>
							</c:forEach>
							</select>
						</div>
					</c:if>
					
					<c:if test='${fn:length(task.id) != 0}'>
						<div data-role="fieldcontain">
							<label for="completed">Completion</label>
						 	<input type="range" name="completed" id="completed" value="${task.percentCompleted}" min="0" max="100" onChange="javascript:roundCompleted();" />
						</div>
					</c:if>
					
					<div data-role="fieldcontain">
						<c:if test='${fn:length(task.id) != 0}'>
							<fieldset class="ui-grid-a">
								<div class="ui-block-a"><a href="javascript:updateTask();" data-role="button" data-theme="a">Update</a></div>
								<div class="ui-block-b"><a href="javascript:closeTask();" data-role="button" data-theme="a">Close</a></div>
							</fieldset>
						</c:if>
						<c:if test='${fn:length(task.id) == 0}'>
							<fieldset class="ui-grid-a">
								<a href="javascript:createTask();" data-role="button" data-theme="a">Create</a>
							</fieldset>
						</c:if>
					</div>
				</form>
			</div>
		
			<div data-role="footer" data-position="fixed">
				Copyright Silverpeas 1999-2011
			</div>
		</div>
		
	</body>
</html>