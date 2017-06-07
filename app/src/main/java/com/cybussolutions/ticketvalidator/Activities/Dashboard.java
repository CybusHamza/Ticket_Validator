package com.cybussolutions.ticketvalidator.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class Dashboard extends AppCompatActivity {

    Toolbar toolbar;
    Button btnStartTrip;
    TextView tvTotalTrips;
    String totalTrips;

    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;


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
    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar)findViewById(R.id.app_bar_dashboard);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        if(isNetworkAvailable()) {
            startService(new Intent(this, HelloService.class));
        }else {
           // Toast.makeText(getApplicationContext(),"You are not connected to internet, Plz check your network connection",Toast.LENGTH_LONG).show();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        totalTrips = preferences.getString("TotalTrips","");


        tvTotalTrips = (TextView)findViewById(R.id.tvTotalTrips);
        tvTotalTrips.setText(totalTrips);
        btnStartTrip = (Button)findViewById(R.id.btnStartTrip);
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Dashboard.this,MainScreen.class);
                startActivity(intent);
                finish();

            }
        });

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
                .withToolbar(toolbar).withDrawerWidthDp(250).addDrawerItems(home, payment, your_trips, EditProfile, logout,feedback
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Dashboard.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
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

                        return true;

                    }

                }).build();


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
