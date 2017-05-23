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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Adapter.CustomHistoryListAdapter;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.cybussolutions.ticketvalidator.Qr_Genrator.Contents;
import com.cybussolutions.ticketvalidator.Qr_Genrator.QRCodeEncoder;
import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.R.attr.id;

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

        Button button =(Button)findViewById(R.id.btnCnfrm);
        button.setOnClickListener(this);

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


            case R.id.btnCnfrm:

                Intent intent = new Intent(Qr_Activity.this,Confirmation.class);
                startActivity(intent);
                finish();
                break;
            // More buttons go here (if any) ...

        }
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                Qr_Activity.this);
        myAlertDialog.setTitle("Are You sure?");
        myAlertDialog.setMessage("Your amount will be deducted after Generating Qr code");

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
                        if (number_of_persons==null || number_of_persons.equals("") || number_of_persons==""){
                            number_of_persons="1";
                        }
                        Random num = new Random();
                        int rnum = num.nextInt(999);


                        if (rnum<0){

                            rnum = rnum*-1;


                        }
                        Calendar cal = Calendar.getInstance();
                        String tym =String.valueOf( cal.getTimeInMillis());
                        final String confirmNum = user_id + "00" + String.valueOf(rnum) + tym;

                        Toast.makeText(getApplicationContext(),confirmNum,Toast.LENGTH_LONG).show();

                        dbManager = new DBManager(Qr_Activity.this);
                        dbManager.open();
                        dbManager.insert_into_history_travel(route_id,confirmNum,user_id,number_of_persons,s,"0000-00-00");
                        dbManager.update_customer_balance(user_id,remaining_balance);


                        StringRequest strreq = new StringRequest(Request.Method.POST,
                                "http://epay.cybussolutions.com/Api_Service/SendhistoryData",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String Response) {
                                       // loading.dismiss();

                                        if(!(Response.equals("")))
                                        {
                                              Toast.makeText(getApplicationContext(), Response, Toast.LENGTH_LONG).show();
//                                            dbManager = new DBManager(Signup_activity.this);
//                                            dbManager.open();
                                            try{
                                                JSONObject jsonObject = new  JSONObject(Response);
//                                                jsonObject.get("userId");
                                              String id = jsonObject.get("userId").toString();
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



                                                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
                                              //  dbManager.insert(id,first_name, last_name,password,phone_number,gender,email,"1");

//                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup_activity.this);
//                                                SharedPreferences.Editor editor = preferences.edit();
//                                                // editor.putString("UserEmail", userEmail);
//                                                editor.putString("id", id);
//                                                editor.commit();
//                                                startService(new Intent(Signup_activity.this, HelloService.class));
//                                                Intent intent = new Intent(Signup_activity.this,Login_Activity.class);
//                                                startActivity(intent);
                                                finish();
                                            }catch (Exception e){
                                                Toast.makeText(getApplicationContext(),"Exception:"+e.toString(),Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                                params.put("date_added", "0000-00-00");
                              //  params.put("gender",gender);
                                params.put("date_modified","557678");
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(Qr_Activity.this);
                        requestQueue.add(strreq);












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