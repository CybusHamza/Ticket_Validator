package com.cybussolutions.ticketvalidator.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.Activities.DBManager;
import com.cybussolutions.ticketvalidator.Activities.History;
import com.cybussolutions.ticketvalidator.Qr_Genrator.Contents;
import com.cybussolutions.ticketvalidator.Qr_Genrator.QRCodeEncoder;
import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.List;
import java.util.Random;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by AQSA SHaaPARR on 4/18/2017.
 */

public class CustomHistoryListAdapter extends BaseAdapter {

    List<HistoryData> historyDataList;
    List<HistoryData> filteredHistoryData;
    Activity activity;
    HistoryData mContacts;
    Context mContext;
    private LayoutInflater inflater;
    String route_id,fareType;


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
        final  int pos =i;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.row_history_layout, null);



        TextView tvCharge = (TextView) view.findViewById(R.id.tvCharge);
        TextView tvDestinition = (TextView) view.findViewById(R.id.tvDestinationPoint);
        TextView tvStart = (TextView) view.findViewById(R.id.tvStartingPoint);
        TextView tvPersons = (TextView) view.findViewById(R.id.tvNumberOfPersons);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        TextView tvCpp = (TextView) view.findViewById(R.id.tvCPPP);
        TextView tvTransId = (TextView) view.findViewById(R.id.tvTransId);

        final HistoryData historyData = historyDataList.get(i);
        int totalFare = Integer.valueOf(historyData.getFare_Price()) * Integer.valueOf(historyData.getPersonTravelling());

        tvCharge.setText("₦" + String.valueOf(totalFare));
        tvCpp.setText("₦" + historyData.getFare_Price());
        tvDestinition.setText(historyData.getRoute_destinition());
        tvStart.setText(historyData.getRouteStart());
        tvPersons.setText(historyData.getPersonTravelling());
        tvDate.setText(historyData.getDate());
        tvTime.setText(historyData.getTime());
        tvTransId.setText(historyData.getTrans_id());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBManager dbManager;
                    dbManager = new DBManager(activity);
                    dbManager.open();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                    String id = preferences.getString("id", "");
                    String name=preferences.getString("name","");
                    String number=preferences.getString("number","");
                    //customer_id,fare,fareType,routeId,transId,transStatusId,from,to,persontraveling,name,number
                    route_id = dbManager.fetch_route_id(historyData.getRouteStart(),historyData.getRoute_destinition());
                    fareType=dbManager.fetch_fare_type(route_id);
                    String QrString=id+","+historyData.getFare_Price()+","+fareType+","+route_id+","+historyData.getTrans_id()+","+historyData.getRouteStart()+","+historyData.getRoute_destinition()+","+historyData.getPersonTravelling()+","+name+","+number;

                    WindowManager manager = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3/4;
                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(QrString,
                            null,
                            Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(),
                            smallerDimension);
                    try {
                        Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.custom_dialog_qr);
                        dialog.setTitle("");

                        ImageView image = (ImageView) dialog.findViewById(R.id.qr_image);
//                        image.setImageResource(R.drawable.ic_launcher);
                        image.setImageBitmap(bitmap);
                        dialog.show();

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(activity,route_id+" "+fareType,Toast.LENGTH_LONG).show();

                }
            });

        return view;
    }

    //    @Override
//    public Filter getFilter() {
//        return null;
//    }






    }


