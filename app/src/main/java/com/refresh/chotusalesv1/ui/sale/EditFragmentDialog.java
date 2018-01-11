package com.refresh.chotusalesv1.ui.sale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.inventory.Product;
import com.refresh.chotusalesv1.domain.inventory.productdetail;
import com.refresh.chotusalesv1.domain.sale.Register;
import com.refresh.chotusalesv1.techicalservices.DatabaseContents;
import com.refresh.chotusalesv1.techicalservices.DatabaseExecutor;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;

import java.util.List;

import static com.refresh.chotusalesv1.domain.inventory.productdetail.round;

/**
 * A dialog for edit a LineItem of sale,
 * overriding price or set the quantity.
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("ValidFragment")
public class EditFragmentDialog extends DialogFragment {
//	private Register register;
	private UpdatableFragment saleFragment;
	private UpdatableFragment reportFragment;
	private EditText quantityBox;
	private EditText priceBox;
	private Button comfirmButton;
	private String saleId;
	private String position;
	private LineItem lineItem;
	private Button removeButton;
	private Register register;
	private DatabaseExecutor dbExec;
	
	/**
	 * Construct a new  EditFragmentDialog.
	 * @param saleFragment
	 * @param reportFragment
	 */
	public EditFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
		super();
		this.saleFragment = saleFragment;
		this.reportFragment = reportFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_saleedit, container, false);
//		try {
			register = new Register(getActivity().getApplicationContext());
		dbExec = new DatabaseExecutor(getActivity().getApplicationContext());
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}
		
		quantityBox = (EditText) v.findViewById(R.id.quantityBox);
//		priceBox = (EditText) v.findViewById(R.id.priceBox);
		comfirmButton = (Button) v.findViewById(R.id.confirmButton);
		removeButton = (Button) v.findViewById(R.id.removeButton);
		
		saleId = getArguments().getString("sale_id");
		position = getArguments().getString("position");

		lineItem = register.getCurrentSale().getLineItemAt(Integer.parseInt(position));
		quantityBox.setText(lineItem.getQuantity()+"");
//		priceBox.setText(lineItem.getPriceAtSale()+"");
		removeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("remove", "id=" + lineItem.getId());
				register.removeItem(lineItem);
				end();
			}
		});

		comfirmButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {

//				productdetail p =getTax(lineItem.getProduct(),Double.parseDouble(priceBox.getText().toString()));
//				round(p.tax,2);
//				round
				register.updateItem(
						Integer.parseInt(saleId),
						lineItem,
						Integer.parseInt(quantityBox.getText().toString()),
						0.00,
						0.00);
				
				end();
			}
			
		});
		return v;
	}
	
	/**
	 * End.
	 */
	private void end(){
		saleFragment.update();
//		reportFragment.update();
		this.dismiss();
	}

	public productdetail getTax(Product p, Double newPrice) {
		productdetail pd = new productdetail();
		pd.taxUnitPrice = newPrice;
		pd.tax =0.00;
		if (p.getTaxid() != -1){
			String queryString4tax = "SELECT * FROM " + DatabaseContents.TABLE_TAX + " WHERE _id= " + p.getTaxid();
			List<Object> objectListtax = dbExec.select(queryString4tax);
			for (Object taxObject : objectListtax) {
				ContentValues contenttax = (ContentValues) taxObject;
				Boolean IsPriceInclusive = contenttax.getAsInteger("IsPriceInclusive") == 1;
				Double taxPercent = contenttax.getAsDouble("taxpercent");
				pd = pd.calculatetax(p.getUnitPrice(), taxPercent, IsPriceInclusive);
				pd.tax = round(pd.tax,2);
				pd.taxUnitPrice = round(pd.taxUnitPrice,2);
				break;
			}
		}
		return pd;
	}
}
