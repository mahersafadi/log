package core.generator.component;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.generator.IGenerate;
import core.generator.Item;
import java.util.*;

import javax.servlet.http.HttpSession;

public class Accordions extends Item implements IGenerate{
	private List<Accordion> accordionItems;
	public Accordions(HttpSession session, Node node) {
		this.setSession(session);
		accordionItems = new ArrayList<Accordion>();
		parse(node);
	}
	
	public List<Accordion> getAccordionItems() {
		return accordionItems;
	}
	public void setAccordionItems(List<Accordion> accordionItems) {
		this.accordionItems = accordionItems;
	}
	
	@Override
	public void parse(Node node) {
		NodeList nl = node.getChildNodes();
		for(int i=0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeType() != Node.TEXT_NODE){
				Accordion acc = new Accordion(this.getSession(), n);
				if(acc != null)
					this.accordionItems.add(acc);
			}
		}
		super.parse(node);
	}
	
	@Override
	public String generate() {
		//generate Headers then generate Contents
		
		String res = "<div style=\"width:100%;height:500px;border-style: outset; overflow: auto;\">";
				res += 	"<div style=\"width:100%;overflow: auto;\">";
		res += "<table width='100%'>";
		for(int i=0; i<accordionItems.size(); i++){
			res += "<tr><td width='100%'>";
			res += accordionItems.get(i).generate();
			res += "</td></tr>";
		}
		res += "</table>";
		return res;
	}
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0; i<this.accordionItems.size(); i++){
			res += accordionItems.get(i).callAfterBuild()+";";
		}
		return res;
	}
}
