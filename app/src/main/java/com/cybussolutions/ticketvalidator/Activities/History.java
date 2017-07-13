package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.cybussolutions.ticketvalidator.Feedback;
import com.cybussolutions.ticketvalidator.Network.End_Points;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {
    String routeId;
    boolean doubleBackToExitPressedOnce = false;
    static  int count;

    ListView historyListView;
    Toolbar toolbar;

    CustomHistoryListAdapter adapter;
    Drawer result;
    SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    PrimaryDrawerItem your_trips = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Your Trips");
    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Profile");
    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");
    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    ArrayList<String> routeIds = new ArrayList<String>();
    ArrayList<String> userIds = new ArrayList<String>();
    ArrayList<String> personTraveling = new ArrayList<String>();
    ArrayList<String> transactionIds = new ArrayList<String>();
    ArrayList<String> dateAdded = new ArrayList<String>();
    ArrayList<String> dateModified = new ArrayList<String>();
    ArrayList<String> stringArrayList5 = new ArrayList<>();
    EditText searchET;
    String routeIdLive, transactionIdLive, personTravelingLive, dateAddedLive, dateModifiedLive;
    String userEmail, userName,profile_pic;
    private List<HistoryData> HistoryList = new ArrayList<HistoryData>();
    private DBManager dbManager;
    String route_id;

    private String url;
    Bitmap[] bitmap1;

    HashMap<Integer,ArrayList<String>> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        count=0;
        historyListView = (ListView) findViewById(R.id.history_list);
       /* historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // route_id = dbManager.fetch_route_id());
                Toast.makeText(getApplicationContext(),historyListView.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }
        });*/


        //id + "," + fare+","+fareType;route_id+","+confirmNum+","+"2"+","+from+","+to+","+number_of_persons+","+name+","+number;

        searchET = (EditText) findViewById(R.id.searchData);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = preferences.getString("UserEmail", null);
        userName = preferences.getString("name", null);
        profile_pic=preferences.getString("pro_pic",null);


        toolbar = (Toolbar) findViewById(R.id.app_bar_history);
        toolbar.setTitle("Your Trips");


        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(History.this)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmap1[0] =bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        Log.e("here","onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("here","onPrepareLoad");
                    }
                });

        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.drawable.bg_ep_slider_header)
                .addProfiles(new ProfileDrawerItem().withName(userName).withEmail(userEmail).withIcon(bitmap1[0]))
                .withProfileImagesVisible(true)
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
        result = new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(4).addDrawerItems(EditProfile,home, payment, your_trips, feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem == your_trips) {
                            Intent intent = new Intent(History.this, History.class);
                            startActivity(intent);
                        }
                        if (drawerItem == logout) {

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem == payment) {
                            Intent intent = new Intent(getApplicationContext(), Payment_Method.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem == EditProfile) {
                            Intent intent = new Intent(getApplicationContext(), Profile_Detailed.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem == home) {
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem == feedback) {

                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==changePassword){
                            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                            startActivity(intent);
                            finish();
                        }

                        return true;

                    }

                }).build();
        if (isNetworkAvailable()) {
            dbManager = new DBManager(History.this);
            dbManager.open();
           // transactionIds = dbManager.fetch_history_trans_id_for_live();
          //  data = dbManager.fetch_trans();
            data=dbManager.fetch_trans_id_live();
            if (data.size() > 0) {

                insertIntoTravelHistory(data.get(count));

            } else {
                getHistory();
                //Toast.makeText(History.this, "No Data To Sycn", Toast.LENGTH_SHORT).show();
            }
           /* for (int i = 0; i < transactionIds.size(); i++) {
                transactionIdLive = transactionIds.get(i);
                routeIdLive = dbManager.fetch_route_id_for_live(transactionIds.get(i));
                personTravelingLive = dbManager.fetch_person_traveling_for_live(transactionIdLive);
                dateAddedLive = dbManager.fetch_date_added_for_live(transactionIdLive);
                insertIntoTravelHistory(transactionIdLive);
                //  Toast.makeText(getApplicationContext(),transactionIds.get(i).toString(),Toast.LENGTH_LONG).show();
            }*/


        } else {
            getHistory();
            // Toast.makeText(getApplicationContext(),"You are not connected to internet, Plz check your network connection",Toast.LENGTH_LONG).show();


        }


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String nameToSearch = searchET.getText().toString();
                ArrayList<HistoryData> filteredLeaves = new ArrayList<HistoryData>();

                for (HistoryData data : HistoryList) {
                    if (data.getRouteStart().toLowerCase().contains(nameToSearch.toLowerCase()) || data.getDate().toLowerCase().equalsIgnoreCase(nameToSearch.toLowerCase())) {
                        filteredLeaves.add(data);
                    }


                }
                /*leaveDatas.clear();
                leaveDatas.addAll(filteredLeaves);
                leaves_adapter.notifyDataSetChanged();*/
                adapter = new CustomHistoryListAdapter(History.this, filteredLeaves);
                historyListView.setAdapter(adapter);

            }                //     listView.setAdapter(leaves_adapter);


            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.mymenu,menu);
