package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.Feedback;
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

public class Payment_Method extends AppCompatActivity {

    TextView fareText,tvSelectPaymentMethod,hiddenTv;
    Toolbar toolbar;
    ImageView hiddeniv;
    Button pay;
    LinearLayout hidden;
    RadioGroup radioGroup;
    String from, to;
    private LinearLayout  card, qr;
    private ProgressDialog loading;
    String fare = "";
    String price;
    boolean doubleBackToExitPressedOnce = false;

    int pos;

    Drawer result;

    SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(2).withName("Home");
    PrimaryDrawerItem payment = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Profile");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");

    SecondaryDrawerItem feedback = new SecondaryDrawerItem().withIdentifier(2).withName("Feedback");

    String userEmail,userName;
    private String url,profile_pic;
    Bitmap[] bitmap1;

    Button confirmPaymentMethodBtn;
    private RadioButton radioButton;
    String checkedBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__method);
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=pref1.getString("UserEmail",null);
        userName=pref1.getString("name",null);
        profile_pic=pref1.getString("pro_pic",null);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Payment Method");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(Payment_Method.this)
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

        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(3).addDrawerItems(EditProfile,home, payment, your_trips,savedQr,feedback,changePassword,  logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem==feedback){

                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }



                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Payment_Method.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem== payment){
//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                           String met= preferences.getString("payment_method_id","");
//


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

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos=radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(pos);
                checkedBox=radioButton.getText().toString();
                //up();

            }
        });
        confirmPaymentMethodBtn=(Button)findViewById(R.id.select_payment_method_btn);
        pos=radioGroup.getCheckedRadioButtonId();
        if(pos!=-1) {
            radioButton = (RadioButton) findViewById(pos);
            checkedBox = radioButton.getText().toString();
        }


        confirmPaymentMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(getApplicationContext(),"Please Select some payment method to proceed",Toast.LENGTH_LONG).show();
                }else {
                    if(checkedBox.equals("Qr Code")){
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("payment_method_id","qr");
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Qr Code Selected",Toast.LENGTH_LONG).show();
                    }else if(checkedBox.equals("Request Qr Card")){
                        Toast.makeText(getApplicationContext(),"Not in operation yet....",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

       /* hiddenTv = (TextView)findViewById(R.id.hiddenTv);
        hiddeniv = (ImageView) findViewById(R.id.hiddenIv);
        hidden = (LinearLayout)findViewById(R.id.hidden);*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
        String p =preferences.getString("payment_method_id","");
        if (p.equals("qr")) {
            radioButton= (RadioButton) findViewById(R.id.radio0);
            radioButton.setChecked(true);

           /* String f = p+","+fare;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
            SharedPreferences.Editor prefEditor = pref.edit();

            prefEditor.putString("qr_string",p);
                prefEditor.apply();
            hiddeniv.setImageDrawable(getDrawable(R.drawable.icon_qr));
            hiddenTv.setText("QR Code");
            hidden.setAlpha(1);*/
        }
        if (p.equals("card")){
          //  hiddeniv.setImageBitmap(getDrawable(R.drawable.qr_card_icon));
           /* Drawable d = getDrawable(R.drawable.qr_card_icon);
            hiddeniv.setImageDrawable(d);
            hiddenTv.setText("QR Card");
            hidden.setAlpha(1);*/


        }
        if (p.isEmpty()){
           /* hidden.setVisibility(View.GONE);*/
        }

        tvSelectPaymentMethod = (TextView)findViewById(R.id.tvSelectPaymentMethod);


        /*tvSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),PaymentMethod_extended.class);
                startActivity(intent);
                finish();
            }
        });*/
//
//        qr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                SharedPreferences.Editor prefEditor = preferences.edit();
//                prefEditor.putString("payment_method_id","qr");
//                String p = preferences.getString("id","")+","+fare;
//                prefEditor.putString("qr_string",p);
//                prefEditor.apply();
//                //  String qr_string = preferences.getString("id","")+","+fare;
//
//                Intent intent1 = new Intent(Payment_Method.this, PaymentMethod_extended.class);
//                // intent1.putExtra("Qr_string",qr_string);
//                startActivity(intent1);
//               // hamza ka code
//
//
//
//
//
//
//
//
//            }
//        });
//
//
//card.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//        SharedPreferences.Editor prefEditor = preferences.edit();
//        prefEditor.putString("payment_method_id","card");
//        prefEditor.apply();
//
//        Intent intent1 = new Intent(Payment_Method.this, PaymentMethod_extended.class);
//        // intent1.putExtra("Qr_string",qr_string);
//        startActivity(intent1);
//
//
//
//    }
//});
//

//PURANA WALA


//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//
//                if (selectedId == sms.getId()) {
//                    Toast.makeText(Payment_Method.this, "Payment by SMS", Toast.LENGTH_SHORT).show();
//
//                } else if (selectedId == card.getId()) {
//                    Toast.makeText(Payment_Method.this, "Payment by Card", Toast.LENGTH_SHORT).show();
//
//                } else if (selectedId == qr.getId()) {
//
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
//                    SharedPreferences.Editor prefEditor = preferences.edit();
//                    prefEditor.putString("payment_method_id","qr");
//                    String p = preferences.getString("id","")+","+fare;
//                    prefEditor.putString("qr_string",p);
//                    prefEditor.apply();
//                  //  String qr_string = preferences.getString("id","")+","+fare;
//
//                    Intent intent1 = new Intent(Payment_Method.this, MainScreen.class);
//                   // intent1.putExtra("Qr_string",qr_string);
//                    startActivity(intent1);
//                    //hamza ka code
//
//
//                } else {
//                    Toast.makeText(Payment_Method.this, "No option Selected", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });


    }
//
//    public void getData() {
//
//        loading = ProgressDialog.show(Payment_Method.this, "", "Please wait..", false, false);
//
//        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//                loading.dismiss();
//
//
//
//                try {
//                    JSONArray array = new JSONArray(response);
//
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = new JSONObject(array.getString(i));
//
//                        fare = object.getString("Fare_price");
//
//                    }
//
//                    fareText.setText(fare);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
//                String message = null;
//                if (error instanceof NetworkError) {
//                    message = "Cannot connect to Internet...Please check your connection!";
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                }
//            }
//
//
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("from", from);
//                map.put("to", to);
//                return map;
//            }
//        };

//        RequestQueue requestQueue = Volley.newRequestQueue(Payment_Method.this);
//        requestQueue.add(request);
//
//    }
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
