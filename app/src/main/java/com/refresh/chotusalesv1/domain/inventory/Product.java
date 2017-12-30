package com.refresh.chotusalesv1.domain.inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Product or item represents the real product in store.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class Product {

	private int id;
	private String name;
	private String barcode;
	private double unitPrice;
	private int taxid;
	
	/**
	 * Static value for UNDEFINED ID.
	 */
	public static final int UNDEFINED_ID = -1;

	/**
	 * Constructs a new Product.
	 * @param id ID of the product, This value should be assigned from database.
	 * @param name name of this product.
	 * @param barcode barcode (any standard format) of this product.
	 * @param salePrice price for using when doing sale.
	 */
	public Product(int id, String name, String barcode, double salePrice, int taxID) {
		this.id = id;
		this.name = name;
		this.barcode = barcode;
		this.unitPrice = salePrice;
		this.taxid = taxID;
	}
	
	/**
	 * Constructs a new Product.
	 * @param name name of this product.
	 * @param barcode barcode (any standard format) of this product.
	 * @param salePrice price for using when doing sale.
	 */
	public Product(String name, String barcode, double salePrice, int taxID) {
		this(UNDEFINED_ID, name, barcode, salePrice, taxID);
	}

	/**
	 * Returns name of this product.
	 * @return name of this product.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name of this product.
	 * @param name name of this product.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets barcode of this product.
	 * @param barcode barcode of this product.
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * Sets price of this product.
	 * @param unitPrice price of this product.
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * Returns id of this product.
	 * @return id of this product.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns barcode of this product.
	 * @return barcode of this product.
	 */
	public String getBarcode() {
		return barcode;
	}
	
	/**
	 * Returns price of this product.
	 * @return price of this product.
	 */
	public double getUnitPrice() {
		return unitPrice;
	}


	/**
	 * Returns taxid of this product.
	 * @return taxid of this product.
	 */

	public int getTaxid(){return taxid;}


	/**
	 * set taxid of this product.
	 * @param taxid of this product.
	 */

	public void setTaxid(int taxid) {this.taxid = taxid;}



	/**
	 * Returns the description of this Product in Map format. 
	 * @return the description of this Product in Map format.
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id + "");
		map.put("name", name);
		map.put("barcode", barcode);
		map.put("unitPrice", unitPrice + "");
		map.put("taxID", taxid+"");
		return map;
		
	}
	
}
