package core.generator.basic;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import core.util.*;
import core.action.Action;
import core.generator.IGenerate;
import core.generator.Item;

public class Select  extends Item implements IGenerate{
	private String source;//"db: from data base, options: from options"
	private String dbtable;
	private String value;
	private String label;
	
	public Select(){
		
	}
	public Select(HttpSession session, Node n){
		setSession(session);
		this.parse(n);
	}
	@Override
	public String generate() {
		String res = "<select ";
		res += super.generate();
		res += ">";
		if("db".equalsIgnoreCase(source)){
			
		}
		else{
			
		}
		res += "</select>";
		return res;
	}

	@Override
	public void parse(Node node) {
		super.parse(node);
		if(node.getAttributes().getNamedItem("source") != null)
			this.source = node.getAttributes().getNamedItem("source").getNodeValue();
		if(node.getAttributes().getNamedItem("dbtable") != null)
			this.dbtable = node.getAttributes().getNamedItem("dbtable").getNodeValue();
		if(node.getAttributes().getNamedItem("value") != null)
			this.value = node.getAttributes().getNamedItem("value").getNodeValue();
		if(node.getAttributes().getNamedItem("label") != null)
			this.label = node.getAttributes().getNamedItem("label").getNodeValue();
	}
	
	public String getComboInfrastructure(){
		try{
			if("db".equalsIgnoreCase(source))
				return  source+","+dbtable+","+	value+","+label;
			else
				return  source+","+				value+","+label;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public String generateOptionsAsString(){
		try{
			String res = "";
			if("db".equalsIgnoreCase(source)){
				Action a = new Action(Action.select, this.dbtable, null);
				a.execute();
				for(int i=0; i<a.getActionResult().getData().size(); i++){
					java.util.Map<String, Object> m = a.getActionResult().getRowAsMap(i);
					String key = m.get(this.value).toString();
					String val = m.get(this.label).toString();
					//res += "<option  value=\""+key+"\" >"+val+"</option>";
					res += "["+key+","+val+"]";
				}
				res += " ";
			}
			else{
				String [] ids = this.value.split(";;");
				String [] vals = this.label.split(";;");
				for(int i=0; i<ids.length; i++){
					res += "["+ids[i]+","+Util.getWordFromDictionary(vals[i])+" ]";
				}
			}
			if(res.length() > 2)
				res = res.substring(0, res.length()-2);
			
			return res;
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		return "";
	}
	
	public String generateOptionsAsHtml(){
		try{
			String res = "<option value=''></option>";
			if("db".equalsIgnoreCase(source)){
				Action a = new Action(Action.select, this.dbtable, null);
				a.execute();
				for(int i=0; i<a.getActionResult().getData().size(); i++){
					java.util.Map<String, Object> m = a.getActionResult().getRowAsMap(i);
					String key = m.get(this.value).toString();
					String val = m.get(this.label).toString();
					val = Util.getWordFromDictionary(val);
					//res += "<option  value=\""+key+"\" >"+val+"</option>";
					res += "<option value='"+key+"'>"+val+"</option>";
				}
			}
			else{
				String [] ids = this.value.split(";;");
				String [] vals = this.label.split(";;");
				for(int i=0; i<ids.length; i++){
					res += "<option id='"+ids[i]+"'>"+Util.getWordFromDictionary(vals[i])+"</option>";
				}
			}
			return res;
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		return "";
	}
	
}
