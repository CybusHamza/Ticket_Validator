package com.cybussolutions.ticketvalidator;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {
TextInputLayout  inputEmail, inputPassword;
    EditText etEmail,etPassword;
    String userEmail,userPassword;
    String LOGIN_URL = "http://epay.cybussolutions.com/Api_Service/loginUser";
    Button loginButton;
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        inputEmail = (TextInputLayout)findViewById(R.id.input_email);
        inputPassword = (TextInputLayout)findViewById(R.id.input_password);
        etEmail = (EditText)findViewById(R.id.etemail);
        etPassword = (EditText) findViewById(R.id.etpassword);
        loginButton = (Button)findViewById(R.id.loginBtn);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userEmail = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();
                StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if (!(response.equals(""))) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Toast.makeText(Login_Activity.this, "No respone", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                        , new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_USERNAME, userEmail);
                        map.put(KEY_PASSWORD, userPassword);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);
                requestQueue.add(request);

            }

        });
    }
}

