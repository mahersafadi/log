package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.action.Action;
import core.util.Util;

/**
 * Servlet implementation class LoginExecuter
 */
public class LoginExecuter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginExecuter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		java.util.Map <String, Object> m = new HashMap<String, Object>();
		m.putAll(request.getParameterMap());
		String [] content = (String[])m.get("content");
		if(content != null && !"".equals(content)){
			String [] ssArr = content[0].split("@@");
			for(int i = 0; i<ssArr.length; i++){
				String key = ssArr[i].split(":")[0];
				String value = ssArr[i].substring(ssArr[i].indexOf(":")+1, ssArr[i].length());
				m.put(key, value);
			}
		}
		else{
			try{
				BufferedReader br = request.getReader();
				StringBuilder sb = new StringBuilder();
				while(br.ready()){
					 sb.append(br.readLine());
				}
				String ss = sb.toString();
				String [] ssArr = ss.split("@@");
				for(int i = 0; i<ssArr.length; i++){
					String key = ssArr[i].split(":")[0];
					String value = ssArr[i].substring(ssArr[i].indexOf(":"), ssArr[i].length());
					m.put(key, value);
				}
			}
			catch(Exception e){
				
			}
		}
		handle(request, response, m);
	}
	private void handle(HttpServletRequest request, HttpServletResponse response, java.util.Map<String, Object> params){
		String cmd = (String)request.getParameter("cmd");
		if("login".equals(cmd)){
			try{
				String userName = (String)params.get("username");
				String password = (String)params.get("password");
				Action action = new Action(Action.select, "users", null);
				action.addField("user_name","=",  userName);
				action.execute();
				boolean ok = false;
				Map m = null;
				if(action.getActionResult() != null && action.getActionResult().getData().size() > 0){
					m = action.getActionResult().getRowAsMap(0);
					if(password != null){
						if(password.equals((String)m.get("pass_word")))
							ok = true;
					}
					else
						ok = true;
				}
				if(ok){
					request.getSession().setAttribute("user",m);
					Util.setInSession(request.getSession().getId(), "user", m);
					response.getWriter().write("ok");
				}
				else
					response.getWriter().write("not ok");
			}
			catch(Exception e){
				try{
					response.getWriter().write("err"+e.getMessage().substring(0, 100));
				}
				catch(Exception ee){;}
			}
			
		}
	}
}
