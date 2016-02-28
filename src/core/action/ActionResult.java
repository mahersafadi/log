package core.action;

import java.sql.*;
import java.util.*;
public class ActionResult {
	private List<String> fieldNames;
	private List<List<Object>> data;
	public ActionResult(){
		fieldNames = new ArrayList<String>();
		data = new ArrayList<List<Object>>();
	}
	public ActionResult(ResultSet rs) throws Exception{
		fieldNames = new ArrayList<String>();
		data = new ArrayList<List<Object>>();
		ResultSetMetaData meta = rs.getMetaData();
		for(int i = 1; i<=meta.getColumnCount(); i++){
			fieldNames.add(meta.getColumnName(i));
		}
		while(rs.next()){
			List<Object> row = new ArrayList<Object>();
			for(int i=0; i<fieldNames.size(); i++){
				row.add(rs.getObject(fieldNames.get(i)));
			}
			data.add(row);
		}
	}
	
	public Map<String, Object> getRowAsMap(int i) throws Exception{
		Map<String, Object> obj = new HashMap<String, Object>();
		List<Object> l = data.get(i);
		for(int j=0; j<fieldNames.size(); j++){
			obj.put(fieldNames.get(j), l.get(j));
		}
		return obj;
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
	
	public List<List<Object>> getData() {
		return data;
	}
	
	public void setData(List<List<Object>> data) {
		this.data = data;
	}
}
