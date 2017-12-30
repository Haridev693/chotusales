package com.refresh.chotusalesv1.domain.inventory;

import android.content.Context;

import com.refresh.chotusalesv1.techicalservices.Database;
import com.refresh.chotusalesv1.techicalservices.NoDaoSetException;
import com.refresh.chotusalesv1.techicalservices.inventory.InventoryDao;
import com.refresh.chotusalesv1.techicalservices.inventory.InventoryDaoAndroid;

/**
 * This class is service locater for Product Catalog and Stock.
 * 
\developed by Sri Haridev Software Solutions
 * 
 */
public class Inventory {

	private Stock stock;
	private ProductCatalog productCatalog;
	private InventoryDao inventoryDao = null;
	private Database database;

	/**
	 * Constructs Data Access Object of inventory. 
	 * @throws NoDaoSetException if DAO is not exist.
	 */
	public Inventory(Context c) {

		inventoryDao = new InventoryDaoAndroid(c);
		stock = new Stock(inventoryDao);
		productCatalog = new ProductCatalog(inventoryDao);
	}

//	/**
//	 * Determines whether the DAO already set or not.
//	 * @return true if the DAO already set; otherwise false.
//	 */
//	public  boolean isDaoSet() {
//		return inventoryDao != null;
//	}
//
//	/**
//	 * Sets the database connector.
//	 * @param dao Data Access Object of inventory.
//	 */
//	public  void setInventoryDao(InventoryDao dao) {
//		inventoryDao = dao;
//	}

	/**
	 * Returns product catalog using in this inventory.
	 * @return product catalog using in this inventory.
	 */
	public ProductCatalog getProductCatalog() {
		return productCatalog;
	}

	/**
	 * Returns stock using in this inventory.
	 * @return stock using in this inventory.
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * Returns the instance of this singleton class.
	 * @return instance of this class.
	 * @throws NoDaoSetException if DAO was not set.
	 */
//	public static Inventory getInstance() throws NoDaoSetException {
//		if (instance == null)
//			instance = new Inventory();
//		return instance;
//	}

}
