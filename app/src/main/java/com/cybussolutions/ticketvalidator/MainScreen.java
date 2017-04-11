package com.cybussolutions.ticketvalidator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {


    Toolbar toolbar;
    ListView Route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Select Destination");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        Route = (ListView) findViewById(R.id.rooute);



        final List<String> distance_list = new ArrayList<String>();
        distance_list.add("30 mins");
        distance_list.add("40 mins");
        distance_list.add("20 mins");
        distance_list.add("25 mins");
        distance_list.add("40 mins");
        distance_list.add("30 mins");


        final List<String> fair_list = new ArrayList<String>();
        fair_list.add("$ 20");
        fair_list.add("$ 30");
        fair_list.add("$ 15");
        fair_list.add("$ 17");
        fair_list.add("$ 30");
        fair_list.add("$ 20");



        // Defined Array values to show in ListView
        final String[] values = new String[] {
                "Gulberg to DHA phase 5",
                "DHA to Johar Town",
                "Johar Town to Faisal Town",
                "DHA to Jail Road",
                "Azadi Chowk to Mall Road",
                "Cavalry Ground to Peco Road",
                "DHA to Jail Road",
                "Cavalry Ground to Peco Road"};



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        Route.setAdapter(adapter);




       Route.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               Intent intent=  new Intent(MainScreen.this,Route_Detailed.class);
               intent.putExtra("route",values[i]);
               intent.putExtra("route_fiar",fair_list.get(i));
               intent.putExtra("route_distance",distance_list.get(i));
               startActivity(intent);
           }
       });


    }




}
