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

<%
	String urlServlet = "/silverpeas/RnewsFeedJSONServlet?type=ALL&View=MyFeed&Init=true";
%>

<!DOCTYPE html>
<html>
<head>
	<title><fmt:message key="pageTitle"/></title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.css" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name = "format-detection" content = "telephone=no">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0b1/jquery.mobile-1.0b1.min.js"></script>
	
	<script type="text/javascript">
			function goToServices() {
				var form = document.forms["formServices"];
				form.submit();
			}
			
			function goToProfile() {
				var form = document.forms["formProfile"];
				form.submit();
			}
			
			function getFeedURL() {
				return '<%=urlServlet%>';
			}
			
			$(function(){
				$('#content').append('start');
				var jsonStream = '<%=jsonNewsData%>';
				
				var html='<ul data-role="listview" data-theme="g">';
				
				var jsonObject = $.parseJSON(jsonStream);
				$.each(jsonObject, function(key,map){
					$('#content').append('each');
					$.each(map, function(i,listSocialInfo){
			        	
			            if(i==0) {
			              html += '<li data-role="list-divider">'+listSocialInfo.day+'</li>';
			            } else {
			              $.each(listSocialInfo, function(index,socialInfo){
			                if(socialInfo.type=='RELATIONSHIP') {
			               	 html += getRelationFragment(socialInfo);
			                } else if(socialInfo.type=='STATUS') {
			               	 html += getStatusFragment(socialInfo);
			                } else { 
			                    html += getFragment(socialInfo);
			                }
			              });
			            }
			          });
				});
				
				html += '</ul>';
			    $('#content').append(html);
			})
			
			function getFragment(socialInfo) {
				var fragment = '';
				
				var publicationid = getPublicationIdFromUrl(socialInfo.url);

				fragment += '<li>'; 
				fragment += '<a href=\'javascript:selectPubli('+publicationid+')\'>'; 
				fragment += getAvatarFragment(socialInfo.author);
				fragment += '<p><b>'+socialInfo.author.displayedName+'</b></p>';
				fragment += '<p >'+socialInfo.label +'</p>';
				fragment += '<p >'+ socialInfo.title +' '+ socialInfo.hour+'</p>';
				fragment += '</a>'; 
				fragment += '</li>';

				return fragment;
			}
			
			function getPublicationIdFromUrl(url){
				var id = '';
				if(url.indexOf('Id=')!=-1){
					id = url.substring(url.indexOf('Id=')+3, url.length);
				}
				
				return id;
			}

			function getStatusFragment(socialInfo) {
				var fragment = '';

				fragment += '<li>';
				fragment += getAvatarFragment(socialInfo.author);
				fragment += '<p><b>'+socialInfo.author.displayedName+'</b></p>';
				fragment += '<p>'+socialInfo.title+' '+socialInfo.hour+'</p>';
				fragment += '<p>'+socialInfo.description+'</p>';
				fragment += '</li>';

				return fragment;
			}

			function getRelationFragment(socialInfo) {
				var fragment = '';

				fragment += '<li >';
				fragment += getAvatarFragment(socialInfo.author);
				fragment += '<span class="ui-li-desc" style="position:relative;padding:0px;top:6px;bottom:0px;left:-35px">';  
				fragment += '<img height="60px" width="60px"  align="left"  src="'+socialInfo.title.profilPhoto+'"/>';
				fragment += '<b>&nbsp;&nbsp;'+socialInfo.author.displayedName+'</b><br>';
				fragment += '&nbsp;&nbsp;'+socialInfo.label+' '+socialInfo.hour +'';
				fragment += '</span>';
				fragment += '</li>';

				return fragment;
			}

			function getAvatarFragment(author) {
				var fragment = '';
				fragment += '<img src="'+author.profilPhoto+'"/>';
				return fragment;
			}
			
			function selectPubli(pubId) {
			var form = document.forms["formPublication"];
			form.elements["pubId"].value = pubId;
			form.submit();
		}

	</script>
	

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
			<h1>Info Thread</h1>
			<a href="javascript:document.forms['formHome'].submit()" data-icon="home" >Home</a>
		</div>
		
		
		
		<div id="content" data-role="content"></div>
	
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
		<form name="formServices" action="${pageContext.request.contextPath}/index.html" method="post">
			<input type="hidden" name="action" value="kmelia"/>
			<input type="hidden" name="subAction" value="home"/>
			<input type="hidden" name="userId" value="${userId}"/>
		</form>
		<form name="formProfile" action="${pageContext.request.contextPath}/index.html" method="post">
			<input type="hidden" name="action" value="profile"/>
			<input type="hidden" name="subAction" value="profile"/>
			<input type="hidden" name="userId" value="${userId}"/>
		</form>
		<form name="formPublication" action="${pageContext.request.contextPath}/index.html" method="post">
                        <input type="hidden" name="action" value="kmelia"/>
                        <input type="hidden" name="subAction" value="publication"/>
                        <input type="hidden" name="userId" value="${userId}"/>
                        <input type="hidden" name="spaceId" value="${spaceId}"/>
                        <input type="hidden" name="componentId" value="${component.id}"/>
                        <input type="hidden" name="pubId" value=""/>
                        <input type="hidden" name="nodeId" value=""/>
                        <input type="hidden" name="from" value="Info Thread"/>
        </form>
		<div data-role="footer" data-position="fixed">
			Copyright Silverpeas 1999-2011
		</div>
	</div>
</body>
</html>