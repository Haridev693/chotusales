package com.refresh.chotusalesv1.domain.salessettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 12/9/2017.
 */

public class taxSettings {

    public int _id;
    public String taxname;
    public double taxpercent;
    public Boolean IsPriceInclusive;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", _id + "");
        map.put("taxname", taxname);
        map.put("taxpercent", taxpercent+"");
        map.put("IsPriceInclusive",IsPriceInclusive +"");
        return map;
    }








//    public static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        long factor = (long) Math.pow(10, places);
//        value = value * factor;
//        long tmp = Math.round(value);
//        return (double) tmp / factor;
//    }


}






