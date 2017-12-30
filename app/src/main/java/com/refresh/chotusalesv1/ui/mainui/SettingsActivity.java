package com.refresh.chotusalesv1.ui.mainui;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.apiops.smsapiops;
import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.BluetoothService;
import com.refresh.chotusalesv1.techicalservices.connectivity;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;

//import static com.refresh.chotusalesv1.staticpackage.DatabaseStat.settingDaoD;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

//    private BluetoothAdapter mBluetoothAdapter;

    private static final String TAG = "Settings Activty";
    private static final boolean DEBUG = true;

//    Settings s = new Settings();


    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;
    private sessionmanager mOrderSession;

    @BindView(R.id.btnBluetoothscan)
    Button bluetoothScan;

    @BindView(R.id.btnSaveSettings)
    Button SaveSettings;

    @BindView(R.id.btnCancel)
    Button Cancel;

    @BindView(R.id.CheckEnablePrinter)
    CheckBox enablePrinter;

    @BindView(R.id.VatNumber)
    EditText vatnumber;

    @BindView(R.id.bluetoothAdress) TextView bluetoothAdress;

    @BindView(R.id.printerFooter)
    EditText printerFooter;

    @BindView(R.id.printerHeader)
    EditText printerHeader;

    private smsapiops SMSChecker;



    @BindView(R.id.SMSKey) EditText SMSKey;

    @BindView(R.id.SMSSenderID) EditText SMSSenderID;


    @BindView(R.id.SMSUsername) EditText SMSUserName;

    @BindView(R.id.checkSMSenabled) CheckBox checkSMSenabled;


    @BindView(R.id.btncheckUnits) Button btncheckUnits;

    Settings ASettings;

    DatabaseStat d ;
    private Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mOrderSession = new sessionmanager(getApplicationContext());

        mService = new BluetoothService(this, mHandler);

        d = new DatabaseStat(getApplicationContext());

        bluetoothScan.setOnClickListener(this);

        SaveSettings.setOnClickListener(this);

        Cancel.setOnClickListener(this);

        enablePrinter.setOnClickListener(this);

        checkSMSenabled.setOnClickListener(this);

        btncheckUnits.setOnClickListener(this);

        ASettings = new Settings();

        Retrofit r  = connectivity.buildSMSRetrofit();

        SMSChecker = r.create(smsapiops.class);



//         = new DatabaseStat(get)

//        DatabaseStat

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
//            finish();
        }

        HashMap<String,String> K=  mOrderSession.getUserDetails();
        String email = K.get(KEY_EMAIL);

        if(email==null) {}
        else{
            ArrayList<Settings> settinglist = d.settingDaoD.getSettings(email);

            if(settinglist.size()>0) {
                ASettings = settinglist.get(0);
                vatnumber.setText(ASettings.vatnumber);
                printerHeader.setText(ASettings.printerHeader);
                printerFooter.setText(ASettings.printerFooter);
                bluetoothAdress.setText(ASettings.bluetoothAddress);
                if(ASettings.SMSenabled) {
                    SMSKey.setText(ASettings.SMSKey);
                    SMSSenderID.setText(ASettings.SMSSenderID);
                    SMSUserName.setText(ASettings.SMSUsername);
                }
                checkSMSenabled.setChecked(ASettings.SMSenabled);
            }
//            ASettings.
        }



//        if(mOrderSession.getBluetooth().isEmpty()) {
//            String BluetoothAddress = mOrderSession.getBluetooth();
//
//                if (BluetoothAdapter.checkBluetoothAddress(BluetoothAddress)) {
////                        mBluetoothAdapter.getBondedDevices()
////                    BluetoothDevice device = mBluetoothAdapter
////                            .getRemoteDevice(address);
//                    BluetoothDevice device = mBluetoothAdapter
//                            .getRemoteDevice(BluetoothAddress);
//
//
////                    if(device.getBondState()=)
//                    mService.connect(device);
//                }
//        }

    }

