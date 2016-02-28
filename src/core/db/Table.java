package core.db;

import java.util.*;
public class Table {
	public static final int DATA_BASE_SEQUENCE = 1;
	public static final int MAX_NUMBER = 2;
	public static final int IDENTITY = 3;
	public static final int NONE = 4;
	//--------------------------------------------
	private int primaryKeyValue;
	private String tableName;
	private String sequnceName;
	private Long maxId;
	
	public Table(String name){
		this.tableName = name;
	}
	public Table(Map<String, Object> m){
		this.tableName = (String)m.get("TABLE_NAME");
		this.sequnceName = (String)m.get("TBL_SEQUENCE");
		this.maxId = new Long(m.get("TBL_MAX_ID").toString());
		this.primaryKeyValue = new Integer(m.get("TBL_PK_MODE").toString());
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSequnceName() {
		return sequnceName;
	}
	public void setSequnceName(String sequnceName) {
		this.sequnceName = sequnceName;
	}
	public Long getMaxId() {
		return maxId;
	}
	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}
	public int getPrimaryKeyValue() {
		return primaryKeyValue;
	}
	public void setPrimaryKeyValue(int primaryKeyValue) {
		this.primaryKeyValue = primaryKeyValue;
	}
}