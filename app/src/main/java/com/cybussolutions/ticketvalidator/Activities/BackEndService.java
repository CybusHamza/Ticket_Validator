package com.cybussolutions.ticketvalidator.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Rizwan Butt on 25-Jul-17.
 */

public class BackEndService extends BroadcastReceiver {
    DBManager dbManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            dbManager = new DBManager(context);
            dbManager.open();
            if (info.isConnected()) {
                //start service
                dbManager.delete_route_table();
                intent=new Intent(context,HelloService.class);
                context.startService(intent);
                Toast.makeText(context,"started", Toast.LENGTH_LONG).show();
            }
            else {
                //stop service
                intent=new Intent(context,HelloService.class);
                context.stopService(intent);
            }
        }
    }
}
