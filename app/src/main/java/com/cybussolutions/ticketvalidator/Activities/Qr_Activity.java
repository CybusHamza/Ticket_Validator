package com.cybussolutions.ticketvalidator.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Qr_Genrator.Contents;
import com.cybussolutions.ticketvalidator.Qr_Genrator.QRCodeEncoder;
import com.cybussolutions.ticketvalidator.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Qr_Activity extends AppCompatActivity implements OnClickListener, BeaconConsumer {

    private String LOG_TAG = "GenerateQRCode";
    String Qrsting;
    boolean isTrue = false;
    Toolbar toolbar;
    private DBManager dbManager;
    String route_id,user_id,number_of_persons,remaining_balance;
    String date;
    private BeaconManager beaconManager;
    private Region region;
    String confirmNum;
    ImageView myImage;
    Button generateQrButton;
    String qrId="";

    TextView showLabel,textViewTop;
    private int REQUEST_PERMISSIONS=1,REQUEST_BLUETOOTH=2;
    BluetoothAdapter bluetooth;
    LocationManager service;
    boolean enabled;

    Button saveQrBtn;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Generate QR Code");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        generateQrButton = (Button) findViewById(R.id.button1);
        saveQrBtn = (Button) findViewById(R.id.saveQrButton);
        showLabel = (TextView) findViewById(R.id.labelForUser);
        textViewTop = (TextView) findViewById(R.id.textView1);
        generateQrButton.setOnClickListener(this);
        generateQrButton.setVisibility(View.VISIBLE);
        saveQrBtn.setVisibility(View.INVISIBLE);
        showLabel.setVisibility(View.INVISIBLE);
        textViewTop.setVisibility(View.VISIBLE);
        beaconManager = BeaconManager.getInstanceForApplication(this);

        dbManager = new DBManager(Qr_Activity.this);
        dbManager.open();
      //  Button button =(Button)findViewById(R.id.btnCnfrm);
       // button.setOnClickListener(this);


        saveQrBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!qrId.equals("")){
                    Toast.makeText(Qr_Activity.this,"You saved the qr code",Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(Qr_Activity.this,Dashboard.class);
                    finish();
                    startActivity(intent);
                }else {
                    DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    dateFormatter1.setLenient(false);
                    Date today1 = new Date();
                    String save_date = dateFormatter1.format(today1);
                    dbManager.insert_into_save_qr_table(Qrsting, save_date);
                    Toast.makeText(Qr_Activity.this, "You saved the qr code", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Qr_Activity.this, Dashboard.class);
                    finish();
                    startActivity(intent);
                }

            }
        });

        //Intent intent = getIntent();
        Intent i=getIntent();
        route_id=i.getStringExtra("route_id");
        user_id=i.getStringExtra("user_id");
        number_of_persons=i.getStringExtra("person_traveling");
        remaining_balance=i.getStringExtra("remaining_balance");
        String checkActivity=i.getStringExtra("activityName");


        if(checkActivity.equals("SaveQrScreen")){
            String savedQrString=i.getStringExtra("savedQrString");
            qrId=i.getStringExtra("qrId");
            generateQrButton.setVisibility(View.INVISIBLE);
            saveQrBtn.setVisibility(View.VISIBLE);
            showLabel.setVisibility(View.VISIBLE);
            textViewTop.setVisibility(View.INVISIBLE);

            beaconManager.bind(Qr_Activity.this);

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3/4;
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(savedQrString,
                    null,
                    Contents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    smallerDimension);
            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                myImage = (ImageView) findViewById(R.id.imageView1);
                myImage.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
            String splitSavedQrString[]=savedQrString.split(",");
            route_id=splitSavedQrString[3];
            confirmNum=splitSavedQrString[4];
            user_id=splitSavedQrString[0];
            number_of_persons=splitSavedQrString[8];
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dateFormatter.setLenient(false);
            Date today = new Date();
            date = dateFormatter.format(today);

          //  route_id,confirmNum,user_id,number_of_persons,date,"0000-00-00"
        }
        //qr_string
          SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Qr_Activity.this);
          Qrsting=  preferences.getString("qr_string","");

       // Qrsting = intent.getStringExtra("Qr_string");
        if (ActivityCompat.checkSelfPermission(Qr_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (Build.VERSION.SDK_INT > 22) {

                requestPermissions(new String[]{Manifest.permission
                                .ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS);

            }else {

            }

        }
         bluetooth= BluetoothAdapter.getDefaultAdapter();

        if (!bluetooth.isEnabled()) {
            Toast.makeText(getApplicationContext(),
                    "Turning ON Bluetooth", Toast.LENGTH_LONG);
            // Intent enableBtIntent = new
            // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
        }

        service = (LocationManager) getSystemService(LOCATION_SERVICE);
        enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                    Qr_Activity.this);
            myAlertDialog.setTitle("Gps must require");
            myAlertDialog.setMessage("You need to turn on your gps location to proceed");

            myAlertDialog.setPositiveButton("Enable",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            myAlertDialog.setNegativeButton("Deny",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            /*Toast.makeText(getApplicationContext(),
                                    "Please turn on your location to proceed", Toast.LENGTH_LONG);
                            finish();*/
                        }
                    });
            myAlertDialog.show();
        }

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:
                bluetooth= BluetoothAdapter.getDefaultAdapter();
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (bluetooth.isEnabled() && enabled) {
                    startDialog();
                }else {
                    if (!enabled) {
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                                Qr_Activity.this);
                        myAlertDialog.setTitle("Gps must require");
                        myAlertDialog.setMessage("You need to turn on your gps location to proceed");

                        myAlertDialog.setPositiveButton("Enable",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                });

                        myAlertDialog.setNegativeButton("Deny",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                            /*Toast.makeText(getApplicationContext(),
                                    "Please turn on your location to proceed", Toast.LENGTH_LONG);
                            finish();*/
                                    }
                                });
                        myAlertDialog.show();
                    }
                    if (!bluetooth.isEnabled()) {
                        Toast.makeText(getApplicationContext(),
                                "Turning ON Bluetooth", Toast.LENGTH_LONG);
                        // Intent enableBtIntent = new
                        // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                    }
                    //Toast.makeText(getApplicationContext(),"Please enable bluetooth and gps information to generate QR code",Toast.LENGTH_LONG).show();
                }
                break;
           /* case R.id.btnCnfrm:

                Intent intent = new Intent(Qr_Activity.this,Confirmation.class);
                startActivity(intent);
                finish();
                break;*/
            // More buttons go here (if any) ...

        }
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Qr_Activity.this);
        myAlertDialog.setTitle("Are You sure?");
        myAlertDialog.setMessage("Your amount will be deducted after Scanning Qr code");

        myAlertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        generateQrButton.setVisibility(View.INVISIBLE);
                        saveQrBtn.setVisibility(View.VISIBLE);
                        showLabel.setVisibility(View.VISIBLE);
                        textViewTop.setVisibility(View.INVISIBLE);

                        beaconManager.bind(Qr_Activity.this);

                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3/4;
                        Random num = new Random();
                        int rnum = num.nextInt(999);
                        if (rnum<0){
                            rnum = rnum*-1;
                        }
                        Calendar cal = Calendar.getInstance();
                        String tym =String.valueOf( cal.getTimeInMillis());
                        confirmNum = user_id + "00" + String.valueOf(rnum) + tym;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String from= preferences.getString("FROM","");
                        String to =preferences.getString("TO","");
                        String name=preferences.getString("name","");
                        String number=preferences.getString("number","");
                        ////final qr string customer_id,fare,fareType,routeId,transId,transStatusId,from,to,persontraveling,name,number////////
                        Qrsting=Qrsting+","+route_id+","+confirmNum+","+"2"+","+from+","+to+","+number_of_persons+","+name+","+number;
                        //Encode with a QR Code image
                        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(Qrsting,
                                null,
                                Contents.Type.TEXT,
                                BarcodeFormat.QR_CODE.toString(),
                                smallerDimension);
                        try {
                            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                            myImage = (ImageView) findViewById(R.id.imageView1);
                            myImage.setImageBitmap(bitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dateFormatter.setLenient(false);
                        Date today = new Date();
                        date = dateFormatter.format(today);

                        if (number_of_persons==null || number_of_persons.equals("") || number_of_persons==""){
                            number_of_persons="1";
                        }

                       // Toast.makeText(getApplicationContext(),confirmNum,Toast.LENGTH_LONG).show();

                        /*dbManager = new DBManager(Qr_Activity.this);
                        dbManager.open();

                        dbManager.update_customer_balance(user_id,remaining_balance);*/
                    }
                });

        myAlertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        myAlertDialog.show();
    }



    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    final Beacon firstBeacon = beacons.iterator().next();
                    //  logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                    String data = firstBeacon.getId1()+","+firstBeacon.getId2()+","+firstBeacon
                            .getId3();
                    isTrue =true;

                  //  Toast.makeText(Qr_Activity.this, data, Toast.LENGTH_SHORT).show();

                    String id1 = firstBeacon.getId1()+"";
                    String id2 = firstBeacon.getId2()+"";
                    String id3 = firstBeacon.getId3()+"";

                    if(id2.equals(user_id))
                    {
                        beaconManager.unbind(Qr_Activity.this);
                        dbManager.update_customer_balance(user_id,remaining_balance);
                        if(!qrId.equals("")) {
                            dbManager.delete_saved_qr(qrId);
                        }
                        insertintoHistoryTravel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                myImage.setImageDrawable(null);
                                showLabel.setText("Thank You For Using Epay Service");
                                new SweetAlertDialog(Qr_Activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Done")
                                        .setConfirmText("OK").setContentText("Transaction successful")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismiss();


                                                Intent intent= new Intent(Qr_Activity.this,Dashboard.class);
                                                finish();
                                                startActivity(intent);


                                            }
                                        })
                                        .show();   }
                        });

                       /* try {

                            Toast.makeText(Qr_Activity.this, "Beacon Found Sucessfully", Toast.LENGTH_SHORT).show();
                           // beaconManager.stopRangingBeaconsInRegion(new Region("apr", null, null, null));


                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }*/
                    }

                    //logToDisplay(data);
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS || requestCode==REQUEST_BLUETOOTH ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

            } else {


                Toast.makeText(Qr_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 0) {
                if (requestCode == 0 && resultCode == RESULT_OK) {


                } else {
                    if (!((BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()){
                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                        builder.setTitle("Bluetooth not enabled");
                        builder.setMessage("Please enable Bluetooth.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }

                        });
                        builder.show();

                    }
//                    Toast.makeText(this, "Bluetooth Permission must required",
//                            Toast.LENGTH_LONG).show();


                }
            }else if(requestCode==1) {
                if (requestCode == 1 && resultCode == RESULT_OK
                        && null != data) {


                } else {
                    Toast.makeText(this, "Location Permission must required",
                            Toast.LENGTH_LONG).show();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getLocalizedMessage(), "test");
//            Toast.makeText(this, "", Toast.LENGTH_LONG)
//                    .show();
        }
    }
public void insertintoHistoryTravel(){
    StringRequest strreq = new StringRequest(Request.Method.POST,
            "http://epay.cybussolutions.com/Api_Service/SendhistoryData",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String Response) {
                    // loading.dismiss();

                    if(!(Response.equals("")))
                    {
                        //Toast.makeText(getApplicationContext(), Response, Toast.LENGTH_LONG).show();
//                                            dbManager = new DBManager(Signup_activity.this);
//                                            dbManager.open();
                        try{
                            JSONObject jsonObject = new  JSONObject(Response);
//                                                jsonObject.get("userId");
                            String id = jsonObject.get("userId").toString();
                           // finish();
////
//                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                SharedPreferences.Editor editor = preferences.edit();
//                             //   editor.putString();
//                                editor.putString("UserEmail", email);
//                                // editor.putString("UserPassword",userPassword);
//                                editor.putString("f_name", first_name);
//                                editor.putString("l_nmae", last_name);
//                                editor.putString("id", id);
//                                editor.putString("sign_in_status","1");
//                                editor.apply();



                            //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
                            //  dbManager.insert(id,first_name, last_name,password,phone_number,gender,email,"1");

//                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup_activity.this);
//                                                SharedPreferences.Editor editor = preferences.edit();
//                                                // editor.putString("UserEmail", userEmail);
//                                                editor.putString("id", id);
//                                                editor.commit();
//                                                startService(new Intent(Signup_activity.this, HelloService.class));

                        }catch (Exception e){
                            //Toast.makeText(getApplicationContext(),"Exception:"+e.toString(),Toast.LENGTH_LONG).show();
                        }
                        //  dbManager.insert(first_name, last_name, email, phone_number);
                        //Cursor cursor=dbManager.fetch();
                    }
                    else {
//                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                            SharedPreferences.Editor editor = preferences.edit();
//                                            editor.putString("sign_in_status","0");
//                                            editor.apply();
                        Toast.makeText(Qr_Activity.this, "There was an error", Toast.LENGTH_SHORT).show();
                    }
                    // showJSON(Response);
                    // get response
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
            e.printStackTrace();
            String message = null;
            if (e instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                date = dateFormatter.format(today);
                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                dbManager.insert_into_history_travel(route_id,confirmNum,user_id,number_of_persons,date,"0000-00-00");
                dbManager.insert_into_history_travel_live(route_id,confirmNum,user_id,number_of_persons,date,"0000-00-00");
               // finish();
            }
            // loading.dismiss();
        }
    }) {
        @Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
            params.put("route_id", route_id);
            params.put("user_id", user_id);
            params.put("person_traveling", number_of_persons);
            params.put("trans_id", confirmNum);
            params.put("date_added", date);
            //  params.put("gender",gender);
            params.put("date_modified","557678");
            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(Qr_Activity.this);
    requestQueue.add(strreq);
}
}