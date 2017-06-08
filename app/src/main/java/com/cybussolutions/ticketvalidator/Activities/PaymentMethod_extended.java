package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PaymentMethod_extended extends AppCompatActivity {
    TextView tvSelectedMethod, tvNonSelected;
    ImageView selectedImg, imgNonselected;
    LinearLayout qrCode,qrCard;
    Toolbar toolbar;
    Drawer result;

    String userEmail,userName;

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
        setContentView(R.layout.activity_payment_method_extended);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=pref1.getString("UserEmail",null);
        userName=pref1.getString("name",null);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

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
        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).addDrawerItems(home, payment, your_trips, EditProfile, logout,feedback
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem== your_trips){
                            Intent intent = new Intent(PaymentMethod_extended.this, History.class);
                            startActivity(intent);
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentMethod_extended.this);
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
                        if (drawerItem==feedback){

                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }

                        return true;

                    }

                }).build();






//        tvSelectedMethod = (TextView)findViewById(R.id.tvSelectedMethod);
//        selectedImg = (ImageView)findViewById(R.id.imgResource);
        tvNonSelected = (TextView) findViewById(R.id.nonSelectedtv);
       // imgNonselected = (ImageView) findViewById(R.id.nonSelectedIcon);

        qrCode = (LinearLayout)findViewById(R.id.qrcode);
        qrCard = (LinearLayout)findViewById(R.id.qrCard);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentMethod_extended.this);
        String pref=preferences.getString("payment_method_id","");
        if (pref.equals("qr")){

            qrCode.setVisibility(View.GONE);
        }
        if (pref.equals("card")){

            qrCard.setVisibility(View.GONE);
        }

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentMethod_extended.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("payment_method_id","qr");
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),Payment_Method.class);
                startActivity(intent);
                finish();

            }
        });

        qrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentMethod_extended.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("payment_method_id","card");
                editor.apply();
                Toast.makeText(getApplicationContext(),"Your request for the card is forwarded to our admin. And you will get your card as soon as possible",Toast.LENGTH_LONG).show();


                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();




            }
        });

//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PaymentMethod_extended.this);
//        String s= preferences.getString("payment_method_id","");
//     if (s.equals("card")){
//        selectedImg.setImageDrawable(getDrawable(R.drawable.qr_card_icon));
//         tvSelectedMethod.setText("QR CARD");
//
//         tvNonSelected.setText("QR CODE");
//         imgNonselected.setImageDrawable(getDrawable(R.drawable.icon_qr));
//
//
//     }
//     if (s.equals("qr")){}
//
//        selectedImg.setImageDrawable(getDrawable(R.drawable.icon_qr));
//        tvSelectedMethod.setText("QR CODE");
//
//        tvNonSelected.setText("QR CARD");
//        imgNonselected.setImageDrawable(getDrawable(R.drawable.qr_card_icon));
//    }
    }
}
