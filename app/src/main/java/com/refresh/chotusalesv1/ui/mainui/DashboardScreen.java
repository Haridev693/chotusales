package com.refresh.chotusalesv1.ui.mainui;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.refresh.chotusalesv1.BuildConfig;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.apiops.usersapiop;
import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.salessettings.userSettings;
import com.refresh.chotusalesv1.responseclasses.responseParser;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.refresh.chotusalesv1.techicalservices.connectivity.buildRetrofit;
import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;


public class DashboardScreen extends AppCompatActivity implements View.OnClickListener{

    private static final int[] READ_LOGS = {103};
    private static final String TAG = "ChotuSales";
    @BindView(R.id.loadChotuSales)
    ImageButton loadChotusales;
    @BindView(R.id.loadSettings)
    ImageButton loadSettings;
    @BindView(R.id.loadtaxsettings)
    ImageButton loadtaxSettings;
    @BindView(R.id.dbsettings)
    ImageButton dbSettings;
    @BindView(R.id.usersettings)
    ImageButton usersettings;

    @BindView(R.id.LogOutC)
    ImageButton LogOut;

    private userSettings userDetail;
//    @BindView(R.id.help) ImageButton helpButton;


//        List<String> "admin"//</item>
//        <item>user</item>
//    </string-array>


    private sessionmanager mOrderSession;
    private BluetoothAdapter mBluetoothAdapter;
    private usersapiop users;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        mOrderSession = new sessionmanager(getApplicationContext());
        userDetail = mOrderSession.getCurrentUser();
//        mOrderSession.checkLogin();

        DateTimeStrategy.setLocale("hi", "IN");

//        DatabaseStat.mAppSession = new sessionmanager(getApplicationContext());
        ButterKnife.bind(this);
        loadChotusales.setOnClickListener(this);
        loadSettings.setOnClickListener(this);
        loadtaxSettings.setOnClickListener(this);
        dbSettings.setOnClickListener(this);
        usersettings.setOnClickListener(this);
        LogOut.setOnClickListener(this);
        Retrofit r = buildRetrofit();
        users = r.create(usersapiop.class);
        HashMap<String, String> K = mOrderSession.getUserDetails();
        email = K.get(KEY_EMAIL);
//        exporti.setOnClickListener(this);
//        helpButton.setOnClickListener(this);



//        loadUsers.setOnClickListener(this);
//        initiateCoreApp();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.loadChotuSales:{
                Intent newActivity = new Intent(DashboardScreen.this,
                        MainActivity.class);
                startActivity(newActivity);
                break;
            }
            case R.id.loadSettings:{

                if(userDetail.usertype.equals(UserTypes.admin.name())) {
                    Intent newActivity = new Intent(DashboardScreen.this,
                            SettingsActivity.class);
                    startActivity(newActivity);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.loadtaxsettings:{
                if(userDetail.usertype.equals(UserTypes.admin.name())) {
                Intent newActivity = new Intent(DashboardScreen.this,
                        TaxSettingActivity.class);
                startActivity(newActivity);
            }
                else
            {
                Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
            }
                break;

            }
            case R.id.dbsettings:
            {
                if(userDetail.usertype.equals(UserTypes.admin.name())) {

                    Intent newActivity = new Intent(DashboardScreen.this,
                            SaveDBtoDrive_Activity.class);
                    startActivity(newActivity);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
                }
                break;


            }

            case R.id.usersettings:
            {
                if(userDetail.usertype.equals(UserTypes.admin.name())) {
                    Intent newActivity = new Intent(DashboardScreen.this,
                            UserSettingActivity.class);
                    startActivity(newActivity);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.LogOutC:
            {
                if(userDetail.usertype.equals(UserTypes.admin.name())) {
                    openDetailDialog();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
                }
            }

        }

        //SplashScreenActivity.this.finish();
    }

    private void openDetailDialog() {
        final AlertDialog.Builder quitDialog = new AlertDialog.Builder(DashboardScreen.this);
        quitDialog.setTitle("Warning !!!!!!!!! Are you sure? you want to exit login");
        quitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                users.logoutUser(email,false).enqueue(complaindetailcall);
            }
        });

        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }



    Callback<ResponseBody> complaindetailcall = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
