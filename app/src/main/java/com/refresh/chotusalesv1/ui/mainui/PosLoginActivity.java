package com.refresh.chotusalesv1.ui.mainui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.salessettings.userSettings;
import com.refresh.chotusalesv1.staticpackage.DatabaseStat;
import com.refresh.chotusalesv1.techicalservices.KeyboardView;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;

import java.util.List;


public class PosLoginActivity extends AppCompatActivity {

    private sessionmanager mOrderSession;
    private KeyboardView layoutview;
    private DatabaseStat dataStat;
    private List<userSettings> users;

//    @BindView(R.id.)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataStat = new DatabaseStat(getApplicationContext());
        setContentView(R.layout.activity_poslogin);
        mOrderSession = new sessionmanager(getApplicationContext());
        mOrderSession.checkLogin();
        layoutview = (KeyboardView) findViewById(R.id.keyboardView);
        users = dataStat.getUserSettings();
    }

    public void onLogin(View view) {

        userSettings u1 = new userSettings();
        Boolean userexist= false;
        Integer userpin =0;
        try {
             userpin = Integer.parseInt(layoutview.getInputText());
        }
        catch (Exception e){}
        for(userSettings u: users){
            if(userpin== u.userpin)
            {
                userexist = true;
                u1= u;
            }
        }
        if(userexist) {

            mOrderSession.writeCurrentuser(u1);

            Intent i = new Intent(this, DashboardScreen.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.dontrights,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
