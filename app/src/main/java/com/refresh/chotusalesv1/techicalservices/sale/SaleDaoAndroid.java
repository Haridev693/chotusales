package com.refresh.chotusalesv1.techicalservices.sale;

import android.content.ContentValues;
import android.content.Context;

import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.inventory.Product;
import com.refresh.chotusalesv1.domain.sale.BuyerClass;
import com.refresh.chotusalesv1.domain.sale.QuickLoadSale;
import com.refresh.chotusalesv1.domain.sale.Sale;
import com.refresh.chotusalesv1.techicalservices.AndroidDatabase;
import com.refresh.chotusalesv1.techicalservices.Database;
import com.refresh.chotusalesv1.techicalservices.DatabaseContents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * DAO used by android for Sale process.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class SaleDaoAndroid implements SaleDao {

	Database database;

	public SaleDaoAndroid(Context c) {
		Database database1 = new AndroidDatabase(c);
		this.database = database1;
	}

	@Override
	public Sale initiateSale(String startTime, Boolean trantax) {
		ContentValues content = new ContentValues();
		content.put("start_time", startTime.toString());
		content.put("status", "ON PROCESS");
		content.put("payment", "n/a");
		content.put("total", "0.0");
		content.put("subtotal", "0.0");
		content.put("buyerid", -1);
        content.put("discount","0.0");
		content.put("trantax",trantax);
		content.put("tax", "0.0");
		content.put("orders", "0");
		content.put("end_time", startTime.toString());
		content.put("PayType","Cash");

		int id = database.insert(DatabaseContents.TABLE_SALE.toString(), content);
		return new Sale(id, startTime);
	}

	@Override
	public void endSale(Sale sale, String endTime) {
		ContentValues content = new ContentValues();
		content.put("_id", sale.getId());
		content.put("status", "ENDED");
		content.put("payment", "n/a");
		content.put("trantax", sale.getTranTax());
		if(sale.getTranTax()){
			content.put("total", sale.Total);
			content.put("subtotal", sale.SubTotal);
			content.put("tax", sale.Tax);
		}
		else {
			content.put("total", sale.getTotal());
			content.put("subtotal", sale.getSubTotal());
			content.put("tax", sale.getTaxTotal());
		}
		content.put("discount",sale.getDiscount());
		content.put("orders", sale.getOrders());
//		content.put()
		content.put("start_time", sale.getStartTime());
		content.put("end_time", endTime);
		content.put("buyerid", sale.getBuyerid());
		content.put("PayType",sale.getPayType());
		database.update(DatabaseContents.TABLE_SALE.toString(), content);
	}

	@Override
	public int addLineItem(int saleId, LineItem lineItem) {
		ContentValues content = new ContentValues();
		content.put("sale_id", saleId);
		content.put("product_id", lineItem.getProduct().getId());
		content.put("quantity", lineItem.getQuantity());
		content.put("unit_price", lineItem.getPriceAtSale());
		content.put("tax", lineItem.getTax());
		int id = database.insert(DatabaseContents.TABLE_SALE_LINEITEM.toString(), content);
		return id;
	}

	@Override
	public void updateLineItem(int saleId, LineItem lineItem) {
		ContentValues content = new ContentValues();
		content.put("_id", lineItem.getId());
		content.put("sale_id", saleId);
		content.put("product_id", lineItem.getProduct().getId());
		content.put("quantity", lineItem.getQuantity());
		content.put("unit_price", lineItem.getPriceAtSale());
		content.put("tax", lineItem.getTax());
		database.update(DatabaseContents.TABLE_SALE_LINEITEM.toString(), content);
	}

	@Override
	public List<Sale> getAllSale() {
		return getAllSale(" WHERE status = 'ENDED'");
	}

	@Override
	public List<Sale> getAllSaleDuring(Calendar start, Calendar end) {
		String startBound = DateTimeStrategy.getSQLDateFormat(start);
		String endBound = DateTimeStrategy.getSQLDateFormat(end);
		List<Sale> list = getAllSale(" WHERE end_time BETWEEN '" + startBound + " 00:00:00' AND '" + endBound + " 23:59:59' AND status = 'ENDED'");
		return list;
	}

	/**d
	 * This method get all Sale *BUT* no LineItem will be loaded.
	 *
	 * @param condition
	 * @return
	 */
	public List<Sale> getAllSale(String condition) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE + condition;
		List<Object> objectList = database.select(queryString);
		List<Sale> list = new ArrayList<Sale>();
		for (Object object : objectList) {
			ContentValues content = (ContentValues) object;
			list.add(new QuickLoadSale(
							content.getAsInteger("_id"),
							content.getAsString("start_time"),
							content.getAsString("end_time"),
							content.getAsString("status"),
							content.getAsDouble("total"),
							content.getAsDouble("tax"),
							content.getAsInteger("orders"),
							content.getAsInteger("buyerid"),
							content.getAsInteger("trantax")==1,
							content.getAsString("PayType"),
							content.getAsDouble("subtotal"),
							content.getAsDouble("discount")
					)
			);
		}
		return list;
	}

	/**
	 * This load complete data of Sale.
	 *
	 * @param id Sale ID.
	 * @return Sale of specific ID.
	 */
	@Override
	public Sale getSaleById(int id) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE + " WHERE _id = " + id;
		List<Object> objectList = database.select(queryString);
		List<Sale> list = new ArrayList<Sale>();
		for (Object object : objectList) {
			ContentValues content = (ContentValues) object;
			list.add(new Sale(
					content.getAsInteger("_id"),
					content.getAsString("start_time"),
					content.getAsString("end_time"),
					content.getAsString("status"),
					getLineItem(content.getAsInteger("_id")),
					content.getAsString("PayType"),
					content.getAsDouble("total"),
					content.getAsInteger("trantax")==1,
					content.getAsDouble("discount"))
			);
		}
		return list.get(0);
	}

	@Override
	public List<LineItem> getLineItem(int saleId) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE_LINEITEM + " WHERE sale_id = " + saleId;
		List<Object> objectList = database.select(queryString);
		List<LineItem> list = new ArrayList<LineItem>();
		for (Object object : objectList) {
			ContentValues content = (ContentValues) object;
			int productId = content.getAsInteger("product_id");
			String queryString2 = "SELECT * FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG + " WHERE _id = " + productId;
			List<Object> objectList2 = database.select(queryString2);

			List<Product> productList = new ArrayList<Product>();
			for (Object object2 : objectList2) {
				ContentValues content2 = (ContentValues) object2;
				productList.add(new Product(productId, content2.getAsString("name"), content2.getAsString("barcode"), content2.getAsDouble("unit_price"), content2.getAsInteger("taxid")));
			}
			list.add(new LineItem(content.getAsInteger("_id"), productList.get(0), content.getAsInteger("quantity"), content.getAsDouble("tax"), content.getAsDouble("unit_price")));
		}
		return list;
	}

	@Override
	public void clearSaleLedger() {
		database.execute("DELETE FROM " + DatabaseContents.TABLE_SALE);
		database.execute("DELETE FROM " + DatabaseContents.TABLE_SALE_LINEITEM);
	}

	@Override
	public void cancelSale(Sale sale, String endTime) {
		ContentValues content = new ContentValues();
		content.put("_id", sale.getId());
		content.put("status", "CANCELED");
		content.put("payment", "n/a");
		content.put("total", sale.getTotal());
		content.put("orders", sale.getOrders());
		content.put("start_time", sale.getStartTime());
		content.put("end_time", endTime);
		database.update(DatabaseContents.TABLE_SALE.toString(), content);

	}

	@Override
	public void removeLineItem(int id) {
		database.delete(DatabaseContents.TABLE_SALE_LINEITEM.toString(), id);
	}

	@Override
	public int addBuyer(String buyername, String buyerphone) {
		ContentValues content = new ContentValues();

//
//        + "buyername TEXT,"
//                + "buyerphone TEXT,"
//                + "buyerAddress TEXT"
		content.put("buyername", buyername);
		content.put("buyerphone", buyerphone);

		int id = database.insert(DatabaseContents.TABLE_BUYERS.toString(), content);
		return id;
	}

	@Override
	public BuyerClass getBuyer(int id) {

		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_BUYERS + " WHERE _id = " + id;

		BuyerClass buyer = new BuyerClass();
		buyer.id = id;
		buyer.Buyername = "";
//		String buyerPhone =
		List<Object> objectList = database.select(queryString);

//		List<Sale> list = new ArrayList<Sale>();
		for (Object object : objectList) {
			ContentValues content = (ContentValues) object;
			buyer.Buyername = content.getAsString("buyername");
			buyer.BuyerPhone = content.getAsString("buyerphone");//, buyerphone);
		}
		return buyer;
	}
}