//                showProgress(false);
                try {
                    if (response != null) {

                        String sres = response.body().string();
                        responseParser responseB = new responseParser();
                        responseB =responseB.getResponse(sres);
//                        responseParser responseB = //gson.fromJson(sres, responseParser.class);
//                        chain c;

                        if (responseB.resStatus.equals("error")) {
//                                prodialog.hide();
                            Toast.makeText(getApplicationContext(), responseB.resText, Toast.LENGTH_SHORT).show();
                        } else if (responseB.resStatus.equals("success")) {

                            if (!responseB.resText.isEmpty() && !response.equals(null)) {
                                processResponse(responseB.resText);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "error while processing response", Toast.LENGTH_SHORT).show();
                            }
//                                Toast.makeText(mContext, resp.resText, Toast.LENGTH_SHORT).show();
//                                thanksmessagescreen();
                        }

                    }

                } catch (Exception e) {

                    Log.e("LoginActivityCallback",e.getMessage());
                    Log.e("LoginActivityStackTrace",e.getStackTrace().toString());
                }
            }
            else
            {
//                showProgress(false);
                Toast.makeText(DashboardScreen.this,"Server is down, Please try again",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
//            showProgress(false);
//            if(connectivity.isNetworkConnected(getApplicationContext()))
//            {
                Toast.makeText(DashboardScreen.this,"Please Check your internet again",Toast.LENGTH_SHORT).show();

//            }
//            else
//            {
////                Toast.makeText()
//            }
//            Snackbar.make(mLoginFormView,"",Snackbar.LENGTH_SHORT);
        }
    };

    private void processResponse(String resText) {
        if(resText=="true")///////////////
            mOrderSession.logoutUser();
    }




    public void sendLogcatMail(Activity baseActivity){
        if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_GRANTED) {
            Log.d(baseActivity.getLocalClassName(), "Got READ_LOGS permissions");
        } else {
            Log.e(baseActivity.getLocalClassName(), "Don't have READ_LOGS permissions");
//            requestContactsPermissions();
           // ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.READ_LOGS}, 103);
            Log.i(baseActivity.getLocalClassName(), "new READ_LOGS permission: " + ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.READ_LOGS));
        }
        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "logcat.txt");
        Log.i("SendLoagcatMail: ", "logcat file is " + outputFile.getAbsolutePath());


        String deviceDetails= "Device details:";
        deviceDetails += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        deviceDetails += "\n OS API Level: "+android.os.Build.VERSION.RELEASE + "("+android.os.Build.VERSION.SDK_INT+")";
        deviceDetails += "\n Device: " + android.os.Build.DEVICE;
        deviceDetails += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        Log.i("SendLoagcatMail: ", "deviceDetails: " + deviceDetails);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        Log.i("SendLoagcatMail: ", "App version: " + versionName + " with id " + versionCode);

        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Error", "Alas error! ", e);
        }

        File file = new File(outputFile.getAbsolutePath());

        Uri uri = Uri.fromFile(file);


        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"shsoftsols@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
//        emailIntent .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
        emailIntent .putExtra(Intent.EXTRA_STREAM, uri);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "ChotuSales App issues report.");
        startActivity(Intent.createChooser(emailIntent , "Email issue report to ChotuSales team?..."));
    }



