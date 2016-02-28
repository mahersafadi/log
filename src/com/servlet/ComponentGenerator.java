package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.generator.ComponentFinder;

/**
 * Servlet implementation class ComponentGenerator
 */
public class ComponentGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComponentGenerator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("ComponentGenerator.doGet");
		String cmd = (String) request.getParameter("cmd");
		if("recreate".equals(cmd)){
			String template = (String) request.getParameter("template");
			String id = (String) request.getParameter("id");
			String type = (String) request.getParameter("type");
			if(template != null &&  !"".equals(template)){
				response.setCharacterEncoding("utf-8");
				ComponentFinder cf = new ComponentFinder(template, id);
				org.w3c.dom.Node n = cf.find();
				if(n != null){
					String res = cf.generateContentAndGetId(type, request.getSession(), n);
					response.setCharacterEncoding("utf-8");
					response.getWriter().write(res);
				}
			}
		}
		else{
			String template = (String) request.getParameter("template");
			if(template != null &&  !"".equals(template)){
				core.generator.Page p = new core.generator.Page(request.getSession() , template);
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(p.generate());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("ComponentGenerator.doPost");
		doGet(request, response);
	}
}
