package core.generator.component;

import core.generator.IGenerate;
import core.generator.Item;
import core.generator.basic.Cell;
import core.generator.basic.Row;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.util.*;

public class Tab extends Item implements IGenerate{
	private String title;
	private String imag;
	private String selected;
	private List<Item> tabItems;
	private boolean defaultTab;
	
	public Tab(HttpSession session, org.w3c.dom.Node n) {
		tabItems = new ArrayList<Item>();
		setSession(session);
		this.parse(n);
	}
	
	@Override
	public void parse(Node node) {
		try{
			title = node.getAttributes().getNamedItem("title").getNodeValue();
			if(node.getAttributes().getNamedItem("default") != null)
				defaultTab = true;
			if(node.getAttributes().getNamedItem("image") != null)
				imag = node.getAttributes().getNamedItem("image").getNodeValue();
			
			if(node.getAttributes().getNamedItem("selected") != null)
				selected = node.getAttributes().getNamedItem("selected").getNodeValue();
			
			super.parse(node);
			NodeList nl = node.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node n = nl.item(i);
				String nodeName = n.getNodeName();
				if(n.getNodeType() != Node.TEXT_NODE){
					Item itm = null;
				
					if("row".equals(nodeName)){
						itm = new Row(this.getSession(), n);
					}
					else if("cell".equals(nodeName)){
						itm = new Cell(this.getSession(), n);
					}
					else if("form".equals(nodeName)){
						itm = new Form(this.getSession(), n);
					}
					else if("tabs".equals(nodeName)){
						itm = new Tabs(this.getSession(), n);
					}
					else if("accordions".equals(nodeName)){
						itm = new Accordions(this.getSession(), n);
					}
					else if("table".equals(nodeName)){
						itm = new Table(this.getSession(), n);
					}
					else if("lov".equals(n.getNodeName()))
						itm = new core.generator.component.ListOfValue(getSession(), n);
					
					if(itm != null)
						this.tabItems.add(itm);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String generateHeader(){
		String res = "";
		String csClass = "tabheader ";
		if(defaultTab)
			csClass += "tabheader_sel";
		res += "<td id=\"tabheader"+getId()+"\" onclick=\"tab.tabClicked('"+getId()+"')\" class=\""+csClass+"\" align='center'";
		if(this.selected != null && !"".equals(selected))
			res += " selected = '"+selected+"' ";
		res += " >";
		res += "<table>";
		res += "<tr><td>";
		if(imag != null){
			res += "<img src="+imag+" />";
		}
		res += "</td></tr>";
		res += "<tr><td>";
		res += "	"+ Util.getWordFromDictionary(title);
		res += "</td></tr>";
		res += "</table>";
		res += "</td>";
		return res;
	}
	
	@Override
	public String generate() {
		String csClass = "tabbody ";
		if(!defaultTab)
			csClass += " hidden";
		String res = "<table id=\"tabbody"+getId()+"\" class=\""+csClass+"\"  ";
		res += super.generate() +">";
		for(int k=0;k<tabItems.size(); k++){
			res += "<tr><td width='100%' valign='top'>";
			res += tabItems.get(k).generate();
			//res += "<div>Test "+k+"</div>";
			res += "</td></tr>";
		}
		
		res += "</table>";
		return res;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Item> getTabItems() {
		return tabItems;
	}

	public void setTabItems(List<Item> tabItems) {
		this.tabItems = tabItems;
	}

	public boolean isDefaultTab() {
		return defaultTab;
	}

	public void setDefaultTab(boolean defaultTab) {
		this.defaultTab = defaultTab;
	}

	public String getImag() {
		return imag;
	}

	public void setImag(String imag) {
		this.imag = imag;
	}
	
	@Override
	public String callAfterBuild() {
		String res = "";
		for(int i=0; i<this.tabItems.size(); i++){
			res += this.tabItems.get(i).callAfterBuild()+";";
		}
		return res;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
}
