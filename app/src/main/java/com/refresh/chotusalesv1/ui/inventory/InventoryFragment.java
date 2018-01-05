package com.refresh.chotusalesv1.ui.inventory;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.inventory.Inventory;
import com.refresh.chotusalesv1.domain.inventory.Product;
import com.refresh.chotusalesv1.domain.inventory.ProductCatalog;
import com.refresh.chotusalesv1.domain.inventory.productdetail;
import com.refresh.chotusalesv1.domain.sale.Register;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.DatabaseContents;
import com.refresh.chotusalesv1.techicalservices.DatabaseExecutor;
import com.refresh.chotusalesv1.techicalservices.Demo;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.ButtonAdapter;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.ui.mainui.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;

/**
 * UI for Inventory, shows list of Product in the ProductCatalog.
 * Also use for a sale process of adding Product into sale.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("ValidFragment")
public class InventoryFragment extends UpdatableFragment {

	protected static final int SEARCH_LIMIT = 0;
	private ListView inventoryListView;
	private ProductCatalog productCatalog;
	private List<Map<String, String>> inventoryList;
	private Button addProductButton;
	private EditText searchBox;
	private Button scanButton;

	private ViewPager viewPager;
	private Register register;
	private MainActivity main;

	private DatabaseExecutor dbExec;
	private UpdatableFragment saleFragment;
	private Resources res;
	private Inventory InvF;
	private DatabaseStat DBSTAT;
	private sessionmanager mOrderSession;
	private com.refresh.chotusalesv1.domain.Settings Settings;

	/**
	 * Construct a new InventoryFragment.
	 * @param saleFragment
	 */
	public InventoryFragment(UpdatableFragment saleFragment) {
		super();
		this.saleFragment = saleFragment;
//		Inventory
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		try {

			this.InvF = new Inventory(getActivity().getApplicationContext());
			productCatalog = InvF.getProductCatalog();
			register = new Register(getActivity().getApplicationContext());
			dbExec = new DatabaseExecutor(getActivity().getApplicationContext());
			DBSTAT = new DatabaseStat(getActivity().getApplicationContext());
		mOrderSession = new sessionmanager(getActivity().getApplicationContext());
		HashMap<String, String> K = mOrderSession.getUserDetails();
		String email = K.get(KEY_EMAIL);
		Settings = DBSTAT.settingDaoD.getSettings(email).get(0);
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}

		View view = inflater.inflate(R.layout.layout_inventory, container, false);

		res = getResources();
		inventoryListView = (ListView) view.findViewById(R.id.productListView);
		addProductButton = (Button) view.findViewById(R.id.addProductButton);
		scanButton = (Button) view.findViewById(R.id.scanButton);
		searchBox = (EditText) view.findViewById(R.id.searchBox);

		main = (MainActivity) getActivity();
		viewPager = main.getViewPager();

		initUI();
		return view;
	}

	/**
	 * Initiate this UI.
	 */
	private void initUI() {

		addProductButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showPopup(v);
			}
		});

		searchBox.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if (s.length() >= SEARCH_LIMIT) {
					search();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});

		inventoryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
				int id = Integer.parseInt(inventoryList.get(position).get("id").toString());

				Product p = productCatalog.getProductById(id);
				productdetail pds = getTax(p);
				p.setUnitPrice(pds.taxUnitPrice);

				Boolean setTranTax = false;

				if(Settings==null){}
				else if(Settings.CheckPrintTranGST ==null){}
				else
				setTranTax = Settings.CheckPrintTranGST;

//				register.setTranTrax();
				register.addItem(p, 1,pds.tax, setTranTax);
				saleFragment.update();
				viewPager.setCurrentItem(1);
			}     
		});

		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				IntentIntegratorSupportV4 scanIntegrator = new IntentIntegratorSupportV4(InventoryFragment.this);
				IntentIntegratorSupportV4 scanIntegrator = new IntentIntegratorSupportV4(InventoryFragment.this);
				scanIntegrator.initiateScan();
//				scanIntegrator.initiateScan();
//				IntentIntegrator.(this).initiateScan();
			}
		});

	}

	/**
	 * Show list.
	 * @param list
	 */
	private void showList(List<Product> list) {

		inventoryList = new ArrayList<Map<String, String>>();
		for(Product product : list) {
			inventoryList.add(product.toMap());
		}

		ButtonAdapter sAdap = new ButtonAdapter(getActivity().getBaseContext(), inventoryList,
				R.layout.listview_inventory, new String[]{"name"}, new int[] {R.id.name}, R.id.optionView, "id");
		inventoryListView.setAdapter(sAdap);
	}

	public productdetail getTax(Product p) {
		productdetail pd = new productdetail();
		pd.taxUnitPrice =p.getUnitPrice();
		pd.tax =0.00;
		if (p.getTaxid() != -1){
			String queryString4tax = "SELECT * FROM " + DatabaseContents.TABLE_TAX + " WHERE _id= " + p.getTaxid();
			List<Object> objectListtax = dbExec.select(queryString4tax);
			for (Object taxObject : objectListtax) {
				ContentValues contenttax = (ContentValues) taxObject;
				Boolean IsPriceInclusive = contenttax.getAsInteger("IsPriceInclusive") == 1;
				Double taxPercent = contenttax.getAsDouble("taxpercent");
				pd = pd.calculatetax(p.getUnitPrice(), taxPercent, IsPriceInclusive);
				break;
			}
		}
	return pd;
	}


	/**
	 * Search.
	 */
	private void search() {
		String search = searchBox.getText().toString();

		if (search.equals("/demo")) {
			testAddProduct();
			searchBox.setText("");
		} else if (search.equals("/clear")) {
			dbExec.dropAllData();
			searchBox.setText("");
		}
		else if (search.equals("")) {
			showList(productCatalog.getAllProduct());
		} else {
			List<Product> result = productCatalog.searchProduct(search);
			showList(result);
			if (result.isEmpty()) {

			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			searchBox.setText(scanContent);
		} else {
			Toast.makeText(getActivity().getBaseContext(), res.getString(R.string.fail),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Test adding product
	 */
	protected void testAddProduct() {
		Demo.testProduct(getActivity());
		Toast.makeText(getActivity().getBaseContext(), res.getString(R.string.success),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show popup.
	 * @param anchorView
	 */
	public void showPopup(View anchorView) {
		AddProductDialogFragment newFragment = new AddProductDialogFragment(InventoryFragment.this);
		newFragment.show(getFragmentManager(), "");
	}

	@Override
	public void update() {
		search();
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

}