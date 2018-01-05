package com.refresh.chotusalesv1.domain.sale;

/**
 * Sale that loads only total and orders.
 * It's for overview report that doesn't need LineItem's information.
 * NOTE: This Sale instance throws NullPointerException
 * when calling method that involve with LineItem.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class QuickLoadSale extends Sale {
	
	private Double total;
	private Double tax;
	private Integer orders;
	private Integer buyerid;
	private String PayType;
	
	/**
	 * @param id ID of this sale.
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param total
	 * @param orders numbers of lineItem in this Sale.
	 * @param trantax
	 * @param subtotal
	 * @param discounts
	 */
	public QuickLoadSale(int id, String startTime, String endTime, String status, Double total, Double tax, Integer orders, Integer buyerid, Boolean trantax, String PayType, Double subtotal, Double discounts) {
		super(id, startTime, endTime, status, null,"",total,trantax, discounts);
		this.total = total;
		this.tax = tax;
		this.orders = orders;
		this.buyerid = buyerid;
		this.PayType = PayType;
		this.setTranTax(trantax);
		this.Total = total;
		this.SubTotal = subtotal;
		this.setDiscount(discounts);
//		this.SubTotal = SubTotal;
	}
	
	@Override
	public int getOrders() {
		return orders;
	}
	
	@Override
	public double getTotal() {
		return total;
	}


	@Override
	public int getBuyerid() {
		if(buyerid==null){
			buyerid =-1;
		}
		return buyerid;
	}


	@Override
	public String getPayType() {
		return this.PayType;
	}


	//	getBuyerid
	@Override
	public double getTaxTotal() {
		return tax;
	}
}
