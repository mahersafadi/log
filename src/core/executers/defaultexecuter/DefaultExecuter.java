package core.executers.defaultexecuter;

import core.action.ActionItem;
import java.util.*;

public class DefaultExecuter {
	public String doLogIn(String userName, String password){
		//Check from data base if such a user is exist
		//if not send a message 'a well one'
		String q = "select * from users where user_name='"+userName+"'";
		
		return "";
	}
	public Object execute(Map<String, String> m){
		//if ids exists, convert it to list of string
		//if ids is comming as *, get table structure and set into list as tableFields
		//if there is search fields, convert it to list of ActionItem		
		try{
			if("table".equalsIgnoreCase(m.get("obj"))){
				String dbName = m.get("table");
				String searchFieldsAsString = m.get("searchFields");
				String tableFieldsAsString = m.get("ids");
				String orderBy = m.get("orderby");
				String data2SaveOrDelete = m.get("data");
				String operation = m.get("kind");
				String pk = m.get("pk");
				List<ActionItem> searchFields = new ArrayList<ActionItem>();
				try{
					String where  = m.get("where");
					String [] whereArr = where.split(";");
					for(int i=0; i<whereArr.length; i++){
						String str = whereArr[i];
						String [] cc = str.split(",");
						ActionItem ai = new ActionItem(cc[0], cc[1], cc[2]);
						searchFields.add(ai);
					}
				}
				catch(Exception e){
					
				}
				
				String [] tableFields = tableFieldsAsString.split(",");
				Object o = executeDefaultForTable(	dbName, 
													searchFields, 
													tableFields, 
													orderBy, 
													data2SaveOrDelete, 
													operation,
													pk
												);
				return o;
			}
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		return null;
	}
	
	private Object executeDefaultForTable
								(	String 				dbName, 	
									List<ActionItem> 	searchFields,
									String[]			tableFields,	
									String 				orderBy, 
									String 				data2SaveOrDelete, 
									String 				operation,
									String 				pk
								)
									throws Exception	
	{
		core.executers.defaultexecuter.Table t = new core.executers.defaultexecuter.Table();
		t.setDbTable(dbName);
		t.setOrderby(orderBy);
		t.setSearchFields(searchFields);
		t.setTableFields(tableFields);
		t.setData2SaveOrDelete(data2SaveOrDelete);
		t.setPk(pk);
		if("save".equalsIgnoreCase(operation)){
			t.save();
		}
		else if("delete".equalsIgnoreCase(operation)){
			t.delete();
		}
		t.search();
		return t.getSearchDataAsMine();
	}
}
