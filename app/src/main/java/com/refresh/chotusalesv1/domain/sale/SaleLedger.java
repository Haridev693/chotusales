package com.refresh.chotusalesv1.domain.sale;

import android.content.Context;

import com.refresh.chotusalesv1.techicalservices.sale.SaleDao;
import com.refresh.chotusalesv1.techicalservices.sale.SaleDaoAndroid;

import java.util.Calendar;
import java.util.List;

/**
 * Book that keeps sale record.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class SaleLedger {
	
//	private SaleLedger instance = null;
	private SaleDao saleDao = null;
	
	public SaleLedger(Context c) {

		saleDao = new SaleDaoAndroid(c);
	}
	
	/**
	 * Determines whether the DAO already set or not.
	 * @return true if the DAO already set; otherwise false.
	 */
//	public boolean isDaoSet() {
//		return saleDao != null;
//	}
	
//	public static SaleLedger getInstance() throws NoDaoSetException {
//		if (instance == null) instance = new SaleLedger();
//		return instance;
//	}

	/**
	 * Sets the database connector.
	 * @param dao Data Access Object of Sale.
	 */
//	public  void setSaleDao(SaleDao dao) {
//		saleDao = dao;
//	}
	
	/**
	 * Returns all sale in the records.
	 * @return all sale in the records.
	 */
	public List<Sale> getAllSale(){

			return saleDao.getAllSale();
	}

	public String getBuyerName(int id)
	{
		return saleDao.getBuyer(id);
//		return "";
	}

	
	/**
	 * Returns the Sale with specific ID.
	 * @param id ID of specific Sale.
	 * @return the Sale with specific ID.
	 */
	public Sale getSaleById(int id) {
		return saleDao.getSaleById(id);
	}

	/**
	 * Clear all records in SaleLedger.	
	 */
	public void clearSaleLedger() {
		saleDao.clearSaleLedger();
	}

	/**
	 * Returns list of Sale with scope of time. 
	 * @param start start bound of scope.
	 * @param end end bound of scope.
	 * @return list of Sale with scope of time.
	 */
	public List<Sale> getAllSaleDuring(Calendar start, Calendar end) {
		return saleDao.getAllSaleDuring(start, end);
	}
}
