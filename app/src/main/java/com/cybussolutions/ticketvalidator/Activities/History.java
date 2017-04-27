package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
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
import com.cybussolutions.ticketvalidator.Profile;
import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;
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
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {
    ListView historyListView;
    Toolbar toolbar;

    CustomHistoryListAdapter adapter;
    private List<HistoryData> HistoryList = new ArrayList<HistoryData>();

    Drawer result;

    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
    SecondaryDrawerItem requestCard = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Request Card");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(3).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(4).withName("Edit Profile");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(5).withName("Logout");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListView = (ListView) findViewById(R.id.history_list);

        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Your Trips");
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
                            Intent intent = new Intent(History.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
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



        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETTRAVEL_HISTORY, new Response.Listener<String>() {
            ProgressDialog loading = ProgressDialog.show(History.this, "", "Please wait...", false, false);
            @Override
            public void onResponse(String response) {
            loading.dismiss();
          //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i=0; i < array.length(); i++) {

                        JSONObject jsonObject = new JSONObject(array.getString(i));

                        HistoryData hd = new HistoryData();
                        hd.setDate(jsonObject.get("route_updated_date").toString());
                        hd.setFare_Price(jsonObject.get("Fare_price").toString());
                        hd.setPersonTravelling(jsonObject.get("person_travling").toString());
                        hd.setRoute_destinition(jsonObject.get("rout_destination").toString());
                       // hd.setTime(jsonObject.get("time").toString());

                        String dateNtime = String.valueOf(jsonObject.get("route_added_date"));
                        String date,time;

                        String[] split = dateNtime.split(" ");
                        date=split[0];
                        time= split[1];
                        hd.setTime(time);
                        hd.setDate(date);
                      //  hd.setRoute_added_date(String.valueOf(jsonObject.get("route_added_date")));
                        hd.setRouteStart(jsonObject.getString("rout_start").toString());

//
//                        b.setBrandName(jsonObject.getString("brand_name"));
//                        b.setId(jsonObject.getString("email"));
//                        b.setUrl(jsonObject.getString("brand_logo"));


                        HistoryList.add(hd);
                        // movieList.add(b);


                    }
                }

                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new CustomHistoryListAdapter(History.this,HistoryList);
                historyListView.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }

                }


        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
               String userid = preferences.getString("id","");
                map.put("userid",userid);
                return map;


            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
