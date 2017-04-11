package com.cybussolutions.ticketvalidator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.R;

public class Payment_Method extends AppCompatActivity {


    String fare;
    TextView fareText;
    Toolbar toolbar;
    Button pay;
    RadioGroup radioGroup;
    private RadioButton sms, card, qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__method);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Proceed To Payment");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        final Intent intent= getIntent();

        fare = intent.getStringExtra("fair");


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


                if(selectedId == sms.getId()) {
                    Toast.makeText(Payment_Method.this, "Payment by SMS", Toast.LENGTH_SHORT).show();

                } else if(selectedId == card.getId()) {
                    Toast.makeText(Payment_Method.this, "Payment by Card", Toast.LENGTH_SHORT).show();

                }
                else if(selectedId == qr.getId()) {
                    Intent intent1 = new Intent(Payment_Method.this,QrCodeScannerActivity.class);
                    startActivity(intent1);


                }

                else
                {
                    Toast.makeText(Payment_Method.this, "No option Selected", Toast.LENGTH_SHORT).show();
                    }





            }
        });

        fareText.setText(fare);


    }
}
