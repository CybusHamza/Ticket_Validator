package com.cybussolutions.ticketvalidator.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.Adapter.CustomHistoryListAdapter;
import com.cybussolutions.ticketvalidator.Adapter.SavedQrAdapter;
import com.cybussolutions.ticketvalidator.Feedback;
import com.cybussolutions.ticketvalidator.R;
import com.cybussolutions.ticketvalidator.pojo.HistoryData;
import com.cybussolutions.ticketvalidator.pojo.SavedQrPojo;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SaveQrScreen extends AppCompatActivity {


    ArrayList<String> savedQrString = new ArrayList<>();
    ArrayList<String> savedQrDate = new ArrayList<>();
    ArrayList<String> savedQrId = new ArrayList<>();
    Toolbar toolbar;

    String userEmail,userName,customer_id,profile_pic;

    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem().withIdentifier(2).withName("Profile");
    SecondaryDrawerItem home = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");
    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");
    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");
    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    PrimaryDrawerItem savedQr = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Saved QR");

    Drawer result;

    private DBManager dbManager;
    private String url;
    Bitmap[] bitmap1;

    GridView qrGridView;
    private List<SavedQrPojo> savedQrList = new ArrayList<SavedQrPojo>();
    SavedQrAdapter adapter;


    private int REQUEST_PERMISSIONS=1,REQUEST_BLUETOOTH=2;
    BluetoothAdapter bluetooth;
    LocationManager service;
    boolean enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_qr_screen);

        dbManager = new DBManager(SaveQrScreen.this);
        dbManager.open();
        toolbar = (Toolbar)findViewById(R.id.app_bar_saved_qr);
        toolbar.setTitle("Saved QR");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        qrGridView= (GridView) findViewById(R.id.gridView);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=preferences.getString("UserEmail",null);
        userName=preferences.getString("name",null);
        customer_id=preferences.getString("id",null);
        profile_pic=preferences.getString("pro_pic",null);

        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(SaveQrScreen.this)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmap1[0] =bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        Log.e("here","onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.e("here","onPrepareLoad");
                    }
                });

        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.drawable.bg_ep_slider_header)

                .addProfiles(new ProfileDrawerItem().withName(userName).withEmail(userEmail).withIcon(bitmap1[0]))
                .withProfileImagesVisible(true)
                .withOnAccountHeaderListener(
                        new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                                return false;
                            }
                        }
                ).build();
        //  new DrawerBuilder().withAccountHeader(header).build();
//      //  new SecondaryDrawerItem().withName("Your Trips")
//        new SecondaryDrawerItem().withName("Edit Profile"),
//                new SecondaryDrawerItem().withName("Logout")


        result= new DrawerBuilder().withActivity(this).withAccountHeader(header)
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(5).addDrawerItems(EditProfile,home, payment, your_trips,savedQr,feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem== your_trips){
                            Intent intent = new Intent(SaveQrScreen.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SaveQrScreen.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem== payment){
                            Intent intent=new Intent(getApplicationContext(),Payment_Method.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem==EditProfile){
                            Intent intent=new Intent(getApplicationContext(),Profile_Detailed.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==home){
                            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(intent);
                            finish();

                        }
                        if (drawerItem==feedback){

                            Intent intent = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==changePassword){
                            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                            startActivity(intent);
                            finish();
                        }
                        if (drawerItem==savedQr){
                            Intent intent = new Intent(getApplicationContext(), SaveQrScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        return true;

                    }

                }).build();


        savedQrString=dbManager.fetch_saved_qr();
        savedQrDate=dbManager.fetch_saved_qr_date();
        savedQrId=dbManager.fetch_saved_qr_id();
        if (savedQrString.size() > 0) {
            for (int i = 0; i < savedQrString.size(); i++) {
                SavedQrPojo qrData = new SavedQrPojo();
                //String stringArrayList = dbManager.fetch_history_table(userid, stringArrayList5.get(i));
                qrData.setQrString(savedQrString.get(i));
                qrData.setQrSaveDate(savedQrDate.get(i));
                qrData.setQrId(savedQrId.get(i));
                savedQrList.add(qrData);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No saved Qr found found", Toast.LENGTH_LONG).show();
        }

        adapter = new SavedQrAdapter(SaveQrScreen.this, savedQrList);
        qrGridView.setAdapter(adapter);
       /* qrGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavedQrPojo qrData = new SavedQrPojo();
                Toast.makeText(SaveQrScreen.this, qrData.getQrString(), Toast.LENGTH_LONG).show();
            }
        });*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS || requestCode==REQUEST_BLUETOOTH ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

            } else {


                Toast.makeText(SaveQrScreen.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 0) {
                if (requestCode == 0 && resultCode == RESULT_OK) {


                } else {
                    if (!((BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()){
                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                        builder.setTitle("Bluetooth not enabled");
                        builder.setMessage("Please enable Bluetooth.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }

                        });
                        builder.show();

                    }
//                    Toast.makeText(this, "Bluetooth Permission must required",
//                            Toast.LENGTH_LONG).show();


                }
            }else if(requestCode==1) {
                if (requestCode == 1 && resultCode == RESULT_OK
                        && null != data) {


                } else {
                    Toast.makeText(this, "Location Permission must required",
                            Toast.LENGTH_LONG).show();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getLocalizedMessage(), "test");
//            Toast.makeText(this, "", Toast.LENGTH_LONG)
//                    .show();
        }
    }
}
