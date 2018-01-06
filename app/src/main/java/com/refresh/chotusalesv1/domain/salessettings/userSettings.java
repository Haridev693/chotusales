package com.refresh.chotusalesv1.domain.salessettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 12/9/2017.
 */

public class userSettings {
    public int _id;
    public String username;
    public String usertype;
    public int userpin;


    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", _id + "");
        map.put("username", username);
        map.put("userpin", userpin+"");
        map.put("usertype", usertype);
        return map;
    }
}
