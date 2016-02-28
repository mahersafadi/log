package core.util.expression.effect;

import org.w3c.dom.*;
import java.util.*;

import core.action.Action;
import core.util.expression.Field;

import java.util.*;

public class TableEffects {
	Node tableNode;
	List<Field> fields;
	List<Node> columnsItems;
	public TableEffects(Node tableNode, List<Field> f) {
		this.tableNode = tableNode;
		fields = new ArrayList<Field>();
		columnsItems = new ArrayList<Node>();
		this.fields.addAll(f);
		collectColumnsItems();
	}
	
	private void collectColumnsItems(){
		Node n = tableNode;
		if("table".equalsIgnoreCase(n.getNodeName())){
			for(int i=0; i<n.getChildNodes().getLength(); i++){
				Node currNode = n.getChildNodes().item(i);
				if(currNode.getNodeType() != Node.TEXT_NODE){
					for(int cc=0;cc<currNode.getChildNodes().getLength(); cc++){
						if(currNode.getChildNodes().item(cc).getNodeType() != Node.TEXT_NODE){
							//Column's Item is found
							currNode = currNode.getChildNodes().item(cc);
							cc= currNode.getChildNodes().getLength()+1;
						}
					}
					if(currNode.getAttributes() != null && currNode.getAttributes().getNamedItem("id")!=null){
						String nodeId = currNode.getAttributes().getNamedItem("id").getNodeValue();
						if(nodeId!= null && !"".equals(nodeId)){
							this.columnsItems.add(currNode);
						}
					}
				}
			}
		}
	}
	
	public void apply(){
		try{
			for(int k=0; k<columnsItems.size(); k++){
				Node affectNode = checkForAffect(this.columnsItems.get(k));
				if(affectNode != null){
					applyColumnEffect(affectNode,k);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Node checkForAffect(Node n){
		Node currNode = n;
		NodeList currNodeChildNodes = currNode.getChildNodes();
		int k = 0;
		while(k<currNodeChildNodes.getLength()){
			if(currNodeChildNodes.item(k).getNodeType() != Node.TEXT_NODE){
				if("affects".equalsIgnoreCase(currNodeChildNodes.item(k).getNodeName()))
					return currNodeChildNodes.item(k);
			} 
			k++;
		}
		return null;
	}
	private void applyColumnEffect(Node affectsNode, int index) throws Exception{
		//index is used to get value and column name
		Field f = this.fields.get(index);
		if(f != null && f.value != null && !"".equals(f.value)){
			NodeList affectList = affectsNode.getChildNodes();
			int k = 0;
			while(k<affectList.getLength()){
				Node currAffectNode = affectList.item(k);
				if(currAffectNode.getNodeType() != Node.TEXT_NODE){
					String type = null;
					String source = null;
					String value = null;
					String dbTable = null;
					String executer = null;
					String field = null;
					String where = null;
					String id = null;
					String res = "";
					if(currAffectNode.getAttributes() != null){
						Node typeNode = currAffectNode.getAttributes().getNamedItem("type");
						if(typeNode != null){
							type = typeNode.getNodeValue();
						}
						
						Node sourceNode = currAffectNode.getAttributes().getNamedItem("source");
						if(sourceNode != null){
							source = sourceNode.getNodeValue();
						}
						
						Node valueNode = currAffectNode.getAttributes().getNamedItem("value");
						if(valueNode != null){
							value = valueNode.getNodeValue();
						}
						
						Node dbTableNode = currAffectNode.getAttributes().getNamedItem("dbtable");
						if(dbTableNode != null){
							dbTable = dbTableNode.getNodeValue();
						}
						
						Node executerNode = currAffectNode.getAttributes().getNamedItem("executer");
						if(executerNode != null){
							executer = executerNode.getNodeValue();
						}
						
						Node fieldNode = currAffectNode.getAttributes().getNamedItem("field");
						if(fieldNode != null){
							field = fieldNode.getNodeValue();
						}
						
						Node whereNode = currAffectNode.getAttributes().getNamedItem("where");
						if(whereNode != null){
							where = whereNode.getNodeValue();
						}
						
						Node idNode = currAffectNode.getAttributes().getNamedItem("id");
						if(idNode != null){
							id = idNode.getNodeValue();
						}
						else
							throw new Exception("No effected id found");
					}
					if("db".equalsIgnoreCase(source)){
						if(dbTable == null || "".equals(dbTable) || field == null || "".equals(field))
							throw new Exception("source is db but table or field are missing");
						Action action = new Action(Action.select, dbTable, null);
						//resolve where
						if(where != null && !"".equals(where)){
							while(where.contains("#{")){
								String key = where.substring(where.indexOf("#{"));
								key = key.substring(2);
								key = key.substring(0, key.indexOf("}"));
								String val = f.value;
								//now replace it in where
								where = where.substring(0, where.indexOf("#{"))+val+ where.substring(where.indexOf("}")+1);
							}
							action.setWhere(where);
						}
						action.execute();
						if(action.getActionResult() != null){
							Map<String , Object> obj = action.getActionResult().getRowAsMap(0);
							//replace values in fields
							//current strategy is to replace value even column has data
							//if other is requested, no replace value even column has data
							Object oo = obj.get(field);
							//fill it in field with name 'id' on fields list
							if(oo != null){
								int c = 0;
								boolean ok = false;
								while(c<fields.size() && !ok ){
									Field ff = fields.get(c);
									if(id.equals(ff.name)){
										ff.value = oo.toString();
									}
									c++;
								}
							}
						}
					}
				}
				k++;
			}
		}
	}
	
	public Node getTableNode() {
		return tableNode;
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public List<Node> getColumnsItems() {
		return columnsItems;
	}
} 
