package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends AppCompatActivity {


    Toolbar toolbar;
    ArrayList<String> from_routes = new ArrayList<>();
    ArrayList<String> to_routes ;
    TextView txt;
    Spinner from,to;
    Button procedd;
    String fromLocation;
    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Select Destination");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        txt = (TextView) findViewById(R.id.textView3);
        from = (Spinner) findViewById(R.id.from_spinner);
        to = (Spinner) findViewById(R.id.to_spinner);
        procedd = (Button) findViewById(R.id.proceed);

        getData();


        from.setOnItemSelectedListener(new CustomOnItemSelectedListener_from());


        procedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent=new Intent(MainScreen.this,Route_Detailed.class);
                intent.putExtra("from",from.getSelectedItem().toString());
                intent.putExtra("to",to.getSelectedItem().toString());
                startActivity(intent);

            }
        });

        // Defined Array values to show in ListView

    }

    public class CustomOnItemSelectedListener_from implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            fromLocation = from_routes.get(pos);

            getRoutes();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    private void getRoutes() {

        loading = ProgressDialog.show(MainScreen.this, "", "Please wait...", false, false);


        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETROUTES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();
                try {
                    JSONArray array = new JSONArray(response);

                    to_routes = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));


                        if(!(to_routes.contains(object.getString("rout_destination"))))
                        {
                            to_routes.add(object.getString("rout_destination"));

                        }
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                            (MainScreen.this, android.R.layout.simple_spinner_item,to_routes);

                    dataAdapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);

                    to.setAdapter(dataAdapter);


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

                map.put("from",fromLocation);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainScreen.this);
        requestQueue.add(request);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.list_btn) {
            Intent intent = new Intent(this, History.class);
            startActivity(intent);


        }
        if (id== R.id.logoutBtn){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent=new Intent(this,Login_Activity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {

        loading = ProgressDialog.show(MainScreen.this, "", "Please wait..", false, false);

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GETDATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                loading.dismiss();

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = new JSONObject(array.getString(i));

                        if(!(from_routes.contains(object.getString("rout_start"))))
                        {
                            from_routes.add(object.getString("rout_start"));

                        }

                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                            (MainScreen.this, android.R.layout.simple_spinner_item,from_routes);

                    dataAdapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);

                    from.setAdapter(dataAdapter);


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

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainScreen.this);
        requestQueue.add(request);

    }

}


