package com.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.*;

import core.action.Action;
import core.generator.ComponentFinder;
import core.util.expression.ClientRowValues;
import core.util.expression.effect.TableEffects;
import core.util.expression.math.MathExpressionExecuter;
import core.util.expression.math.MathExpressionParser;

/**
 * Servlet implementation class ExpressionServlet
 */
public class ExpressionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExpressionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			request.setCharacterEncoding("utf-8");
			java.util.Map<String, String> m = new HashMap<String, String>();
			Iterator it = request.getParameterMap().keySet().iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				String val = request.getParameter(key);
				m.put(key, val);
			}
			if("tableapplyall".equalsIgnoreCase(m.get("cmd"))){
				executeAllTableEvents(m, response);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void executeAllTableEvents(Map<String, String> m, HttpServletResponse response){
		try{
			//Get table from xml
			String template = m.get("template");
			String id = m.get("id");
			ComponentFinder cf = new ComponentFinder(template, id);
			org.w3c.dom.Node n = cf.find();
			//__________________________________________________
			ClientRowValues crv = new ClientRowValues(m.get("val"));
			crv.convertFromStringToList();
			//now set columns names
			crv.setColumnsNames(m.get("colIds"));
			//Check if there is math expression and if there is apply it. set result in crv
			String expression = null;
			if(n.getAttributes().getNamedItem("math-expression") != null)
				expression = n.getAttributes().getNamedItem("math-expression").getNodeValue();
			if(expression != null && !"".equals(expression)){
				MathExpressionParser mep = new MathExpressionParser();
				mep.setExpressions(expression);
				mep.setRealValues(crv.getMathExpRealValues());
				MathExpressionExecuter mee = new MathExpressionExecuter(mep);
				mee.execute();
				crv.mergeMathExpRealValsWithFields(mep.getRealValues());
			}
			//_____________________________________________________________
			//For each column in table, Check if there is apply expression and if there is apply it. set result in crv
			TableEffects te = new TableEffects(n, crv.getFields());
			te.apply();
			crv.setFields(te.getFields());
			response.setCharacterEncoding("utf-8");
			crv.convertFromListToString();
			response.getWriter().write(crv.getValsAsStr());
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}
}