//    private void requestContactsPermissions() {
//        // BEGIN_INCLUDE(contacts_permission_request)
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_LOGS)) {
//
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // For example, if the request has been denied previously.
//            Log.i(TAG,
//                    "Displaying contacts permission rationale to provide additional context.");
//
//            // Display a SnackBar with an explanation and a button to trigger the request.
//            Snackbar.make(this.helpButton,"request permissions",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(DashboardScreen.this, new String[]{Manifest.permission.READ_LOGS}, 103);
//                        }
//                    })
//                    .show();
//        } else {
//            // Contact permissions have not been granted yet. Request them directly.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_LOGS},103);
//        }
//        // END_INCLUDE(contacts_permission_request)
//    }


//    /**
//     * Display the {@link CameraPreviewFragment} in the content area if the required Camera
//     * permission has been granted.
//     */
//    private void showCameraPreview() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
//                .addToBackStack("contacts")
//                .commit();
//    }

    /**
     * Display the {@link ContactsFragment} in the content area if the required contacts
     * permissions
     * have been granted.
     */
//    private void showContactDetails() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.sample_content_fragment, ContactsFragment.newInstance())
//                .addToBackStack("contacts")
//                .commit();
//    }


    /**
     * Callback received when a permissions request has been completed.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        if (requestCode == READ_LOGS[0]) {
//            // BEGIN_INCLUDE(permission_result)
//            // Received permission result for camera permission.
//            Log.i(TAG, "Received response for Camera permission request.");
//
//            // Check if the only required permission has been granted
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Camera permission has been granted, preview can be displayed
//                Log.i(TAG, "Read logs permission has now been granted. Showing preview.");
//                Snackbar.make(this.helpButton,"Request logs" ,
//                        Snackbar.LENGTH_SHORT).show();
//            } else {
//                Log.i(TAG, "Read Logs permission was NOT granted.");
//                Snackbar.make(this.helpButton, "permission granted",
//                        Snackbar.LENGTH_SHORT).show();
//
//            }
//        }
//            // END_INCLUDE(permission_result)
//
////        } else if (requestCode == REQUEST_CONTACTS) {
////            Log.i(TAG, "Received response for contact permissions request.");
////
////            // We have requested multiple permissions for contacts, so all of them need to be
////            // checked.
////            if (PermissionUtil.verifyPermissions(grantResults)) {
////                // All required permissions have been granted, display contacts fragment.
////                Snackbar.make(mLayout, R.string.permision_available_contacts,
////                        Snackbar.LENGTH_SHORT)
////                        .show();
////            } else {
////                Log.i(TAG, "Contacts permissions were NOT granted.");
////                Snackbar.make(mLayout, R.string.permissions_not_granted,
////                        Snackbar.LENGTH_SHORT)
////                        .show();
////            }
////
////        }
//        else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }



//    public void SendLogcatMail(){
//
//        // save logcat in file
//        File outputFile = new File(Environment.getExternalStorageDirectory(),
//                "logcat.txt");
//        try {
//            Runtime.getRuntime().exec(
//                    "logcat -f " + outputFile.getAbsolutePath());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        //send file using email
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        // Set type to "email"
//        emailIntent.setType("vnd.android.cursor.dir/email");
//        String to[] = {"shsoftsols@gmail.com"};
//        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
//        // the attachment
//        emailIntent .putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());
//        // the mail subject
//        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
//        startActivity(Intent.createChooser(emailIntent , "Send email..."));
//    }

    //    private void initiateCoreApp() {
//        Database database = new AndroidDatabase(this);
//        InventoryDao inventoryDao = new InventoryDaoAndroid(database);
//        SaleDao saleDao = new SaleDaoAndroid(database);
//        SettingsDao settingDao= new SettingsDaoAndroid(database);
//        DatabaseStat.settingDaoD =settingDao;
//        DatabaseExecutor.setDatabase(database);
//        LanguageController.setDatabase(database);
//
////        SettingsActivity.
//
//
//        Inventory.setInventoryDao(inventoryDao);
//        Register.setSaleDao(saleDao);
//        SaleLedger.setSaleDao(saleDao);
//
//        DateTimeStrategy.setLocale("th", "TH");
//        setLanguage(LanguageController.getInstance().getLanguage());
//
//        Log.d("Core App", "INITIATE");
//    }

//    private void setLanguage(String localeString) {
//        Locale locale = new Locale(localeString);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());
//    }





}
