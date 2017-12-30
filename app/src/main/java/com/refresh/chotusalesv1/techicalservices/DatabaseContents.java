package com.refresh.chotusalesv1.techicalservices;

/**
 * Enum for name of tables in database.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public enum DatabaseContents {
	
	DATABASE("com.refresh.db1"),
	TABLE_PRODUCT_CATALOG("product_catalog"),
	TABLE_STOCK("stock"),
	TABLE_SALE("sale"),
	TABLE_SALE_LINEITEM("sale_lineitem"),
	TABLE_STOCK_SUM("stock_sum"),
	LANGUAGE("language"),
	TABLE_SETTINGS("settings"),
	TABLE_TAX("tax"),
	TABLE_USERS("Users"),
	TABLE_BUYERS("Buyers");
	
	private String name;
	
	/**
	 * Constructs DatabaseContents.
	 * @param name name of this content for using in database.
	 */
	private DatabaseContents(String name) {
		this.name = name;
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
