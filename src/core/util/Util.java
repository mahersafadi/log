package core.util;

import java.util.*;

import javax.servlet.http.HttpSession;

import core.util.expression.Field;

public class Util {
	private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	public static void setLangInSession(String l){
		
	}
	public static String getLangFromSession(){
		return "ar";
	}
	
	public static String getWordFromDictionary(String key){
		try{
			String res = core.lang.LanguageHandler.get(getLangFromSession(), key);
			if(res != null)
				return res;
			else
				return key;
				
		}
		catch(Exception e){
			e.printStackTrace();
			return key;
		}
	}
	
	public static Object getFromSession(String id, String key){
		try{
			HttpSession s = sessions.get(id);
			return s.getAttribute(key);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setInSession(String id, String key, Object value){
		try{
			HttpSession s = sessions.get(id);
			s.setAttribute(key, value);
		}
		catch(Exception e){
			
		}
	}
	
	public static void updateSession(HttpSession s){
		sessions.put(s.getId(), s);
	}
}
