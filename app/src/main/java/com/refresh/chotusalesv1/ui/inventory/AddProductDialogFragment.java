package com.refresh.chotusalesv1.ui.inventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.inventory.Inventory;
import com.refresh.chotusalesv1.domain.inventory.ProductCatalog;
import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.ui.component.taxSettingsAdapter;

import java.util.ArrayList;

/**
 * A dialog of adding a Product.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("ValidFragment")
public class AddProductDialogFragment extends DialogFragment {

	private EditText barcodeBox;
	private ProductCatalog productCatalog;
	private Button scanButton;
	private EditText priceBox;
	private Spinner taxSpinner;
	private EditText nameBox;
	private Button confirmButton;
	private Button clearButton;
	private UpdatableFragment fragment;
	private Resources res;
	private Inventory AddInv;
	private ArrayList<taxSettings> taxTypes;
	private DatabaseStat dataStat;

	/**
	 * Construct a new AddProductDialogFragment
	 * @param fragment
	 */
	public AddProductDialogFragment(UpdatableFragment fragment) {
		
		super();
		this.fragment = fragment;
		this.taxTypes = new ArrayList<taxSettings>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AddInv = new Inventory(getActivity().getApplicationContext());
		dataStat = new DatabaseStat(getActivity().getApplicationContext());

		taxSettings t = new taxSettings();

		t._id =-1;
		t.taxname = "NoTax";
		taxTypes.add(t);
		taxTypes.addAll(dataStat.settingDaoD.getTaxSettings());


//		try {
		productCatalog = AddInv.getProductCatalog();



//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}
		
		View v = inflater.inflate(R.layout.layout_addproduct, container,
				false);
		
		res = getResources();

		taxSpinner = (Spinner) v.findViewById(R.id.taxSpinner);
		barcodeBox = (EditText) v.findViewById(R.id.barcodeBox);
		scanButton = (Button) v.findViewById(R.id.scanButton);
		priceBox = (EditText) v.findViewById(R.id.priceBox);
		nameBox = (EditText) v.findViewById(R.id.nameBox);
		confirmButton = (Button) v.findViewById(R.id.confirmButton);
		clearButton = (Button) v.findViewById(R.id.clearButton);


		fillSpinner();
		initUI();
		return v;
	}

	private void fillSpinner()
	{
		taxSettingsAdapter adapter = new taxSettingsAdapter(getActivity(), android.R.layout.simple_spinner_item, taxTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		taxSpinner.setAdapter(adapter);
	}


	/**
	 * Construct a new 
	 */
	private void initUI() {
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegratorSupportV4 scanIntegrator = new IntentIntegratorSupportV4(AddProductDialogFragment.this);
				scanIntegrator.initiateScan();
			}
		});

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (nameBox.getText().toString().equals("")
					|| barcodeBox.getText().toString().equals("")
					|| priceBox.getText().toString().equals("")) {
					
					Toast.makeText(getActivity().getBaseContext(),
							res.getString(R.string.please_input_all), Toast.LENGTH_SHORT)
							.show();
					
				} else {
					int taxid;
					if(taxSpinner.getCount()>0) {
						taxid = ((taxSettings) taxSpinner.getSelectedItem())._id;
					}
					else
					{
						taxid =-1;
					}
					boolean success = productCatalog.addProduct(nameBox
							.getText().toString(), barcodeBox.getText()
							.toString(), Double.parseDouble(priceBox.getText()
							.toString()),taxid);

					if (success) {
						Toast.makeText(getActivity().getBaseContext(),
								res.getString(R.string.success) + ", "
										+ nameBox.getText().toString(), 
								Toast.LENGTH_SHORT).show();
						
						fragment.update();
						clearAllBox();
						AddProductDialogFragment.this.dismiss();
						
					} else {
						Toast.makeText(getActivity().getBaseContext(),
								res.getString(R.string.fail),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barcodeBox.getText().toString().equals("") && nameBox.getText().toString().equals("") && priceBox.getText().toString().equals("")){
					AddProductDialogFragment.this.dismiss();
				} else {
					clearAllBox();
				}
			}
		});
	}

	/**
	 * Clear all box
	 */
	private void clearAllBox() {
		barcodeBox.setText("");
		nameBox.setText("");
		priceBox.setText("");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			barcodeBox.setText(scanContent);
		} else {
			Toast.makeText(getActivity().getBaseContext(),
					res.getString(R.string.fail),
					Toast.LENGTH_SHORT).show();
		}
	}
}
