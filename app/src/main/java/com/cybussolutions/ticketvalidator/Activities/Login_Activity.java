package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Login_Activity extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    TextInputLayout inputEmail, inputPassword;
    EditText etEmail, etPassword;
    String userEmail, userPassword;
    CheckBox rememberMeCheckBox;
    Button loginButton,signUpButton;
    Boolean checkBoxValue;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        inputEmail = (TextInputLayout) findViewById(R.id.input_email);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);
        etEmail = (EditText) findViewById(R.id.etemail);
        etPassword = (EditText) findViewById(R.id.etpassword);
        loginButton = (Button) findViewById(R.id.loginBtn);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        signUpButton = (Button) findViewById(R.id.btnSignUp);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean checkBoxSavedData = preferences.getBoolean("checkBoxRememberMe", false);

        if (checkBoxSavedData) {
            Intent intent = new Intent(Login_Activity.this, MainScreen.class);
            finish();
            startActivity(intent);
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Signup_activity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userEmail = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();

                Login();

    }

    public void Login()
    {

        loading = ProgressDialog.show(Login_Activity.this, "Please wait...", "Checking Credentails ...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            loading.dismiss();

            if (!(response.equals("\t\r\n\r\n\tfalse"))) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i = 0; i <= jsonResponse.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonResponse.getString(i));
                        String f_name = jsonObject.get("first_name").toString();
                        String l_name = jsonObject.get("last_name").toString();
                        String id = jsonObject.get("id").toString();

                        Toast.makeText(Login_Activity.this, response, Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login_Activity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("UserEmail", userEmail);
                        // editor.putString("UserPassword",userPassword);
                        editor.putString("f_name", f_name);
                        editor.putString("l_nmae", l_name);
                        editor.putString("id", id);
                        editor.apply();
                        if (rememberMeCheckBox.isChecked()) {
                            checkBoxValue = true;
                            editor.putBoolean("checkBoxRememberMe", checkBoxValue);
                            // editor.putString("checkBoxRememberMe",checkBoxValue.toString());
                            editor.apply();
                        } else {
                            editor.putBoolean("checkBoxRememberMe", false);
                            editor.apply();
                        }
                        Intent intent = new Intent(Login_Activity.this,MainScreen.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(Login_Activity.this, "Incorrect User name or Password", Toast.LENGTH_SHORT).show();
            }
        }

    }
            , new Response.ErrorListener()

    {
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

