package com.refresh.chotusalesv1.ui.mainui;

//import android.app.AlertDialog;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.salessettings.taxSettings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxSettingActivity extends AppCompatActivity {

    @BindView(R.id.listtaxitems)
    ListView taxListView;

    @BindView(R.id.btnaddtaxitem)
    FloatingActionButton btnAddtaxitem;
//    private ArrayList<taxSettings> taxList;
    private List<Map<String, String>> taxList;
    private LayoutInflater inflater;
    private View Viewlayout;
    private Resources res;
    private android.support.v7.app.AlertDialog.Builder popDialog;
    private EditText taxpercentBox,taxNameBox;
    private CheckBox IsInclusivePrice;
    private Button confirmbutton,clearbutton;
    private AlertDialog alert;
//    private SettingsDao taxSettingsDao;
    private DatabaseStat dataStat;
    private int setItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_setting);
//        try {
            dataStat = new DatabaseStat(getApplicationContext());
//        }catch (NoDaoSetException e) {
//            e.printStackTrace();
//        }

//        taxSettingsDao =dataStat.getTaxSettings();

        ButterKnife.bind(this);
        res = getResources();
        initUI(savedInstanceState);

    }

    private void showList(List<taxSettings> list) {

        taxList = new ArrayList<Map<String, String>>();
        for (taxSettings productLot : list) {
            taxList.add(productLot.toMap());
        }

        SimpleAdapter sAdap = new SimpleAdapter(TaxSettingActivity.this, taxList,
                R.layout.listview_tax, new String[] { "taxname",
                "taxpercent","IsPriceInclusive" }, new int[] {
                R.id.taxname, R.id.taxpercent, R.id.ListInclusivePrice });
        taxListView.setAdapter(sAdap);


    }


    @Override
    protected void onResume() {
        super.onResume();
        dataStat = new DatabaseStat(getApplicationContext());
        showList(dataStat.getTaxSettings());


    }



    /**
     * Initiate this UI.
     * @param savedInstanceState
     */
    private void initUI(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_addtaxitemd);
        popDialog = new AlertDialog.Builder(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        btnAddtaxitem.setOnClickListener(
                new View.OnClickListener() {
            public void onClick(View v) {
                showAddTax(-1,-1);
            }
        });

        taxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long myLng) {

                int id =-1;
                try {
                   id = Integer.parseInt(taxList.get(position).get("id"));
                }
                catch(Exception e){
//                    id =
                }

                showAddTax(id,position);


//                dataStat.settingDaoD.getTaxSettings()
//                taxSettings s = (taxSettings) parent.getSelectedItem();
//
//                dataStat.settingDaoD.updatetaxSetting(s._id,s.taxname,s.taxpercent);
                //s._id
            }});




