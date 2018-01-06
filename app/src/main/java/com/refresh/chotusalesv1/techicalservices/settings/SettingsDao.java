package com.refresh.chotusalesv1.techicalservices.settings;

import com.refresh.chotusalesv1.domain.Settings;
import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.domain.salessettings.userSettings;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/8/2017.
 */

public interface SettingsDao {

    int addSettings(Settings set);

    Boolean updateSettings(int id,Settings set);

    ArrayList<Settings> getSettings(String UserID);

    ArrayList<taxSettings> getTaxSettings();

    ArrayList<userSettings> getUserSettings();

    Boolean updatetaxSetting(int productId, String name, Double Percentage, Boolean isChecked);


    Boolean addtaxSettings(taxSettings set);

    int updatetaxSettings(int id,taxSettings set);



    int adduserSettings(userSettings set);

    Boolean updateuserSettings(int id,userSettings set);


}
