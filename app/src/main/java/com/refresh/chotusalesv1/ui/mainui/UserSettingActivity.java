package com.refresh.chotusalesv1.ui.mainui;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.salessettings.userSettings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSettingActivity extends AppCompatActivity {

    @BindView(R.id.useritems)
    ListView userlistView;

//    @BindView(R.id.)

//    @BindView(R.id.btnAddUser)
//    FloatingActionButton btnAddUsers;

    private DatabaseStat dataStat;
    private ArrayList<Map<String, String>> Userlist;
    private LayoutInflater inflater;
    private android.support.v7.app.AlertDialog.Builder popDialog;
    private View Viewlayout;
    private int setItemID;
    private EditText usernameBox,userpinBox;
    private Spinner usertypeSpinner;
    private Button confirmbutton;
    private Button clearbutton;
    private Resources res;
    private Dialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);
        dataStat = new DatabaseStat(getApplicationContext());
        initUI(savedInstanceState);
    }


    private void showList(List<userSettings> list) {

        Userlist = new ArrayList<Map<String, String>>();
        for (userSettings productLot : list) {
            Userlist.add(productLot.toMap());
        }

        SimpleAdapter sAdap = new SimpleAdapter(UserSettingActivity.this, Userlist,
                R.layout.listviewusers, new String[] { "username",
                "userpin","usertype" }, new int[] {
                R.id.usernameL, R.id.userpinL, R.id.userTypesL });
        userlistView.setAdapter(sAdap);


    }


    private void initUI(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_addtaxitemd);
        res = getResources();
        popDialog = new AlertDialog.Builder(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        btnAddtaxitem.setOnClickListener(
//                new View.OnClickListener() {
//                    public void onClick(View v) {
//                        showAddTax(-1,-1);
//                    }
//                });

        userlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long myLng) {

                int id = -1;
                try {
                    id = Integer.parseInt(Userlist.get(position).get("id"));
                }
                catch(Exception e){

                }

                showAddTax(id, position);


//                dataStat.settingDaoD.getTaxSettings()
//                taxSettings s = (taxSettings) parent.getSelectedItem();
//
//            }
//        });
            }
        });
    }

    private void showAddTax(int idl, int position){
        Viewlayout = inflater.inflate(R.layout.adduserdialog,
                (ViewGroup) findViewById(R.id.addusersID));
        popDialog.setView(Viewlayout);

        setItemID = idl;

        usernameBox = (EditText) Viewlayout.findViewById(R.id.username1);
        userpinBox = (EditText) Viewlayout.findViewById(R.id.userpin);
        usertypeSpinner = (Spinner) Viewlayout.findViewById(R.id.UserTypeSpinner);


//        PayTypeSpinner = (Spinner) v.findViewById(R.id.);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.usertypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        usertypeSpinner.setAdapter(adapter);
        usertypeSpinner.setSelection(0);
        usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });
        if(setItemID!=-1) {
            usernameBox.setText(Userlist.get(position).get("username"));
            userpinBox.setText(Userlist.get(position).get("userpin"));
            if(Userlist.get(position).get("usertype").equals(UserTypes.admin.name()))
            usertypeSpinner.setSelection(0);
            else
                usertypeSpinner.setSelection(1);
        }



        confirmbutton = (Button) Viewlayout.findViewById(R.id.adduserconfirmButton);
        clearbutton = (Button) Viewlayout.findViewById(R.id.adduserclearButton);
        confirmbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (usernameBox.getText().toString().equals("") || userpinBox.getText().toString().length()<4) {
                    Toast.makeText(UserSettingActivity.this,
                            res.getString(R.string.please_input_all), Toast.LENGTH_SHORT)
                            .show();
                } else {

                    userSettings s = new userSettings();

                    s.username = usernameBox.getText().toString();

                    boolean success = false;
                    s.userpin = Integer.parseInt(userpinBox.getText().toString());//Double.parseDouble(taxpercentBox.getText().toString());
                    s.usertype = usertypeSpinner.getSelectedItem().toString();
                    if(setItemID==-1) {
//                        success = dataStat.settingDaoD.addtaxSettings(s);
                    }
                    else
                    {
                        s._id = setItemID;
                        success = dataStat.settingDaoD.updateuserSettings(s._id,s);
                    }

//                    if(idl!=null)



                    if (success) {
                        Toast.makeText(UserSettingActivity.this, res.getString(R.string.success), Toast.LENGTH_SHORT).show();
                        usernameBox.setText("");
                        userpinBox.setText("");
                        usertypeSpinner.setSelection(0);
                        onResume();
                        alert.dismiss();


                    } else {
                        Toast.makeText(UserSettingActivity.this, res.getString(R.string.fail) ,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        clearbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(usernameBox.getText().toString().equals("") && userpinBox.getText().toString().equals("")){
                    alert.dismiss();
                    onResume();
                }
                else{
                    usernameBox.setText("");
                    userpinBox.setText("");
                }
            }
        });

        alert = popDialog.create();
        alert.show();
    }

//                dataStat.settingDaoD.updatetaxSetting(s._id,s.taxname,s.taxpercent);
                //s._i);




//        cancelEditButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                cancelEdit();


    @Override
    protected void onResume() {
        super.onResume();
        dataStat = new DatabaseStat(getApplicationContext());
        showList(dataStat.getUserSettings());

    }
}
