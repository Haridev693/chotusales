package com.refresh.chotusalesv1.techicalservices;

/**
 * Created by Lenovo on 11/23/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.refresh.chotusalesv1.domain.salessettings.userSettings;
import com.refresh.chotusalesv1.ui.mainui.LoginActivity;

import java.util.HashMap;



/**
 * Created by Lenovo on 11/1/2017.
 */

public class sessionmanager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "swachm";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_USERID = "UserID";//USER_ID

    // Constructor
    public sessionmanager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email, String UserID){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_USERID, UserID);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    public void writeCurrentuser( userSettings u){

        editor.putString("username",u.username);
        editor.putString("usertype",u.usertype);
        editor.putInt("userpin", u.userpin);
        editor.commit();
    }


    public userSettings getCurrentUser()
    {
        userSettings s = new userSettings();
        s.username = pref.getString("username","");
        s.usertype = pref.getString("usertype","");
        s.userpin = pref.getInt("userpin",0);
        return s;
    }



    public void saveAccessToken(String token) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        //SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_access_token", token);
        editor.apply(); // This line is IMPORTANT !!!
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        // return user
        return user;
    }

    public void Store(String key,String object)
    {
        editor.putString(key,object);
        editor.commit();
    }




    public Integer getShopID(String shopID)
    {
        Integer s = pref.getInt(shopID,1);
        return s;
        // editor.putString(key);
        // editor.commit();
    }

    public String getShopCat(String Shopcat)
    {
        String s = pref.getString("ShopCat","");
        return s;
    }

//    public void Storechain(chain c)
//    {
//        editor.putInt("ChainID",c.id);
//        editor.putString("ChainName", c.name);
//        editor.putString("ChainContact",c.contact_no);
//        editor.commit();
////        editor.apply();
//    }
//
//
//    public void StoreDevices(chain c)
//    {
//        editor.putInt("shopID",c.id);
//        editor.putString("ChainName", c.name);
//        editor.putString("ChainContact",c.contact_no);
//        editor.commit();
////        editor.apply();
//    }
//
//
//    public void Storeshops(Shop s, String Categories, String ShopSetting)
//    {
//        editor.putInt("ShopID",s.id);
//        editor.putString("ShopName", s.shop_name);
//        editor.putString("ShopAddress",s.shop_address);
//        editor.putString("ShopCat", Categories);
//        editor.putString("ShopSettings",ShopSetting);
//        editor.commit();
//
//    }
//
//    public String GetShopSettings()
//    {
//        return pref.getString("ShopSettings","");
//    }
//
//
//
//    public Shop getShop()
//    {
//        Shop s= new Shop();
//        s.id=  pref.getInt("ShopID",1);
//        s.shop_address = pref.getString("ShopAddress","");
//        s.shop_name = pref.getString("ShopName","");
//        return s;
//    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }

    public void saveBluetoothAddress(String address) {
        editor.putString("BluetoothAddress",address);
        editor.commit();
    }

    public String getBluetooth()
    {
        return pref.getString("BluetoothAddress","");
    }


}

