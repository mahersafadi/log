package core.db;

import java.sql.*;
import java.util.*;

import core.action.*;
import core.exceptions.*;
import core.util.Config;
/**
 * @author Maher Safadi
 *
 */
public class OracleDataBaseEngine implements DataBaseEngine{
	private Map<String, Table> tables;
	private String dbConnection;
	private static Connection conn;
	private Statement stmt;
	
	public OracleDataBaseEngine(){
		//dbConnection = "jdbc:oracle:thin:@localhost:1521:orcl";
		dbConnection = Config.getConnectionType()+":@"+Config.getHost()+":"+Config.getPort()+":"+Config.getInstance();
		conn = getConnection();
		loadTables();
	}
	
	private void loadTables(){
		tables = new HashMap<String, Table>();
		try{
			Action action = new Action(Action.select, "TABLES", null);
			action = executeSelect(action);
			for(int i=0; i<action.getActionResult().getData().size(); i++){
				Map<String, Object> m = action.getActionResult().getRowAsMap(i);
				String key = m.get("TBL_NAME").toString();
				tables.put(key.toUpperCase(), new Table(m));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	@Override
	public Connection createConnection() {
		try{
			Class.forName(Config.getDriver());
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(dbConnection, Config.getUser(), Config.getPassword());
		}
		catch(Exception ex){
			ex.printStackTrace();
			conn = null;
		}
		return conn;
	}

	@Override
	public Connection getConnection() {
		if(conn == null){
			conn = createConnection();
		} 
		return conn;
	}
	
	@Override
	public Action execute(Action action){
		/*
		 * Begin Transaction
		 * on finish commit
		 * on exception roll back
		 * */
		try{
			//....
			conn = getConnection();
			conn.setAutoCommit(false);
			if(action.getType() == Action.insert){
				action = executeInsert(action);
			}
			else if(action.getType() == Action.update){
				action = executeUpdate(action);
			}
			else if(action.getType() == Action.delete){
				action = executeDelete(action);
			}
			else if(action.getType() == Action.select){
				action = executeSelect(action);
			}
			else if(action.getType() == Action.executeFreeStatement){
				action = executeFreeStatement(action);
			}
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch(Exception e){
			try{
				conn.rollback();
			}
			catch (Exception ex) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return action;
	}

	@Override
	public Action executeInsert(Action action) throws Exception {
		/*
		 * If primary key is coming with data
		 *  Get it
		 * if primary key is from sequence 
		 * 	Get Related database sequence
		 * else if primary key is from table definitions
		 * 	Get max number + 1 then update table definitions
		 * create insert statement
		 * execute insert statement 
		 * */
		Table table = tables.get(action.getName().toUpperCase());
		if(table == null)
			throw new NotExistTable();
		Object pkValue;
		if(table.getPrimaryKeyValue() == Table.DATA_BASE_SEQUENCE){
			//select seq.nextVal from dual
			Action getter = new Action(Action.executeFreeStatement, "select "+table.getSequnceName()+".NEXTVAL from DUAL", null);
			getter = this.executeFreeStatement(getter);
			pkValue = getter.getActionResult().getData().get(0).get(0);
		}
		else if(table.getPrimaryKeyValue() == Table.MAX_NUMBER){
			Action updateMaxIdAction = new Action(Action.update, "TABLES", null);
			updateMaxIdAction.setPrimaryKeyName("TBL_NAME");
			updateMaxIdAction.setPrimarKeyValue(action.getName());
			updateMaxIdAction.addField("TBL_MAX_ID", null, (table.getMaxId()+1));
			action.addSubAction(updateMaxIdAction);
			table.setMaxId(table.getMaxId()+1);
			pkValue = table.getMaxId();
		}
		else if(table.getPrimaryKeyValue() == Table.IDENTITY){
			//no need for primary key it is auto generated
			pkValue = null;
		}
		else //none
			pkValue = null;
		String query = "";
		String keys = "";
		String values = "";
		if(action.getFields() == null || action.getFields().size() == 0)
			throw new InsertFailException();
		for (int i = 0; i<action.getFields().size(); i++){
			ActionItem currItem = action.getFields().get(i);
			keys += currItem.getItem()+",";
			if(currItem.getValue() instanceof String)
				values += "'"+currItem.getValue()+"',";
			else
				values += currItem.getValue()+",";
		}
		keys = keys.substring(0, keys.length() - 1);
		values = values.substring(0, values.length() - 1);
		if(pkValue != null){
			action.setPrimarKeyValue(pkValue);
			keys += ","+action.getPrimaryKeyName();
			if(pkValue instanceof String)
				values += ",'"+pkValue+"'";
			else
				values += ","+pkValue;
		}
		query = "insert into "+action.getName() +"("+keys+") values("+values+")";
		stmt = conn.createStatement();
		boolean res = stmt.execute(query);
		/*if(res == false)
			throw new InsertFailException();*/
		//Execute Sub Actions if there is
		for(int i=0; i<action.getSubActions().size(); i++){
			Action currAction = action.getSubActions().get(i);
			if(currAction.getType() == Action.insert){
				this.executeInsert(currAction);
			}
			else if(currAction.getType() == Action.delete){
				this.executeDelete(currAction);
			}
			else if(currAction.getType() == Action.update){
				this.executeUpdate(currAction);
			}
			else if(currAction.getType() == Action.executeFreeStatement){
				this.executeFreeStatement(currAction);
			}
		}
		return action;
	}

	@Override
	public Action executeUpdate(Action action) throws Exception {
		/*
		 * 
		 * */
		if(action.getFields() == null || action.getFields().size() == 0)
			return action;
		else{
			String query = "update "+action.getName()+" set ";
			for (int i=0; i< action.getFields().size(); i++){
				ActionItem currItem = action.getFields().get(i);
				if(currItem.getValue() instanceof String)
					query +=  currItem.getItem()+" = '"+currItem.getValue()+"' ,";
				else
					query +=  currItem.getItem()+" = "+currItem.getValue() +" ,";
			}
			if(query.endsWith(","))
				query = query.substring(0, query.length() - 1);
			query+= "where "+action.getPrimaryKeyName() +"=";
			if(action.getPrimarKeyValue() instanceof String)
				query += "'"+action.getPrimarKeyValue()+"'";
			else
				query += action.getPrimarKeyValue();
			
			stmt = conn.createStatement();
			boolean res = stmt.execute(query);
			/*if(res == false)
				throw new UpdateFailedException();*/
			
			//Execute Sub Actions if there is
			for(int i=0; i<action.getSubActions().size(); i++){
				Action currAction = action.getSubActions().get(i);
				if(currAction.getType() == Action.insert){
					this.executeInsert(currAction);
				}
				else if(currAction.getType() == Action.delete){
					this.executeDelete(currAction);
				}
				else if(currAction.getType() == Action.update){
					this.executeUpdate(currAction);
				}
				else if(currAction.getType() == Action.executeFreeStatement){
					this.executeFreeStatement(currAction);
				}
			}
			
			stmt.close();
		}		
		return action;
	}

	@Override
	public Action executeSelect(Action action) throws Exception {
		/*
		 * Execute select based on where
		 * No nested actions in select statment
		 * */
		String query = "select * from "+action.getName()+" where 1=1 ";
		String where = " ";
		for(int i=0; i<action.getFields().size(); i++){
			ActionItem currItem = action.getFields().get(i);
			if(currItem.getValue() instanceof String)
				where+= " and "+currItem.getItem()+ " "+currItem.getOperation()+" '"+currItem.getValue()+"' ";
			else 
				where+= " and "+currItem.getItem()+ " "+currItem.getOperation()+" "+currItem.getValue();
		}
		query += where;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ActionResult aRes = new ActionResult(rs);
		action.setActionResult(aRes);
		stmt.close();
		return action;
	}

	@Override
	public Action executeDelete(Action action) throws Exception {
		/*
		 * Execute delete based on where or based on Primary Key
		 * */
		
		return null;
	}

	@Override
	public Action executeFreeStatement(Action action) throws Exception {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(action.getName());
		ActionResult aRes = new ActionResult(rs);
		action.setActionResult(aRes);
		stmt.close();
		return action;
	}

	@Override
	public Action executeSingleValueProcedure(Action action) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action executeMultiValueProcedure(Action action) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action executeFunction(Action action) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTableFields(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}	
}
