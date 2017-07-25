package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangePassword extends AppCompatActivity {

    Drawer result;
    Toolbar toolbar;
    String userEmail,userName,userId,profile_pic;
    private String url;
    Bitmap[] bitmap1;

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem().withIdentifier(2).withName("Profile");
    SecondaryDrawerItem home = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");
    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");
    PrimaryDrawerItem changePassword = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Change Password");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");


    EditText etOldPass,etNewPass,etConfirmPass;
    TextView tvOldPass,tvNewPass,tvConfirmPass;
    Button btnOk,btnSaveNewPass;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=preferences.getString("UserEmail",null);
        userName=preferences.getString("name",null);
        userId = preferences.getString("id", "");
        profile_pic=preferences.getString("pro_pic",null);

        toolbar = (Toolbar)findViewById(R.id.app_bar_dashboard);
        toolbar.setTitle("Change Password");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        tvOldPass=(TextView)findViewById(R.id.labeloldPass);
        tvNewPass=(TextView)findViewById(R.id.labenewPass);
        tvConfirmPass=(TextView)findViewById(R.id.labeconfirmPass);
        etOldPass= (EditText) findViewById(R.id.old_pass_et);
        etNewPass= (EditText) findViewById(R.id.new_pass_et);
        etConfirmPass= (EditText) findViewById(R.id.confirm_pass_et);
        btnOk= (Button) findViewById(R.id.btn_ok);
        btnSaveNewPass= (Button) findViewById(R.id.btn_save_new_pass);
        etOldPass.setVisibility(View.VISIBLE);
        tvOldPass.setVisibility(View.VISIBLE);
        etNewPass.setVisibility(View.INVISIBLE);
        tvNewPass.setVisibility(View.INVISIBLE);
        etConfirmPass.setVisibility(View.INVISIBLE);
        tvConfirmPass.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        btnSaveNewPass.setVisibility(View.INVISIBLE);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOldPass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Old Password",Toast.LENGTH_LONG).show();
                }else {
                    verifyOldPassword();
                }
            }
        });
        btnSaveNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass=etNewPass.getText().toString();
                String confirmPass=etConfirmPass.getText().toString();
                if(newPass.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Some Password",Toast.LENGTH_LONG).show();
                }else if(newPass.length()<8){
                    Toast.makeText(getApplicationContext(),"Password should have minimum of 8 characters",Toast.LENGTH_LONG).show();
                }else if(!newPass.equals(confirmPass)){
                    Toast.makeText(getApplicationContext(),"Password Mismatches",Toast.LENGTH_LONG).show();
                }else {
                    changePassword();
                }
            }
        });
        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(ChangePassword.this)
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
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(7).addDrawerItems(EditProfile,home, payment, your_trips,savedQr,feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem== your_trips){
                            Intent intent = new Intent(ChangePassword.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
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
    }

    public void verifyOldPassword() {
        final ProgressDialog loading = ProgressDialog.show(ChangePassword.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.VERIFY_OLD_PASS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                if(response.trim().equals("1")){
                    etNewPass.setVisibility(View.VISIBLE);
                    tvNewPass.setVisibility(View.VISIBLE);
                    etConfirmPass.setVisibility(View.VISIBLE);
                    tvConfirmPass.setVisibility(View.VISIBLE);
                    etOldPass.setVisibility(View.INVISIBLE);
                    tvOldPass.setVisibility(View.INVISIBLE);
                    btnOk.setVisibility(View.INVISIBLE);
                    btnSaveNewPass.setVisibility(View.VISIBLE);
                   // changePassword();
                }else{
                    Toast.makeText(getApplicationContext(), "Your Password is Incorrect", Toast.LENGTH_LONG).show();
                }

                // dbManager.delete_both_history_tables();
                //getHistory();
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                map.put("old_password", etOldPass.getText().toString());
                map.put("user_id", userId);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void changePassword() {
        final ProgressDialog loading = ProgressDialog.show(ChangePassword.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                if(response.trim().equals("1")){
                    dbManager = new DBManager(ChangePassword.this);
                    dbManager.open();
                    dbManager.update_local_password(userId,etNewPass.getText().toString());
                    new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setConfirmText("OK").setContentText("Password Changed Successfully")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }else {
                    new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Password Not Changed")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }


                // dbManager.delete_both_history_tables();
                //getHistory();
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("No Internet Connection")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else if (error instanceof TimeoutError) {

                            new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                            //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("new_password", etNewPass.getText().toString());
                map.put("user_id", userId);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
