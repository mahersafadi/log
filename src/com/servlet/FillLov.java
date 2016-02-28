package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import core.action.Action;
import core.action.ActionResult;

/**
 * Servlet implementation class FillLov
 */
public class FillLov extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FillLov() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String srcName = (String)request.getParameter("srcname");
			String dbId = (String)request.getParameter("dbid");
			String dbName = (String)request.getParameter("dbname");
			String where = (String)request.getParameter("where");
			Action action = new Action(Action.select, srcName, null);
			if(where != null && !"".equals(where))
				action.addField(" 1","=1 ", where);
			action.execute();
			ActionResult aRes = action.getActionResult();
			response.setCharacterEncoding("utf-8");
			if(aRes != null){
				String res = "";
				for(int i=0; i<aRes.getData().size(); i++){
					Map<String, Object> m = aRes.getRowAsMap(i);
					res += m.get(dbId)+","+m.get(dbName)+";";
				}
				if(res.length() > 0){
					res = res.substring(0, res.length() - 1);
				}
				response.getWriter().write(res);
			}
			else
				response.getWriter().write("");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
