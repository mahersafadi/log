package core.generator.component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.util.*;
import core.generator.IGenerate;
import core.generator.Item;
import core.generator.basic.Date;
import core.generator.basic.Select;
import core.generator.basic.Text;

public class Table extends Item implements IGenerate{
	private String title;
	private String dbTable;
	private boolean allowInsert;
	private boolean allowDelete;
	private boolean fillData;
	private String saveMethod;
	private String deleteMethod;
	private String searchMethod;
	private String newMethod;
	private String methExpression;
	
	public String getMethExpression() {
		return methExpression;
	}

	public void setMethExpression(String methExpression) {
		this.methExpression = methExpression;
	}

	public String getNewMethod() {
		return newMethod;
	}

	public void setNewMethod(String newMethod) {
		this.newMethod = newMethod;
	}

	private List<Column> columns;
	private String where;
	private String orderBy;
	private String uniqeName;
	public String getUniqeName() {
		return uniqeName;
	}

	public void setUniqeName(String uniqeName) {
		this.uniqeName = uniqeName;
	}

	public Table (HttpSession session, Node node){
		this.setSession(session);
		this.columns = new ArrayList<Column>();
		this.parse(node);
	}
	
	@Override
	public void parse(Node node) {
		super.parse(node);
		this.allowInsert = false;
		this.allowDelete = false;
		this.allowDelete = false;
		this.fillData  = false;
		this.uniqeName = getUniqueName(node);
		if(node.getAttributes() != null){
			if(node.getAttributes().getNamedItem("title") != null)
				this.title = node.getAttributes().getNamedItem("title").getNodeValue();
			if(node.getAttributes().getNamedItem("db_table") != null)
				this.dbTable = node.getAttributes().getNamedItem("db_table").getNodeValue();
			if(node.getAttributes().getNamedItem("where") != null)
				this.where = node.getAttributes().getNamedItem("where").getNodeValue();
			if(node.getAttributes().getNamedItem("order-by") != null)
				this.orderBy = node.getAttributes().getNamedItem("order-by").getNodeValue();
			if(node.getAttributes().getNamedItem("allow_insert") != null){
				String v = node.getAttributes().getNamedItem("allow_insert").getNodeValue();
				v = v.trim();
				if(v != null){
					if("true".equalsIgnoreCase(v))
						this.allowInsert = true;
					else if("1".equalsIgnoreCase(v))
						this.allowInsert = true;
				}
			}
			
			if(node.getAttributes().getNamedItem("allow_delete") != null){
				String v = node.getAttributes().getNamedItem("allow_delete").getNodeValue();
				v = v.trim();
				if(v != null){
					if("true".equalsIgnoreCase(v))
						this.allowDelete = true;
					else if("1".equalsIgnoreCase(v))
						this.allowDelete = true;
				}
			}
			
			/*if(node.getAttributes().getNamedItem("allow_update") != null){
				String v = node.getAttributes().getNamedItem("allow_update").getNodeValue();
				v = v.trim();
				if(v != null){
					if("true".equalsIgnoreCase(v))
						this.allowUpdate = true;
					else if("1".equalsIgnoreCase(v))
						this.allowUpdate = true;
				}
			}*/
			
			if(node.getAttributes().getNamedItem("savemethod") != null){
				String saveMeth = node.getAttributes().getNamedItem("savemethod").getNodeValue();
				saveMeth = saveMeth.trim();
				this.saveMethod = saveMeth;
			}
			if(node.getAttributes().getNamedItem("newmethod") != null){
				String newMeth = node.getAttributes().getNamedItem("newmethod").getNodeValue();
				newMeth = newMeth.trim();
				this.newMethod = newMeth;
			}
			if(node.getAttributes().getNamedItem("deletemethod") != null){
				String delMeth = node.getAttributes().getNamedItem("deletemethod").getNodeValue();
				delMeth = delMeth.trim();
				this.deleteMethod = delMeth;
			}
			if(node.getAttributes().getNamedItem("searchmethod") != null){
				String searchMeth = node.getAttributes().getNamedItem("searchmethod").getNodeValue();
				searchMeth = searchMeth.trim();
				this.searchMethod = searchMeth;
			}
			/*if(node.getAttributes().getNamedItem("oncellchange") != null){
				String oncellchangeMeth = node.getAttributes().getNamedItem("oncellchange").getNodeValue();
				oncellchangeMeth = oncellchangeMeth.trim();
				this.oncellchange = oncellchangeMeth;
			}*/
			
			if(node.getAttributes().getNamedItem("math-expression") != null){
				String mathExpr = node.getAttributes().getNamedItem("math-expression").getNodeValue();
				mathExpr = mathExpr.trim();
				this.methExpression = mathExpr;
			}
			if(node.getAttributes().getNamedItem("fill_data") != null){
				String v = node.getAttributes().getNamedItem("fill_data").getNodeValue();
				v = v.trim();
				if(v != null){
					if("true".equalsIgnoreCase(v))
						this.fillData = true;
					else if("1".equalsIgnoreCase(v))
						this.fillData = true;
				}
			}
			
			
			
			NodeList nl = node.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node nn = nl.item(i);
				if(nn.getNodeType() != Node.TEXT_NODE){
					if(nn.getNodeName().equalsIgnoreCase("column")){
						this.columns.add(new Column(this.getSession(), nn));
					}
				}
			}
		}
	}
	
	@Override
	public String generate() {
		// TODO Auto-generated method stub
		String res = "<table id='table"+getId()+"' width='100%'>";
		res += "<tr>";
		res +=  "<td width='100%' style='height:40px !important;'><table><tr>";
		res += 	 "<td nowrap='nowrap' class='btn' align='center' onclick=\"Table._save('"+getId()+"')\">";
		res += 		"<a >"+Util.getWordFromDictionary("save")+"</a>";
		res += 	 "</td>";
		if(allowInsert){
		res += 	 "<td nowrap='nowrap' class='btn' align='center' onclick=\"Table._new ('"+getId()+"')\">";
			res += 		"<a >"+Util.getWordFromDictionary("new")+"</a>";
		res += 	 "</td>";
		}
		if(allowDelete){
		res += 	 "<td nowrap='nowrap' class='btn' align='center' onclick=\"Table._delete('"+getId()+"')\">";
		
			res += 		"<a >"+Util.getWordFromDictionary("delete")+"</a>";
		res += 	 "</td>";
		}
		res += 	 "<td nowrap='nowrap' class='btn' align='center' onclick=\"Table._search('"+getId()+"')\">";
		res += 		"<a >"+Util.getWordFromDictionary("search")+"</a>";
		res += 	 "</td>";
		res +=   "<td nowrap='nowrap' class='btn' align='center' onclick=\"Table.recreate('"+getId()+"')\" >";
		res += 		"<a >"+Util.getWordFromDictionary("recreate_table")+"</a>";
		res +=   "</td>";
		res +=  "</tr></table></td>";
		res += "</tr>";
		
		res += "<tr>";
		res += "<td colsan='5' class='seperator'>";
		res += "</td>";
		res += "</tr>";
		
		//set date search fields
		String dsf = "";
		for(int i=0; i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c != null && c.getSearch() != null && "true".equalsIgnoreCase(c.getSearch())){
				if(c.getItem() instanceof Date){
					dsf += "search_"+c.getSearchFieldName()+",";
				}
			}
		}
		if(dsf.length() > 1)
			dsf = dsf.substring(0, dsf.length()-1);
		
		res += "<tr><td >" +
				"<input type='hidden' id='"+getId()+"datefields' value='"+dsf+"'>"+
				"</td></tr>";
		//set onchange event if we need it
		/*if(this.oncellchange != null && !"".equals(oncellchange.trim())){
			res += "<tr><td>"+
					"<input type='hidden' id='grid"+getId()+"cellChanged' value='"+oncellchange+"'/>"+
				"</td></tr>";
		}*/
		//Create Search Criteria
		res += "<tr>";
		res +=  "<td width='100%'><table id='"+getId()+"sc'>";
		int tds = 0;
		for(int i=0; i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c != null && c.getSearch() != null && "true".equalsIgnoreCase(c.getSearch())){
				if(tds == 0)
					res += "<tr>";
				res += "<td style='text-align:right' class='label'>";
				res += 	Util.getWordFromDictionary(c.getTitle());
				res += "</td>";
				res += "<td title='"+c.getSearchCondition()+"'>";
				if(c.getItem() instanceof Select){
					res += "<select id='search_"+c.getSearchFieldName()+"'>";
					Select s = (Select)c.getItem();
					res += s.generateOptionsAsHtml();
					res += "<select>";
				}
				else{
					res += 	"<input type='text' id='search_"+c.getSearchFieldName()+"' value='' />";
				}
				res += "</td>";
				res += "<td width='10%'/>";
				if(tds == 1){
					res += "</tr>";
					tds = 0;
				}
				else
					tds ++;
			}
		}
		if(!res.endsWith("</tr>"))
			res += "</tr>";
		res +=  "</table></td>";
		res += "</tr>";
		//--------------------------------
		res += "<tr>";
		res += 	"<td width='"+getTotalWidth()+"px'>";
		res += "<div id=\"mygrid"+getId()+"\"";
		res += " style='width:100%;height:"+getHeight()+"px;'";
		res += " title='"+this.getTitle()+"' ";
		res += " dbtable='"+this.getDbTable()+"' ";
		res += " insert='"+allowInsert+"' ";
		res += " delete='"+allowDelete+"' ";
		//res += " update='"+allowUpdate+"' ";
		res += " filldata='"+fillData+"' ";
		res += " header='"+getHeaders()+"' ";
		res += " initWidths='"+getInitialWidths()+"' ";
		res += " colsalign='"+getColsAlign()+"' ";
		res += " colstype='"+getColsTypes()+"'";
		res += " colsort='"+getColsSorting()+"' ";
		res += " ids='"+getIds()+"'";
		res += " searchFields='"+getSearchFields()+"' ";
		res += " skin='dhx_skyblue' ";
		//res += " skin='modern' ";
		//res += " skin='mt' ";
		//res += " skin='xp' ";
		//res += " skin='dhx_blue' ";
		//res += " skin='sbdark' ";
		//res += " skin='clear' ";
		res += " pk='"+getPkName()+"' ";
		res += " where='"+getWhere()+"' ";
		res += " orderby='"+getOrderBy()+"' ";
		res += " combos='"+generateCombos()+"' ";
		res += " uniquename='"+getUniqeName()+"' ";
		res += " math_exp='"+getMethExpression()+"' ";
		res += " onchangefields='"+getColsOnChanges()+"' ";
		//res += " combosis='"+generateCombosIS()+"' ";
		if(saveMethod != null && !"".equals(saveMethod.trim()))
			res += " savemethod='"+saveMethod+"' ";
		if(deleteMethod != null && !"".equals(deleteMethod.trim()))
			res += " deletemethod= '"+deleteMethod+"' ";
		if(searchMethod != null && !"".equals(searchMethod.trim()))
			res += " searchmethod= '"+searchMethod+"' ";
		if(newMethod != null && !"".equals(newMethod.trim()))
			res += " newmethod= '"+newMethod+"' ";
		
		res += ">\n";
		res += "</div >";
		res += 	"</td>";
		res += "</tr>";
		res += "</table>";
		return res;
	}
	
	private String getHeaders(){
		String str = "";
		for(int i=0;i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			str += Util.getWordFromDictionary(c.getTitle())+",";
		}
		if(str.length() > 1)
			str = str.substring(0, str.length()-1);
		return str;
	}
	
	private String getIds(){
		String res = "";
		try{
			for(int i = 0; i<this.columns.size(); i++){
				Column c = this.columns.get(i);
				res += c.getItem().getId()+",";
			}
			if(res.length() > 0)
				res = res.substring(0, res.length()-1);
		}
		catch(Exception ee){
			ee.printStackTrace();
			res = "*";
		}
		return res;
	}
	
	private String getInitialWidths(){
		String str = "";
		for(int i=0;i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c.getWidth()!=null && !"".equals(c.getWidth()))
				str += c.getWidth()+",";
			else
				str += "100,";
		}
		if(str.length() > 1)
			str = str.substring(0, str.length()-1);
		return str;
	}
	
	private String getColsAlign(){
		String str = "";
		for(int i=0;i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c.getHorizentalAlign()!=null && !"".equals(c.getHorizentalAlign()))
				str += c.getHorizentalAlign()+",";
			else
				str += "center,";
		}
		if(str.length() > 1)
			str = str.substring(0, str.length()-1);
		return str;
	}
	
	private String getColsTypes(){
		String str = "";
		for(int i=0;i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			Item itm = c.getItem();
			if(itm instanceof core.generator.basic.Select)
				str += "co,";
			//else if(itm instanceof core.generator.basic.Date)
			//	str += "da";
			else if(itm instanceof core.generator.basic.Date)
				str += "dhxCalendar,";
			else if(c.isReadOnly())
				str += "ro,";
			else
				str += "ed,";
		}
		if(str.length() > 1)
			str = str.substring(0, str.length()-1);
		System.out.println("Table:"+getId()+":"+str);
		return str;
	}
	
	private String generateCombos(){
		String res = "";
		for(int i=0;i<columns.size(); i++){
			Column c = columns.get(i);
			Item itm = c.getItem();
			if(itm instanceof Select){
				Select s = (Select)itm;
				res += i+":"+s.generateOptionsAsString()+";";
			}
		}
		if(res.length() > 1)
			res = res.substring(0, res.length()-1);
		
		return res;
	}
	
	private String generateCombosIS(){
		String res = "";
		for(int i=0;i<columns.size(); i++){
			Column c = columns.get(i);
			Item itm = c.getItem();
			if(itm instanceof Select){
				Select s = (Select)itm;
				res += i+":"+s.getComboInfrastructure()+";";
			}
		}
		if(res.length() > 2)
			res = res.substring(0, res.length()-1);
		
		return res;
	}
	
	private String getColsSorting(){
		String str = "";
		for(int i=0;i<this.columns.size(); i++){
			Item itm = this.columns.get(i).getItem();
			if(itm instanceof Text)
				str += "str,";
			else if(itm instanceof Date)
				//str += "data,";
				str += "str,";
			else if(itm instanceof Select)
				str += "int,";
		}
		if(str.length() > 1)
			str = str.substring(0, str.length()-1);
		return str;
	}
	
	

	public String getTitle() {
		return Util.getWordFromDictionary(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDbTable() {
		return dbTable;
	}

	public void setDbTable(String dbTable) {
		this.dbTable = dbTable;
	}

	public boolean isAllowInsert() {
		return allowInsert;
	}

	public void setAllowInsert(boolean allowInsert) {
		this.allowInsert = allowInsert;
	}

	/*public boolean isAllowUpdate() {
		return allowUpdate;
	}*/

	/*public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}*/

	public boolean isAllowDelete() {
		return allowDelete;
	}

	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public boolean isFillData() {
		return fillData;
	}

	public void setFillData(boolean fillData) {
		this.fillData = fillData;
	}
	
	@Override
	public String callAfterBuild() {
		return "Table.create:"+getId();
	}

	public String getSaveMethod() {
		return saveMethod;
	}

	public void setSaveMethod(String insertMethod) {
		this.saveMethod = insertMethod;
	}

	public String getDeleteMethod() {
		return deleteMethod;
	}

	public void setDeleteMethod(String deleteMethod) {
		this.deleteMethod = deleteMethod;
	}

	public String getSearchMethod() {
		return searchMethod;
	}

	public void setSearchMethod(String searchMethod) {
		this.searchMethod = searchMethod;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	private int getTotalWidth(){
		int w = 0;
		for(int i=0;i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			try{w += new Integer(c.getWidth());}
			catch(Exception ee){w+=100;}
		}
		return w;
	}
	private String getSearchFields(){
		String res = "";
		for(int i=0; i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c.getSearch() != null && 
					(	"true".equalsIgnoreCase(c.getSearch()) ||
						"1".equals(c.getSearch())
					)
			){
				res += "search_"+c.getSearchFieldName();//+",";
				if(c.getSearchCondition() == null || "".equals(c.getSearchCondition()))
					c.setSearchCondition("like");
				res += "-"+c.getSearchCondition()+",";
			}
		}
		if(res.length() > 1)
			res = res.substring(0, res.length()-1);
		return res;
	}
	
	private String getPkName(){
		String res = "";
		for(int i=0; i<this.columns.size(); i++){
			Column c = this.columns.get(i);
			if(c.isPk())
				res+=c.getItem().getId()+",";
		}
		if(res.length() > 1)
			res = res.substring(0, res.length()-1);
		return res;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getUniqueName(Node n){
		String res = "";
		try{
			List<String> l = new ArrayList<String>();
			Node temp = n;
			while(temp != null && temp.getNodeType() != 9){
				String nn = temp.getAttributes().getNamedItem("id").getNodeValue();
				l.add(nn);
				temp = temp.getParentNode();
			}
			for(int i=l.size()-1; i>0; i--){
				res += l.get(i)+":";
			}
			res += l.get(0);
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		return res;
	}
	
	private String getColsOnChanges(){
		String res = "";
		try{
			for(int i=0; i<columns.size(); i++){
				Column c = columns.get(i);
				if(c.getItem().getOnChanged() != null && !"".equals(c.getItem().getOnChanged())){
					res += i+":"+c.getItem().getOnChanged()+",";
				}
			}
			if(res.length() > 1)
				res = res.substring(0, res.length() - 1);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
}