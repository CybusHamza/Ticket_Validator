package com.cybussolutions.ticketvalidator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.Activities.HelloService;
import com.cybussolutions.ticketvalidator.Activities.Login_Activity;
import com.cybussolutions.ticketvalidator.Activities.MainScreen;
import com.cybussolutions.ticketvalidator.Activities.Payment_Method;
import com.cybussolutions.ticketvalidator.Activities.Qr_Activity;
import com.cybussolutions.ticketvalidator.Activities.Signup_activity;

public class Splash extends AppCompatActivity {

    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;

    String Qrsting,SignInStatus;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
//        startService(new Intent(this, HelloService.class));
        progressDialog = ProgressDialog.show(Splash.this, "Please wait...", "Loading Data ...", false, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        Qrsting=  preferences.getString("qr_string","");
        SignInStatus =preferences.getString("sign_in_status","");

            Thread CustomThread = new Thread() {

                public void run() {

                    try {
                        sleep(3000);
                    } catch (InterruptedException w) {
                        w.printStackTrace();

                    } finally {
//                        if (Qrsting.isEmpty()) {
//
//                            Intent intent = new Intent(Splash.this, Payment_Method.class);
//                            startActivity(intent);
//                        }
//                         else {
//                            Intent intent = new Intent(Splash.this, MainScreen.class);
//                            startActivity(intent);
//                        }

                        if (Qrsting.isEmpty()|| SignInStatus == "0") {

                            Intent intent = new Intent(Splash.this, Login_Activity.class);
                            startActivity(intent);
                        }
                        if (!(Qrsting.isEmpty())|| SignInStatus=="1"){
                            Intent intent = new Intent(Splash.this, Payment_Method.class);
                            startActivity(intent);
                        }


//
//                        if ( SignInStatus=="1" && Qrsting.isEmpty()) {
//
//                            Intent intent = new Intent(Splash.this, Payment_Method.class);
//                            startActivity(intent);
//                        }
//                        else if (SignInStatus=="0")
//                         {
//                            Intent intent = new Intent(Splash.this, Signup_activity.class);
//                            startActivity(intent);
//                        }
//                        else if (!(Qrsting.isEmpty())){
//                            Intent intent = new Intent(Splash.this, MainScreen.class);
//                            startActivity(intent);
//
//
//                        }
//                        else {
//
//
//                            Toast.makeText(getApplicationContext(),"deadlock",Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(Splash.this, MainScreen.class);
//                            startActivity(intent);
//
//
//                        }
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