//
//    @Override
//    public synchronized void onResume() {
//        super.onResume();
//
//        if (mService != null) {
//
//            if (mService.getState() == BluetoothService.STATE_NONE) {
//                // Start the Bluetooth services
//                String BluetoothAddress = mOrderSession.getBluetooth();
//
//                if(!BluetoothAddress.isEmpty()) {
//                    BluetoothDevice device = mBluetoothAdapter
//                            .getRemoteDevice(BluetoothAddress);
//
////                    if(device.getBondState()=)
//                    mService.start(BluetoothAddress);
//                }
//            }
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (DEBUG)
//            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == this.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
//                        mBluetoothAdapter.getBondedDevices()
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        bluetoothAdress.setText(address);
//                        mOrderSession.saveBluetoothAddress(address);
                        // Attempt to connect to the device
                        mService.connect(device);

                    }
                }
                break;
            }

            case REQUEST_ENABLE_BT:{
                // When the request to enable Bluetooth returns
                if (resultCode == this.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    private void KeyListenerInit() {
        mService = new BluetoothService(this, mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mService == null)
                mService = new BluetoothService(this, mHandler);
            //KeyListenerInit();//监听
        }
    }


    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
//                    if (DEBUG)
//                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
//                            mTitle.setText(R.string.title_connected_to);
//                            mTitle.append(mConnectedDeviceName);
//                            btnScanButton.setText(getText(R.string.Connecting));
                            break;
                        case BluetoothService.STATE_CONNECTING:
//                            mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
//                            mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

//        switch ()

        switch (v.getId()) {

            case R.id.btnBluetoothscan: {
                Intent serverIntent = new Intent(SettingsActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            }
            case R.id.btnSaveSettings: {
                    if(validateSettings())
                    saveSettingstoDB();
                break;
            }

            case R.id.btnCancel:{
                openQuitDialog();
                break;

            }

            case R.id.CheckEnablePrinter:{
                if(enablePrinter.isChecked())
                {
                    bluetoothAdress.setText("");
                }
            }

            case R.id.checkSMSenabled:{
                if(checkSMSenabled.isChecked()){
                    SMSUserName.setVisibility(View.VISIBLE);
                    SMSKey.setVisibility(View.VISIBLE);
                    SMSSenderID.setVisibility(View.VISIBLE);
                }
                else
                {
                    SMSUserName.setVisibility(View.GONE);
                    SMSKey.setVisibility(View.GONE);
                    SMSSenderID.setVisibility(View.GONE);

                }
            }

            case R.id.btncheckUnits:{

//                SMSChecker.posttextLocalSMS()

            }
        }

    }

    private Boolean validateSettings() {

        Boolean valid = true;
        if(vatnumber.getText().toString().equals(""))
        {
            Toast.makeText(SettingsActivity.this,"Please enter VAT number",Toast.LENGTH_SHORT).show();
            valid= false;
        }

        return valid;

    }

    /**
     * Open quit dialog.
     */
    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                SettingsActivity.this);
        quitDialog.setTitle(res.getString(R.string.dialog_quit));
        quitDialog.setPositiveButton(res.getString(R.string.quit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        quitDialog.show();
    }

    private void saveSettingstoDB() {



        HashMap<String,String> K=  mOrderSession.getUserDetails();
        String email = K.get(KEY_EMAIL);
        Settings s = new Settings();



        if(ASettings.userid==null) {

            s.vatnumber = vatnumber.getText().toString();
            s.printerHeader = printerHeader.getText().toString();
            s.printerFooter = printerFooter.getText().toString();
            s.bluetoothAddress = bluetoothAdress.getText().toString();
            s.SMSenabled = checkSMSenabled.isChecked();

            if(checkSMSenabled.isChecked()) {
                s.SMSKey = SMSKey.getText().toString();
                s.SMSSenderID = SMSSenderID.getText().toString();
                s.SMSUsername = SMSUserName.getText().toString();
            }
            else
            {
                s.SMSKey = "";//SMSKey.getText().toString();
                s.SMSSenderID = "";//SMSSenderID.getText().toString();
                s.SMSUsername = "";//SMSUserName.getText().toString();
            }

            s.userid = email;
            d.settingDaoD.addSettings(s);
        }
        else
        {
            s.vatnumber = vatnumber.getText().toString();
            s.printerHeader = printerHeader.getText().toString();
            s.printerFooter = printerFooter.getText().toString();
            s.bluetoothAddress = bluetoothAdress.getText().toString();
            if(checkSMSenabled.isChecked()) {
                s.SMSKey = SMSKey.getText().toString();
                s.SMSSenderID = SMSSenderID.getText().toString();
                s.SMSUsername = SMSUserName.getText().toString();
            }
            else
            {
                s.SMSKey = "";//SMSKey.getText().toString();
                s.SMSSenderID = "";//SMSSenderID.getText().toString();
                s.SMSUsername = "";//SMSUserName.getText().toString();
            }
            s.SMSenabled = checkSMSenabled.isChecked();

            s.userid =email;
            s._id = ASettings._id;
//            ASettings.userid = ASettings.userid;
           if( d.settingDaoD.updateSettings(s._id,s))
           {
               Toast.makeText(SettingsActivity.this,"Updated settings",Toast.LENGTH_SHORT).show();
           }
           else{
               Toast.makeText(SettingsActivity.this,"Problem while updating settings",Toast.LENGTH_SHORT).show();
           }
        }

//       content.put("start_time", .toString());
//        content.put("status", "ON PROCESS");
//        content.put("payment", "n/a");
//        content.put("total", "0.0");
//        content.put("orders", "0");
        //content.put("end_time", startTime.toString());
    }
}
