package com.cybussolutions.ticketvalidator;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

public class Signup_activity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText inputFirstName,inputLastName, inputEmail, inputPassword,inputReenterPassword,inputPhoneNumber;
    private TextInputLayout inputLayoutFirstName,inputLayoutLastName, inputLayoutEmail, inputLayoutPassword,inputLayoutReenterPassword,inputLayoutPhoneNumber;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutReenterPassword = (TextInputLayout) findViewById(R.id.input_layout_re_enter_password);
        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phone_number);

        inputFirstName = (EditText) findViewById(R.id.input_first_name);
        inputLastName = (EditText) findViewById(R.id.input_last_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputReenterPassword = (EditText) findViewById(R.id.input_re_enter_password);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phone_number);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

    }
}
