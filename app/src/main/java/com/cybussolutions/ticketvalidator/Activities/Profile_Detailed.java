package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cybussolutions.ticketvalidator.R;
import com.squareup.picasso.Picasso;

public class Profile_Detailed extends AppCompatActivity {

    Button editProfile;
    TextView tvName,tvEmail,tvNum;



    String email,number,name;
    ImageView userImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__detailed);



        editProfile = (Button)findViewById(R.id.editPro);



        userImg = (ImageView)findViewById(R.id.PDimageView);
        tvEmail = (TextView)findViewById(R.id.tvUserEmail);
        tvName = (TextView)findViewById(R.id.tvUserName);
        tvNum = (TextView)findViewById(R.id.tvuserNumber);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        email = preferences.getString("UserEmail","");
        String pro_pic = preferences.getString("pro_pic","");
        number = preferences.getString("number","");
        name =  preferences.getString("name","");


        Picasso.with(getApplicationContext()).load("http://epay.cybussolutions.com/epay/" + pro_pic).into(userImg);


        tvEmail.setText(email);
        tvName.setText(name);
        tvNum.setText(number);



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
