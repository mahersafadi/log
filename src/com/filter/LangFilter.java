package com.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import core.util.*;
import java.util.*;

/**
 * Servlet Filter implementation class LangFilter
 */
public class LangFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LangFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest r = (HttpServletRequest)request;
		Util.updateSession(r.getSession());
		request.setCharacterEncoding("windows-1256");
		String currLang  = "ar"; 
		if(currLang  == null){
			currLang = "ar";
	    }
		request.setAttribute("currLang", "ar");
		//Get Language From Data Base
		//Put it in map
		request.setAttribute("lang", core.lang.LanguageHandler.getLanguageAsMap(currLang));
        request.setAttribute("dir", "ar".equals(currLang)?"RTL":"LTR");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
