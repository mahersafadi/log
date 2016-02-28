package core.generator.basic;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import core.util.*;
import core.generator.IGenerate;
import core.generator.Item;

public class Button extends Item implements IGenerate{
	private String label;
	private String action;
	
	public Button(HttpSession session, Node n){
		setSession(session);
		super.parse(n);
		this.parse(n);
	}
	@Override
	public String generate() {
		// TODO Auto-generated method stub
		String str = "<a";
		str += super.generate();
		str += ">";
		str += Util.getWordFromDictionary(label);
		str += "</a>";
		
		return str;
	}

	@Override
	public void parse(Node node) {
		// TODO Auto-generated method stub
		super.parse(node);
		Node labelNode = node.getAttributes().getNamedItem("label");
		if(labelNode != null)
			label = labelNode.getNodeValue();
		else
			label = "btn";
		
		Node actionNode = node.getAttributes().getNamedItem("action");
		if(actionNode != null)
			action = actionNode.getNodeValue();
		else
			action = "DefaultAction.execute";
		
		if(getOnClick() == null)
			setOnClick("applyAction");
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
