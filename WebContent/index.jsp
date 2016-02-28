<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="${dir}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="style/style.css"/>
<link type="text/css" rel="stylesheet" href="style/reset.css"/>
<script type="text/javascript" src="script/core.js"></script>
<title>${lang.index_page_name}</title>
</head>
<script type="text/javascript">
	document.onkeydown = function(evt) {
	    evt = evt || window.event;
	    if(evt.keyCode == 13){
	    	defaulthandler.login(evt);
	    }
	};
</script>

<body onload="Generator.init()" class='body' style='background-color: #eee'>
	<table width='100%'>
		<tr>
			<td width="25%" />
			<td align="center" width='50%'>
				<input type="hidden" id='templates' name='templates' value='mycontent'></input>
				<div style='width:100%;' id="mycontent" template='Login.xml'>
					
				</div>
			<td>
			<td width="25%" />
		</tr>
	</table>
</body>
</html>