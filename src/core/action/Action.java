package core.action;

import java.util.*;

import core.db.DataBaseEngine;
import core.exceptions.*;
import core.service.ServiceProvider;
import core.util.Message;
import core.util.User;
public class Action {
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	public static final int select = 1;
	public static final int insert = 2;
	public static final int delete = 3;
	public static final int update = 4;
	public static final int executeFreeStatement = 5;
	//-------------------------------------------
	private int type;
	private String primaryKeyName;
	private Object primarKeyValue;
	private Map<String, Object> foreignKeys;
	private String name;
	private User user;
	private Action parent;
	private List<Action> subActions;
	private List<ActionItem> fields;
	private ActionResult actionResult;
	private List<Message> messages;
	private String where;
 
	public Action(int actionType, String actionName, User actionUser){
		type = actionType;
		name = actionName;
		user = actionUser;
		subActions = new ArrayList<Action>();
		fields = new ArrayList<ActionItem>();
		actionResult = new ActionResult();
		messages = new ArrayList<Message>();
		parent = null;
	}
	
	public void execute(){
		try{
			DataBaseEngine db = (DataBaseEngine)ServiceProvider.getService(ServiceProvider.dataBaseService);
			Action act = db.execute(this);
			this.primarKeyValue = act.getPrimarKeyValue();
			this.actionResult = act.getActionResult();
		}
		catch(NotValidActionException ex1){
			//add not valid action message
			this.messages.add(new Message("NotValidAction", Message.error));
		}
		catch(UpdateFailedException ex2){
			//add Fail to update message
			this.messages.add(new Message("UpdateFaildMessage", Message.error));
		}
		catch(DataBaseException ex3){
			//add data base exception
			this.messages.add(new Message("DatabaseMessage", Message.error));
		}
		catch(Exception ex){
			//add unknow error
			ex.printStackTrace();
			this.messages.add(new Message("UnknownMessage", Message.error));
		}
	}
	
	public String getName() {
		return name;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<ActionItem> getFields() {
		return fields;
	}
	public void setFields(List<ActionItem> fields) {
		if(fields != null)
			this.fields = fields;
		else
			this.fields.clear();
	}
	public List<Action> getSubActions() {
		return subActions;
	}
	public void setSubActions(List<Action> children) {
		this.subActions = children;
	}
	public void addSubAction(Action action){
		this.subActions.add(action);
		action.parent = this;
	}
	public ActionResult getActionResult() {
		return actionResult;
	}
	public void setActionResult(ActionResult actionResult) {
		this.actionResult = actionResult;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public boolean hasMessages(){
		if(messages != null && messages.size()>0)
			return true;
		else
			return false;
	}

	public Object getPrimarKeyValue() {
		return primarKeyValue;
	}

	public void setPrimarKeyValue(Object primarKeyValue) {
		this.primarKeyValue = primarKeyValue;
	}

	public Map<String, Object> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(Map<String, Object> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	public Action getParent() {
		return parent;
	}

	public void setParent(Action parent) {
		this.parent = parent;
	}

	public static int getExecutefreestatement() {
		return executeFreeStatement;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	
	public void addField(String name, String op, Object value){
		ActionItem actionItem = new ActionItem(name, op, value);
		this.fields.add(actionItem);
	}
	public void addField(String name, Object value){
		ActionItem actionItem = new ActionItem(name, null, value);
		this.fields.add(actionItem);
	}
	public Object getFieldValueByName(String name){
		if(name == null || "".equals(name))
			return null;
		
		Object res = null;
		int i=0;
		while(i<this.fields.size() && res == null){
			if(this.fields.get(i).getItem().equals(name))
				res = this.fields.get(i).getValue();
			i++;
		}
		return res;
	}
	
	public void updateFieldValue(String key, Object value) throws Exception{
		if(key != null && !"".equals(key)){
			ActionItem field = null;
		
			int i=0;
			while(i<this.fields.size() && field == null){
				if(this.fields.get(i).getItem().equals(name))
					field = this.fields.get(i);
				i++;
			}
			if(field != null){
				field.setValue(value);
			}
		}
		else
			throw new Exception("ActionItem in name "+key+", is not exist");
	}
	
}

