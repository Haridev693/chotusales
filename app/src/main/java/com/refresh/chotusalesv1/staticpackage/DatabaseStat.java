package com.refresh.chotusalesv1.staticpackage;

import android.content.Context;

import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.techicalservices.BluetoothService;
import com.refresh.chotusalesv1.techicalservices.settings.SettingsDao;
import com.refresh.chotusalesv1.techicalservices.settings.SettingsDaoAndroid;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/8/2017.
 */

public class DatabaseStat {

//    private static DatabaseStat instance = null;
    //private static InventoryDao inventoryDao = null;

    public  SettingsDao settingDaoD = null;

//    public static Settings ShopSetting;

//    public static sessionmanager mAppSession;

    public static BluetoothService mbluetoothService;


//    	 *//
    public DatabaseStat(Context c){ //throws NoDaoSetException {
        settingDaoD = new SettingsDaoAndroid(c);
    }

    /**
     * Determines whether the DAO already set or not.
     * @return true if the DAO already set; otherwise false.
     */
//    public static boolean isDaoSet() {
//        return settingDaoD != null;
//    }
//
//    /**
//     * Sets the database connector.
//     * @param dao Data Access Object of inventory.
//     */
//    public static void setSettingsDao(SettingsDao dao) {
//        settingDaoD = dao;
//    }


//    public static DatabaseStat getInstance() throws NoDaoSetException {
//        if (instance == null)
//            instance = new DatabaseStat();
//        return instance;
//    }

    public ArrayList<taxSettings> getTaxSettings()
    {
        return settingDaoD.getTaxSettings();
    }

}
