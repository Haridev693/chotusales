package com.refresh.chotusalesv1.domain;

/**
 * Created by Lenovo on 12/8/2017.
 */

public class Settings {
    				//+ "_id INTEGER PRIMARY KEY,"
    public int _id;
    public String userid; //TEXT,"
    public String vatnumber;
    public String printerHeader;// TEXT,"
    public String printerFooter;// TEXT;
    public String bluetoothAddress; //TEXT"
    public String SMSSenderID;// TEXT,"
    public String SMSUsername;// TEXT;
    public String SMSKey; //TEXT"
    public Boolean SMSenabled;
    public Boolean PrintDupReceipt;
    public String ShopName;
    public Double CGSTPercent;
    public Double SGSTPercent;
    public Boolean CheckPrintGSTProds;
    public Boolean CheckPrintTranGST;
    public String AddressLine1;
    public String AddressLine2;

}
