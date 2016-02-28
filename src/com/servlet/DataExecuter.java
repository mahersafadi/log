package com.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.executers.defaultexecuter.DefaultExecuter;

/**
 * Servlet implementation class DataExecuter
 */
public class DataExecuter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataExecuter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		java.util.Map<String, String> m = new HashMap<String, String>();
		Iterator it = request.getParameterMap().keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			String val = request.getParameter(key);
			//val = new String(val.getBytes("utf-8"));
			m.put(key, val);
		}
		m.put("mode", request.getParameter("mode"));
		execute(m, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void execute(java.util.Map<String, String> m , HttpServletResponse response){
		try{
			String mode = m.get( "mode");
			Object result = "";
			if("default".equalsIgnoreCase(mode)){
				DefaultExecuter defaultExecuter = new DefaultExecuter();
				result = defaultExecuter.execute(m);
			}
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result.toString());
		}
		catch(Exception e){
			
		}
	}
}
