package com.cybussolutions.ticketvalidator.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.R;

public class Dashboard extends AppCompatActivity {

    Toolbar toolbar;
    Button btnStartTrip;
    TextView tvTotalTrips;
    String totalTrips;

    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar)findViewById(R.id.app_bar_dashboard);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        if(isNetworkAvailable()) {
            startService(new Intent(this, HelloService.class));
        }else {
            Toast.makeText(getApplicationContext(),"You are not connected to internet, Plz check your network connection",Toast.LENGTH_LONG).show();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        totalTrips = preferences.getString("TotalTrips","");


        tvTotalTrips = (TextView)findViewById(R.id.tvTotalTrips);
        tvTotalTrips.setText(totalTrips);
        btnStartTrip = (Button)findViewById(R.id.btnStartTrip);
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Dashboard.this,MainScreen.class);
                startActivity(intent);
                finish();

            }
        });


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
