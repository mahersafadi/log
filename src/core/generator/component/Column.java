package core.generator.component;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.generator.IGenerate;
import core.generator.Item;
import core.generator.basic.Date;
import core.generator.basic.Text;
import core.generator.basic.Select;

public class Column extends Item implements IGenerate{
	private String title;
	private String search;
	private String filter;
	private Item item;
	private String searchCondition;
	private String searchFieldName;
	private boolean pk;
	
	public Column(HttpSession session, Node node){
		setSession(session);
		parse(node);
	}
	
	@Override
	public String generate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(Node n) {
		super.parse(n);
		if(n.getAttributes() != null){
			if(n.getAttributes().getNamedItem("title") != null)
				this.title = n.getAttributes().getNamedItem("title").getNodeValue();
			if(n.getAttributes().getNamedItem("search") != null){
				this.search = n.getAttributes().getNamedItem("search").getNodeValue();
			}
			if(n.getAttributes().getNamedItem("filter") != null)
				this.filter = n.getAttributes().getNamedItem("filter").getNodeValue();
			if(n.getAttributes().getNamedItem("search-cond") != null)
				this.searchCondition = n.getAttributes().getNamedItem("search-cond").getNodeValue();
			
			this.pk = false;
			if(n.getAttributes().getNamedItem("pk") != null)
				if	(	"true".equalsIgnoreCase(n.getAttributes().getNamedItem("pk").getNodeValue())
						|| 
						"1".equals(n.getAttributes().getNamedItem("pk").getNodeValue())
					)
					this.pk = true;
			
			NodeList nl = n.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node nn = nl.item(i);
				if(nn.getNodeType() != Node.TEXT_NODE){
					Item itm = null;
					if(nn.getNodeName().equalsIgnoreCase("text")){
						this.item  = new Text(this.getSession(), nn);
					}
					else if(nn.getNodeName().equalsIgnoreCase("select")){
						itm = new Select(this.getSession(), nn);
					}
					else if(nn.getNodeName().equalsIgnoreCase("date")){
						itm = new Date(this.getSession(), nn);
					}
					if(nn.getNodeName().equalsIgnoreCase("radio")){
						
					}
					if(nn.getNodeName().equalsIgnoreCase("checkbox")){
						
					}
					if(nn.getNodeName().equalsIgnoreCase("lov")){
						
					}
					
					if(itm != null)
						this.item = itm;
				}
			}
		}
		try{
			this.searchFieldName = this.getItem().getId()+"_"+(Item.counter++);
		}
		catch(Exception e){
			
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchFieldName() {
		return searchFieldName;
	}

	public void setSearchFieldName(String searchFieldName) {
		this.searchFieldName = searchFieldName;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}
	
	public boolean isReadOnly(){
		try{
			String ro = this.item.getReadOnly();
			if("true".equalsIgnoreCase(ro) || "1".equals(ro))
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
