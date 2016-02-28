package core.generator.basic;

import java.util.*;

import javax.servlet.http.HttpSession;

import core.generator.*;
import core.generator.basic.*;
import org.w3c.dom.*;

public class Cell extends Item implements IGenerate{
	List<Item> items;
	private String colspan;
	public Cell(HttpSession session, Node n){
		setSession(session);
		parse(n);
	}

	@Override
	public String generate() {
		String str = "<td nowrap='nowrap' style='height:30px !important;' ";
		if(colspan != null && !"".equals(colspan))
			str += " colspan='"+colspan+"' ";
		str += super.generate();
		str += setAppropriateClass();
		str += " >";
		for(int i=0;i<items.size(); i++){
			Item item = items.get(i);
			str += item.generate();
		}
		str += "</td>";
		return str;
	}

	@Override
	public void parse(Node node) {
		items = new ArrayList<Item>();
		super.parse(node);
		Node colspanNode = node.getAttributes().getNamedItem("colspan");
		if(colspanNode != null)
			this.colspan = colspanNode.getNodeValue();
		NodeList nl = node.getChildNodes();
		for(int i=0;i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeType() != Node.TEXT_NODE){
				Item item = null;
				if("text".equals(n.getNodeName()))
					item = new core.generator.basic.Text(getSession(), n);
				else if("button".equals(n.getNodeName()))
					item = new core.generator.basic.Button(getSession(), n);
				else if("image".equals(n.getNodeName()))
					item = new core.generator.basic.Image(getSession(), n);
				else if("label".equals(n.getNodeName()))
					item = new core.generator.basic.Label(getSession(), n);
				else if("select".equals(n.getNodeName()))
					item = new core.generator.basic.Select(getSession(), n);
				else if("date".equals(n.getNodeName()))
					item = new core.generator.basic.Date(getSession(), n);
				else if("table".equals(n.getNodeName()))
					item = new core.generator.component.Table(getSession(), n);
				else if("tree".equals(n.getNodeName()))
					;
				else if("tabs".equals(n.getNodeName()))
					item = new core.generator.component.Tabs(getSession(), n);
				else if("form".equals(n.getNodeName()))
					item = new core.generator.component.Form(getSession(), n);
				else if("accordion".equals(n.getNodeName()))
					item = new core.generator.component.Accordions(getSession(), n);
				else if("lov".equals(n.getNodeName()))
					item = new core.generator.component.ListOfValue(getSession(), n);
				
				if(item != null)
					this.items.add(item);
			}
			
		}
	}
	
	private String setAppropriateClass(){
		if(this.getCsClass() == null){
			if(this.items != null && this.items.size()>0){
				if(this.items.get(0) instanceof Button)
					return " class = 'btn'";
				else if(this.items.get(0) instanceof Label)
					return " class = 'label'";
				else
					return "";
			}
			else
				return "";
		}
		else
			return "";
	}
	
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0 ;i<items.size(); i++){
			res += super.callAfterBuild()+";";
		}
		return res;
	}
}
