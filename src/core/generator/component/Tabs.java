package core.generator.component;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.generator.IGenerate;
import core.generator.Item;
import java.util.*;

import javax.servlet.http.HttpSession;

public class Tabs extends Item implements IGenerate{
	private List<Tab> tabs;
	private String title;
	
	public Tabs(HttpSession session, Node node) {
		this.setSession(session);
		tabs = new ArrayList<Tab>();
		this.parse(node);
	}
	
	@Override
	public void parse(Node node) {
		super.parse(node);
		NodeList nl = node.getChildNodes();
		for(int i=0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeType() != Node.TEXT_NODE){
				Tab t = new Tab(this.getSession(), n);
				if(t != null)
					tabs.add(t);
			}
		}
	}
	
	@Override
	public String generate() {
		String res = "<div ";
		res += super.generate() + "class=\"tabber\""+">";
		res += "<table width='100%'><tr><td><table><tr>";
		
		for(int i=0; i<tabs.size(); i++){
			res += tabs.get(i).generateHeader();
		}
		
		res += "</tr></table></td></tr>";
		res += "<tr>";
		res += "	<td width='100%'  colspan='"+tabs.size()+"'>";
		for(int i=0; i<tabs.size(); i++){
			
			res += tabs.get(i).generate();
		}
		res +=	"	</td>";
		res += "</tr>";
		res += "</table>";
		res += "</div>";
		return res;
	}
	
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0;i<this.tabs.size(); i++){
			res += this.tabs.get(i).callAfterBuild()+";";
		}
		return res;
	}
}
