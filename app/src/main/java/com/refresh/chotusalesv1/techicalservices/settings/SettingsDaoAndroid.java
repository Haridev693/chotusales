package com.refresh.chotusalesv1.techicalservices.settings;

import android.content.ContentValues;
import android.content.Context;

import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.domain.salessettings.userSettings;
import com.refresh.chotusalesv1.techicalservices.AndroidDatabase;
import com.refresh.chotusalesv1.techicalservices.Database;
import com.refresh.chotusalesv1.techicalservices.DatabaseContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/8/2017.
 */

public class SettingsDaoAndroid implements SettingsDao{


    Database database;
    public SettingsDaoAndroid(Context c) {
        Database database1 = new AndroidDatabase(c);
        this.database = database1;
    }

    @Override
    public int addSettings(Settings set) {

        ContentValues content = new ContentValues();
        content.put("vatnumber",set.vatnumber);
        content.put("printerHeader",set.printerHeader);
        content.put("printerFooter",set.printerFooter);
        content.put("bluetoothAddress",set.bluetoothAddress);
        content.put("ShopName",set.ShopName);
        content.put("PrintDupReceipt",set.PrintDupReceipt);
        content.put("AddressLine1",set.AddressLine1);
        content.put("AddressLine2",set.AddressLine2);
        content.put("CheckPrintGSTProds", set.CheckPrintGSTProds);
        content.put("CheckPrintTranGST", set.CheckPrintTranGST);
        content.put("CGSTPercent",set.CGSTPercent);
        content.put("SGSTPercent",set.SGSTPercent);
        content.put("userid",set.userid);
        content.put("SMSKey",set.SMSKey);
        content.put("SMSSenderID",set.SMSSenderID);
        content.put("SMSUsername",set.SMSUsername);
        content.put("SMSenabled",set.SMSenabled);

        int id = database.insert(DatabaseContents.TABLE_SETTINGS.toString(), content);

        return id;
    }

    @Override
    public Boolean updateSettings(int id , Settings set) {

        ContentValues content = new ContentValues();

        content.put("_id", id);// INTEGER PRIMARY KEY,"
        content.put("vatnumber",set.vatnumber);
        content.put("printerHeader",set.printerHeader);
        content.put("printerFooter",set.printerFooter);
        content.put("bluetoothAddress",set.bluetoothAddress);
        content.put("ShopName",set.ShopName);
        content.put("AddressLine1",set.AddressLine1);
        content.put("AddressLine2",set.AddressLine2);
        content.put("PrintDupReceipt",set.PrintDupReceipt);
        content.put("CheckPrintGSTProds", set.CheckPrintGSTProds);
        content.put("CheckPrintTranGST", set.CheckPrintTranGST);
        content.put("CGSTPercent",set.CGSTPercent);
        content.put("SGSTPercent",set.SGSTPercent);
        content.put("userid",set.userid);
        content.put("SMSKey",set.SMSKey);
        content.put("SMSSenderID",set.SMSSenderID);
        content.put("SMSUsername",set.SMSUsername);
        content.put("SMSenabled",set.SMSenabled);

        return database.update(DatabaseContents.TABLE_SETTINGS.toString(),content);

//        content.put("userid",set.userid);

    }

    @Override
    public ArrayList<Settings> getSettings(String UserID) {
        String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SETTINGS + " WHERE userid = '"+UserID+"'";
//        Object obj = database.select(queryString);
        List<Object> objectList = database.select(queryString);
        Settings s = new Settings();

        ArrayList<Settings> list = new ArrayList<Settings>();

        for (Object object: objectList) {
            if (object == null) {
            } else {
                ContentValues content = (ContentValues) object;
                s.bluetoothAddress = content.getAsString("bluetoothAddress");
                s.printerFooter = content.getAsString("printerFooter");
                s.printerHeader = content.getAsString("printerHeader");
                s.vatnumber = content.getAsString("vatnumber");
                s.userid = content.getAsString("userid");
                s.SMSKey = content.getAsString("SMSKey");
                s.SMSSenderID = content.getAsString("SMSSenderID");
                s.SMSUsername = content.getAsString("SMSUsername");
                s.SMSenabled= content.getAsInteger("SMSenabled")==1;
                s.PrintDupReceipt = content.getAsInteger("PrintDupReceipt")==1;

                s.ShopName =content.getAsString("ShopName");//,set.ShopName);
                s.AddressLine1 = content.getAsString("AddressLine1");//,set.AddressLine1);
                s.AddressLine2 = content.getAsString("AddressLine2");//,set.AddressLine2);
                s.CheckPrintGSTProds = content.getAsInteger("CheckPrintGSTProds")==1;// set.CheckPrintGSTProds);
                s.CheckPrintTranGST = content.getAsInteger("CheckPrintTranGST")==1;//, set.CheckPrintTranGST);
                s.CGSTPercent = content.getAsDouble("CGSTPercent");//,set.CGSTPercent);
                s.SGSTPercent = content.getAsDouble("SGSTPercent");//,set.SGSTPercent);
                s._id = content.getAsInteger("_id");
                list.add(s);
            }
        }
        return list;
    }



