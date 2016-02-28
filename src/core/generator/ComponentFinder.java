package core.generator;

import javax.servlet.http.HttpSession;

import org.w3c.dom.*;
import core.xml.XmlParser;

public class ComponentFinder {
	private String templateName;
	private String id;
	private org.w3c.dom.Node node;
	public ComponentFinder(String template, String id){
		this.templateName = template;
		this.id = id;
	}
	
	public org.w3c.dom.Node find(){
		try{
			String [] ids = id.split(":");
			core.Root r = new core.Root();
			String path = r.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.substring(1, path.indexOf("WEB-INF")+7);
			path += "/templates/"+templateName;
			Document doc = XmlParser.getXmlFileAsDocument(path, "cp1256");
			Node n = doc.getFirstChild();
			if(ids[0].equals(n.getAttributes().getNamedItem("id").getNodeValue())){
				int j = 1;
				boolean ok = false;
				Node currNode = null;
				for(int i=0; i<n.getChildNodes().getLength()&&!ok&&j<ids.length; i++){
					currNode = n.getChildNodes().item(i);
					if(currNode.getNodeType() != Node.TEXT_NODE){
						String currId =currNode.getAttributes().getNamedItem("id").getNodeValue();
						if(currId.equals(ids[j])){
							if(currNode.getChildNodes() != null){
								if(j < ids.length-1){
									i = 0;
									n = currNode;
									j++;
								}
								else
									ok = true;
							}
							else{
								ok = true;
							}
						}
					}
				}
				return currNode;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String generate(String type,HttpSession session,  Node node){
		if("table".equals(type)){
			core.generator.component.Table t = 
					new core.generator.component.Table(session, node);
			return t.generate();
		}
		return "";
	}
	public String generateContentAndGetId(String type,HttpSession session,  Node node){
		String res = this.generate(type, session, node);
		if(res != null){
			String header = res.substring(0, res.indexOf(">"));
			String content = res.substring(header.length(), res.length());
			content = content.substring(1, content.length()-"</table>".length());
			String id = header.substring(header.indexOf("id"), header.length());
			id = id.substring(4, id.length());
			id = id.substring(0, id.indexOf("'"));
			return id+"@@"+content;
		}
		
		return "";
	}
}
