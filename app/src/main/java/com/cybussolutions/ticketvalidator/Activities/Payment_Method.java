package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Feedback;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.cybussolutions.ticketvalidator.Profile;
import com.cybussolutions.ticketvalidator.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Payment_Method extends AppCompatActivity {

    TextView fareText,tvSelectPaymentMethod,hiddenTv;
    Toolbar toolbar;
    ImageView hiddeniv;
    Button pay;
    LinearLayout hidden;
    RadioGroup radioGroup;
    String from, to;
    private LinearLayout  card, qr;
    private ProgressDialog loading;
    String fare = "";
    String price;

    Drawer result;

    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Edit Profile");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");


SecondaryDrawerItem feedback = new SecondaryDrawerItem().withIdentifier(2).withName("Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__method);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.drawable.bg_ep_slider_header)
                .addProfiles(new ProfileDrawerItem().withName("Aqsa").withEmail("whatever@gmil.com"))
                .withProfileImagesVisible(false)
                .withOnAccountHeaderListener(
                        new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                                return false;
                            }
                        }
                ).build();
        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).addDrawerItems(home, payment, your_trips, EditProfile, logout,feedback
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem==feedback){

                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }



                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Payment_Method.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem== payment){
//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                           String met= preferences.getString("payment_method_id","");
//


                            Intent intent=new Intent(getApplicationContext(),Payment_Method.class);
                            startActivity(intent);
                            finish();


                        }
                        if (drawerItem==EditProfile){
                            Intent intent=new Intent(getApplicationContext(),Profile_Detailed.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==home){
                            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(intent);
                            finish();

                        }

                        return true;

                    }

                }).build();





        hiddenTv = (TextView)findViewById(R.id.hiddenTv);
        hiddeniv = (ImageView) findViewById(R.id.hiddenIv);
        hidden = (LinearLayout)findViewById(R.id.hidden);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
        String p =preferences.getString("payment_method_id","");
        if (p.equals("qr")) {



            String f = p+","+fare;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
            SharedPreferences.Editor prefEditor = pref.edit();

            prefEditor.putString("qr_string",p);

                prefEditor.apply();


                hiddeniv.setImageDrawable(getDrawable(R.drawable.icon_qr));
            hiddenTv.setText("QR Code");
            hidden.setAlpha(1);




        }
        if (p.equals("card")){
          //  hiddeniv.setImageBitmap(getDrawable(R.drawable.qr_card_icon));
            Drawable d = getDrawable(R.drawable.qr_card_icon);
            hiddeniv.setImageDrawable(d);
            hiddenTv.setText("QR Card");
            hidden.setAlpha(1);


        }
        if (p.isEmpty()){
            hidden.setVisibility(View.GONE);
        }


//        final Intent intent = getIntent();
//
//        from = intent.getStringExtra("from");
//        to = intent.getStringExtra("to");
//        price = intent.getStringExtra("price");


//        getData();
//        tvFareData = (TextView)findViewById(R.id.tvFareData);
//
//        tvFareData.setText("$ "+price);
//
//
//        fareText = (TextView) findViewById(R.id.fair);
//        pay = (Button) findViewById(R.id.pay);
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
      //  sms = (LinearLayout) findViewById(R.id.radioButton);
      //  card = (LinearLayout) findViewById(R.id.radioButton2);
       // qr = (LinearLayout) findViewById(R.id.radioButton3);
        tvSelectPaymentMethod = (TextView)findViewById(R.id.tvSelectPaymentMethod);


        tvSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),PaymentMethod_extended.class);
                startActivity(intent);
                finish();


//
//            if (card.getAlpha()==0&& qr.getAlpha()==0) {
//                //    sms.setAlpha(1);
//                card.setAlpha(1);
//                qr.setAlpha(1);
//            }
//            else {
//                card.setAlpha(0);
//                qr.setAlpha(0);
//
//            }
//



            }
        });
//
//        qr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                SharedPreferences.Editor prefEditor = preferences.edit();
//                prefEditor.putString("payment_method_id","qr");
//                String p = preferences.getString("id","")+","+fare;
//                prefEditor.putString("qr_string",p);
//                prefEditor.apply();
//                //  String qr_string = preferences.getString("id","")+","+fare;
//
//                Intent intent1 = new Intent(Payment_Method.this, PaymentMethod_extended.class);
//                // intent1.putExtra("Qr_string",qr_string);
//                startActivity(intent1);
//               // hamza ka code
//
//
//
//
//
//
//
//
//            }
//        });
//
//
//card.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//        SharedPreferences.Editor prefEditor = preferences.edit();
//        prefEditor.putString("payment_method_id","card");
//        prefEditor.apply();
//
//        Intent intent1 = new Intent(Payment_Method.this, PaymentMethod_extended.class);
//        // intent1.putExtra("Qr_string",qr_string);
//        startActivity(intent1);
//
//
//
//    }
//});
//

//PURANA WALA


//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//
//                if (selectedId == sms.getId()) {
//                    Toast.makeText(Payment_Method.this, "Payment by SMS", Toast.LENGTH_SHORT).show();
//
//                } else if (selectedId == card.getId()) {
//                    Toast.makeText(Payment_Method.this, "Payment by Card", Toast.LENGTH_SHORT).show();
//
//                } else if (selectedId == qr.getId()) {
//
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                    SharedPreferences.Editor prefEditor = preferences.edit();
//                    prefEditor.putString("payment_method_id","qr");
//                    String p = preferences.getString("id","")+","+fare;
//                    prefEditor.putString("qr_string",p);
//                    prefEditor.apply();
//                  //  String qr_string = preferences.getString("id","")+","+fare;
//
//                    Intent intent1 = new Intent(Payment_Method.this, MainScreen.class);
//                   // intent1.putExtra("Qr_string",qr_string);
//                    startActivity(intent1);
//                    //hamza ka code
//
//
//                } else {
//                    Toast.makeText(Payment_Method.this, "No option Selected", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });


    }
//
//    public void getData() {
//
//        loading = ProgressDialog.show(Payment_Method.this, "", "Please wait..", false, false);
//
//        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//                loading.dismiss();
//
//
//
//                try {
//                    JSONArray array = new JSONArray(response);
//
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = new JSONObject(array.getString(i));
//
//                        fare = object.getString("Fare_price");
//
//                    }
//
//                    fareText.setText(fare);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
//                String message = null;
//                if (error instanceof NetworkError) {
//                    message = "Cannot connect to Internet...Please check your connection!";
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                }
//            }
//
//
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("from", from);
//                map.put("to", to);
//                return map;
//            }
//        };

//        RequestQueue requestQueue = Volley.newRequestQueue(Payment_Method.this);
//        requestQueue.add(request);
//
//    }
}
