<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	
	Enumeration it = request.getSession().getAttributeNames();
	while(it.hasMoreElements()){
		try{
			request.getSession().removeAttribute(it.nextElement().toString());
		}
		catch(Exception ex){}
	}
%>
<html><script>window.location.href='index.jsp'</script><body></body></html>