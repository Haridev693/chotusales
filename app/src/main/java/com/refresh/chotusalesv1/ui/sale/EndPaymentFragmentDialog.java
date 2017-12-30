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
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.BluetoothService;
import com.refresh.chotusalesv1.techicalservices.connectivity;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.utils.commands.Command;
import com.refresh.chotusalesv1.utils.commands.PrinterCommand;

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
	private Settings ShopSetting = new Settings();
	private Button doneButton;
	private TextView chg;
	private Register regis;
	private UpdatableFragment saleFragment;
	private UpdatableFragment reportFragment;
	private String buyerName, buyerPhone;



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

//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		mService = new BluetoothService(getActivity(),mHandler);
//		initPrinter();
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
//		try {
			regis = new Register(getActivity().getApplicationContext());
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}
		
		View v = inflater.inflate(R.layout.dialog_paymentsuccession, container,false);
		String strtext=getArguments().getString("edttext");
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
		reportFragment.update();
		buyerName ="";
		buyerPhone="";
		PayType ="";
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
					SendDataByte(Command.ESC_Init);
					SendDataByte(("Invoice Number:" + regis.getCurrentSale().getId() + "\n").getBytes());
					SendDataByte(("Date : " + DateTimeStrategy.getCurrentTime() + "\n").getBytes());
					SendDataByte(("Customer:" + buyerName + "\n").getBytes());
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					SendDataByte(PrinterCommand.POS_Set_UnderLine(2));
					Command.GS_ExclamationMark[2] = 0x10;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte((ShopSetting.printerHeader + "\n").getBytes());
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);

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
					SendDataByte(getTotals().getBytes());
					SendDataByte(LF);
					SendDataByte(("GST Number:" + ShopSetting.vatnumber).getBytes());
					SendDataByte(LF);
					SendDataString(ShopSetting.printerFooter);
					SendDataByte(LF);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
					SendDataByte(PrinterCommand.POS_Set_PrtInit());
				}
			}
			}
		else
		{
			Toast.makeText(getActivity(),"Please check bluetooth printer is connected",Toast.LENGTH_SHORT).show();
		}
	}


	public String buildSMSReceipt()
	{
		StringBuilder s = new StringBuilder();
		s.append(ShopSetting.printerHeader + "\n");
		s.append("Invoice Number:" +regis.getCurrentSale().getId()+"\n");//.getBytes());
		s.append("Date : "+DateTimeStrategy.getCurrentTime()+"\n");//.getBytes());
		s.append("Customer:"+ buyerName+"\n");
		s.append("Name        Qty Price   Total\n");//.getBytes());//.getBytes(),
//		SendDataByte(PrinterCommand.POS_Set_UnderLine(0));
		s.append(getProducts());
		s.append(String.format("Tendered by: \n"));
		s.append("  "+PayType+" \n\n");
		s.append(getTotals()+"\n");
		s.append("GST Number:"+ShopSetting.vatnumber+"\n");
		s.append(ShopSetting.printerFooter);

		return s.toString();
//		SendDataByte(LF);
	}

	public String getTotals()
	{

		StringBuilder prodSB = new StringBuilder();
		prodSB.append(padText("SubTotal",22)+ String.valueOf(regis.getSubTotal())+"\n");
		prodSB.append(padText("Tax",22)+ String.valueOf(regis.getTaxTotal())+"\n");
		prodSB.append(padText("Total",22)+String.valueOf(regis.getTotal())+"\n");

		//.toString()+"\n");
		//prodSB.append(padText("Total",26)+ taxutil.round(Total,2).toString()+"\n");

		return prodSB.toString();
	}



	public String getProducts() {

		StringBuilder prodSB = new StringBuilder();
//                    PrinterCommand.POS_Set_UnderLine(1),
//                    PrinterCommand.POS_S_Align(1),
//		prodSB.append("Name        Qty Price  Tax Total\n");//.getBytes(),
//		prodSB.append(String.PrinterCommand.POS_Set_UnderLine(2)));

		for (LineItem p : regis.getCurrentSale().getAllLineItem()) {
			Double PItemTotal = round(p.getTotalPriceAtSale(),2);// * p.count;
			Double PItemTax = round(p.getTotalTaxAtSale(),1);

			p.getProduct().getTaxid();


			prodSB.append(padTextProd(p.getProduct().getName(),12)+" "+padText(String.valueOf(p.getQuantity()),3) +padText(String.valueOf(round(p.getPriceAtSale(),2)),8)
					+padText(String.valueOf(PItemTotal),8));
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
