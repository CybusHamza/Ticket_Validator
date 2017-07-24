package com.cybussolutions.ticketvalidator.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.R;
import com.interswitchng.sdk.model.RequestOptions;
import com.interswitchng.sdk.payment.IswCallback;
import com.interswitchng.sdk.payment.android.inapp.Pay;
import com.interswitchng.sdk.payment.model.PurchaseResponse;

public class Payment_Recharge extends AppCompatActivity {

    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__recharge);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestOptions options = RequestOptions.builder()
                        .setClientId("IKIA43A9EE2EF1FAB58341922EF2557C46D94B8FE96C")
                        .setClientSecret("oMwb2KmHY7UcLYkIW7CiEFL4WK2Qk0PoU8POxAS1Nmg=")
                        .build();

                Pay pay = new Pay(Payment_Recharge.this, "12", "Payment for yicket", "50", "NGN", options,
                        new IswCallback<PurchaseResponse>()  {
                            @Override
                            public void onError(Exception error) {
                                // Handle error.
                                // Payment not successful.

                                Toast.makeText(Payment_Recharge.this, error.toString(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onSuccess(PurchaseResponse response) {

                                Toast.makeText(Payment_Recharge.this, response.getAmount(), Toast.LENGTH_SHORT).show();
                            }
                        });
                pay.start();
            }
        });


    }
}
