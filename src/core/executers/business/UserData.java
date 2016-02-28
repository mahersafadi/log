package core.executers.business;

import java.util.*;

import core.action.Action;
import core.action.ActionItem;
import core.db.DataBaseEngine;
import core.service.ServiceProvider;

public class UserData {
	public static String OPERATION = "op";
	public static String DATA = "data";
	public static String SAVE = "s";
	public static String DELETE = "d";
	public static String PRIMARY_KEY = "pk";
	private Map<String, String> dataFromUser = new HashMap<String, String>();
	private List<Map<String, String>> dbDataFromUser;
	private Map<String, Object> output = new HashMap<String, Object>();
	private List<String> messages = new ArrayList<String>();
	public UserData(Map<String, String> dataFromUser) {
		super();
		this.dataFromUser = dataFromUser;
	}
	public UserData() {
		// TODO Auto-generated constructor stub
	}
	public Map<String, String> getDataFromUser() {
		return dataFromUser;
	}
	public void setDataFromUser(Map<String, String> dataFromUser) {
		this.dataFromUser = dataFromUser;
	}
	public Map<String, Object> getOutput() {
		return output;
	}
	public void setOutput(Map<String, Object> output) {
		this.output = output;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public String getFromMap(String key){
		if(dataFromUser != null && dataFromUser.size() > 0){
			return dataFromUser.get(key);
		}
		return null;
	}

	public boolean isPrimaryKeyAttributeExist(){
		String pk = this.dataFromUser.get(UserData.PRIMARY_KEY);
		if(pk != null && !"".equals(pk))
			return true;
		else
			return false;
	}
	
	public boolean isNewRow(int i){
		//based on pk and check if pk is comming or not we can decide if it is new or not
 		Map<String, String> m =  this.dbDataFromUser.get(i);
 		String pk = dataFromUser.get(UserData.PRIMARY_KEY);
 		if(m.get(pk) == null || "".equals(m.get(pk))) 
 			return true;
 		else
 			return false;
	}
	
	public boolean mustUpdate(Map<String, String> userData, Map<String, Object> dbData){
		return true;
	}
	public List<Map<String, String>> getDbDataFromUser() {
		return dbDataFromUser;
	}
	public void setDbDataFromUser(List<Map<String, String>> dbDataFromUser) {
		this.dbDataFromUser = dbDataFromUser;
	}
	
	public List<String> getTableFields(String tableName){
		DataBaseEngine dbe = (DataBaseEngine)ServiceProvider.getService(ServiceProvider.dataBaseService);
		return dbe.getTableFields(tableName);
	}
	
	public Action prepareInsertAction(String tableName, int index){
		try{
			List<String> fieldsNames = getTableFields(tableName);
			if(fieldsNames != null && fieldsNames.size() > 0){
				Action action = new Action(Action.insert, tableName, null);
				action.setPrimaryKeyName(this.getDataFromUser().get(PRIMARY_KEY));
				for(int i=0; i<fieldsNames.size(); i++){
					String val = this.getDbDataFromUser().get(index).get(fieldsNames.get(i));
					//String val = this.getDataFromUser().get(fieldsNames.get(i)) ;
					if(val != null && !"".equals(val))
						action.addField(fieldsNames.get(i), val);
				}
				return action;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public Action prepareDeleteAction(String tableName, int index){
		try{
			String primaryKeyName = this.dataFromUser.get(PRIMARY_KEY);
			Map<String, String> currRowFromUser = this.dbDataFromUser.get(index);
			String primaryKeyValue = currRowFromUser.get(primaryKeyName);
			Action deleteAction = new Action(Action.delete, tableName, null);
			deleteAction.setPrimaryKeyName(primaryKeyName);
			deleteAction.setPrimarKeyValue(primaryKeyValue);
			return deleteAction;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public Action prepareUpdateAction(String tableName, int index){
		try{
			//Get primary key value
			//execute select and get record
			//match what is changed, and but them in action map
			String primaryKeyName = this.dataFromUser.get(PRIMARY_KEY);
			Map<String, String> currRowFromUser = this.dbDataFromUser.get(index);
			String primaryKeyValue = currRowFromUser.get(primaryKeyName);
			
			Action searchAction = new Action(Action.select, tableName, null);
			searchAction.addField(primaryKeyName,"=", primaryKeyValue);
			searchAction.execute();
			
			Map<String, Object> dbMap = searchAction.getActionResult().getRowAsMap(0);
			boolean change = false;
			List<ActionItem> actionItems = new ArrayList<ActionItem>();
			Iterator<String> it = currRowFromUser.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				String userItm = currRowFromUser.get(key);
				Object dbItm = dbMap.get(key);
				if(dbItm != null){
					if(!dbItm.toString().equals(userItm)){
						change = true;
						actionItems.add(new ActionItem(key, null, userItm));
					}
				}
				else{
					if(userItm != null){
						change = true;
						actionItems.add(new ActionItem(key, null, userItm));
					}
				}
			}
			if(change){
				Action updateAction = new Action(Action.update, tableName, null);
				updateAction.setPrimaryKeyName(primaryKeyName);
				updateAction.setPrimarKeyValue(primaryKeyValue);
				updateAction.setFields(actionItems);
				return updateAction;
			}
			else
				return null;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public void convertDataFromUserToListOfMap(){
		String data = (String)this.getDataFromUser().get(DATA);
		this.setDbDataFromUser(this.convertStringToListOfMap(data));
	}
	
	public Map<String, String> getRowFromList(int index){
		return this.dbDataFromUser.get(index);
	}
	
	public Map<String, Object> getRecordFromDataBase(String tableName, int index) throws Exception{
		String primaryKeyName = this.getFromMap(this.PRIMARY_KEY);
		String primaryKeyValue = this.dbDataFromUser.get(index).get(primaryKeyName);
		Action action = new Action(Action.select, tableName, null);
		action.addField(primaryKeyName, "=", primaryKeyValue);
		action.execute();
		return action.getActionResult().getRowAsMap(0);
	}
	
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	//______________________________________________________________
	public Map<String, String> convertStringToMap(String str){
        Map<String, String> ret = new HashMap<String, String>();
        try{
            if(str == null ||   "".equals(str))
              return ret;
            
            str = str.substring(1, str.length() - 1);
            StringTokenizer st = new StringTokenizer(str, "][");
                        
            while(st.hasMoreTokens()){
                String currItem = st.nextToken();
                while(currItem.length()>0 && currItem.startsWith("["))
                  currItem = currItem.substring(1, currItem.length());
                while(currItem.length() > 0 && currItem.endsWith("]"))
                    currItem = currItem.substring(0, currItem.length() - 1);
                if(!"".equals(currItem)){
                    String [] currItemArray = currItem.split(":");
                    String key = currItemArray[0];
                    String currValue = currItem.substring(key.length()+1);
                    Object currValueAsObject = currValue;
                    try{
                        String [] dayFragments = currValue.split(",");
                        Calendar c = Calendar.getInstance();
                        int month = Integer.parseInt(dayFragments[1]);
                        if(month >0)
                            month = month - 1;
                        c.set(
                           Integer.parseInt(dayFragments[0]), 
                           month, 
                           Integer.parseInt(dayFragments[2]), 
                           Integer.parseInt(dayFragments[3]), 
                           Integer.parseInt(dayFragments[4]),
                           Integer.parseInt(dayFragments[5]));
                        currValueAsObject = c.getTime();
                    }
                    catch(Exception ex){
                        try{
                            currValueAsObject = Integer.parseInt(currValue);
                        }
                        catch(Exception ex1){
                            try{
                                currValueAsObject = Long.parseLong(currValue);
                            }
                            catch(Exception ex2){
                                try{
                                    currValueAsObject = Double.parseDouble(currValue);
                                }
                                catch(Exception ex3){
                                    try{
                                      if("true".equals(currValue)|| "false".equals(currValue))
                                        currValueAsObject = Boolean.parseBoolean(currValue);
                                      else
                                        currValueAsObject = currValue;
                                    }
                                    catch(Exception ex4){
                                        currValueAsObject = currValue;
                                    }
                                }
                            }
                        }
                    }
                    if(currValueAsObject != null)
                    	ret.put(key, currValueAsObject.toString());
                    else
                    	ret.put(key, null);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return ret;
    }
    
    public String convertMapToString(Map<String, Object> obj){
        StringBuilder sb = new StringBuilder();
        try{
            sb.append("[");
            Iterator<String> it = obj.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                Object value = obj.get(key);
                sb.append("["+key+":"+value+"]");
            }
          sb.append("]");
        }
        catch(Exception ex){
            ex.printStackTrace();
            sb.append("]");
        }
        return sb.toString();
    }      
    //--------------------------------------------------------------------
    //operation on list of map
    public List<Map<String, String>> convertStringToListOfMap(String str){
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        try{
            if(str == null || "".equals(str)){
                return ret;
            }
            str = str.substring(1, str.length()-1);
            String [] array = str.split("],\\[");
            for(int i=0; i<array.length ; i++){
                String currMap = array[i];
                if(currMap != null && !"".equals(currMap)){
                    if(currMap.startsWith("[["));
                    else currMap = "["+currMap;
                    
                    if(currMap.endsWith("]]]"))
                        currMap = currMap.substring(0, currMap.length()-1);
                    else  if(currMap.endsWith("]]"));
                          else currMap += "]";
                    if(currMap != null && !"".equals(currMap)){
                        Map<String, String> res = convertStringToMap(currMap);
                        if(res != null)
                            ret.add(res);
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return ret;
    }
    
    public String convertListOfMapToString(List<Map<String, Object>> obj){
        StringBuilder sb = new StringBuilder();
        try{
            sb.append("[");
            for(int i=0; i<obj.size()-1; i++){
                sb.append(convertMapToString(obj.get(i))+",");
            }
            sb.append(convertMapToString(obj.get(obj.size()-1)));
            sb.append("]");
        }
        catch(Exception ex){
            ex.printStackTrace();
            sb.append("]");
        }
        return sb.toString();
    }
}
