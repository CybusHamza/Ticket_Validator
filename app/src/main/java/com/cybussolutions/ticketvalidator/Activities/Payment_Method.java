package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment_Method extends AppCompatActivity {

    TextView fareText;
    Toolbar toolbar;
    Button pay;
    RadioGroup radioGroup;
    String from, to;
    private RadioButton sms, card, qr;
    private ProgressDialog loading;
    String fare = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__method);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        final Intent intent = getIntent();

        from = intent.getStringExtra("from");
        to = intent.getStringExtra("to");


        getData();

        fareText = (TextView) findViewById(R.id.fair);
        pay = (Button) findViewById(R.id.pay);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        sms = (RadioButton) findViewById(R.id.radioButton);
        card = (RadioButton) findViewById(R.id.radioButton2);
        qr = (RadioButton) findViewById(R.id.radioButton3);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = radioGroup.getCheckedRadioButtonId();


                if (selectedId == sms.getId()) {
                    Toast.makeText(Payment_Method.this, "Payment by SMS", Toast.LENGTH_SHORT).show();

                } else if (selectedId == card.getId()) {
                    Toast.makeText(Payment_Method.this, "Payment by Card", Toast.LENGTH_SHORT).show();

                } else if (selectedId == qr.getId()) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Payment_Method.this);
                    String qr_string = preferences.getString("id","")+","+fare;

                    Intent intent1 = new Intent(Payment_Method.this, Qr_Activity.class);
                    intent1.putExtra("Qr_string",qr_string);
                    startActivity(intent1);


                } else {
                    Toast.makeText(Payment_Method.this, "No option Selected", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void getData() {

        loading = ProgressDialog.show(Payment_Method.this, "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETROUTES_RATES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();



                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));

                        fare = object.getString("Fare_price");

                    }

                    fareText.setText(fare);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
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
                map.put("from", from);
                map.put("to", to);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Payment_Method.this);
        requestQueue.add(request);

    }
}