    @Override
    public ArrayList<taxSettings> getTaxSettings() {
        String queryString = "SELECT * FROM " + DatabaseContents.TABLE_TAX;
//        Object obj = database.select(queryString);
        List<Object> objectList = database.select(queryString);

        ArrayList<taxSettings> list = new ArrayList<>();
        for (Object object: objectList) {
            if (object == null) {
            } else {
                ContentValues content = (ContentValues) object;
                taxSettings s = new taxSettings();
                s._id = content.getAsInteger("_id");
                s.taxname = content.getAsString("taxname");
                s.taxpercent = content.getAsDouble("taxpercent");
                s.IsPriceInclusive = content.getAsInteger("IsPriceInclusive")==1;//content.getAsBoolean("IsPriceInclusive").booleanValue();
                list.add(s);
            }
        }
        return list;
    }

    @Override
    public ArrayList<userSettings> getUserSettings() {

        String queryString = "SELECT * FROM " + DatabaseContents.TABLE_TAX +"'";
//        Object obj = database.select(queryString);
        List<Object> objectList = database.select(queryString);
        userSettings s = new userSettings();

        ArrayList<userSettings> list = new ArrayList<>();

        for (Object object: objectList) {
            if (object == null) {
            } else {
                ContentValues content = (ContentValues) object;
                s.username = content.getAsString("username");
                s.usertype = content.getAsString("usertype");
                s.userpin = content.getAsInteger("userpin");
                list.add(s);
            }
        }
        return list;
//        return null;
    }

    @Override
    public Boolean addtaxSettings(taxSettings set) {

        ContentValues content = new ContentValues();
//        content.put("_id", id);// INTEGER PRIMARY KEY,"
        content.put("taxname",set.taxname);
        content.put("taxpercent",set.taxpercent);
        content.put("IsPriceInclusive",set.IsPriceInclusive);


        int id = database.insert(DatabaseContents.TABLE_TAX.toString(), content);
        return id != -1;
    }


    @Override
    public Boolean updatetaxSetting(int productId, String name, Double Percentage, Boolean isChecked) {
        ContentValues content = new ContentValues();
        content.put("_id", productId);
        content.put("taxname", name);
        content.put("taxpercent",Percentage);
        content.put("IsPriceInclusive",isChecked);

        return database.update(DatabaseContents.TABLE_TAX.toString(), content);
    }

    @Override
    public int updatetaxSettings(int id, taxSettings set) {

        ContentValues content = new ContentValues();
        content.put("_id", id);// INTEGER PRIMARY KEY,"
        content.put("taxname",set.taxname);
        content.put("taxpercent",set.taxpercent);

        database.update(DatabaseContents.TABLE_TAX.toString(),content);
        return 0;
    }

    @Override
    public int adduserSettings(userSettings set) {

        ContentValues content = new ContentValues();

//        content.put("_id", id);// INTEGER PRIMARY KEY,"
        content.put("username",set.username);
        content.put("userpin",set.userpin);
        content.put("usertype",set.usertype);

        int id = database.insert(DatabaseContents.TABLE_USERS.toString(), content);
        return id;
    }


    @Override
    public int updateuserSettings(int id, userSettings set) {

        ContentValues content = new ContentValues();

        content.put("_id", id);// INTEGER PRIMARY KEY,"
        content.put("username",set.username);
        content.put("userpin",set.userpin);
        content.put("usertype",set.usertype);
//        content.put("bluetoothAddress",set.bluetoothAddress);

        database.update(DatabaseContents.TABLE_USERS.toString(),content);
        return 0;
    }


}
