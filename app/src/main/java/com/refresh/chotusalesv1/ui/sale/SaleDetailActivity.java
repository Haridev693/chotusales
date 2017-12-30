package com.refresh.chotusalesv1.ui.sale;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.sale.Sale;
import com.refresh.chotusalesv1.domain.sale.SaleLedger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UI for showing the detail of Sale in the record.
\developed by Sri Haridev Software Solutions
 *
 */
public class SaleDetailActivity extends AppCompatActivity{
	
	private TextView totalBox;
	private TextView dateBox;
	private TextView TenderType;
	private ListView lineitemListView;
	private List<Map<String, String>> lineitemList;
	private Sale sale;
	private int saleId;
	private SaleLedger saleLedger;
    private String PayType;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		try {
			saleLedger = new SaleLedger(getApplicationContext());
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}
		
		saleId = Integer.parseInt(getIntent().getStringExtra("id"));
		sale = saleLedger.getSaleById(saleId);
		
		initUI(savedInstanceState);
	}


	/**
	 * Initiate actionbar.
	 */
	@SuppressLint("NewApi")
	private void initiateActionBar() {
//		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			ActionBar actionBar = getActionBar();
			android.support.v7.app.ActionBar actionBar = getSupportActionBar();

					if(actionBar==null){

					}
					else {
						actionBar.setDisplayHomeAsUpEnabled(true);
						actionBar.setTitle(getResources().getString(R.string.sale));
						actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
					}
//		}
	}

//	private void initiateActionBar() {
////		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//
//		if(actionBar==null){}else {
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			actionBar.setTitle(res.getString(R.string.product_detail));
//			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
//		}
////		}
//	}
	

	/**
	 * Initiate this UI.
	 * @param savedInstanceState
	 */
	private void initUI(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_saledetail);
		
		initiateActionBar();
		
		totalBox = (TextView) findViewById(R.id.totalBox);
		TenderType = (TextView) findViewById(R.id.TenderType);
		dateBox = (TextView) findViewById(R.id.dateBox);
		lineitemListView = (ListView) findViewById(R.id.lineitemList);
	}

	/**
	 * Show list.
	 * @param list
	 */
	private void showList(List<LineItem> list) {
		lineitemList = new ArrayList<Map<String, String>>();
		for(LineItem line : list) {
			lineitemList.add(line.toMap());
		}

		SimpleAdapter sAdap = new SimpleAdapter(SaleDetailActivity.this, lineitemList,
				R.layout.listview_lineitem, new String[]{"name","quantity","tax","price"}, new int[] {R.id.name,R.id.quantity,R.id.taxReport,R.id.price});
		lineitemListView.setAdapter(sAdap);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Update UI.
	 */
	public void update() {
		totalBox.setText(sale.getTotal() + "");
		dateBox.setText(sale.getEndTime() + "");
        TenderType.setText(sale.getPayType());
		showList(sale.getAllLineItem());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		update();
	}
}
