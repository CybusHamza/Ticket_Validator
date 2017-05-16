package com.cybussolutions.ticketvalidator.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cybussolutions.ticketvalidator.Qr_Genrator.Contents;
import com.cybussolutions.ticketvalidator.Qr_Genrator.QRCodeEncoder;
import com.cybussolutions.ticketvalidator.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Qr_Activity extends AppCompatActivity implements OnClickListener {

    private String LOG_TAG = "GenerateQRCode";
    String Qrsting;
    Toolbar toolbar;
    private DBManager dbManager;
    String route_id,user_id,number_of_persons,remaining_balance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Generate QR Code");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

       // Intent intent = getIntent();
        //qr_string
          SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Qr_Activity.this);
          Qrsting=  preferences.getString("qr_string","");
       // Qrsting = intent.getStringExtra("Qr_string");

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:

               startDialog();

                break;

            // More buttons go here (if any) ...

        }
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Qr_Activity.this);
        myAlertDialog.setTitle("Are You sure?");
        myAlertDialog.setMessage("Your ammount will be detected after Generated Qr code");

        myAlertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Find screen size
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3/4;

                        //Encode with a QR Code image
                        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(Qrsting,
                                null,
                                Contents.Type.TEXT,
                                BarcodeFormat.QR_CODE.toString(),
                                smallerDimension);
                        try {
                            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
                            myImage.setImageBitmap(bitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dateFormatter.setLenient(false);
                        Date today = new Date();
                        String s = dateFormatter.format(today);
                        Intent i=getIntent();
                        route_id=i.getStringExtra("route_id");
                        user_id=i.getStringExtra("user_id");
                        number_of_persons=i.getStringExtra("person_traveling");
                        remaining_balance=i.getStringExtra("remaining_balance");
                        if (number_of_persons==null){
                            number_of_persons="1";
                        }
                        dbManager = new DBManager(Qr_Activity.this);
                        dbManager.open();
                        dbManager.insert_into_history_travel(route_id,user_id,number_of_persons,s,"0000-00-00");
                        dbManager.update_customer_balance(user_id,remaining_balance);
                    }
                });

        myAlertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        myAlertDialog.show();
    }

}