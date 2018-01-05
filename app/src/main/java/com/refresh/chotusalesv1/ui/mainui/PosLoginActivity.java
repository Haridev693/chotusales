package com.refresh.chotusalesv1.ui.mainui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;


public class PosLoginActivity extends AppCompatActivity {

    private sessionmanager mOrderSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poslogin);
        mOrderSession = new sessionmanager(getApplicationContext());
        mOrderSession.checkLogin();
    }

    public void onLogin(View view) {
//        shownavigationscreen();
        Intent i = new Intent(this,DashboardScreen.class);
        startActivity(i);
    }
}
