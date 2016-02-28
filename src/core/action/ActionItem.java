package core.action;

public class ActionItem {
	private String item;
	private String operation;
	private Object value;
	public ActionItem(String item, String opString, Object value){
		this.item = item;
		this.operation = opString;
		this.value = value;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
