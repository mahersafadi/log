package core.executers.defaultexecuter;

import java.util.*;

import core.action.Action;
import core.action.ActionItem;
import core.action.ActionResult;

public class Table {
	private String dbTable;
	private List<ActionItem> searchFields;
	private String[] tableFields;
	private String orderby;
	private ActionResult actionResult;
	private String data2SaveOrDelete;
	private String pk;
	public void search(){
		Action action = new Action(Action.select, dbTable, null);
		action.setFields(searchFields);
		/*if(orderby != null && !"".equals(orderby)){
			if(orderby.startsWith("order by") || 
					orderby.startsWith("ORDER BY"))
				action.addField("", "", ""+orderby);
			else
				action.addField("", "", "order by "+orderby);
		}*/
		action.execute();
		this.setActionResult(action.getActionResult());
	}
	
	public String getSearchDataAsStringOfXML(){
		try{
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	public String getSearchDataAsJSArray(){
		try{
			String res = "[";
			for(int i=0; i<getActionResult().getData().size(); i++){
				Map<String, Object> m = getActionResult().getRowAsMap(i);
				String temp = "[";
				for(int k=0; k<tableFields.length; k++){
					String key = tableFields[k];
					Object val = m.get(key);
					temp += val+",";
				}
				if(temp.length()>1)
					temp = temp.substring(0, temp.length()-1);
				temp += "],";
				res += temp;
			}
			
			if(res.length() > 1)
				res = res.substring(0, res.length()-1);
			res += "]";
			return res;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	public String getSearchDataAsMine(){
		String res = "";
		try{
			for(int i=0; i<getActionResult().getData().size(); i++){
				Map<String, Object> m = getActionResult().getRowAsMap(i);
				String temp = "";
				for(int k=0; k<tableFields.length; k++){
					String key = tableFields[k];
					Object val = m.get(key);
					temp += val+";;";
				}
				if(temp.endsWith(";;"))
					temp = temp.substring(0, temp.length()-2);
				
				res += temp+"@@";
			}
			if(res.endsWith("@@"))
				res = res.substring(0, res.length()-2);
		}
		catch(Exception ee){
			ee.printStackTrace();
			res = "";
		}
		return res;
	}
	
	public String getSearchDataAsStringOfJSon(){
		return "";
	}
	
	public Object save(){
		//Split Rows of data, if it is insert then do insert
		//if it is update then do update
		try{
			List<Map<String, String>> l = new ArrayList<Map<String,String>>();
			String [] rows = data2SaveOrDelete.split(";;");
			List<Object> msgs = new ArrayList<Object>();
			for(int i=0; i<rows.length; i++){
				Map<String, String> m = new HashMap<String, String>();
				String [] currCells = rows[i].split("@@");
				for(int k =0; k < currCells.length; k++){
					String key = currCells[k].split(":::")[0];
					String val = null;
					try{val = currCells[k].split(":::")[1];}
					catch(Exception eeee){;}
					m.put(key, val);
				}
				try{
					Action action = new Action(Action.insert, dbTable, null);
					action.setPrimaryKeyName(pk);
					String pkVal = m.get(pk);
					if(pkVal != null && !"".equals(pkVal)){
						action.setPrimarKeyValue(pkVal);
						action.setType(Action.update);
						action = doUpdate(action, m);
						if(action.getMessages() != null)
							msgs.addAll(action.getMessages());
					}
					else{
						//Do insert action
						Iterator<String> it = m.keySet().iterator();
						while(it.hasNext()){
							String _key = it.next();
							if(!pk.equals(_key)){
								String _val = m.get(_key);
								action.addField(_key, _val);
							}
							else{
								action.setPrimaryKeyName(pk);
								action.setPrimarKeyValue(pkVal);
							}
						}
						action.execute();
						if(action.getMessages()!=null){
							msgs.addAll(action.getMessages());
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
					String str = e.getMessage();
					int kk = e.getMessage().length();
					while(kk>100){
						str = str.substring(0, kk/2);
						kk = str.length();
					}
					msgs.add(m.get("pk")+", Fail"+":"+str);
				}
				l.add(m);
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private Action doUpdate(Action action, Map<String, String> usrMap){
		try{
			//Get Row from table,
			//Compare
			//if there is one change at least, do update
			Action searchAction = new Action(Action.select, action.getName(), null);
			searchAction.addField(action.getPrimaryKeyName(), "=", action.getPrimarKeyValue());
			searchAction.execute();
			Map<String, Object> dbMap = searchAction.getActionResult().getRowAsMap(0);
			boolean change = false;
			//Check for changes if there are
			Iterator<String> it = usrMap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				String userItm = usrMap.get(key);
				Object dbItm = dbMap.get(key);
				if(dbItm != null){
					if(!dbItm.toString().equals(userItm)){
						change = true;
						action.addField(key, userItm);
					}
				}
				else{
					if(userItm != null){
						change = true;
						action.addField(key, userItm);
					}
				}
			}
			if(change){
				//action.setPrimaryKeyName(pk);
				//action.setPrimarKeyValue(usrMap.get(pk));
				action.execute();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return action;
	}
	
	public Object delete(){
		Action action = new Action(Action.delete, dbTable, null);
		if(this.getPk() == null || "".equals(this.getPk()))
			action.setPrimaryKeyName("id");
		else
			action.setPrimaryKeyName(this.getPk());
		action.setPrimarKeyValue(data2SaveOrDelete);
		action.execute();
		if(action.getMessages() != null && action.getMessages().size() > 0)
			return "false";
		else
			return "ok";
	}

	public String getDbTable() {
		return dbTable;
	}

	public void setDbTable(String dbTable) {
		this.dbTable = dbTable;
	}
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public ActionResult getActionResult() {
		return actionResult;
	}

	public void setActionResult(ActionResult actionResult) {
		this.actionResult = actionResult;
	}

	public String getData2SaveOrDelete() {
		return data2SaveOrDelete;
	}

	public void setData2SaveOrDelete(String data2SaveOrDelete) {
		this.data2SaveOrDelete = data2SaveOrDelete;
	}

	public List<ActionItem> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(List<ActionItem> searchFields) {
		this.searchFields = searchFields;
	}

	public String[] getTableFields() {
		return tableFields;
	}

	public void setTableFields(String[] tableFields) {
		this.tableFields = tableFields;
	}

	public String getPk() {
		return this.pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
}