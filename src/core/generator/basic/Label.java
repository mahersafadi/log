package core.generator.basic;

import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import core.util.*;
import core.generator.IGenerate;
import core.generator.Item;

public class Label  extends Item implements IGenerate{
	private String value;
	public Label(HttpSession session, Node n){
		setSession(session);
		this.parse(n);
	}
	
	@Override
	public String generate() {
		String str = "<span ";
		str += super.generate()+">";
		str += getGeneratedValue();
		str += "</span>";
		return str;
	}

	@Override
	public void parse(Node node) {
		if(node != null){
			super.parse(node);
			Node valueNode = node.getAttributes().getNamedItem("value");
			if(valueNode != null)
				this.value = valueNode.getNodeValue();
			else
				this.value = "label"+(new java.util.Date().getTime());
			
			if(this.getCsClass() == null)
				this.setCsClass("label");
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getGeneratedValue(){
		try{
			String s = this.getValue();
			if(s.startsWith("#")){
				s = s.substring(1);
				String ss = s.toLowerCase();
				if(ss.startsWith("session")){
					s = s.substring("session.".length());
					StringTokenizer st = new StringTokenizer(ss, ".");
					Object currObjInSession = null;
					while(st.hasMoreTokens()){
		                String curr = st.nextToken();
						if("session".equals(curr)){
							curr = st.nextToken();
							currObjInSession = getSession().getAttribute(curr);
						}
						else{
							if(currObjInSession != null){
								if(currObjInSession instanceof java.util.Map){
									currObjInSession = ((java.util.Map)currObjInSession).get(curr);
								}
							}
						}
					}
					s = currObjInSession == null ? "": currObjInSession.toString();
					return s;
				}
			}
			else
				return Util.getWordFromDictionary(this.getValue());
			return "";
		}
		catch(Exception e){
			return "Label"+getId()+"..not exit";
		}
	}
}
