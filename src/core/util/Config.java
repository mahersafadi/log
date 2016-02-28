package core.util;

public class Config {
	public static int OracleType = 1;
	public static int SqlType = 2;
	private static String userName = null;
	private static String password = null;
	private static Integer type = null;
	private static String driver = null;
	private static String host = null;
	private static String port = null;
	private static String connectionType = null;
	private static String instance = null;
	private static void parseConfigurationXMLFile(){
		/*userName = "CMMI";
		password = "CMMI";
		type = OracleType;
		driver = "oracle.jdbc.driver.OracleDriver";
		host = "localhost";
		port = "1521";
		connectionType = "jdbc:oracle:thin";
		instance = "orcl";*/
		userName = "sa";
		password = "stockadmin";
		type = SqlType;
		driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		host = "localhost";
		port = "1433";
		//connectionType = "jdbc:oracle:thin";
		instance = "ESTORE";
	}
	public static String getUser(){
		if(userName == null || "".equals(userName))
			parseConfigurationXMLFile();
		return userName;
	}
	public static String getPassword(){
		if(password == null || "".equals(password))
			parseConfigurationXMLFile();
		return password;
	}
	public static Integer getType(){
		if(type == null)
			parseConfigurationXMLFile();
		return type;
	}
	public static String getDriver(){
		if(driver == null || "".equals(driver))
			parseConfigurationXMLFile();
		return driver;
	}
	public static String getHost(){
		if(host == null || "".equals(host))
			parseConfigurationXMLFile();
		return host;
	}
	public static String getPort(){
		if(port == null || "".equals(port))
			parseConfigurationXMLFile();
		return port;
	}
	public static String getConnectionType(){
		if(connectionType == null || "".equals(connectionType))
			parseConfigurationXMLFile();
		return connectionType;
	}
	public static String getInstance(){
		if(instance == null || "".equals(instance))
			parseConfigurationXMLFile();
		return instance;
	}
}
