package com.cybussolutions.ticketvalidator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cybussolutions.ticketvalidator.Activities.MainScreen;

public class Splash extends AppCompatActivity {
        ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        progressDialog = ProgressDialog.show(Splash.this, "Please wait...", "Loading Data ...", false, false);
        Thread CustomThread = new Thread(){



            public void run(){

                   try {
                       sleep(3000);
                   }
                   catch (InterruptedException w){
                       w.printStackTrace();

                   }
                   finally {
                       Intent intent = new Intent(Splash.this, MainScreen.class);
                       startActivity(intent);
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
