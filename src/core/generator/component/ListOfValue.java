package core.generator.component;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.util.*;
import core.generator.IGenerate;
import core.generator.Item;

public class ListOfValue extends Item implements IGenerate{
	private String kind;
	private String sourceName;
	private String where;
	private String dbId;
	private String pageId;
	private String dbName;
	private String pageName;
	private String label;
	private String title;
	
	public ListOfValue(HttpSession session, Node node) {
		this.setSession(session);
		this.parse(node);
	}
	
	@Override
	public void parse(Node node) {
		try{
			super.parse(node);
			if(node.getAttributes().getNamedItem("title") != null){
				this.title = node.getAttributes().getNamedItem("title").getNodeValue();
			}
			
			NodeList nl = node.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node n = nl.item(i);
				if(n.getNodeType() != Node.TEXT_NODE){
					if("source".equalsIgnoreCase(n.getNodeName())){
						Node sourceKind = n.getAttributes().getNamedItem("kind");
						if(sourceKind != null)
							this.kind = sourceKind.getNodeValue();
						
						Node srceName = n.getAttributes().getNamedItem("name");
						if(srceName != null)
							this.sourceName = srceName.getNodeValue();
					}
					else if("where".equalsIgnoreCase(n.getNodeName())){
						if(n.getAttributes() != null){
							Node whereValueNode = n.getAttributes().getNamedItem("value");
							if(whereValueNode != null){
								this.where = whereValueNode.getNodeValue();
							}
						}
					}
					else if("id".equalsIgnoreCase(n.getNodeName())){
						Node dbIdNode = n.getAttributes().getNamedItem("dataname");
						if(dbIdNode != null){
							this.dbId = dbIdNode.getNodeValue();
						}
						Node pageIdNode = n.getAttributes().getNamedItem("pagename");
						if(pageIdNode != null){
							this.pageId = pageIdNode.getNodeValue();
						}
					}
					else if("display".equalsIgnoreCase(n.getNodeName())){
						Node dbNameNode = n.getAttributes().getNamedItem("dataname");
						if(dbNameNode != null){
							this.dbName = dbNameNode.getNodeValue();
						}
						Node pageNameNode = n.getAttributes().getNamedItem("pagename");
						if(pageNameNode != null){
							this.pageName = pageNameNode.getNodeValue();
						}
					}
					else if("label".equalsIgnoreCase(n.getNodeName())){
						Node labelNode = n.getAttributes().getNamedItem("value");
						if(labelNode != null){
							this.label = labelNode.getNodeValue();
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public String generate() {
		String res = "<table ";
		res +=  super.generate();
		res += " width='100%' >";
		res += " <tr>";
		res += "	<td>";
		res += "		<table>";
		res += " 			<tr>";
		res += "				<td class='label'>";
		res += 						Util.getWordFromDictionary(label); 
		res += "				</td>";
		res += "				<td>";
		res += "					<input type='hidden' id='"+getPageId()+"' >";
		String resrouce = "kind:"+kind+";srcname:"+sourceName+
							";where:"+where+";dbid:"+dbId+";dbname:"+dbName+
							";pageid:"+pageId+";pagename:"+pageName;
		res += "					<input type='hidden' id=\"lov"+getId()+"resource\" value=\""+resrouce+"\" />";
		res += "					<input type='text' id='"+getPageName()+"' readonly='true'>";
		res += "				</td>";
		res += "				<td style='cursor:pointer;'>";
		res += "					<img src='images/lov.png' onclick=\"defaulthandler.displaylov('"+getId()+"')\">";
		res += "					</img>";
		res += "				</td>";
		res += "				<td colspan='3'>";
		res += 						drowLOV();
		res += "				<td>";
		res += " 			</tr>";
		res += "		</table>";
		res += "	</td>";
		res += " </tr>";
		res += "</table>";
		return res;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	private String drowLOV(){
		String res = "<div id='lov"+getId()+"popup' class=\"popupArea hidden\" >";
		res += "<table style='border:1px solid red;'>";
		res += " <tr>";
		res += "  <td>";
		if(this.title != null)
			res += Util.getWordFromDictionary(this.title);
		res += "  </td>";
		res += "  <td style='cursor:pointer;'>";
		res += "	<img src='images/close.png' onclick=\"defaulthandler.displaylov('"+getId()+"')\">";
		res += "  </td>";
		res += "</tr>";
		res += " <tr>";
		res += "  <td>";
		res += "	<input type='text' />";
		res += "  </td>";
		res += " </tr>";
		res += " <tr>";
		res += "	<td colspan='2'>";
		res += "	  <div style='overflow:auto;height:200px;'>";
		res += "		<table class='wizardTable' width='100%'>";
		res += "			<thead>";
		res += "				<th>"+Util.getWordFromDictionary("id")+"</th>";
		res += "				<th>"+Util.getWordFromDictionary("name")+"</th>";
		res += "			<thead>";
		res += "				<tbody id='lov"+getId()+"content' >";
		res += "				<tbody>";
		res += "		<table>";
		res += "	  <div>";
		res += "	</td>";
		res += " </tr>";
		res += "</table>";
		res += "</div>";
		return res;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String callAfterBuild() {
		// TODO Auto-generated method stub
		return super.callAfterBuild();
	}
}
