package com.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.executers.business.IBusiness;

/**
 * Servlet implementation class BusinessServlet
 */
public class BusinessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusinessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//collect Fields as Map of string
		//new Business Executer
		//cast to IBusiness
		//execute it
		request.setCharacterEncoding("utf-8");
		java.util.Map<String, String> m = new HashMap<String, String>();
		Iterator it = request.getParameterMap().keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			String val = request.getParameter(key);
			//val = new String(val.getBytes("utf-8"));
			m.put(key, val);
		}
		String executer = m.get("e");
		if(executer == null || "".equals(executer)){
			response.getWriter().write("<script>alert('Executer is Not Defained');</script>");
		}
		else{
			try{
				//ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				//Class cls = classLoader.loadClass(executer);
				Class cls = Class.forName(executer);
				IBusiness business = (IBusiness)cls.newInstance();
				business.setRequestData(m);
				business.setHttpSession(request.getSession());
				business.execute();
				response.getWriter().write(business.getResponseAsString());
			}
			catch(Exception ex){
				response.getWriter().write(ex.getMessage());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
