package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private DBManager dbManager;

    Toolbar toolbar;
    ArrayList<String> from_routes = new ArrayList<>();
    ArrayList<String> to_routes ;
    TextView txt;

    Spinner from,to;
    Button procedd;
    Button back;
    String fromLocation;
    private ProgressDialog loading;


    Drawer result;

    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Profile");

SecondaryDrawerItem feedback = new SecondaryDrawerItem().withIdentifier(6).withName("Feedback");

    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");
    ArrayList<String> stringArrayList,stringArrayList1;
    String route_id,route_time;

    String userEmail,userName,profile_pic;
    private String url;
    Bitmap[] bitmap1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        stringArrayList=new ArrayList<String>();
        stringArrayList1=new ArrayList<String>();

        dbManager = new DBManager(MainScreen.this);
        dbManager.open();

        stringArrayList=dbManager.fetch_route_start();


//        for (int i=2;i<stringArrayList.size()/3;i=i+2)
//        to_routes.add(stringArrayList.get(i));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Select Destination");



        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=preferences.getString("UserEmail",null);
        userName=preferences.getString("name",null);
        profile_pic=preferences.getString("pro_pic",null);

        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(MainScreen.this)
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
        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(2).addDrawerItems( EditProfile,home, payment, your_trips,savedQr,feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        if (drawerItem== your_trips){
                            Intent intent = new Intent(MainScreen.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
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
                            Intent intent=new Intent(getApplicationContext(),Profile_Detailed.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==home){
                            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==feedback){
                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==changePassword){
                            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==savedQr){
                            Intent intent = new Intent(getApplicationContext(), SaveQrScreen.class);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }

                }).build();

        txt = (TextView) findViewById(R.id.textView3);
        from = (Spinner) findViewById(R.id.from_spinner);
        to = (Spinner) findViewById(R.id.to_spinner);
        procedd = (Button) findViewById(R.id.proceed);
        back = (Button) findViewById(R.id.back);

       // getData();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (MainScreen.this, R.layout.spinner_item,stringArrayList);

        //               (MainScreen.this, android.R.layout.simple_spinner_item,stringArrayList);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        from.setAdapter(dataAdapter);

        from.setOnItemSelectedListener(new CustomOnItemSelectedListener_from());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainScreen.this,Dashboard.class);
                finish();
                startActivity(intent);
            }
        });
        procedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check1=from.getSelectedItem().toString();

                if(check1!=""&& check1!="<Select>") {
                    String check2=to.getSelectedItem().toString();
                    if(check2!=""&& check2!="<Select>") {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("FROM", from.getSelectedItem().toString());
                        editor.putString("TO", to.getSelectedItem().toString());
                        editor.apply();

                        route_id = dbManager.fetch_route_id(from.getSelectedItem().toString(), to.getSelectedItem().toString());
                        route_time = dbManager.fetch_route_elapsed_time(from.getSelectedItem().toString(), to.getSelectedItem().toString());
                        Intent intent = new Intent(MainScreen.this, Route_Detailed.class);
                        intent.putExtra("route_id", route_id.toString());
                        intent.putExtra("from", from.getSelectedItem().toString());
                        intent.putExtra("to", to.getSelectedItem().toString());
                        intent.putExtra("time", route_time);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"Please select your destination to proceed",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please select your Start Point to proceed",Toast.LENGTH_LONG).show();
                }
            }
        });
        // Defined Array values to show in ListView
    }

    public class CustomOnItemSelectedListener_from implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {
            if(pos!=0) {
                fromLocation = stringArrayList.get(pos);
                stringArrayList1 = dbManager.fetch_route_table(fromLocation);
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>
                        (MainScreen.this, R.layout.spinner_item, stringArrayList1);

                dataAdapter1.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);

                to.setAdapter(dataAdapter1);

                //  getRoutes();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    private void getRoutes() {

        loading = ProgressDialog.show(MainScreen.this, "", "Please wait...", false, false);


        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETROUTES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();
                try {
                    JSONArray array = new JSONArray(response);

                    to_routes = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));


                        if(!(to_routes.contains(object.getString("rout_destination"))))
                        {
//                            to_routes.add(object.getString("rout_destination"));
                        }
                    }

                    /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                            (MainScreen.this, android.R.layout.simple_spinner_item,to_routes);

                    dataAdapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);

                    to.setAdapter(dataAdapter);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("from",fromLocation);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainScreen.this);
        requestQueue.add(request);

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mymenu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    // handle button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.list_btn) {
//            Intent intent = new Intent(this, History.class);
//            startActivity(intent);
//
//
//        }
//        if (id== R.id.logoutBtn){
//
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.apply();
//
//            Intent intent=new Intent(this,Login_Activity.class);
//            startActivity(intent);
//            finish();
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getData() {

        loading = ProgressDialog.show(MainScreen.this, "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETDATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));

                        if(!(from_routes.contains(object.getString("rout_start"))))
                        {
                           // from_routes.add(object.getString("rout_start"));

                        }

                    }

                   /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                            (MainScreen.this, android.R.layout.simple_spinner_item,from_routes);

                    dataAdapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);

                    from.setAdapter(dataAdapter);
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainScreen.this);
        requestQueue.add(request);

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
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


