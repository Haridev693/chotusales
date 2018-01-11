package com.refresh.chotusalesv1.ui.sale;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.sale.Register;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.ui.mainui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.refresh.chotusalesv1.domain.inventory.productdetail.round;
import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;

//import android.app.AlertDialog;

/**
 * UI for Sale operation.
\developed by Sri Haridev Software Solutions
 *
 */
public class SaleFragment extends UpdatableFragment {
    
	private Register register;
	private ArrayList<Map<String, String>> saleList;
	private ListView saleListView;
	private Button clearButton;
	private TextView totalPrice;
	private Button endButton;
	private UpdatableFragment reportFragment;
	private Resources res;
	private BluetoothAdapter mBluetoothAdapter = null;
	private sessionmanager mOrderSession;
	private DatabaseStat DBStat;
	private Settings ShopSetting;
	private TextView subtotalPrice,taxPrice;
	private Boolean TrantaxEnable;
    private EventBus bus;


    /**
	 * Construct a new SaleFragment.
	 * @param
	 */
//	public SaleFragment(UpdatableFragment reportFragment) {
//		super();
//		this.reportFragment = reportFragment;
//	}

	public SaleFragment(){

		this.bus = EventBus.getDefault();
		this.bus.register(this);
		this.reportFragment = new ReportFragment();
	}

//	public SaleFragment(){
//
//	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		DBStat = new DatabaseStat(getActivity().getApplicationContext());
		mOrderSession = new sessionmanager(getActivity().getApplicationContext());
		register =new Register(getActivity().getApplicationContext());


		HashMap<String,String> K=  mOrderSession.getUserDetails();
		String email = K.get(KEY_EMAIL);
		TrantaxEnable =false;
		ShopSetting = new Settings();

		if(email==null) {}
		else{
			if(DBStat.settingDaoD.getSettings(email).size()>0) {
				ShopSetting = DBStat.settingDaoD.getSettings(email).get(0);

				if (ShopSetting == null) {
				} else if (ShopSetting.CheckPrintTranGST) {
					TrantaxEnable = ShopSetting.CheckPrintTranGST;
				}
			}
		}


		View view = inflater.inflate(R.layout.layout_sale, container, false);
		
		res = getResources();
		saleListView = (ListView) view.findViewById(R.id.sale_List);
		totalPrice = (TextView) view.findViewById(R.id.totalPrice);
		subtotalPrice = (TextView) view.findViewById(R.id.subtotalPrice);
		taxPrice = (TextView) view.findViewById(R.id.taxPrice);
		clearButton = (Button) view.findViewById(R.id.clearButton);
		endButton = (Button) view.findViewById(R.id.endButton);
		
		initUI();
//		initPrinter();
		return view;
	}

	/**
	 * Initiate this UI.
	 */
	private void initUI() {
		
		saleListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showEditPopup(arg1,arg2);
			}
		});

		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewPager viewPager = ((MainActivity) getActivity()).getViewPager();
				viewPager.setCurrentItem(1);
			}
		});

		endButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(register.hasSale()){
					showPopup(v);
				} else {
					Toast.makeText(getActivity().getBaseContext() , res.getString(R.string.hint_empty_sale), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!register.hasSale() || register.getCurrentSale().getAllLineItem().isEmpty()) {
					Toast.makeText(getActivity().getBaseContext() , res.getString(R.string.hint_empty_sale), Toast.LENGTH_SHORT).show();
				} else {
					showConfirmClearDialog();
				}
			} 
		});
	}
	
	/**
	 * Show list
	 * @param list
	 */
	private void showList(List<LineItem> list) {
		
		saleList = new ArrayList<Map<String, String>>();
		for(LineItem line : list) {
			saleList.add(line.toMap());
		}
		
		SimpleAdapter sAdap;
		sAdap = new SimpleAdapter(getActivity().getBaseContext(), saleList,
				R.layout.listview_lineitem, new String[]{"name","quantity","tax","price"}, new int[] {R.id.name,R.id.quantity,R.id.taxReport,R.id.price});
		saleListView.setAdapter(sAdap);
	}

	/**
	 * Try parsing String to double.
	 * @param value
	 * @return true if can parse to double.
	 */
	public boolean tryParseDouble(String value)  
	{  
		try  {  
			Double.parseDouble(value);  
			return true;  
		} catch(NumberFormatException e) {  
			return false;  
		}  
	}
	
	/**
	 * Show edit popup.
	 * @param anchorView
	 * @param position
	 */
	public void showEditPopup(View anchorView,int position){
		Bundle bundle = new Bundle();
		bundle.putString("position",position+"");
		bundle.putString("sale_id",register.getCurrentSale().getId()+"");
		bundle.putString("product_id",register.getCurrentSale().getLineItemAt(position).getProduct().getId()+"");
		
		EditFragmentDialog newFragment = new EditFragmentDialog(SaleFragment.this, reportFragment);
		newFragment.setArguments(bundle);
		newFragment.show(getFragmentManager(), "");
		
	}

	/**
	 * Show popup
	 * @param anchorView
	 */
	public void showPopup(View anchorView) {
//		initPrinter();
		Bundle bundle = new Bundle();
		bundle.putString("edttext", totalPrice.getText().toString());
		PaymentFragmentDialog newFragment = new PaymentFragmentDialog(SaleFragment.this, reportFragment);
		newFragment.setArguments(bundle);
		newFragment.show(getFragmentManager(), "");
	}

	@Override
	public void update() {
		if(register.hasSale()){
			showList(register.getCurrentSale().getAllLineItem());
			if(TrantaxEnable) {

				register.setTranTrax(TrantaxEnable);
				Double NewSubTotal = register.getSubTotal();
                register.setSubTotal(NewSubTotal);
				Double CGST = round(((NewSubTotal * ShopSetting.CGSTPercent) / 100),2);
				register.setCGST(CGST);
				Double SGST = round(((NewSubTotal * ShopSetting.SGSTPercent) / 100),2);
				register.setSGST(SGST);
				register.setTaxTotal(round(CGST+SGST,2));
				register.setTotal(round(NewSubTotal+CGST+SGST,2));
				subtotalPrice.setText(round(register.getSubTotalVal(),2)+"");
				taxPrice.setText(round(register.getTaxTotalVal(),2)+"");
				totalPrice.setText(round(register.getTotalVal(),2)  + "");
			}
			else {
				subtotalPrice.setText(round(register.getSubTotal(),2) + "");
				taxPrice.setText(round(register.getTaxTotal(),2)+"");
				totalPrice.setText(round(register.getTotal(),2) + "");
			}
		}
		else{
			showList(new ArrayList<LineItem>());
			totalPrice.setText("0.00");
			subtotalPrice.setText("0.00");
			taxPrice.setText("0.00");
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		update();
	}
	
	/**
	 * Show confirm or clear dialog.
	 */
	private void showConfirmClearDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(res.getString(R.string.dialog_clear_sale));
		dialog.setPositiveButton(res.getString(R.string.no), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		dialog.setNegativeButton(res.getString(R.string.clear), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				register.cancleSale();
				update();
			}
		});

		dialog.show();
	}


	//catch Event from fragment A
	@Subscribe
	public void onEvent(String event) {
		if(event.equals("true"))
			update();
	}

}
