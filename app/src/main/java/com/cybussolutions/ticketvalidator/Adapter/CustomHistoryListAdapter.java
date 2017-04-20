package com.cybussolutions.ticketvalidator.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;

import java.util.List;

/**
 * Created by AQSA SHaaPARR on 4/18/2017.
 */

public class CustomHistoryListAdapter extends BaseAdapter {

    List<HistoryData> historyDataList;
    Activity activity;
    private LayoutInflater inflater;


    public CustomHistoryListAdapter(Activity activity, List<HistoryData> historyDataListItems) {
        this.activity = activity;
        this.historyDataList = historyDataListItems;
    }



    @Override
    public int getCount() {
        return historyDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater== null)
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.row_history_layout, null);


        TextView tvCharge = (TextView)view.findViewById(R.id.tvCharge);
        TextView tvDestinition = (TextView)view.findViewById(R.id.tvDestinationPoint);
        TextView tvStart = (TextView)view.findViewById(R.id.tvStartingPoint);
        TextView tvPersons = (TextView)view.findViewById(R.id.tvNumberOfPersons);
        TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
        TextView tvTime = (TextView)view.findViewById(R.id.tvTime);
        TextView tvCpp = (TextView)view.findViewById(R.id.tvCPPP);

        HistoryData historyData = historyDataList.get(i);
        int totalFare =Integer.valueOf(historyData.getFare_Price()) * Integer.valueOf( historyData.getPersonTravelling());

       tvCharge.setText("$"+String.valueOf(totalFare));
        tvCpp.setText("$"+historyData.getFare_Price());
        tvDestinition.setText( historyData.getRoute_destinition());
         tvStart.setText(historyData.getRouteStart());
        tvPersons.setText(historyData.getPersonTravelling());
        tvDate.setText(historyData.getDate());
        tvTime.setText(historyData.getTime());



        return view;
    }
}