//        cancelEditButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                cancelEdit();
//            }
//        });
    }


    private void showAddTax(int idl, int position){
        Viewlayout = inflater.inflate(R.layout.layout_addtaxitemd,
                (ViewGroup) findViewById(R.id.addtaxitemD));
        popDialog.setView(Viewlayout);

        setItemID = idl;

        taxNameBox = (EditText) Viewlayout.findViewById(R.id.taxnameBox);
        taxpercentBox = (EditText) Viewlayout.findViewById(R.id.percentBox);
        IsInclusivePrice = (CheckBox) Viewlayout.findViewById(R.id.IsPriceInclusive);

        if(setItemID!=-1) {
            taxNameBox.setText(taxList.get(position).get("taxname"));
            taxpercentBox.setText(taxList.get(position).get("taxpercent"));
            try {
                IsInclusivePrice.setChecked(Boolean.parseBoolean(taxList.get(position).get("IsPriceInclusive")));
            }
            catch(Exception e){}
        }



        confirmbutton = (Button) Viewlayout.findViewById(R.id.addtaxDconfirmButton);
        clearbutton = (Button) Viewlayout.findViewById(R.id.addtaxDclearButton);
        confirmbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (taxNameBox.getText().toString().equals("") || taxpercentBox.getText().toString().equals("")) {
                    Toast.makeText(TaxSettingActivity.this,
                            res.getString(R.string.please_input_all), Toast.LENGTH_SHORT)
                            .show();
                } else {

                    taxSettings s = new taxSettings();

                    s.taxname = taxNameBox.getText().toString();

                    boolean success = false;
                    try {
                        s.taxpercent = Double.parseDouble(taxpercentBox.getText().toString());
                    }
                    catch(Exception e){}
                    s.IsPriceInclusive = IsInclusivePrice.isChecked();
                    if(setItemID==-1) {
                        success = dataStat.settingDaoD.addtaxSettings(s);
                    }
                    else
                    {
                        s._id = setItemID;
                        success = dataStat.settingDaoD.updatetaxSetting(s._id,s.taxname,s.taxpercent,s.IsPriceInclusive);
                    }

//                    if(idl!=null)



                    if (success) {
                        Toast.makeText(TaxSettingActivity.this, res.getString(R.string.success), Toast.LENGTH_SHORT).show();
                        taxNameBox.setText("");
                        taxpercentBox.setText("");
                        onResume();
                        alert.dismiss();


                    } else {
                        Toast.makeText(TaxSettingActivity.this, res.getString(R.string.fail) ,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        clearbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(taxNameBox.getText().toString().equals("") && taxpercentBox.getText().toString().equals("")){
                    alert.dismiss();
                    onResume();
                }
                else{
                    taxNameBox.setText("");
                    taxpercentBox.setText("");
                }
            }
        });

        alert = popDialog.create();
        alert.show();
    }



//    private void editItem(int idl, int position){
//        Viewlayout = inflater.inflate(R.layout.layout_addtaxitemd,
//                (ViewGroup) findViewById(R.id.addtaxitemD));
//        popDialog.setView(Viewlayout);
//
//        setItemID = idl;
//
//        taxNameBox = (EditText) Viewlayout.findViewById(R.id.taxnameBox);
//        taxpercentBox = (EditText) Viewlayout.findViewById(R.id.percentBox);
//
//        if(setItemID!=-1) {
//            taxNameBox.setText(taxList.get(position).get("taxname"));
//            taxpercentBox.setText(taxList.get(position).get("taxpercent"));
//        }
//        confirmbutton = (Button) Viewlayout.findViewById(R.id.addtaxDconfirmButton);
//        clearbutton = (Button) Viewlayout.findViewById(R.id.addtaxDclearButton);
//        confirmbutton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if (taxNameBox.getText().toString().equals("") || taxpercentBox.getText().toString().equals("")) {
//                    Toast.makeText(TaxSettingActivity.this,
//                            res.getString(R.string.please_input_all), Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    taxSettings s = new taxSettings();
//                    s.taxname = taxNameBox.getText().toString();
//                    s.taxpercent = Double.parseDouble(taxpercentBox.getText().toString());
//                    s._id = setItemID;
//                    boolean success = dataStat.settingDaoD.updatetaxSetting(s._id,s.taxname,s.taxpercent);
//                    if (success) {
//                        Toast.makeText(TaxSettingActivity.this, res.getString(R.string.success), Toast.LENGTH_SHORT).show();
//                        taxNameBox.setText("");
//                        taxpercentBox.setText("");
//                        onResume();
//                        alert.dismiss();
//
//
//                    } else {
//                        Toast.makeText(TaxSettingActivity.this, res.getString(R.string.fail) ,Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });
//        clearbutton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                if(taxNameBox.getText().toString().equals("") && taxpercentBox.getText().toString().equals("")){
//                    alert.dismiss();
//                    onResume();
//                }
//                else{
//                    taxNameBox.setText("");
//                    taxpercentBox.setText("");
//                }
//            }
//        });
//
//        alert = popDialog.create();
//        alert.show();
//    }


}
