<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	if(request.getSession().getAttribute("user") == null){
 %>
<html><script>window.location.href='index.jsp'</script><body></body></html>
<%
	}
	else{
%>
<html dir="${dir}">
<head>
	<meta http-equiv="Content-Type" content="text/html; utf-8">
	<link type="text/css" rel="stylesheet" href="style/style.css"/>
	<!--  <link rel="STYLESHEET" type="text/css" href="dhtml/codebase/dhtmlxgrid.css">
	<link rel="stylesheet" type="text/css" href="dhtml/codebase/skins/dhtmlxgrid_dhx_skyblue.css">
	<link rel="STYLESHEET" type="text/css" href="dhtml/codebase/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_skins.css">
	
	<script  src="dhtml/codebase/dhtmlxcommon.js"></script>
	<script  src="dhtml/codebase/dhtmlxgrid.js"></script>        
	<script  src="dhtml/codebase/dhtmlxgridcell.js"></script>        
	<script  src="dhtml/codebase/excells/dhtmlxgrid_excell_dhxcalendar.js"></script>
	
	<script  src="dhtml/codebase/dhtmlxCalendarcodebase/dhtmlxcalendar.js"></script>
	<script  src="dhtml/codebase/dhtmlxCalendarcodebase/dhtmlxcommon.js"></script>
	 -->
	 <link type="text/css" rel="stylesheet" href="style/reset.css"/>
	 
	 <link rel="STYLESHEET" type="text/css" href="dhtml/codebase/dhtmlxgrid_std.css">
	 
	<script type="text/javascript" src="dhtml/codebase/dhtmlxgrid_std.js"></script>	
	<script type="text/javascript" src="script/core.js"></script>
	<script type="text/javascript" src="script/business.js"></script>
	<title>${lang.home_page_name}</title>
</head>
<body onload="Generator.init();" class='body'>
	<input type="hidden" id='templates' name='template' value='content1,content2'></input>
	<div style='width:100%' id="mycontent">
		<table width='100%'>
			<tr>
				<td height="60px" width="100%" valign='top'>
					<div style='width:100%' id="content1" template='basic_menu.xml'>
						
					</div>
				</td>
			</tr>
			<tr>
				<td width='100%' height='450px' valign='top'>
					<table style='width:100%'>
						<tr>
							<td id="content2" template='main_tabs.xml' width="100%">
								
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
<%}%>