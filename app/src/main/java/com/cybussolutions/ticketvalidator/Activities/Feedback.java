package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {



    EditText etEmail,etFeedback;
    RatingBar rb;

    Button sendFeedback;

    Drawer result;

    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Edit Profile");

    SecondaryDrawerItem feedback = new SecondaryDrawerItem().withIdentifier(6).withName("Feedback");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");

Toolbar toolbar;
String email,feedBackt;


String serviceString;
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
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);


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
                            Intent intent = new Intent(getApplicationContext(), History.class);
                            startActivity(intent);
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

                if ((response.equals(""))) {
                    Toast.makeText(getApplicationContext(),"Email Sent",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Email Not Sent", Toast.LENGTH_SHORT).show();
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



}
