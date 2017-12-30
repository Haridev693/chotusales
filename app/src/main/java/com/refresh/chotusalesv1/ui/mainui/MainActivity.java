package com.refresh.chotusalesv1.ui.mainui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.apiops.usersapiop;
import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.LanguageController;
import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.domain.inventory.Inventory;
import com.refresh.chotusalesv1.domain.inventory.Product;
import com.refresh.chotusalesv1.domain.inventory.ProductCatalog;
import com.refresh.chotusalesv1.responseclasses.responseParser;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.BluetoothService;
import com.refresh.chotusalesv1.techicalservices.connectivity;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;
import com.refresh.chotusalesv1.ui.inventory.InventoryFragment;
import com.refresh.chotusalesv1.ui.inventory.ProductDetailActivity;
import com.refresh.chotusalesv1.ui.sale.ReportFragment;
import com.refresh.chotusalesv1.ui.sale.SaleFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.refresh.chotusalesv1.staticpackage.DatabaseStat.mbluetoothService;
import static com.refresh.chotusalesv1.techicalservices.connectivity.buildRetrofit;
import static com.refresh.chotusalesv1.techicalservices.sessionmanager.KEY_EMAIL;

//import android.app.ActionBar;
//import android.app.AlertDialog;

/**
 * This UI loads 3 main pages (Inventory, Sale, Report)
 * Makes the UI flow by slide through pages using ViewPager.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	private ViewPager viewPager;
	private ProductCatalog productCatalog;
	private String productId;
	private Product product;
	private static boolean SDK_SUPPORTED;
	private PagerAdapter pagerAdapter;
	private Resources res;
	private PagerSlidingTabStrip tabLayout;

	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the services
//	private BluetoothService mService = null;
	private sessionmanager mAppSession;
	private String mConnectedDeviceName;
//	private Settings ShopSetting;


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

	private Inventory Minv;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CHOSE_BMP = 3;
	private static final int REQUEST_CAMER = 4;
    private String email;

	private DatabaseStat dbStat;
	private Settings ShopSetting;
    private usersapiop users;
    private View layoutlinear, mLogOutProgress;
//	private Settings ASettings;
//	private BluetoothService mbluetoothService;

//	@SuppressLint("NewApi")

	/**
	 * Initiate this UI.
	 */
//	private void initiateTabBar() {
//		if (SDK_SUPPORTED) {
//			ActionBar actionBar = getActionBar();
//
//			tabLayout.PagerSlidingTabStrip(ActionBar.NAVIGATION_MODE_TABS);
//
//			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//				@Override
//				public void onTabReselected(Tab tab, FragmentTransaction ft) {
//				}
//
//				@Override
//				public void onTabSelected(Tab tab, FragmentTransaction ft) {
//					viewPager.setCurrentItem(tab.getPosition());
//				}
//
//				@Override
//				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//				}
//			};
//			actionBar.addTab(actionBar.newTab().setText(res.getString(R.string.inventory))
//					.setTabListener(tabListener), 0, false);
//			actionBar.addTab(actionBar.newTab().setText(res.getString(R.string.sale))
//					.setTabListener(tabListener), 1, true);
//			actionBar.addTab(actionBar.newTab().setText(res.getString(R.string.report))
//					.setTabListener(tabListener), 2, false);
//
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//				actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color
//						.parseColor("#73bde5")));
//			}
//
//		}
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		res = getResources();
		setContentView(R.layout.layout_main);
		DateTimeStrategy.setLocale("hi", "IN");
		Retrofit r = buildRetrofit();
		users = r.create(usersapiop.class);

		dbStat = new DatabaseStat(getApplicationContext());

		Minv = new Inventory(getApplicationContext());
		viewPager = (ViewPager) findViewById(R.id.pager);
//		getActionBar();
//		actionBar = getActionBar();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
		} else {
			actionBar.setLogo(R.drawable.chotusalesnewlogo);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}

		tabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        
        layoutlinear = findViewById(R.id.layoutlinear);

        mLogOutProgress = findViewById(R.id.logout_progress);



