package com.refresh.chotusalesv1.ui.sale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.apiops.smsapiops;
import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.domain.inventory.LineItem;
import com.refresh.chotusalesv1.domain.sale.Register;
import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.BluetoothService;
import com.refresh.chotusalesv1.techicalservices.connectivity;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.utils.commands.Command;
import com.refresh.chotusalesv1.utils.commands.PrinterCommand;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.refresh.chotusalesv1.domain.inventory.productdetail.round;
import static com.refresh.chotusalesv1.staticpackage.DatabaseStat.mbluetoothService;
import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;
import static com.refresh.chotusalesv1.utils.commands.Command.LF;

//import static com.refresh.chotusalesv1.staticpackage.DatabaseStat.ShopSetting;
//import static com.refresh.chotusalesv1.staticpackage.DatabaseStat.mbluetoothService;

/**
 * A dialog shows the total change and confirmation for Sale.
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("ValidFragment")
public class EndPaymentFragmentDialog extends DialogFragment  {

	private static final String TAG = "";
	private DatabaseStat dataStat;
	private Settings ShopSetting = new Settings();
	private Button doneButton;
	private TextView chg;
	private Register regis;
	private UpdatableFragment saleFragment;
	private UpdatableFragment reportFragment;
	private String buyerName, buyerPhone;
	private Double Discounts;



	//QRcode
	private static final int QR_WIDTH = 350;
	private static final int QR_HEIGHT = 350;
	/*******************************************************************************************************/
	private static final String CHINESE = "GBK";
	private static final String THAI = "CP874";
	private static final String KOREAN = "EUC-KR";
	private static final String BIG5 = "BIG5";


	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	private sessionmanager mOrderSession;
	private DatabaseStat DBStat;
	private String mConnectedDeviceName;
	private smsapiops smssender;
	private String PayType;
    private Activity Act;
	private ArrayList<taxSettings> taxsetting;
	private boolean PrintGST,PrintTranGST;
	private Double SGSTTranTax,CGSTTranTax;
//	private Register register;
//	private Settings ShopSetting;
//	private Handler mHandler;


	/**
	 * End this UI.
	 * @param saleFragment
	 * @param reportFragment
	/ */
	public EndPaymentFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
		super();
		this.saleFragment = saleFragment;
		this.reportFragment = reportFragment;

//		this.dataStat = new DatabaseStat(getActivity().getApplicationContext());
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		DBStat = new DatabaseStat(getActivity().getApplicationContext());

		mOrderSession = new sessionmanager(getActivity().getApplicationContext());
		HashMap<String, String> K = mOrderSession.getUserDetails();
		String email = K.get(KEY_EMAIL);

//		regis.getCurrentSale().getAllLineItem();
//		regis.getTotal();

		if (email == null) {
		} else {
			if (DBStat.settingDaoD.getSettings(email).size() > 0) {
				ShopSetting = DBStat.settingDaoD.getSettings(email).get(0);
			}
		}

		if(ShopSetting==null){}
		else if(ShopSetting.CheckPrintGSTProds==null){}
		else if(ShopSetting.CheckPrintGSTProds){
			PrintGST = true;
		}

		if(ShopSetting==null){}
		else if(ShopSetting.CheckPrintTranGST==null){}
		else if(ShopSetting.CheckPrintTranGST){
			PrintTranGST = true;
			CGSTTranTax = ShopSetting.CGSTPercent;
			SGSTTranTax = ShopSetting.SGSTPercent;
		}


		taxsetting = DBStat.getTaxSettings();
//		try {
			regis = new Register(getActivity().getApplicationContext());
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}
		
		View v = inflater.inflate(R.layout.dialog_paymentsuccession, container,false);
		String strtext=getArguments().getString("edttext");
		Discounts = getArguments().getDouble("Discounts");
		buyerName = getArguments().getString("buyername");
		buyerPhone = getArguments().getString("buyerPhone");
		PayType = getArguments().getString("PayType");
		Retrofit r = connectivity.buildSMSRetrofit();

		smssender= r.create(smsapiops.class);
		chg = (TextView) v.findViewById(R.id.changeTxt);
		chg.setText(strtext);
		doneButton = (Button) v.findViewById(R.id.doneButton);
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				end();
			}
		});
