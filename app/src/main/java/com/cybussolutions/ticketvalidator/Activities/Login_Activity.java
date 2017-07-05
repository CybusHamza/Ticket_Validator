package com.cybussolutions.ticketvalidator.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class Login_Activity extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    TextInputLayout inputEmail, inputPassword;
    EditText etEmail, etPassword;
    String userEmail, userPassword;
    CheckBox rememberMeCheckBox;
    Button loginButton;
    TextView tvSignUp;
    Boolean checkBoxValue;
    private ProgressDialog loading;
    private DBManager dbManager;
    TextView forgotPassword;

    ArrayList<String> stringArrayLogin=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);

//        inputEmail = (TextInputLayout) findViewById(R.id.input_email);
  //      inputPassword = (TextInputLayout) findViewById(R.id.input_password);
        etEmail = (EditText) findViewById(R.id.etemail);
        etPassword = (EditText) findViewById(R.id.etpassword);
        loginButton = (Button) findViewById(R.id.loginBtn);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        tvSignUp = (TextView) findViewById(R.id.btnSignUp);
        forgotPassword= (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean checkBoxSavedData = preferences.getBoolean("checkBoxRememberMe", false);

        if (checkBoxSavedData) {
            Intent intent = new Intent(Login_Activity.this, Dashboard.class);
            finish();
            startActivity(intent);
        }

        tvSignUp.setOnClickListener(new View.OnClickListener() {
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
                if (userEmail.length() ==0 || userPassword.length()==0){
                    Toast.makeText(getApplicationContext(),"Please enter email and password",Toast.LENGTH_SHORT).show();
                }
                else {

                    Login();
                }
    }

    public void Login()
    {

        loading = ProgressDialog.show(Login_Activity.this, "Please wait...", "Checking Credentails ...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            loading.dismiss();
            String checkCredentials=response.trim().toString();
            if(checkCredentials.equals("\"Incorrect Information\"")){
                Toast.makeText(Login_Activity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            }

            else if (!(response.equals("\t\r\n\r\n\tfalse"))) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i = 0; i <= jsonResponse.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonResponse.getString(i));
                        String f_name = jsonObject.get("fname").toString();
                        String l_name = jsonObject.get("lname").toString();
                        String id = jsonObject.get("customer_id").toString();
                        String num = jsonObject.get("phone").toString();
                        String email = jsonObject.get("email").toString();
                        String pro_pic = jsonObject.get("profile_pic").toString();


                       // Toast.makeText(Login_Activity.this, response, Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login_Activity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("UserEmail", userEmail);
                        editor.putString("number",num);
                        editor.putString("first_name",f_name);
                        editor.putString("last_name",l_name);
                        editor.putString("name",f_name + "  "+ l_name);

                        editor.putString("pro_pic",pro_pic);


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
                        Intent intent = new Intent(Login_Activity.this,Dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(Login_Activity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
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
                dbManager = new DBManager(Login_Activity.this);
                dbManager.open();
                message = "Cannot connect to Internet...Please check your connection!";
                String id=dbManager.login(userEmail,userPassword);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login_Activity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserEmail", userEmail);
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
                if(!id.equals("0")){
                    Intent intent = new Intent(Login_Activity.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Incorrect Email or password", Toast.LENGTH_LONG).show();
                }
               // dbManager.insert("3","rizwan", "jillani","demo456","03134969548","1","rizwan@gmail.com","1");
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

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
    public void forgotPasswordDialog() {
        // canvas.setMode(CanvasView.Mode.TEXT);
        AlertDialog.Builder alert = new AlertDialog.Builder(Login_Activity.this);
        alert.setTitle("Forgot Password"); //Set Alert dialog title here
        alert.setMessage("Enter Your Email Here"); //Message here

        // Set an EditText view to get user input
        final EditText input = new EditText(Login_Activity.this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
                String srt = input.getEditableText().toString();

            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

}

