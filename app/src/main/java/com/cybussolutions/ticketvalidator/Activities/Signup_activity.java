package com.cybussolutions.ticketvalidator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.cybussolutions.ticketvalidator.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup_activity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword, inputReenterPassword, inputPhoneNumber;
    private TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutEmail, inputLayoutPassword, inputLayoutReenterPassword, inputLayoutPhoneNumber;
    private Button btnSignUp;

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String gender;

    private ProgressDialog loading;

    private DBManager dbManager;
    String test;

    public static Phonenumber.PhoneNumber phonenumberProto;
    PhoneNumberUtil phoneNumberUtil;
    Boolean isValid;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


//        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
//        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
  //      inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
    //    inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
      //  inputLayoutReenterPassword = (TextInputLayout) findViewById(R.id.input_layout_re_enter_password);
       // inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phone_number);

        inputFirstName = (EditText) findViewById(R.id.input_first_name);
        inputLastName = (EditText) findViewById(R.id.input_last_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputReenterPassword = (EditText) findViewById(R.id.input_re_enter_password);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phone_number);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        phoneNumberUtil = PhoneNumberUtil.getInstance();
        phonenumberProto = new Phonenumber.PhoneNumber();
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),test,Toast.LENGTH_LONG).show();
                getData();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        final String first_name = inputFirstName.getText().toString().trim();
        final String last_name = inputLastName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        String re_enter_password = inputReenterPassword.getText().toString().trim();
        final String phone_number = inputPhoneNumber.getText().toString().trim();

        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        if(radioSexButton.getText().toString().equals("Male")){
            gender="1";
        }else {
            gender="0";
        }
        //String password = editTextPass.getText().toString().trim();
        if (inputFirstName.getText().toString().trim().equals("") || inputLastName.getText().toString().trim().equals("") || inputEmail.getText().toString().trim().equals("") || inputPassword.getText().toString().trim().equals("") || inputReenterPassword.getText().toString().trim().equals("") || inputPhoneNumber.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please Fill all the fields", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!inputPassword.getText().toString().trim().equals(inputReenterPassword.getText().toString().trim())) {
                Toast.makeText(this, "Password Mismatches", Toast.LENGTH_LONG).show();
                return;
            }
            /*if (phone_number.length() < 10 || phone_number.length() > 14) {
                Toast.makeText(this, "Please enter valid Phone Number", Toast.LENGTH_LONG).show();
                return;
            }*/

            if(!email.matches(emailPattern)){
                Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_LONG).show();
                return;
            }
            if(inputPassword.getText().toString().length()<8){
                Toast.makeText(this, "Minimum password length should be 8 characters", Toast.LENGTH_LONG).show();
                return;
            }
            try {


                phonenumberProto = phoneNumberUtil.parse(phone_number, "NG");
                // Toast.makeText(getApplicationContext(),"number is entered",Toast.LENGTH_SHORT).show();
                isValid = phoneNumberUtil
                        .isValidNumber(phonenumberProto);
            } catch (NumberParseException e) {


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            //catch (Exception e) {
            //  e.printStackTrace();
            //}

            if (!(isValid)) {
                // String internationalFormat = phoneNumberUtil.format(phonenumberProto,PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                Toast.makeText(
                        getApplicationContext(),
                        "Phone number is INVALID: " + phone_number,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        loading = ProgressDialog.show(Signup_activity.this, "Please wait...", "Signing Up...", false, false);



        StringRequest strreq = new StringRequest(Request.Method.POST,
                End_Points.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        loading.dismiss();
                        if (Response.trim().toString().equals("User already Registered with an this Email Adress")) {
                            Toast.makeText(Signup_activity.this,"User already Registered with this Email Address",Toast.LENGTH_LONG).show();
                        }

                        else if(!(Response.equals("")))
                        {
                           Toast.makeText(getApplicationContext(), "user registered successfully", Toast.LENGTH_LONG).show();
                            dbManager = new DBManager(Signup_activity.this);
                            dbManager.open();
                            try{
                                JSONObject jsonObject = new  JSONObject(Response);
                                String id = jsonObject.get("userId").toString();
//
//                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                SharedPreferences.Editor editor = preferences.edit();
//                             //   editor.putString();
//                                editor.putString("UserEmail", email);
//                                // editor.putString("UserPassword",userPassword);
//                                editor.putString("f_name", first_name);
//                                editor.putString("l_nmae", last_name);
//                                editor.putString("id", id);
//                                editor.putString("sign_in_status","1");
//                                editor.apply();



                                //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
                                dbManager.insert(id,first_name, last_name,password,phone_number,gender,email,"1");

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup_activity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                               // editor.putString("UserEmail", userEmail);
                                editor.putString("id", id);
                                editor.commit();
                                startService(new Intent(Signup_activity.this, HelloService.class));
                                Intent intent = new Intent(Signup_activity.this,Login_Activity.class);
                                startActivity(intent);
                                finish();
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"There was an error",Toast.LENGTH_LONG).show();
                            }
                              //  dbManager.insert(first_name, last_name, email, phone_number);
                            //Cursor cursor=dbManager.fetch();
                        }
                        else {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("sign_in_status","0");
                            editor.apply();
                            Toast.makeText(Signup_activity.this, "There was an error", Toast.LENGTH_SHORT).show();
                        }
                        // showJSON(Response);
                        // get response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                String message = null;
                if (e instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
                loading.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", inputFirstName.getText().toString().trim());
                params.put("last_name", inputLastName.getText().toString().trim());
                params.put("email", inputEmail.getText().toString().trim());
                params.put("password", inputPassword.getText().toString().trim());
                params.put("phone_number", inputPhoneNumber.getText().toString().trim());
                params.put("gender",gender);
                params.put("cardtype","1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Signup_activity.this);
        requestQueue.add(strreq);
    }

    @Override
    public void onClick(View v) {

    }

}