//		SDK_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
//		initiateTabBar();
		FragmentManager fragmentManager = getSupportFragmentManager();
		pagerAdapter = new PagerAdapter(fragmentManager, res);
		viewPager.setAdapter(pagerAdapter);
		mAppSession = new sessionmanager(getApplicationContext());
		viewPager.setCurrentItem(1);
		tabLayout.setViewPager(viewPager);
		tabLayout.setBackground(new ColorDrawable(Color
				.parseColor("#73bde5")));

//		initPrinter();
		//PagerSlidingTabStrip  tabLayout =etup(viewPager);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openQuitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onResume() {

		super.onResume();
//		initPrinter();
	}

	/**
	 * Open quit dialog.
	 */
	private void openQuitDialog() {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(
				MainActivity.this);
		quitDialog.setTitle(res.getString(R.string.dialog_quit));
		quitDialog.setPositiveButton(res.getString(R.string.quit), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		quitDialog.setNegativeButton(res.getString(R.string.no), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		quitDialog.show();
	}

	/**
	 * Option on-click handler.
	 *
	 * @param view
	 */
	public void optionOnClickHandler(View view) {
		viewPager.setCurrentItem(0);
		String id = view.getTag().toString();
		productId = id;
		try {
			productCatalog = Minv.getProductCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		product = productCatalog.getProductById(Integer.parseInt(productId));
		openDetailDialog();

	}

	/**
	 * Open detail dialog.
	 */
	private void openDetailDialog() {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainActivity.this);
		quitDialog.setTitle(product.getName());
		quitDialog.setPositiveButton(res.getString(R.string.remove), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				openRemoveDialog();
			}
		});

		quitDialog.setNegativeButton(res.getString(R.string.product_detail), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent newActivity = new Intent(MainActivity.this,
						ProductDetailActivity.class);
				newActivity.putExtra("id", productId);
				startActivity(newActivity);
			}
		});

		quitDialog.show();
	}

	/**
	 * Open remove dialog.
	 */
	private void openRemoveDialog() {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(
				MainActivity.this);
		quitDialog.setTitle(res.getString(R.string.dialog_remove_product));
		quitDialog.setPositiveButton(res.getString(R.string.no), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		quitDialog.setNegativeButton(res.getString(R.string.remove), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				productCatalog.suspendProduct(product);
				pagerAdapter.update(0);
			}
		});

		quitDialog.show();
	}

	/**
	 * Get view-pager
	 *
	 * @return
	 */
	public ViewPager getViewPager() {
		return viewPager;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		HashMap<String, String> K = mAppSession.getUserDetails();
		email = K.get(KEY_EMAIL);
		if (email == null) {
		} else {
			if (dbStat.settingDaoD.getSettings(email).size() > 0) {
				ShopSetting = dbStat.settingDaoD.getSettings(email).get(0);
				if (ShopSetting.bluetoothAddress.equals("")) {
					Log.i(TAG,"Bluetooth is not enabled");
				} else {
					mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					// If Bluetooth is not on, request that it be enabled.
					// setupChat() will then be called during onActivityResult
					if (!mBluetoothAdapter.isEnabled()) {
						Intent enableIntent = new Intent(
								BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
						// Otherwise, setup the session
					} else if (mbluetoothService == null) {
							mbluetoothService = new BluetoothService(this, mHandler);
							initPrinter();
						}
						else
							initPrinter();
				}
			}
		}
	}

	private void initPrinter() {
		if (BluetoothAdapter.checkBluetoothAddress(ShopSetting.bluetoothAddress)) {
			BluetoothDevice device = mBluetoothAdapter
					.getRemoteDevice(ShopSetting.bluetoothAddress);
			if (mbluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
				// Attempt to connect to the device
//				mbluetoothService.stop();
				mbluetoothService.connect(device);
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.logout: {
                users.logoutUser(email,false).enqueue(complaindetailcall);
			}

//            	setLanguage("en");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Set language
	 *
	 * @param localeString
	 */
	private void setLanguage(String localeString) {
		Locale locale = new Locale(localeString);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		LanguageController.getInstance().setLanguage(localeString);
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

//		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//			fragment.onActivityResult(requestCode, resultCode, data);
//		}

		//if (D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
			case REQUEST_ENABLE_BT: {
				Log.i("MainActivity", "requestCode==REQUEST_ENABLE_BT");
				// When the request to enable Bluetooth returns
				if (resultCode == AppCompatActivity.RESULT_OK) {
					Log.i("MainActivity", "onActivityResult: resultCode==OK");
					// Bluetooth is now enabled, so set up a chat session
					Log.i("MainActivity", "onActivityResult: starting setupComm()...");

					mbluetoothService = new BluetoothService(this, mHandler);
					initPrinter();
				}
			}
		}
	}


    private void buildAPIRetrofit() {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        gson = new GsonBuilder()
//                .registerTypeAdapter(complaintview.class,new complainviewdeserializer())
//                .enableComplexMapKeySerialization()
//                .serializeNulls()
//                .setVersion(1.0)
//                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(connectivity.hostip)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);

        Retrofit retrofit = builder.build();

        users = retrofit.create(usersapiop.class);

        // complaint = retrofit.create(complaintsapiop.class);

    }


    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            layoutlinear.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutlinear.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutlinear.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mLogOutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLogOutProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLogOutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLogOutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutlinear.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    Callback<ResponseBody> complaindetailcall = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                showProgress(false);
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
                showProgress(false);
                Toast.makeText(MainActivity.this,"Server is down, Please try again",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            showProgress(false);
            if(connectivity.isNetworkConnected(getApplicationContext()))
            {
                Toast.makeText(MainActivity.this,"Server is down, Please try again",Toast.LENGTH_SHORT).show();

            }
            else
            {
//                Toast.makeText()
            }
//            Snackbar.make(mLoginFormView,"",Snackbar.LENGTH_SHORT);
        }
    };

    private void processResponse(String resText) {
        if(resText=="true")///////////////
        mAppSession.logoutUser();
    }


    @SuppressLint("HandlerLeak")
	public final Handler mHandler = new Handler() {
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
					Toast.makeText(MainActivity.this,
							"Connected to " + mConnectedDeviceName,
							Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(MainActivity.this,
							msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
							.show();
					break;
				case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
					Toast.makeText(MainActivity.this, "Device connection was lost",
							Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_UNABLE_CONNECT:     //无法连接设备
					Toast.makeText(MainActivity.this, "Unable to connect device",
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	/**
	 * @author Refresh team
	 */
	class PagerAdapter extends FragmentStatePagerAdapter {

		private UpdatableFragment[] fragments;
		private String[] fragmentNames;

		/**
		 * Construct a new PagerAdapter.
		 *
		 * @param fragmentManager
		 * @param res
		 */
		public PagerAdapter(FragmentManager fragmentManager, Resources res) {

			super(fragmentManager);

			UpdatableFragment reportFragment = new ReportFragment();

//			Bundle b = new Bundle();
//			b.putParcelable("reportFragment",reportFragment);

//			Bundle b = new Bundle();
			UpdatableFragment saleFragment = new SaleFragment(reportFragment);
			UpdatableFragment inventoryFragment = new InventoryFragment(
					saleFragment);

			fragments = new UpdatableFragment[]{inventoryFragment, saleFragment,
					reportFragment};
			fragmentNames = new String[]{res.getString(R.string.inventory),
					res.getString(R.string.sale),
					res.getString(R.string.report)};

		}

		@Override
		public Fragment getItem(int i) {
			return fragments[i];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int i) {
			return fragmentNames[i];
		}

		/**
		 * Update
		 *
		 * @param index
		 */
		public void update(int index) {
			fragments[index].update();
		}

	}
}