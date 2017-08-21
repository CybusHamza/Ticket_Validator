package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.R;

import java.io.BufferedReader;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;

public class Confirmation extends AppCompatActivity {
TextView tvTo,TvFrom,tvBalance,tvNop,tvfare;
String from,to,balance,nop,fare,Uniqueid;
    Toolbar toolbar;
Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("Receipt");
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));


        tvTo = (TextView)findViewById(R.id.tvTo);
        TvFrom = (TextView)findViewById(R.id.tvFrom);
tvBalance = (TextView)findViewById(R.id.tvBalance);
        tvNop = (TextView)findViewById(R.id.tvNumberOfPersons);
tvfare = (TextView)findViewById(R.id.tvTotalCharge);
confirm = (Button)findViewById(R.id.btnConfirm);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        from= preferences.getString("FROM","");
        to =preferences.getString("TO","");
        balance = preferences.getString("balance","");
        nop = preferences.getString("NOP","");
        fare = preferences.getString("fare","");
        Uniqueid = preferences.getString("id","");
















        tvTo.setText(to);
        TvFrom.setText(from);
        tvBalance.setText(balance);

        tvfare.setText(fare);


        if (nop.equals( "")){

            nop = "1";
            tvNop.setText(nop);
        }
        else {
            tvNop.setText(nop);
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();




            }
        });





    }
}
