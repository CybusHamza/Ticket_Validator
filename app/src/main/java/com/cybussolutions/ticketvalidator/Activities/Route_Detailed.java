package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class Route_Detailed extends AppCompatActivity {

    Toolbar toolbar;
    String route ,fiar ,distance;
    TextView tvRoute,Tfiar,tvPrice,tvTime;
    String KEY_FROM= "from";
    String KEY_TO = "to";
    String to,from , price;
   // TextView Tdistance;
    Button proceed;
    ProgressBar progressBar;

    Drawer result;

    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
    SecondaryDrawerItem requestCard = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Request Card");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Edit Profile");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detailed);
       // setContentView(R.layout.activity_route_detailed);

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
        //  new DrawerBuilder().withAccountHeader(header).build();
//      //  new SecondaryDrawerItem().withName("Your Trips")
//        new SecondaryDrawerItem().withName("Edit Profile"),
//                new SecondaryDrawerItem().withName("Logout")
        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).addDrawerItems(home, requestCard, your_trips, EditProfile, logout
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
                        if (drawerItem== requestCard){
                            Intent intent=new Intent(getApplicationContext(),RequestCard.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem==EditProfile){
                            Intent intent=new Intent(getApplicationContext(),Profile.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==home){
                            Intent intent=new Intent(getApplicationContext(),MainScreen.class);
                            startActivity(intent);
                            finish();

                        }

                        return true;

                    }

                }).build();



        tvRoute = (TextView)findViewById(R.id.tvRoute);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTime = (TextView)findViewById(R.id.tvTime);
     //   getData();
        Intent intent= getIntent();
        to = intent.getStringExtra("to");
        from = intent.getStringExtra("from");
        tvRoute.setText("From " +from + " To " + to );

        fiar = intent.getStringExtra("route_fiar");
        distance = intent.getStringExtra("route_distance");

        final ProgressDialog progressDialog = ProgressDialog.show(Route_Detailed.this, "Please wait...", "Checking Credentails ...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                     Toast.makeText(Route_Detailed.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                String t = null;
                try{
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i< array.length(); i++){

                        JSONObject obj = new JSONObject(array.getString(i));
                        //  String fareId=   obj.get("Fare_ID").toString();
                        //    Toast.makeText(getApplicationContext(), farePrice + time+ fareId,Toast.LENGTH_SHORT).show();
                       t = obj.get("time").toString() + "  min";




                        price = obj.get("Fare_price").toString();
                    }

                    tvTime.setText(t);
                    tvPrice.setText("$ " + price);
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








        proceed = (Button) findViewById(R.id.pay);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Route_Detailed.this);
                String PaymentMethod =preferences.getString("payment_method_id","");
                if (PaymentMethod.matches("qr")){

                    Intent intent = new Intent(Route_Detailed.this, Qr_Activity.class);
                    //intent.putExtra("fair", fiar);
                  //  intent.putExtra("price",price);
                    startActivity(intent);
                    finish();



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

