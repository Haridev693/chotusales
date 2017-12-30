package com.refresh.chotusalesv1.techicalservices;

import android.content.Context;

import java.util.List;

/**
 * Uses to directly access to database.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class DatabaseExecutor {

	private Database database;
//	private DatabaseExecutor instance;
	
	public DatabaseExecutor(Context c) {
		Database database1 = new AndroidDatabase(c);
		this.database = database1;
	}
	
	/**
	 * Sets database for use in DatabaseExecutor.
//	 * @param db database.
	 */
//	public void setDatabase() {
//
//	}
	
//	public static DatabaseExecutor getInstance() {
//		if (instance == null)
//			instance = new DatabaseExecutor();
//		return instance;
//	}
	
	/**
	 * Drops all data in database.
	 */
	public void dropAllData() {
		execute("DELETE FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_SALE + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_SALE_LINEITEM + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_STOCK + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_STOCK_SUM + ";");
		execute("VACUUM;");
	}
	
	/**
	 * Directly execute to database.
	 * @param queryString query string for execute.
	 */
	private void execute(String queryString) {
		database.execute(queryString);
	}


	public List<Object> select(String queryString){
		List<Object> objectList = database.select(queryString);
		return objectList;
	}

	
	
}
