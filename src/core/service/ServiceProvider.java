package core.service;

import java.util.*;

import core.db.DataBaseEngine;
import core.db.OracleDataBaseEngine;
import core.db.SqlServerDataBaseEngine;
import core.util.Config;
public class ServiceProvider {
	private static Map<String, Service> services;
	public static final String dataBaseService = "dbs";
	public static boolean refreshServices(){
		if(services == null)
			services = new HashMap<String, Service>();
		else
			services.clear();
		services.put(dataBaseService, loadDataBaseService());
		return true;
	}
	public static Service getService(String serviceName){
		if(services == null || services.size() == 0)
			refreshServices();
		return services.get(serviceName);
	}
	private static DataBaseEngine loadDataBaseService(){
		/*
		 * From Configurations check what is active data base then connect to it
		 * Currently and by default it is oracle 
		*/
		DataBaseEngine dbe = null;
		if(Config.getType() == Config.OracleType)
			dbe = new OracleDataBaseEngine();
		else if (Config.getType() == Config.SqlType)
			dbe = new SqlServerDataBaseEngine();
		return dbe;
			
	}
}
