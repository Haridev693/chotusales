package com.refresh.chotusalesv1.ui.sale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;

import java.util.HashMap;

import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;

/**
 * A dialog for input a money for sale.
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("ValidFragment")
public class PaymentFragmentDialog extends DialogFragment {
	
	private TextView totalPrice;
	private EditText input, buyerName, buyerPhone,EdtDiscount;
	private Settings ShopSetting = new Settings();
	private Button clearButton;
	private Button confirmButton;
	private String strtext;
	private UpdatableFragment saleFragment;
	private UpdatableFragment reportFragment;
	private Spinner PayTypeSpinner;
	private sessionmanager mOrderSession;
	private DatabaseStat DBStat;
	private Double Discounts;

	/**
	 * Construct a new PaymentFragmentDialog.
	 * @param saleFragment
	 * @param reportFragment
	 */
	public PaymentFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
		super();
		this.saleFragment = saleFragment;
		this.reportFragment = reportFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_payment, container,false);
		DBStat = new DatabaseStat(getActivity().getApplicationContext());
		strtext=getArguments().getString("edttext");
		input = (EditText) v.findViewById(R.id.dialog_saleInput);
		totalPrice = (TextView) v.findViewById(R.id.payment_total);

		EdtDiscount = (EditText) v.findViewById(R.id.EdtDiscount);

		mOrderSession = new sessionmanager(getActivity().getApplicationContext());
		HashMap<String, String> K = mOrderSession.getUserDetails();
		String email = K.get(KEY_EMAIL);

		Discounts= 0.0;

//		regis.getCurrentSale().getAllLineItem();
//		regis.getTotal();

		if (email == null) {
		} else {
			if (DBStat.settingDaoD.getSettings(email).size() > 0) {
				ShopSetting = DBStat.settingDaoD.getSettings(email).get(0);
			}
		}

        buyerName = (EditText) v.findViewById(R.id.Buyername);
        buyerPhone = (EditText) v.findViewById(R.id.Buyerphone);


		totalPrice.setText(strtext);
		clearButton = (Button) v.findViewById(R.id.clearButton);

		PayTypeSpinner = (Spinner) v.findViewById(R.id.PayTypeSpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
				R.array.PayType, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		PayTypeSpinner.setAdapter(adapter);
		PayTypeSpinner.setSelection(0);
		PayTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }

		});

		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				end();
			}
		});
		
		confirmButton = (Button) v.findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String inputString = input.getText().toString();

				if(EdtDiscount.length()>0)
				Discounts = Double.parseDouble(EdtDiscount.getText().toString());
				
				if (inputString.equals("")) {
					Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.please_input_all), Toast.LENGTH_SHORT).show();
					return;
				}

				if(ShopSetting==null){}

				else {
                    if (ShopSetting.SMSenabled == null) {
                    } else if(ShopSetting.SMSenabled) {
                    if (buyerPhone.getText().length() != 10) {
                        Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.please_fill_10_digits), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
				}
				if(buyerPhone.getText().length()>0)
				{
					if(buyerPhone.getText().length()!=10)
					{
						Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.please_fill_10_digits), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				double a = Double.parseDouble(strtext)-Discounts;
				double b = Double.parseDouble(inputString);
				if (b < a) {
					Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.need_money) + " " + (b - a), Toast.LENGTH_SHORT).show();
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("edttext", b - a + "");
					bundle.putDouble("Discounts", Discounts);
					bundle.putString("buyername",buyerName.getText().toString());
					bundle.putString("buyerPhone",buyerPhone.getText().toString());
					bundle.putString("PayType", PayTypeSpinner.getSelectedItem().toString());
					EndPaymentFragmentDialog newFragment = new EndPaymentFragmentDialog(
							saleFragment, reportFragment);
					newFragment.setArguments(bundle);
					newFragment.show(getFragmentManager(), "");
					end();
				}

			}
		});


		totalPrice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				input.setText(totalPrice.getText());
			}
		});

		return v;
	}

    private void SaveBuyer(String buyerName, String buyerPhone) {
//		SaleDaoAndroid s = new SaleDaoAndroid

    }

    /**
	 * End.
	 */
	private void end() {
		this.dismiss();
		
	}
	

}
