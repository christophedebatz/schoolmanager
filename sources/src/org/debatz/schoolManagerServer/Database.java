package org.debatz.schoolManagerServer;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.debatz.Utils.Utils;


public class Database implements Serializable {
	

	private static final long serialVersionUID = 1L;
	public static int UNKNOW_ID = -1;
	private Properties dbConfig = null;	
	private static Connection connect;
	private static Database _instance;
	
	
	
	
	public Database () {
		try {
			dbConfig = Utils.loadProperties(Utils.dbConfigFile);
			
			connect = DriverManager.getConnection(
					dbConfig.getProperty("db_protocol") + 
					dbConfig.getProperty("db_name") + 
					";create=" + dbConfig.getProperty("db_create")
			);
			
			System.out.println("Now connected to " + dbConfig.getProperty("db_name"));
	        loadDriver();
	        
	        if (!tableExists("courses")) {
	        	createTables();
	        	System.out.println("Tables has been created on " + dbConfig.getProperty("db_name"));
	        }
	        
		} catch (Exception e) {
			Utils.alert(e);
		}
	}
	
	
	
	
	
	public static Database getInstance () {
		if (_instance == null)
			_instance = new Database();
		return _instance;
	}
	
	
	
	public static void removeInstance () {
		_instance = null;
	}
	
	
	
	
	private void createTables () throws SQLException {
		Statement st = connect.createStatement();
		String createTablesFile = "createTables.query";
		Scanner scanner;
		
		try {
			scanner = new Scanner(new File(createTablesFile));
			while (scanner.hasNextLine()) {
				String line = (String) scanner.nextLine();
			    st.execute(line);
			    System.out.println("Nouvelle table crŽŽe : " + line.substring(13, 20));
			}
			scanner.close();
			st.close();
		} catch (Exception e) {
			Utils.alert(e);
		}
	}
	
	    
	public boolean tableExists (String tableName) throws Exception {
		DatabaseMetaData metadata = null;
	    metadata = connect.getMetaData();
	    ResultSet tableNames = metadata.getTables(null, null, null, null);
	    while(tableNames.next()) {
	    	String tab = tableNames.getString("TABLE_NAME");
	    	if (tableName != null && tab.equalsIgnoreCase(tableName))
	    		return true;
	    }
	    return false;
	} 

	
	    
	public ResultSet select (String query) throws SQLException {
	   	Statement s = connect.createStatement();
	   	return s.executeQuery(query); // never forget to close cursor
   }
	   
	    
	    
	public void closeConnection () {
	    if (connect == null) {
	    	try {
	    		connect.close();
	    		DriverManager.getConnection(
	    			dbConfig.getProperty("db_protocol") + 
					dbConfig.getProperty("db_name") + ";shutdown=true"
	    		);
	   		} catch (SQLException se) {
	   			if (se.getErrorCode() == 50000 && ("XJ015").equals(se.getSQLState())) {
	   				System.out.println("Derby shut down normally.");
                } else {
                    System.err.println("Derby did not shut down normally.");
                    printSQLException(se);
                } // end if
            } // end try
	    } // end if
	}
	  

	private void loadDriver() {      
		try {
			Class.forName(dbConfig.getProperty("db_driver")).newInstance();
	        System.out.println("Loaded the appropriate driver.");
	    } catch (ClassNotFoundException cnfe) {
	    	System.err.println("\nUnable to load the JDBC driver " + dbConfig.getProperty("db_driver"));
	        System.err.println("Please check your CLASSPATH.");
	        cnfe.printStackTrace(System.err);
	    } catch (InstantiationException ie) {
	        System.err.println("\nUnable to instantiate the JDBC driver " + dbConfig.getProperty("db_driver"));
	        ie.printStackTrace(System.err);
	    } catch (IllegalAccessException iae) {
	        System.err.println("\nNot allowed to access the JDBC driver " + dbConfig.getProperty("db_driver"));
	        iae.printStackTrace(System.err);
	    }
	}


	public void printSQLException(SQLException e) {
		while (e != null) {
			System.err.println("\n----- SQL Exception -----");
	        System.err.println("  SQL State:  " + e.getSQLState());
	        System.err.println("  Error Code: " + e.getErrorCode());
	        e = e.getNextException();
	    }
	}
	
	
	public static boolean remove () {
		File rm1 = new File ("derby.log");
		File rm2 = new File ("./derbyDB/");
		System.out.println("Deleting database");
		return Utils.deleteDirectory(rm1) && Utils.deleteDirectory(rm2);
	}
	
	
	public static int getLastId (String tableName) throws SQLException {
		Statement ps = connect.createStatement();
		ResultSet result = ps.executeQuery(
				"select identity_val_local() as last_id from " + tableName.toLowerCase());
		return result.next() ? result.getInt("last_id") : UNKNOW_ID;
	}
	
	
	public String getConfig (String propertyName) {
		return dbConfig.getProperty(propertyName).toString();
	}
	
	
	public Connection getConnection () {
		return connect;
	}
	
	
	@Override
	protected void finalize () throws Throwable {
		try {
			closeConnection();
		} finally {
			super.finalize();
		}
	}
	
}
