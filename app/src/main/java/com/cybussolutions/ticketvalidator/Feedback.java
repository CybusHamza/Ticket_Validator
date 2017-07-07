package com.cybussolutions.ticketvalidator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Activities.Dashboard;
import com.cybussolutions.ticketvalidator.Activities.History;
import com.cybussolutions.ticketvalidator.Activities.Login_Activity;
import com.cybussolutions.ticketvalidator.Activities.Payment_Method;
import com.cybussolutions.ticketvalidator.Activities.Profile_Detailed;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {


    boolean doubleBackToExitPressedOnce = false;
    EditText etEmail,etFeedback;
    RatingBar rb;

    Button sendFeedback;

    Drawer result;

    SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Profile");

    PrimaryDrawerItem feedback = new PrimaryDrawerItem().withIdentifier(1).withName("Feedback");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");

    Toolbar toolbar;
    String email,feedBackt;

    String serviceString;
    private String userEmail,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Feedback");
        rb = (RatingBar)findViewById(R.id.rating);
        etEmail = (EditText)findViewById(R.id.email);
        etFeedback = (EditText)findViewById(R.id.feedback);
        setSupportActionBar(toolbar);

        etEmail.setFocusable(false);
        etEmail.setClickable(false);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);

        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=pref1.getString("UserEmail",null);
        userName=pref1.getString("name",null);

        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.drawable.bg_ep_slider_header)
                .addProfiles(new ProfileDrawerItem().withName(userName).withEmail(userEmail))
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
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(5).addDrawerItems(EditProfile,home, payment, your_trips,feedback, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {




                        if (drawerItem== your_trips){
                            Intent intent = new Intent(getApplicationContext(), History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Feedback.this);
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


sendFeedback = (Button)findViewById(R.id.buttonFeedback);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        email=  preferences.getString("UserEmail","");
        etEmail.setText(email);




        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOvStars = rb.getNumStars();
                float rating = rb.getRating();
               String r = String.valueOf(rating);
               // int r = Integer.valueOf(rating);
           //  int ab = Integer.parseInt(rb.getRating());


                email = etEmail.getText().toString();
               // feedBackt = etFeedback.getText().toString();
                String feedback=etFeedback.getText().toString();
                if(feedback.equals("")){
                    Toast.makeText(getApplicationContext(),"You cant proceed without giving us feedback",Toast.LENGTH_SHORT).show();
                    return;
                }


                switch (r)
                {
                    case "0.0":

                        Toast.makeText(getApplicationContext(),"You cant proceed without Rating our app",Toast.LENGTH_SHORT).show();

                       // serviceString = "Rating was poor";
                        break;
                    case "1.0":
                        serviceString = "Rating was poor";
                        feedBackt = etFeedback.getText().toString() + "\n" +serviceString;
                        sendEmail();
                        break;
                    case "2.0":
                        serviceString = "Rating was fair";
                        feedBackt = etFeedback.getText().toString() + "\n" +serviceString;
                        sendEmail();
                        break;
                    case "3.0":
                        serviceString = "Rating was normal";
                        feedBackt = etFeedback.getText().toString() + "\n" +serviceString;
                        sendEmail();
                        break;
                    case "4.0":
                        serviceString = "Rating was good";
                        feedBackt = etFeedback.getText().toString() + "\n" +serviceString;
                        sendEmail();
                        break;
                    case "5.0":
                        serviceString = "Rating was excellent";
                        feedBackt = etFeedback.getText().toString() + "\n" +serviceString;
                        sendEmail();
                        break;



                }



            }
        });


    }

    public void sendEmail(){

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.SENDFEEDBACK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // loading.dismiss();

                if ((response.trim().toString().equals("Mail Sent Successfully"))) {
                    Toast.makeText(getApplicationContext(),"Feedback Sent Successfully",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Feedback Not Sent",Toast.LENGTH_LONG).show();

                }
            }

        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   loading.dismiss();
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
                map.put("cust_email", email);
                map.put("feedback_body",feedBackt);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
