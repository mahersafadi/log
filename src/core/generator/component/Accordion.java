package core.generator.component;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.util.*;
import core.generator.IGenerate;
import core.generator.Item;
import java.util.*;

import javax.servlet.http.HttpSession;

public class Accordion extends Item implements IGenerate{
	private String title;
	private List<Item> items;
	public Accordion(HttpSession session, Node node) {
		this.setSession(session);
		items = new ArrayList<Item>();
		parse(node);
	}
	@Override
	public void parse(Node node) {
		super.parse(node);
		if(node.getAttributes().getNamedItem("title") != null){
			this.title = node.getAttributes().getNamedItem("title").getNodeValue();
		}
		NodeList nl = node.getChildNodes();
		for(int i=0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			Item itm = null;
			if(n.getNodeType() != Node.TEXT_NODE){
				if("table".equalsIgnoreCase(n.getNodeName())){
					itm = new Table(this.getSession(), n);
				}
				if(itm != null)
					this.items.add(itm);
			}
		}
	}
	
	private String generateHeader(){
		String res = "<tr>";
		res += "	<td class='label' id='c"+getId()+"' width='10px'>+</td>";
		res += "	<td class='accordion_header_item' id='itemHeader"+getId()+"' width='100%' onclick=\"accordion.accordionClicked('"+getId()+"') \">";
		res += 			Util.getWordFromDictionary(this.title);
		res += "	</td>";
		res += "</tr>";
		return res;
	}
	@Override
	public String generate() {
		String res = "<table width='100%'>";
		//Generate Header, then Generate hidden body
		res += generateHeader();
		res += "<tr>";
		res += 	"<td colspan='2' id='accordionbody"+getId()+"' class='accordion hidden'>";
		//res +=	"Conent number "+getId();
		for(int i=0; i<items.size(); i++)
			res += items.get(i).generate();
		res +=  "</td>";
		res += "</tr>";
		res += "</table>";
		return res;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0; i<this.items.size(); i++){
			res += items.get(i).callAfterBuild()+";";
		}
		return res;
	}
}
