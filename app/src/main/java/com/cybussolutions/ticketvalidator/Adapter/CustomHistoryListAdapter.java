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
            view = inflater.inflate(R.layout.row_history, null);


        TextView tvCharge = (TextView)view.findViewById(R.id.tvCharge);
        TextView tvRouteName = (TextView)view.findViewById(R.id.tvroute);
        TextView tvPersons = (TextView)view.findViewById(R.id.tvPersons);
        TextView tvDate = (TextView)view.findViewById(R.id.tvDate);

        HistoryData historyData = historyDataList.get(i);
        tvCharge.setText(historyData.getFare_Price());
        tvRouteName.setText("From " +historyData.getRouteStart()+" to "+ historyData.getRoute_destinition());
        tvPersons.setText(historyData.getPersonTravelling());
        tvDate.setText(historyData.getDate());


        return view;
    }
}