//		initPrinter();
		
		return v;
	}
	
	/**
	 * End
	 */
	private void end(){
//		regis.saveUser();

		int id =saveUser();



		regis.setDiscount(Discounts);
        if(ShopSetting==null){}
		else {
			printReceipt();
			if (ShopSetting.SMSenabled==null) {}
			else if(ShopSetting.SMSenabled){
				sendsms();
			}
		}
		regis.setCurrentSaleBuyerID(id);
		regis.setCurrentSalePayType(PayType);

		regis.endSale(DateTimeStrategy.getCurrentTime());
		saleFragment.update();
//		reportFragment.update();
		buyerName ="";
		buyerPhone="";
		PayType ="";
		Discounts=0.0;
		this.dismiss();
	}

	private void sendsms() {
		String receipt = buildSMSReceipt();
		//http://tra1.smsmyntraa.com/api/send_transactional_sms.php?
		// username=u3190&msg_token=A4rQtm&sender_id=CSALES&message=Mobile+number+is+&mobile=8121155693
		//		smssender.postsms("u3190","A4rQtm","CSALES",receipt,"8121155693").enqueue(smscallback);
//		smssender.postSpringsms(ShopSetting.SMSKey,ShopSetting.SMSSenderID,buyerPhone,"test message","json").enqueue(smscallback);

//		smssender.postSpringsms("69iq54a4m4s4ib0agg135o3y0yfbkbmbu","SEDEMO","8121155693","test message","json").enqueue(smscallback);

///		c25e2048e4813c8a6d7a93fb5e72e6f0455ae21897787fe2f1ec93f012ecf362

		smssender.posttextLocalSMS(ShopSetting.SMSUsername,ShopSetting.SMSKey,receipt,ShopSetting.SMSSenderID,buyerPhone,"0").enqueue(smscallback);
	}

	private int saveUser() {

		return regis.SaveUser(buyerName,buyerPhone);
	}




