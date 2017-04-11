package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cybussolutions.ticketvalidator.R;

public class Route_Detailed extends AppCompatActivity {

    Toolbar toolbar;
    String route ,fiar ,distance;
    TextView Troute,Tfiar,Tdistance;
    Button proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detailed);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        Intent intent= getIntent();

        route = intent.getStringExtra("route");
        fiar = intent.getStringExtra("route_fiar");
        distance = intent.getStringExtra("route_distance");


        Troute = (TextView) findViewById(R.id.route);
        Tfiar = (TextView) findViewById(R.id.fair);
        Tdistance = (TextView) findViewById(R.id.time);
        proceed = (Button) findViewById(R.id.pay);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=  new Intent(Route_Detailed.this,Payment_Method.class);
                intent.putExtra("fair",fiar);
                startActivity(intent);

            }
        });


        Troute.setText(route);
        Tfiar.setText(fiar);
        Tdistance.setText(distance);


    }
}
