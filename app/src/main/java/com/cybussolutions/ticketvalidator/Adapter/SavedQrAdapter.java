package com.cybussolutions.ticketvalidator.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
import com.cybussolutions.ticketvalidator.Activities.Qr_Activity;
import com.cybussolutions.ticketvalidator.Qr_Genrator.Contents;
import com.cybussolutions.ticketvalidator.Qr_Genrator.QRCodeEncoder;
import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;
import com.cybussolutions.ticketvalidator.pojo.SavedQrPojo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Rizwan Butt on 24-Jul-17.
 */

public class SavedQrAdapter extends BaseAdapter {

    List<SavedQrPojo> SavedQrDataList;
    Activity activity;
    Context mContext;
    private LayoutInflater inflater;
    public SavedQrAdapter(Activity activity, List<SavedQrPojo> historyDataListItems) {
        this.activity = activity;
        this.SavedQrDataList = historyDataListItems;
    }
    @Override
    public int getCount() {
        return SavedQrDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return SavedQrDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_saved_qr_layout, null);
        TextView tvFrom = (TextView) convertView.findViewById(R.id.from);
        TextView tvTo = (TextView) convertView.findViewById(R.id.to);
        TextView tvDate = (TextView) convertView.findViewById(R.id.date);
     //   ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_image);
        final SavedQrPojo savedQr = SavedQrDataList.get(position);
        final String QRstring=savedQr.getQrString();
        ////final qr string customer_id,fare,fareType,routeId,transId,transStatusId,from,to,persontraveling,name,number////////
        String split[]=QRstring.split(",");
        tvFrom.setText("From :"+split[6]);
        tvTo.setText("To :"+split[7]);
        tvDate.setText(savedQr.getQrSaveDate());
        WindowManager manager = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(QRstring,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView imageView1 = (ImageView)convertView.findViewById(R.id.grid_image);
            imageView1.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetooth;
                LocationManager service;
                boolean enabled;
                service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
                bluetooth= BluetoothAdapter.getDefaultAdapter();
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                            activity);
                    myAlertDialog.setTitle("Gps must require");
                    myAlertDialog.setMessage("You need to turn on your gps location to proceed");

                    myAlertDialog.setPositiveButton("Enable",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activity.startActivity(intent);
                                }
                            });

                    myAlertDialog.setNegativeButton("Deny",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                            /*Toast.makeText(getApplicationContext(),
                                    "Please turn on your location to proceed", Toast.LENGTH_LONG);
                            finish();*/
                                }
                            });
                    myAlertDialog.show();
                }
                if (!bluetooth.isEnabled()) {
                    Toast.makeText(activity,
                            "Turning ON Bluetooth", Toast.LENGTH_LONG);
                    // Intent enableBtIntent = new
                    // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                }
                //Toast.makeText(getApplicationContext(),"Please enable bluetooth and gps information to generate QR code",Toast.LENGTH_LONG).show();
                if (bluetooth.isEnabled() && enabled) {
                    Intent intent = new Intent(activity, Qr_Activity.class);
                    intent.putExtra("activityName", "SaveQrScreen");
                    intent.putExtra("savedQrString", savedQr.getQrString());
                    intent.putExtra("qrId", savedQr.getQrId());
                    activity.finish();
                    activity.startActivity(intent);
                }
                //Toast.makeText(activity, savedQr.getQrString(), Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
