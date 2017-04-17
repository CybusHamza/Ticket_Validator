package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.cybussolutions.ticketvalidator.R;

import java.util.HashMap;
import java.util.Map;

public class Route_Detailed extends AppCompatActivity {

    Toolbar toolbar;
    String route ,fiar ,distance;
    TextView tvRoute,Tfiar;
    String KEY_FROM= "from";
    String KEY_TO = "to";
   // TextView Tdistance;
    Button proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detailed);
       // setContentView(R.layout.activity_route_detailed);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        tvRoute = (TextView)findViewById(R.id.tvRoute);
        Intent intent= getIntent();
       final String to = intent.getStringExtra("to");
       final String from = intent.getStringExtra("from");
        tvRoute.setText("From " +from + " To " + to );
      //  route = intent.getStringExtra("route");
        fiar = intent.getStringExtra("route_fiar");
        distance = intent.getStringExtra("route_distance");

      //  Troute = (TextView) findViewById(R.id.route);
        Tfiar = (TextView) findViewById(R.id.fair);
     //   Tdistance = (TextView) findViewById(R.id.time);
        proceed = (Button) findViewById(R.id.pay);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Route_Detailed.this, Payment_Method.class);
                intent.putExtra("fair", fiar);
                startActivity(intent);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Route_Detailed.this, response, Toast.LENGTH_SHORT).show();

                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Route_Detailed.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                           map.put(KEY_FROM,from);
                         map.put(KEY_TO,to);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);



            }
        });


        //Troute.setText(route);
//        Tfiar.setText(fiar);
     //   Tdistance.setText(distance);


    }
}
