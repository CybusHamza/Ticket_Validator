package com.cybussolutions.ticketvalidator.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.cybussolutions.ticketvalidator.R;
import com.interswitchng.sdk.auth.Passport;
import com.interswitchng.sdk.model.RequestOptions;
import com.interswitchng.sdk.payment.IswCallback;
import com.interswitchng.sdk.payment.Payment;
import com.interswitchng.sdk.payment.android.inapp.PayWithCard;
import com.interswitchng.sdk.payment.model.PurchaseResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Dashboard extends AppCompatActivity {
    ArrayList<String> stringArrayList5 = new ArrayList<>();

    boolean doubleBackToExitPressedOnce = false;

    AlertDialog myalertdialog;
    Toolbar toolbar;
    Button btnStartTrip, btnRecharge;
    TextView tvTotalTrips, tvMWBalance;
    String totalTrips, MWBalance;

    String amount, message, trans_ref, trans_identifier, cardtype;
    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;


    Drawer result;
    String userEmail, userName, customer_id, customer_total_balance, profile_pic;

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem().withIdentifier(2).withName("Profile");
    PrimaryDrawerItem home = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");
    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");
    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");
    Bitmap[] bitmap1;
    private DBManager dbManager;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dbManager = new DBManager(Dashboard.this);
        dbManager.open();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customer_id = preferences.getString("id", null);
        toolbar = (Toolbar) findViewById(R.id.app_bar_dashboard);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getHistory();
        if (isNetworkAvailable()) {
            startService(new Intent(this, HelloService.class));
        } else {

            // Toast.makeText(getApplicationContext(),"You are not connected to internet, Plz check your network connection",Toast.LENGTH_LONG).show();
        }


        totalTrips = preferences.getString("TotalTrips", "0");
        userEmail = preferences.getString("UserEmail", null);
        userName = preferences.getString("name", null);
        customer_id = preferences.getString("id", null);
        profile_pic = preferences.getString("pro_pic", null);


        tvTotalTrips = (TextView) findViewById(R.id.tvTotalTrips);
        tvMWBalance = (TextView) findViewById(R.id.tvMWBalance);


        btnStartTrip = (Button) findViewById(R.id.btnStartTrip);
        btnRecharge = (Button) findViewById(R.id.btnRecharge);
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Dashboard.this, MainScreen.class);
                startActivity(intent);
                finish();

            }
        });
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.balance_popup, null);
                    builder.setView(dialogView);

                    final EditText balance = (EditText) dialogView.findViewById(R.id.balance);
                    Button next = (Button) dialogView.findViewById(R.id.next);
                    Button Cancel = (Button) dialogView.findViewById(R.id.Cancel);

                    myalertdialog = builder.create();
                    myalertdialog.show();

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String strBalance =  balance.getText().toString();

                            if(strBalance.equals("") || Integer.valueOf(strBalance) <= 10)
                            {
                                Toast.makeText(Dashboard.this, "The Enter Amount is Invalid", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                myalertdialog.dismiss();
                                balanceEpay(strBalance);
                            }
                        }
                    });

                    Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            myalertdialog.dismiss();
                        }
                    });



                } else {
                    Toast.makeText(getApplicationContext(), "You are not connected to internet, Plz check your network connection", Toast.LENGTH_LONG).show();
                }

            }
        });
        bitmap1 = new Bitmap[1];
        url = "http://epay.cybussolutions.com/epay/" + profile_pic;

        Picasso.with(Dashboard.this)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmap1[0] = bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        Log.e("here", "onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("here", "onPrepareLoad");
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
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(2).addDrawerItems(EditProfile, home, payment, your_trips,savedQr, feedback, changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem == your_trips) {
                            Intent intent = new Intent(Dashboard.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem == logout) {

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
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
                        if (drawerItem == changePassword) {
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

        customer_total_balance = dbManager.fetch_customer_balance(customer_id);
        if (customer_total_balance != null) {
            tvMWBalance.setText("₦" + customer_total_balance);
        } else {
            tvMWBalance.setText("₦");
        }

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                customer_total_balance = dbManager.fetch_customer_balance(customer_id);
                if (customer_total_balance != null) {
                    tvMWBalance.setText("₦" + customer_total_balance);
                } else {
                    tvMWBalance.setText("₦");
                }
            }
        }.start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void  balanceEpay(String balance)
    {
        Passport.overrideApiBase(Passport.SANDBOX_API_BASE);
        Payment.overrideApiBase(Payment.SANDBOX_API_BASE);

        RequestOptions options = RequestOptions.builder()
                .setClientId("IKIA9614B82064D632E9B6418DF358A6A4AEA84D7218")
                .setClientSecret("XCTiBtLy1G9chAnyg0z3BcaFK4cVpwDg/GTw2EmjTZ8=")
                .build();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);

        PayWithCard pay = new PayWithCard(Dashboard.this, preferences.getString("first_name",""), "Recharge Your Account", balance, "NGN", options,
                new IswCallback<PurchaseResponse>() {
                    @Override
                    public void onError(Exception error) {
                        // Handle error.
                        // Payment not successful.


                        Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSuccess(PurchaseResponse response) {

                        cardtype = response.getCardType();
                        trans_identifier = response.getTransactionIdentifier();
                        trans_ref = response.getTransactionRef();
                        message = response.getMessage();
                        amount = response.getAmount();

                        sendTrans();
                        //   Toast.makeText(Dashboard.this, response.getAmount(), Toast.LENGTH_SHORT).show();
                    }
                });
        pay.start();

    }
    private void getHistory() {
        // final ProgressDialog loading = ProgressDialog.show(Dashboard.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETTRAVEL_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    loading.dismiss();
                //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                try {
                    JSONArray array = new JSONArray(response);
                    String s = Integer.toString(array.length());

                    tvTotalTrips.setText(s);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("TotalTrips", s);
                    editor.apply();


                }

                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dbManager = new DBManager(Dashboard.this);
                        dbManager.open();
                        //loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";

                            //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
                            String userid = preferences.getString("id", "");
                            stringArrayList5 = dbManager.fetch_history_trans_id(customer_id);
                            String s = Integer.toString(stringArrayList5.size());
                            tvTotalTrips.setText(s);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("TotalTrips", s);
                            editor.apply();


                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
                String userid = preferences.getString("id", "");
                map.put("userid", userid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void sendTrans() {
        final ProgressDialog loading = ProgressDialog.show(Dashboard.this, "", "Please wait...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.TRANS_LOGS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Balance Updated Succesfully", Toast.LENGTH_LONG).show();
                dbManager.update_customer_balance(customer_id,response.trim());
                tvMWBalance.setText("₦ " + response.trim());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();

                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            new SweetAlertDialog(Dashboard.this, SweetAlertDialog.ERROR_TYPE)
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

                            new SweetAlertDialog(Dashboard.this, SweetAlertDialog.ERROR_TYPE)
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
                String userid = preferences.getString("id", "");
                map.put("userid", userid);
                map.put("amount", amount);
                map.put("card_type", cardtype);
                map.put("trans_identifier", trans_identifier);
                map.put("trans_ref_num", trans_ref);
                map.put("message", message);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void oldPass() {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.custom_dialog_old_password);
        dialog.setTitle("Change Password");
        final EditText inputOldPass = (EditText) dialog.findViewById(R.id.old_pass_et);
        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputOldPass.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Old Password", Toast.LENGTH_LONG).show();
                } else {
                    newPass();
                }
            }
        });


        dialog.show();
    }

    public void newPass() {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.custom_dialog_new_password);
        dialog.setTitle("Change Password");
        EditText inputnewPass = (EditText) dialog.findViewById(R.id.new_pass_et);
        EditText inputconfirmPass = (EditText) dialog.findViewById(R.id.confirm_pass_et);
        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);

        dialog.show();
    }
}
