package core.db;

import java.sql.Connection;
import core.action.*;
import core.service.Service;
/**
 * @author Maher Safadi
 *
 */
public interface DataBaseEngine extends Service{
	public Connection createConnection();
	public Connection getConnection();
	public Action execute(Action action) throws Exception;
	public Action executeInsert(Action action) throws Exception;
	public Action executeUpdate(Action action) throws Exception;
	public Action executeSelect(Action action) throws Exception;
	public Action executeDelete(Action action) throws Exception;
	public Action executeFreeStatement(Action action) throws Exception;
	public Action executeSingleValueProcedure(Action action) throws Exception;
	public Action executeMultiValueProcedure(Action action)  throws Exception;
	public Action executeFunction(Action action) throws Exception;
	public java.util.List<String> getTableFields(String tableName);
	
}