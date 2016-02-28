package core.lang;

import java.util.*;
import core.action.Action;
public class LanguageHandler implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Map<String, String> langMap = null;
	public static Map<String, String> getLanguageAsMap(String lang){
		if(langMap == null)
			langMap = new HashMap<String, String>();
		if(langMap.size() == 0){
			try{
				Action action = new Action(Action.select, "LANG", null);
				action.execute();
				for(int i=0; i<action.getActionResult().getData().size(); i++){
	        		Map<String, Object> m = action.getActionResult().getRowAsMap(i);
	        		if("en".equals(lang)){
						langMap.put(m.get("k").toString().trim(), m.get("en").toString().trim());
					}
					else{
						langMap.put(m.get("k").toString().trim(), m.get("ar").toString().trim());
					}
				}
			}
			catch(Exception ee){
				ee.printStackTrace();
			}
		}
		return langMap;
	}
	
	public static String get(String lang, String key){
		String s = "";
		Map<String,String> m = getLanguageAsMap(lang);
		return m.get(key) == null?key:m.get(key);
	}
}