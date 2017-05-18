package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Route_Detailed extends AppCompatActivity {

    String rates;

    Toolbar toolbar;
    int s;
    String route ,fiar ,distance,numOfPersons;
    EditText EtnumberOfPersons;
    TextView tvRoute,Tfiar,tvPrice,tvTime;
    String KEY_FROM= "from";
    String KEY_TO = "to";
    String to,from , price,route_id;
   // TextView Tdistance;
    Button proceed;
    ProgressBar progressBar;

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
    private DBManager dbManager;
     String customer_id,customer_total_balance;
    TextView tvTarrif;
    TextView tvBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detailed);
       // setContentView(R.layout.activity_route_detailed);
        dbManager = new DBManager(Route_Detailed.this);
        dbManager.open();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Route_Detailed.this);

        customer_id=preferences.getString("id",null);
        customer_total_balance=dbManager.fetch_customer_balance(customer_id);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        EtnumberOfPersons = (EditText)findViewById(R.id.etNumberOfPersons);
        EtnumberOfPersons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(rates!=null) {
                    numOfPersons = EtnumberOfPersons.getText().toString();
                    if(charSequence.length()<1)
                        numOfPersons="1";
                    int result = Integer.valueOf(numOfPersons) * Integer.valueOf(rates);
                    tvTarrif.setText(String.valueOf(result));
                }else {
                    tvPrice.setText("No rates defined");
                    Toast.makeText(getApplicationContext(),"No rates defined for this route",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        numOfPersons = EtnumberOfPersons.getText().toString();
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
        //  new DrawerBuilder().withAccountHeader(header).build();
//      //  new SecondaryDrawerItem().withName("Your Trips")
//        new SecondaryDrawerItem().withName("Edit Profile"),
//                new SecondaryDrawerItem().withName("Logout")
        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).addDrawerItems(home, payment, your_trips, EditProfile, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Route_Detailed.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Route_Detailed.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem== payment){
                            Intent intent=new Intent(getApplicationContext(),Payment_Method.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem==EditProfile){
                            Intent intent=new Intent(getApplicationContext(),Profile.class);
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



        //tvRoute = (TextView)findViewById(R.id.tvRoute);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvTarrif=(TextView) findViewById(R.id.tvTariffAmount);
        tvBalance= (TextView) findViewById(R.id.tvBalanceAmount);
        tvBalance.setText(customer_total_balance);
     //   getData();
        Intent intent= getIntent();
        route_id=intent.getStringExtra("route_id");
        to = intent.getStringExtra("to");
        from = intent.getStringExtra("from");
      //  tvRoute.setText("From " +from + " To " + to );

        fiar = intent.getStringExtra("route_fiar");
        distance = intent.getStringExtra("route_distance");

        rates=dbManager.fetch_fare(route_id);
        if(rates==null){
            tvPrice.setText("no rates defined");
            Toast.makeText(getApplicationContext(),"no rates defined",Toast.LENGTH_LONG).show();
        }else {
            tvPrice.setText(rates.toString());
            tvTarrif.setText(rates.toString());
        }


        /*final ProgressDialog progressDialog = ProgressDialog.show(Route_Detailed.this, "Please wait...", "Checking Credentails ...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                     Toast.makeText(Route_Detailed.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                String t = null;
                try{
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i< array.length(); i++) {

                        JSONObject obj = new JSONObject(array.getString(i));
                        //  String fareId=   obj.get("Fare_ID").toString();
                        //    Toast.makeText(getApplicationContext(), farePrice + time+ fareId,Toast.LENGTH_SHORT).show();
                        t = obj.get("time").toString() + "  min";


                        price = obj.get("Fare_price").toString();
//                   int j =     Integer.parseInt(price);
//                        int f = Integer.parseInt(numOfPersons);
//                   s  =  Integer.valueOf(price) * Integer.valueOf(numOfPersons);
//                    }
                    }
                    tvTime.setText(t);
                    tvPrice.setText("$ " +price);
                }
                catch (JSONException e){}

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Route_Detailed.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_FROM,from);
                map.put(KEY_TO,to);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
*/



//
//EtnumberOfPersons.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//    @Override
//    public void onFocusChange(View view, boolean b) {
//        int f=Integer.parseInt( EtnumberOfPersons.getText().toString());
//        int z =Integer.parseInt(price);
//
//        int m = f*z;
//        tvPrice.setText(m);
//
//
//    }
//});


        proceed = (Button) findViewById(R.id.pay);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Route_Detailed.this);
                String PaymentMethod =preferences.getString("payment_method_id","qr");
                if (PaymentMethod.matches("qr")) {
                    if (!tvPrice.getText().equals("no rates defined")) {
                        String test = tvTarrif.getText().toString();
                        int fare = Integer.valueOf(test);
                        //String balancecheck=customer_total_balance;
                        int balancecheck = Integer.valueOf(customer_total_balance);
                        int remainingbalance = balancecheck - fare;
                        if (fare <= balancecheck) {


                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Route_Detailed.this);
                            SharedPreferences.Editor prefEditor = pref.edit();
                            String id = pref.getString("id", "");
                            String f = id + "," + fare;

                            prefEditor.putString("qr_string", f);

                            prefEditor.apply();

                            if(!EtnumberOfPersons.getText().equals("")) {

                                Intent intent = new Intent(Route_Detailed.this, Qr_Activity.class);
                                intent.putExtra("route_id", route_id);
                                intent.putExtra("user_id", customer_id);
                                intent.putExtra("person_traveling", EtnumberOfPersons.getText().toString());
                                intent.putExtra("remaining_balance", String.valueOf(remainingbalance));
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Plz enter no. of persons to proceed",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "your balance is not enough to proceed", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Rates are not defined for this route", Toast.LENGTH_LONG).show();
                    }
                    }
                    if (PaymentMethod.matches("")) {


                        Toast.makeText(getApplicationContext(), "No Payment Method Selected", Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(Route_Detailed.this, Qr_Activity.class);
//                    //intent.putExtra("fair", fiar);
//                    //  intent.putExtra("price",price);
//                    startActivity(intent);
//                    finish();

                    }
                    if (PaymentMethod.matches("card")) {
                        Toast.makeText(getApplicationContext(), "Select QR code as payment method to proceed ", Toast.LENGTH_LONG).show();


                    }
//                editor.clear();
//                editor.apply();


//
//                Intent intent = new Intent(Route_Detailed.this, Payment_Method.class);
//                //intent.putExtra("fair", fiar);
//                intent.putExtra("price",price);
//                startActivity(intent);

            }
        });


        //Troute.setText(route);
//        Tfiar.setText(fiar);
     //   Tdistance.setText(distance);


    }
//    public void getData(){
//        final ProgressDialog progressDialog = ProgressDialog.show(Route_Detailed.this, "Please wait...", "Checking Credentails ...", false, false);
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //                     Toast.makeText(Route_Detailed.this, response, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                try{
//                    JSONArray array = new JSONArray(response);
//                    for(int i = 0; i<= array.length(); i++){
//
//                        JSONObject obj = new JSONObject(array.getString(i));
//                      //  String fareId=   obj.get("Fare_ID").toString();
//                    //    Toast.makeText(getApplicationContext(), farePrice + time+ fareId,Toast.LENGTH_SHORT).show();
//                        String t = obj.get("time").toString() + "  min";
//                        tvTime.setText( t);
//
//                        tvPrice.setText("$ " +obj.get("Fare_price").toString());
//
//                        price = obj.get("Fare_price").toString();
//                    }
//
//
//
//                }
//                catch (JSONException e){}
//
//            }
//        }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Route_Detailed.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put(KEY_FROM,from);
//                map.put(KEY_TO,to);
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//
//
//
//    }


    }

