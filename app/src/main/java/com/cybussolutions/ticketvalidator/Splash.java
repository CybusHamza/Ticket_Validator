package com.cybussolutions.ticketvalidator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cybussolutions.ticketvalidator.Activities.MainScreen;
import com.cybussolutions.ticketvalidator.Activities.Payment_Method;
import com.cybussolutions.ticketvalidator.Activities.Qr_Activity;

public class Splash extends AppCompatActivity {
    String Qrsting;
        ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        progressDialog = ProgressDialog.show(Splash.this, "Please wait...", "Loading Data ...", false, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        Qrsting=  preferences.getString("qr_string","");

            Thread CustomThread = new Thread() {


                public void run() {

                    try {
                        sleep(3000);
                    } catch (InterruptedException w) {
                        w.printStackTrace();

                    } finally {
                        if (Qrsting.isEmpty()) {
                            Intent intent = new Intent(Splash.this, Payment_Method.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(Splash.this, MainScreen.class);
                            startActivity(intent);
                        }
                    }
                }


            };

            CustomThread.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

}