//        MenuItem item = menu.findItem(R.id.custom_search_bar);
//
//        SearchView sv = (SearchView)item.getActionView();
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//
//
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//
//
//
//                return true;
//            }
//        });
//
//
//
//
//
//        return super.onCreateOptionsMenu(menu);
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getHistory() {
        final ProgressDialog loading = ProgressDialog.show(History.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETTRAVEL_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                try {
                    JSONArray array = new JSONArray(response);
                    String s = Integer.toString(array.length());

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    String userid = preferences.getString("id", "");
                    editor.putString("TotalTrips", s);
                    editor.apply();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = new JSONObject(array.getString(i));

                        HistoryData hd = new HistoryData();
                        hd.setDate(jsonObject.get("date_added").toString());
                        hd.setFare_Price(jsonObject.get("Fare_price").toString());
                        hd.setPersonTravelling(jsonObject.get("person_travling").toString());
                        hd.setRoute_destinition(jsonObject.get("rout_destination").toString());
                        hd.setTrans_id(jsonObject.get("trans_id").toString());
                        // hd.setTime(jsonObject.get("time").toString());

                        String dateNtime = String.valueOf(jsonObject.get("date_added"));
                        String date, time;

                        String[] split = dateNtime.split(" ");
                        date = split[0];
                        time = split[1];
                        hd.setTime(time);
                        hd.setDate(date);
                        //  hd.setRoute_added_date(String.valueOf(jsonObject.get("route_added_date")));

                        hd.setRouteStart(jsonObject.getString("rout_start").toString());

                     //   dbManager.delete_both_history_tables();

                    //    dbManager.insert_into_history_travel(jsonObject.get("route_id").toString(),jsonObject.get("trans_id").toString(),userid,jsonObject.get("person_travling").toString(),jsonObject.get("route_updated_date").toString(),jsonObject.get("route_updated_date").toString());



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
                adapter = new CustomHistoryListAdapter(History.this, HistoryList);
                historyListView.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dbManager = new DBManager(History.this);
                        dbManager.open();
                        loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
                            String userid = preferences.getString("id", "");

                            stringArrayList5 = dbManager.fetch_history_trans_id(userid);
                            String s = Integer.toString(stringArrayList5.size());

                            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences1.edit();
                            editor.putString("TotalTrips", s);
                            editor.apply();


                            if (stringArrayList5.size() > 0) {
                                for (int i = 0; i < stringArrayList5.size(); i++) {
                                    HistoryData hd = new HistoryData();
                                    String stringArrayList = dbManager.fetch_history_table(userid, stringArrayList5.get(i));
                                    String stringArrayList1 = dbManager.fetch_history_table_date(userid,stringArrayList5.get(i));
                                    routeId = dbManager.fetch_route_id_for_history(userid, stringArrayList5.get(i));
                                    String stringArrayList2 = dbManager.h_fetch_route_table_start(routeId);
                                    String stringArrayList3 = dbManager.h_fetch_route_table_dest(routeId);
                                    String stringArrayList4 = dbManager.h_fetch_route_fare_price(routeId);
                                    hd.setDate(stringArrayList1);
                                    hd.setFare_Price(stringArrayList4);
                                    hd.setPersonTravelling(stringArrayList);
                                    hd.setRoute_destinition(stringArrayList3);
                                    hd.setTrans_id(stringArrayList5.get(i));
                                    // hd.setTime(jsonObject.get("time").toString());

                                        /* String dateNtime = String.valueOf("");
                                        String date,time;

                                        String[] split = dateNtime.split(" ");
                                        date=split[0];
                                     time= split[1];*/
                                    hd.setTime("");
                                    //hd.setDate("");
                                    //  hd.setRoute_added_date(String.valueOf(jsonObject.get("route_added_date")));
                                    hd.setRouteStart(stringArrayList2);
                                    HistoryList.add(hd);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "No previous history found", Toast.LENGTH_LONG).show();
                            }

                            adapter = new CustomHistoryListAdapter(History.this, HistoryList);
                            historyListView.setAdapter(adapter);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
                String userid = preferences.getString("id", "");
                map.put("userid", userid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void insertIntoTravelHistory(final  ArrayList<String> transId) {
        final ProgressDialog loading = ProgressDialog.show(History.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.INSERT_TRAVEL_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                dbManager = new DBManager(History.this);
                dbManager.open();
                int size = data.size()-1;
                if(size >= count)
                {   ++count;
                    insertIntoTravelHistory(data.get(count));
                    dbManager.delete_history_data_local(transId.get(0));
                    dbManager.delete_history_data_live(transId.get(0));
                }
                getHistory();

                // dbManager.delete_both_history_tables();
                //getHistory();
               // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dbManager = new DBManager(History.this);
                        dbManager.open();
                        loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(History.this);
                String userid = preferences.getString("id", "");
                map.put("user_id", userid);
                map.put("route_id", transId.get(1));
                map.put("trans_id", transId.get(2));
                map.put("person_travling", transId.get(4));
                map.put("date_added", transId.get(5));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
