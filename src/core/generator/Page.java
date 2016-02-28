package core.generator;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.w3c.dom.*;
import core.xml.XmlParser;

public class Page extends Item implements IGenerate{
	private List<Item> items;
	private List<String> links;
	private List<String> scripts;
	private String title;
	
	public Page(){
		items = new ArrayList<Item>();
	}
	
	public Page(HttpSession session, String templateName){
		this.setSession(session);
		//Get template full path
		//load it as document, then call the other constructor
		core.Root r = new core.Root();
		String path = r.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.indexOf("WEB-INF")+7);
		path += "/templates/"+templateName;
		Document doc = XmlParser.getXmlFileAsDocument(path, "cp1256");
		parse(doc);
	}
	
	public Page(HttpSession session, Document doc){
		setSession(session);
		parse(doc);
	}
	
	private void parse(Document doc){
		items = new ArrayList<Item>();
		Node pageNode = doc.getFirstChild();
		if("page".equals(pageNode.getNodeName())){
			NodeList nl = pageNode.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node n = nl.item(i);
				Item item = null;
				if("form".equalsIgnoreCase(n.getNodeName()))
					item = new core.generator.component.Form(getSession(), n);
				else if("menu".equalsIgnoreCase(n.getNodeName())){
					item = new core.generator.component.Menu(getSession(), n);
				}
				else if("tabs".equalsIgnoreCase(n.getNodeName())){
					item = new core.generator.component.Tabs(getSession(), n);
				}
				else if("table".equalsIgnoreCase(n.getNodeName())){
					item = new core.generator.component.Table(getSession(), n);
				}
					
				if(item != null)
					this.items.add(item);
			}
		}
	}
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<String> getLinks() {
		return links;
	}
	public void setLinks(List<String> links) {
		this.links = links;
	}
	public List<String> getScripts() {
		return scripts;
	}
	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}

	@Override
	public String generate() {
		// TODO Auto-generated method stub
		String str = "";
		for(int i=0;i<items.size(); i++){
			Item item = items.get(i);
			str += item.generate();
		}
		str += "@@";
		for(int i=0;i<items.size(); i++){
			Item item = items.get(i);
			str += item.callAfterBuild()+";";
		}
		return str;
	}
	
	public String generate(String id){
		try{
			String [] ids = id.split(".");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return  "";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