//	public void initPrinter()
//	{
//		if (mbluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
//			mbluetoothService.stop();
//
//			//ShopSetting = DBStat.settingDaoD.getSettings(email).get(0);
//			// Attempt to connect to the device
//			if(ShopSetting==null){}
//			else if(!ShopSetting.bluetoothAddress.equals(""))
//
//				if (BluetoothAdapter.checkBluetoothAddress(ShopSetting.bluetoothAddress)) {
////                        mBluetoothAdapter.getBondedDevices()
//					mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//					BluetoothDevice device = mBluetoothAdapter
//							.getRemoteDevice(ShopSetting.bluetoothAddress);
//					mbluetoothService.connect(device);
//				}
////			mbluetoothService.start(ShopSetting.bluetoothAddress);
//		}
//	}

	/*****************************************************************************************************/
	/*
	 * SendDataString
	 */
	private void SendDataString(String data) {

		if (mbluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (data.length() > 0) {
			try {
				mbluetoothService.write(data.getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
     *SendDataByte
     */
	private void SendDataByte(byte[] data) {

		if (mbluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		mbluetoothService.write(data);
	}

	private void printReceipt() {

		if(mbluetoothService==null){

//			mbluetoothService = new BluetoothService(this,mHandler);
		}
		else if(mbluetoothService.getState() == BluetoothService.STATE_CONNECTED)
		{
			if(ShopSetting==null){}
			else {
				if (ShopSetting.bluetoothAddress.equals("")) {
				} else {
					PrintNow();
					if(ShopSetting.PrintDupReceipt) {
						PrintNow();
					}
				}
			}
			}
		else
		{
			Toast.makeText(getActivity(),"Please check bluetooth printer is connected",Toast.LENGTH_SHORT).show();
		}
	}

	private void PrintNow() {


		SendDataByte(Command.ESC_Init);
		SendDataByte(("Invoice Number:" + regis.getCurrentSale().getId() + "\n").getBytes());
		SendDataByte(("Date : " + DateTimeStrategy.getCurrentTime() + "\n").getBytes());
		SendDataByte(("Customer:" + buyerName + "\n").getBytes());
		Command.ESC_Align[2] = 0x01;
		SendDataByte(Command.ESC_Align);
		SendDataByte(PrinterCommand.POS_Set_UnderLine(2));
		Command.GS_ExclamationMark[2] = 0x10;
		SendDataByte(Command.GS_ExclamationMark);
		SendDataByte((ShopSetting.ShopName + "\n").getBytes());
		Command.GS_ExclamationMark[2] = 0x00;
		SendDataByte(Command.GS_ExclamationMark);

		SendDataByte((ShopSetting.AddressLine1+"\n").getBytes());
		SendDataByte((ShopSetting.AddressLine2+"\n").getBytes());

//				SendDataByte((+"\n").getBytes());
		Command.ESC_Align[2] = 0x00;
		SendDataByte(Command.ESC_Align);
		SendDataByte(("Name        Qty Price   Total\n").getBytes());//.getBytes(),
		SendDataByte(PrinterCommand.POS_Set_UnderLine(0));
		SendDataByte(LF);
		SendDataByte((getProducts()).getBytes());
		SendDataByte(LF);
		SendDataByte(LF);

//				SendDataByte(PrinterCommand.POS_Set_UnderLine(2));
//				SendDataByte(getTotals().getBytes());
		SendDataByte(String.format("Tendered by: \n").getBytes());
		SendDataByte(("  "+PayType+"  \n\n").getBytes());
		SendDataByte((getTotals()+"\n").getBytes());
		SendDataByte(("GST Number:" + ShopSetting.vatnumber+"\n").getBytes());
		SendDataByte((ShopSetting.printerFooter+"\n").getBytes());
		SendDataByte((" \n").getBytes());
	}


	public String buildSMSReceipt()
	{
		StringBuilder s = new StringBuilder();
		s.append(ShopSetting.ShopName + "\n");
		s.append("Invoice Number:" +regis.getCurrentSale().getId()+"\n");//.getBytes());
		s.append("Date : "+DateTimeStrategy.getCurrentTime()+"\n");//.getBytes());
		s.append("Customer:"+ buyerName+"\n");
		s.append("Name        Qty Price   Total\n");//.getBytes());//.getBytes(),
//		SendDataByte(PrinterCommand.POS_Set_UnderLine(0));
		s.append(getProducts());
		s.append(String.format("Tendered by: \n"));
		s.append("  "+PayType+" \n\n");
		s.append(getTotals()+"\n");
		if(ShopSetting.vatnumber.equals("")){}
		else {
			s.append("GST Number:" + ShopSetting.vatnumber + "\n");
		}
		s.append(ShopSetting.printerFooter);

		return s.toString();
//		SendDataByte(LF);
	}

	public String getTotals()
	{

		StringBuilder prodSB = new StringBuilder();
		prodSB.append(padText("SubTotal",22)+ String.valueOf(regis.getSubTotal())+"\n");


		if(PrintTranGST){
			regis.setTranTrax(PrintTranGST);
			Double NewSubTotal = regis.getSubTotal();
			regis.setSubTotal(NewSubTotal);
			Double CGST = round(((NewSubTotal * CGSTTranTax) / 100),2);
			regis.setCGST(CGST);
			Double SGST = round(((NewSubTotal * SGSTTranTax) / 100),2);
			regis.setSGST(SGST);
			regis.setTaxTotal(CGST+SGST);
			prodSB.append(padText("Discount",22)+String.valueOf(regis.getDiscounts())+"\n");
			regis.setTotal(NewSubTotal+ CGST+SGST);

			prodSB.append(padText("CGST"+"("+String.valueOf(CGSTTranTax)+")",20)+ String.valueOf(CGST)+"\n");
			prodSB.append(padText("SGST"+"("+String.valueOf(SGSTTranTax)+")",20)+ String.valueOf(SGST)+"\n");
			prodSB.append(padText("Total",22)+String.valueOf(round(regis.getTotalVal()-regis.getDiscounts(),2))+"\n");
		}
		else if(PrintGST){
			prodSB.append(padText("Tax", 22) + String.valueOf(regis.getTaxTotal()) + "\n");
			prodSB.append(padText("Total",22)+String.valueOf(regis.getTotal())+"\n");
			if(Discounts>0) {
				prodSB.append(padText("Discounts", 22) + String.valueOf(regis.getDiscounts())+ "\n");
				prodSB.append(padText("Total(After Disc)", 20) + String.valueOf(regis.getTotal()-regis.getDiscounts())+ "\n");
			}
		}
		else
		{
//			prodSB.append()
			prodSB.append(padText("Tax", 22) + String.valueOf(regis.getTaxTotal()) + "\n");

			prodSB.append(padText("Total",22)+String.valueOf(regis.getTotal())+"\n");
			if(Discounts>0) {
				prodSB.append(padText("Discounts", 20) + String.valueOf(regis.getDiscounts())+"\n");
				prodSB.append(padText("Total(After Disc)", 20) + String.valueOf(regis.getTotal() - regis.getDiscounts()));
			}

		}



		//.toString()+"\n");
		//prodSB.append(padText("Total",26)+ taxutil.round(Total,2).toString()+"\n");

		return prodSB.toString();
	}



	public String getProducts() {

//		Boolean PrintGST = false;


		StringBuilder prodSB = new StringBuilder();
//                    PrinterCommand.POS_Set_UnderLine(1),
//                    PrinterCommand.POS_S_Align(1),
//		prodSB.append("Name        Qty Price  Tax Total\n");//.getBytes(),
//		prodSB.append(String.PrinterCommand.POS_Set_UnderLine(2)));

		for (LineItem p : regis.getCurrentSale().getAllLineItem()) {
			Double PItemTotal = round(p.getTotalPriceAtSale(),2);// * p.count;
			Double PItemTax = round(p.getTotalTaxAtSale(),2);

			int taxid = p.getProduct().getTaxid();

            taxSettings t = new taxSettings();
            if(taxid==-1){}
            else {
                 t = taxsetting.get(taxid-1);
            }


			prodSB.append(padTextProd(p.getProduct().getName(),12)+" "+padText(String.valueOf(p.getQuantity()),3) +padText(String.valueOf(round(p.getPriceAtSale(),2)),8)
					+padText(String.valueOf(PItemTotal),8)+ "\n");

			if(PrintGST)
			{
				prodSB.append(padTextProd("(CGST)"+String.valueOf(round((t.taxpercent/2),2))+'%',15)
						+String.valueOf(round((PItemTax/2),2))+"\n");
				prodSB.append(padTextProd("(SGST)"+String.valueOf(round((t.taxpercent/2),2))+'%',15)
						+String.valueOf(round((PItemTax/2),2))+"\n");
			}


////                padText(pr.product_name);
//			prodSB.append(padText(p.getProduct().getName(),12)+" "+padText(String.valueOf(p.getQuantity()),3) +padText(String.valueOf(round(p.getPriceAtSale(),1)),7)
//					+ padText(PItemTax.toString(),4) +padText(PItemTotal.toString(),7));

		}

		return prodSB.toString();
	}

	private String padText(String product_name, int Maxnum) {

		StringBuffer padded = new StringBuffer(product_name);

//        Strings.padEnd("string", 10, ' ');
		while (padded.length() < Maxnum)
		{
			padded.append(" ");
		}

//		padded.setLength(Maxnum);
		return padded.toString();

	}

	private String padTextProd(String product_name, int Maxnum) {

		StringBuffer padded = new StringBuffer(product_name);

//        Strings.padEnd("string", 10, ' ');
		while (padded.length() < Maxnum)
		{
			padded.append(" ");
		}

		padded.setLength(Maxnum);
		return padded.toString();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "@onAttach");
        Act = activity;
	}



	Callback<ResponseBody> smscallback = new Callback<ResponseBody>() {
		@Override
		public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

			if (response.isSuccessful()) {

				Toast.makeText(Act.getBaseContext(),"Successfully sent sms",Toast.LENGTH_SHORT).show();
			}
			else
			{
//				showProgress(false);
				Toast.makeText(Act.getBaseContext(),"Server is down, Please try again",Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onFailure(Call<ResponseBody> call, Throwable t) {
//			showProgress(false);
			if(connectivity.isNetworkConnected(Act.getBaseContext()))
			{
				Toast.makeText(Act.getBaseContext(),"Server is down, Please try again",Toast.LENGTH_SHORT).show();

			}
			else
			{
//                Toast.makeText()
			}
//            Snackbar.make(mLoginFormView,"",Snackbar.LENGTH_SHORT);
		}
	};

}
