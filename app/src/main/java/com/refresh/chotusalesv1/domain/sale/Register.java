package com.refresh.chotusalesv1.domain.sale;

import android.content.Context;

import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.inventory.Product;
import com.refresh.chotusalesv1.domain.inventory.Stock;
import com.refresh.chotusalesv1.techicalservices.Database;
import com.refresh.chotusalesv1.techicalservices.inventory.InventoryDao;
import com.refresh.chotusalesv1.techicalservices.inventory.InventoryDaoAndroid;
import com.refresh.chotusalesv1.techicalservices.sale.SaleDao;
import com.refresh.chotusalesv1.techicalservices.sale.SaleDaoAndroid;

/**
 * Handles all Sale processes.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class Register {
//	private static Register instance = null;
	private SaleDao saleDao = null;
	private Stock stock = null;
	private InventoryDao inventoryDao;
	
	private static Sale currentSale;
	private Database db;
	
	public Register(Context c){
//		if (!isDaoSet()) {
//			throw new NoDaoSetException();
//		}
//		this.db =  new AndroidDatabase(context);
		inventoryDao = new InventoryDaoAndroid(c);
//		 = new SettingsDao(c);
		stock = new Stock(inventoryDao);
		saleDao = new SaleDaoAndroid(c);
		
	}
	
	/**
	 * Determines whether the DAO already set or not.
	 * @return true if the DAO already set; otherwise false.
	 */
//	public static boolean isDaoSet() {
//		return saleDao != null;
////	}
	
//	public static Register getInstance() throws NoDaoSetException {
//		if (instance == null) instance = new Register();
//		return instance;
//	}

	/**
	 * Injects its sale DAO
//	 * @param dao DAO of sale
//	 */
//	public static void setSaleDao(SaleDao dao) {
//		saleDao = dao;
//	}
//
	/**
	 * Initiates a new Sale.
	 * @param startTime time that sale created.
	 * @return Sale that created.
	 */
	public Sale initiateSale(String startTime) {
		if (currentSale != null) {
			return currentSale;
		}
		currentSale = saleDao.initiateSale(startTime);
		return currentSale;
	}
	
	/**
	 * Add Product to Sale.
	 * @param product product to be added.
	 * @param quantity quantity of product that added.
	 * @return LineItem of Sale that just added.
	 */
	public LineItem addItem(Product product, int quantity, double tax) {
		if (currentSale == null)
			initiateSale(DateTimeStrategy.getCurrentTime());

		LineItem lineItem = currentSale.addLineItem(product, quantity, tax);
		
		if (lineItem.getId() == LineItem.UNDEFINED) {
			int lineId = saleDao.addLineItem(currentSale.getId(), lineItem);
			lineItem.setId(lineId);
		} else {
			saleDao.updateLineItem(currentSale.getId(), lineItem);
		}
		
		return lineItem;
	}
	
	/**
	 * Returns total price of Sale.
	 * @return total price of Sale.
	 */
	public double getTotal() {
		if (currentSale == null) return 0;
		return currentSale.getTotal();
	}


	public double getTaxTotal() {
		if (currentSale == null) return 0;
		return currentSale.getTaxTotal();
	}


	public double getSubTotal() {
		if (currentSale == null) return 0;
		return currentSale.getSubTotal();
	}

	/**
	 * End the Sale.
	 * @param endTime time that Sale ended.
	 */
	public void endSale(String endTime) {
		if (currentSale != null) {
			saleDao.endSale(currentSale, endTime);
			for(LineItem line : currentSale.getAllLineItem()){
				stock.updateStockSum(line.getProduct().getId(), line.getQuantity());
			}

			currentSale = null;
		}
	}


	public int SaveUser(String name, String Phonenum)
	{
		int id= saleDao.addBuyer(name,Phonenum);

		return id;
	}

	/**
	 * Returns the current Sale of this Register.
	 * @return the current Sale of this Register.
	 */
	public Sale getCurrentSale() {
		if (currentSale == null)
			initiateSale(DateTimeStrategy.getCurrentTime());
		return currentSale;
	}

	/**
	 * Sets the current Sale of this Register.
	 * @param id of Sale to retrieve.
	 * @return true if success to load Sale from ID; otherwise false.
	 */
	public boolean setCurrentSale(int id) {
		currentSale = saleDao.getSaleById(id);
		return false;
	}

	public void setCurrentSaleBuyerID(int id)
	{
		currentSale.setBuyerid(id);
	}

	public void setCurrentSalePayType(String Type)
	{
		currentSale.setPayType(Type);
	}

	/**
	 * Determines that if there is a Sale handling or not.
	 * @return true if there is a current Sale; otherwise false.
	 */
	public boolean hasSale(){
		if(currentSale == null)return false;
		return true;
	}
	
	/**
	 * Cancels the current Sale.
	 */
	public void cancleSale() {
		if (currentSale != null){
			saleDao.cancelSale(currentSale,DateTimeStrategy.getCurrentTime());
			currentSale = null;
		}
	}

	/**
	 * Edit the specific LineItem 
	 * @param saleId ID of LineItem to be edited. 
	 * @param lineItem LineItem to be edited.
	 * @param quantity a new quantity to set.
	 * @param priceAtSale a new priceAtSale to set.
	 */
	public void updateItem(int saleId, LineItem lineItem, int quantity, double priceAtSale, double tax) {
//		lineItem.setUnitPriceAtSale(priceAtSale);
		lineItem.setQuantity(quantity);
//		lineItem.setTax(tax);

//		lineItem.setTax(tax);

		saleDao.updateLineItem(saleId, lineItem);
	}

	/**
	 * Removes LineItem from the current Sale.
	 * @param lineItem lineItem to be removed.
	 */
	public void removeItem(LineItem lineItem) {
		saleDao.removeLineItem(lineItem.getId());
		currentSale.removeItem(lineItem);
		if (currentSale.getAllLineItem().isEmpty()) {
			cancleSale();
		}
	}
	
}