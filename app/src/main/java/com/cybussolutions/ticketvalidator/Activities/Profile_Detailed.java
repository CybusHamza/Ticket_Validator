package com.cybussolutions.ticketvalidator.Activities;

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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.cybussolutions.ticketvalidator.Profile;
import com.squareup.picasso.Target;

public class Profile_Detailed extends AppCompatActivity {

    Button editProfile;
    TextView tvName,tvEmail,tvNum,tvAddress;



    String email,number,name,address;
    ImageView userImg;
    Drawer result;

    SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    PrimaryDrawerItem EditProfile = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Profile");

    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");

    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");

    Toolbar toolbar;
    private String userEmail,userName,profile_pic;
    private String url;
    Bitmap[] bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__detailed);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=pref1.getString("UserEmail",null);
        userName=pref1.getString("name",null);
        profile_pic=pref1.getString("pro_pic",null);


        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(Profile_Detailed.this)
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
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(1).addDrawerItems(EditProfile,home, payment, your_trips,savedQr,feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {




                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Profile_Detailed.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Profile_Detailed.this);
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

        editProfile = (Button)findViewById(R.id.editPro);



        userImg = (ImageView)findViewById(R.id.PDimageView);
        tvEmail = (TextView)findViewById(R.id.tvUserEmail);
        tvName = (TextView)findViewById(R.id.tvUserName);
        tvNum = (TextView)findViewById(R.id.tvuserNumber);
        tvAddress = (TextView)findViewById(R.id.tvuserAddresss);
        userImg.setImageDrawable(getResources().getDrawable(R.drawable.man));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        email = preferences.getString("UserEmail","");
        String pro_pic = preferences.getString("pro_pic","");
        number = preferences.getString("number","");
        name =  preferences.getString("name","");
        address =  preferences.getString("address","");

        if(!pro_pic.equals(""))
        Picasso.with(getApplicationContext()).load("http://epay.cybussolutions.com/epay/" + pro_pic.trim()).placeholder(getResources().getDrawable(R.drawable.man)).into(userImg);


        tvEmail.setText(email);
        tvName.setText(name);
        tvNum.setText(number);
        tvAddress.setText(address);



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile_Detailed.this,Profile.class);
                startActivity(intent);
                finish();


            }
        });


    }
}
