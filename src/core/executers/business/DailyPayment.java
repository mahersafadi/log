package core.executers.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import core.action.Action;
import core.util.Message;

public class DailyPayment implements IBusiness{
	
	/*
	 * If operation is save or update, save item in db and check if save item category is either stock item or service
	 * If saved item is stock item, decrease the quantity in stock, either, do not do any thing 
	 * 
	 *
	*/
	UserData userData;
	public DailyPayment() {
		userData = new UserData();
	}
	@Override
	public void setRequestData(Map<String, String> m) {
		userData.setDataFromUser(m);
	}

	@Override
	public String getResponseAsString() {
		List<Map<String, Object>> ll = new ArrayList<Map<String,Object>>();
		ll.addAll((Collection<? extends Map<String, Object>>) userData.getDbDataFromUser());
		return userData.convertListOfMapToString(ll);
	}

	@Override
	public void execute() {
		//either it is save , update
		//or it is delete
		//search is default
		try{
			List<Message> messages = new ArrayList<Message>();
			String operation = userData.getFromMap(UserData.OPERATION);
			if(UserData.SAVE.equals(operation)){
				//Loop for each row, if new one save, if not check for update, if there is make update
				System.out.println("Save Operation is Sent");
				userData.convertDataFromUserToListOfMap();
				for(int i=0; i<userData.getDbDataFromUser().size(); i++){
					Action dailyPaymentAction = null;
					if(userData.isNewRow(i)){
						//do insert
						dailyPaymentAction = userData.prepareInsertAction("daily_payment", i);
						//Check if quantity available in stock [inserting case]
						Object qObj = dailyPaymentAction.getFieldValueByName("quantity");
						float qF = Float.valueOf(qObj.toString());
						Action checkQAction = new Action(Action.select, "items", null);
						checkQAction.addField("id", "=", dailyPaymentAction.getFieldValueByName("item"));
						checkQAction.execute();
						Map<String, Object> m = checkQAction.getActionResult().getRowAsMap(0);
						float f = Float.valueOf(m.get("quantity").toString());
						if(f >= qF){
							//operation is ok i.e. requested Quantity is available 
							Action updateItemQuantity = new Action(Action.update, "items", null);
							updateItemQuantity.setPrimaryKeyName("id");
							updateItemQuantity.setPrimarKeyValue(m.get("id").toString());
							updateItemQuantity.addField("quantity", ""+(f-qF));
							dailyPaymentAction.addSubAction(updateItemQuantity);
						}
						else{
							messages.add(new Message("NOT_ENOUGH_QUANTITY", Message.error));
						}
					}
					else {//must update or not
						//get fields from data base; then compare
						//do Update
						dailyPaymentAction = userData.prepareUpdateAction("daily_payment", i);
						//if dailyPaymentAction == null, that means no need for update
						//Check if quantity availble in stock [update case]
						Object qObj = dailyPaymentAction.getFieldValueByName("quantity");
						if(qObj != null && !"".equals(qObj.toString())){
							//That means there is a changing in quantity, and items must be updated
							
							float qF = Float.valueOf(qObj.toString());
							//Quantity Difference = newQunatity - oldQuantity
							//if it is < 0, update items by adding abs(difference)
							//if it is > 0, update items by subtracting abs(difference)
							//if it is == 0, do not make any update on items
							Action recordFromDBAction = new Action(Action.select, "daily_payment", null);
							recordFromDBAction.addField(dailyPaymentAction.getPrimaryKeyName(),"=", dailyPaymentAction.getPrimarKeyValue());
							recordFromDBAction.execute();
							Map<String, Object> recordInDataBase = recordFromDBAction.getActionResult().getRowAsMap(0);
							float quantityInDataBase = Float.valueOf(recordInDataBase.get("quantity").toString());
							float difference = qF - quantityInDataBase;
							Action updateItemsAction = new Action(Action.update, "items", null);
							updateItemsAction.setPrimaryKeyName("id");
							updateItemsAction.setPrimarKeyValue(dailyPaymentAction.getFieldValueByName("item"));
							if(difference < 0){
								//set differnce back in stock
								updateItemsAction.addField("quantity", "=", quantityInDataBase+ Math.abs(difference));
							}
							else if(difference > 0){
								if(difference > quantityInDataBase){
									messages.add(new Message("requested_amount_more_than_available", Message.error));
								}
								else
									updateItemsAction.addField("quantity", "=", quantityInDataBase-difference);
							}
							dailyPaymentAction.addSubAction(updateItemsAction);
						}
					}
					//check if there is avilable items in stock
					//execute the action here
					//check if there is messages, add it to messages
					dailyPaymentAction.execute();
					if(dailyPaymentAction.hasMessages())
						messages.addAll(dailyPaymentAction.getMessages());
				}
			}
			else if(UserData.DELETE.equals(operation)){
				//get items back to stock, then execute delete
				System.out.println("Save Operation is Sent");
				userData.convertDataFromUserToListOfMap();
				for(int i=0; i<userData.getDbDataFromUser().size(); i++){
					Action deleteAction = userData.prepareDeleteAction("daily_payment", i);
					Map<String, Object> recordFromDataBase = userData.getRecordFromDataBase("daily_payment", i); 
					float q = Float.valueOf(recordFromDataBase.get("quantity").toString());
					Action updateItemsAction = new Action(Action.update, "daily_payment", null);
					updateItemsAction.setPrimaryKeyName("id");
					updateItemsAction.setPrimarKeyValue(recordFromDataBase.get("item"));
					updateItemsAction.addField("quantity", "=", "quantity + "+q);
					deleteAction.addSubAction(updateItemsAction);
					deleteAction.execute();
					if(deleteAction.hasMessages())
						messages.addAll(deleteAction.getMessages());
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public UserData getUserData() {
		return userData;
	}
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	public int itemQuantity(int itemId){
		Action action = new Action(Action.select, "items", null);
		action.addField("id", "=", itemId);
		action.execute();
		return action.getActionResult().getData().size();
	}
	@Override
	public void setHttpSession(HttpSession session) {
		// TODO Auto-generated method stub
		
	}
}
