package com.cybussolutions.ticketvalidator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybussolutions.ticketvalidator.Activities.ChangePassword;
import com.cybussolutions.ticketvalidator.Activities.Dashboard;
import com.cybussolutions.ticketvalidator.Activities.History;
import com.cybussolutions.ticketvalidator.Activities.Login_Activity;
import com.cybussolutions.ticketvalidator.Activities.Payment_Method;
import com.cybussolutions.ticketvalidator.Activities.Profile_Detailed;
import com.cybussolutions.ticketvalidator.Activities.SaveQrScreen;
import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Profile extends AppCompatActivity {

    ProgressDialog ringProgressDialog;

    boolean doubleBackToExitPressedOnce = false;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    String picturePath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText etEmail, etName, etNum;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMG = 0;
    String imgDecodableString;
    Context context;

    ImageView userImg;
    Drawer result;
    String formattedDate;

    SecondaryDrawerItem home = new SecondaryDrawerItem().withIdentifier(2).withName("Home");
    SecondaryDrawerItem payment = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Payment");
    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Your Trips");

    PrimaryDrawerItem EditProfile = new PrimaryDrawerItem()
            .withIdentifier(1).withName("Profile");

    SecondaryDrawerItem feedback = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Feedback");


    SecondaryDrawerItem logout = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Logout");

    SecondaryDrawerItem changePassword = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Change Password");
    SecondaryDrawerItem savedQr = new SecondaryDrawerItem()
            .withIdentifier(2).withName("Saved QR");

    String b64;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    String email,name,number,id,fname,lname;
    //StringEntity se;
    String entityContents="";
    //
    private static final int REQUEST_PERMISSIONS=0;

    public static Phonenumber.PhoneNumber phonenumberProto;
    PhoneNumberUtil phoneNumberUtil;
    Boolean isValid;
    String userEmail,userName;
    String dateAndTime;
    AlertDialog myalertdialog;
    Calendar calender;
    int seconds;
    Button btnUpdate;
    String url,profile_pic;
    Bitmap[] bitmap1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//         collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
//           AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuu);
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail=pref1.getString("UserEmail",null);
        userName=pref1.getString("name",null);
        profile_pic=pref1.getString("pro_pic","");

      //  url =  "http://epay.cybussolutions.com/epay/"+profile_pic.trim();
        bitmap1 = new Bitmap[1];
        url =  "http://epay.cybussolutions.com/epay/"+profile_pic;

        Picasso.with(Profile.this)
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
                .withToolbar(toolbar).withDrawerWidthDp(250).withSelectedItemByPosition(1).addDrawerItems( EditProfile,home, payment, your_trips,savedQr,feedback,changePassword, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {




                        if (drawerItem== your_trips){
                            Intent intent = new Intent(Profile.this, History.class);
                            startActivity(intent);
                            finish();
                        }
                        if(drawerItem== logout){

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
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


        etEmail = (EditText) findViewById(R.id.userEmail);
        etName = (EditText) findViewById(R.id.userName);
        etNum = (EditText) findViewById(R.id.userNumber);
        btnUpdate = (Button)findViewById(R.id.update);
        ImageView iv=(ImageView)findViewById(R.id.imageBtn);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.man));
        if(!profile_pic.equals("")) {
            Picasso.with(Profile.this)
                    .load("http://epay.cybussolutions.com/epay/"+profile_pic.trim()).placeholder(getResources().getDrawable(R.drawable.man))
                    .into(iv);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        email = preferences.getString("UserEmail","");
        number = preferences.getString("number","");
        name =  preferences.getString("name","");
        fname =  preferences.getString("first_name","");
        lname =  preferences.getString("last_name","");
        id = preferences.getString("id","");



        etEmail.setText(email);
        etNum.setText(number);
        etName.setText(name);


        phoneNumberUtil = PhoneNumberUtil.getInstance();
        phonenumberProto = new Phonenumber.PhoneNumber();




        context = getApplicationContext();
        calender = Calendar.getInstance();
        dateAndTime=String.valueOf(calender.get(Calendar.DATE)) +String.valueOf(calender.get(Calendar.MONTH))+ String.valueOf(calender.get(Calendar.YEAR))+ String.valueOf(calender.get(Calendar.HOUR)) + String.valueOf(calender.get(Calendar.MINUTE)) + String.valueOf(calender.get(Calendar.SECOND));

//Toast.makeText(getApplicationContext(),dateAndTime,Toast.LENGTH_LONG).show();
        userImg = (ImageView) findViewById(R.id.imageBtn);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = etNum.getText().toString();
                email = etEmail.getText().toString();


                name = etName.getText().toString();





                if (number.length() > 0 && (!number.equals("") && number!=null) && (!name.equals("")&& name!=null) && (!email.equals("")&& email!=null)) {

                    try {


                        phonenumberProto = phoneNumberUtil.parse(number, "NG");
                        // Toast.makeText(getApplicationContext(),"number is entered",Toast.LENGTH_SHORT).show();
                        isValid = phoneNumberUtil
                                .isValidNumber(phonenumberProto);
                    } catch (NumberParseException e) {


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    //catch (Exception e) {
                    //  e.printStackTrace();
                    //}

                    if (!(isValid)) {
                        // String internationalFormat = phoneNumberUtil.format(phonenumberProto,PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                        Toast.makeText(
                                getApplicationContext(),
                                "Phone number is INVALID: " + number,
                                Toast.LENGTH_SHORT).show();


                    }
                    if (isValid && email.matches(EMAIL_PATTERN) && (number!="" ||number!=null) && (name!=""|| name!=null) && (email!=""|| email!=null)) {
                        /*String[] names = name.split(" ");

                        fname = names[0];
                        lname = names[1];*/
                        ringProgressDialog = ProgressDialog.show(Profile.this, "Please wait ...", "Uploading data ...", true);
                        ringProgressDialog.setCancelable(false);
                        ringProgressDialog.show();
                        if(picturePath==null) {
                            updateProfile();
                        }else if(b64!=null){
                            LoadImage();
                        }

                    }

                    if (isValid && !(email.matches(EMAIL_PATTERN))) {

                        Toast.makeText(getApplicationContext(), "Invalid Pattern", Toast.LENGTH_LONG).show();


                    } else if (number.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Mobile Number is required", Toast.LENGTH_SHORT).show();

                    }


                }else {
                    if(number.equals("")){
                        Toast.makeText(getApplicationContext(), "Number field is empty", Toast.LENGTH_LONG).show();
                    }
                    if(name.equals("")){
                        Toast.makeText(getApplicationContext(), "Name field is empty", Toast.LENGTH_LONG).show();
                    }
                    if(email.equals("")){
                        Toast.makeText(getApplicationContext(), "Email field is empty", Toast.LENGTH_LONG).show();
                    }
                }


            };
        });


        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.ui_alert_dialog);
//                dialog.show();
//
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.ui_alert_dialog, null);
                builder.setView(dialogView);

                Button gallery = (Button) dialogView.findViewById(R.id.gallery_btn);
                Button camera = (Button)dialogView.findViewById(R.id.camera_btn);
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        myalertdialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                            if (Build.VERSION.SDK_INT > 22) {

                                requestPermissions(new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);

                            }

                        } else {
                            gallery();

                        }


                    }
                });
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        myalertdialog.dismiss();

                        if (ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                            if (Build.VERSION.SDK_INT > 22) {

                                requestPermissions(new String[]{Manifest.permission
                                                .CAMERA},
                                        REQUEST_IMAGE_CAPTURE);

                            }

                        } else {
                            camera();

                        }


                    }
                });

                myalertdialog = builder.create();
                myalertdialog.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                int w=userImg.getWidth();
                int h=userImg.getHeight();
                //Bitmap bm = BitmapFactory.decodeFile(URI.getPath());
                //Bitmap bm = BitmapFactory.decodeFile(picturePath);
                Bitmap bm=decodeSampledBitmapFromResource(picturePath, w, h);
                if (!picturePath.equals("")) {
                   /* Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                    byte[] ba = bao.toByteArray();*/
                    //int w=userImg.getWidth();
                    //int h=userImg.getHeight();
                /* Get the size of the image */
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                     BitmapFactory.decodeFile(picturePath,bmOptions);
                   // BitmapFactory.decodeFile(picturePath, bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
                    int scaleFactor = 1;
                    if ((w > 0) || (h > 0)) {
                        scaleFactor = Math.min(photoW/w, photoH/h);
                    }

		/* Set bitmap options to scale the image decode target */
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                   // Bitmap unscaled=BitmapFactory.decodeFile(picturePath);
                   Bitmap scaled=bm.createScaledBitmap(bm,w,h,true);

                    Uri tempUri = getImageUri(getApplicationContext(), scaled);
                    String Data_Path = tempUri.getPath();
                    try
                    {
                        ExifInterface exif = new ExifInterface(picturePath);
                        int exifOrientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);
                        Log.v("MainController", "Orient: " + exifOrientation);
                        int rotate = 0;
                        switch (exifOrientation)
                        {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotate = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotate = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 270;
                                break;
                        }
                        Log.v("MainController", "Rotation: " + rotate);
                        if (rotate != 0)
                        {
                            // Getting width & height of the given image.
                            int w1 = userImg.getWidth();
                            int h1 = userImg.getHeight();
                            // Setting pre rotate
                            Matrix mtx = new Matrix();
                            mtx.preRotate(rotate);
                            // Rotating Bitmap
                            scaled = Bitmap.createBitmap(scaled, 0, 0, w1, h1, mtx, false);
                            userImg = (ImageView) findViewById(R.id.imageBtn);
                            userImg.setImageBitmap(scaled);
                        }else {
                            userImg = (ImageView) findViewById(R.id.imageBtn);
                            userImg.setImageBitmap(scaled);
                        }

                    }
                    catch (IOException e)
                    {
                        Log.e("MainController", "Couldn't correct orientation: " + e.toString());
                    }
//added lines

//added lines
                    // byte[] b = baos.toByteArray();
//                    b64 = Base64.encodeToString(ba, Base64.DEFAULT);

                    //   Login(b64,dateAndTime);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formattedDate = df.format(c.getTime());

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    scaled.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                    byte[] ba = bao.toByteArray();
                    b64 = Base64.encodeToString(ba,Base64.NO_WRAP);
                    //   LoadImage();

                }


                else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == 1 && resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

                picturePath=getPathFromURI(tempUri);
                Bitmap bm = BitmapFactory.decodeFile(picturePath);
                if (!picturePath.equals("")) {
                   /* Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                    byte[] ba = bao.toByteArray();*/

//added lines
                    userImg = (ImageView) findViewById(R.id.imageBtn);
                    userImg.setImageBitmap(bm);
//added lines
                    // byte[] b = baos.toByteArray();
//                    b64 = Base64.encodeToString(ba, Base64.DEFAULT);

                    //   Login(b64,dateAndTime);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formattedDate = df.format(c.getTime());

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                    byte[] ba = bao.toByteArray();
                    b64 = Base64.encodeToString(ba,Base64.NO_WRAP);



                    //   LoadImage();

                }
            }



        }

        catch(Exception e){
            e.printStackTrace();
            Log.e(e.getLocalizedMessage(), "test");
//            Toast.makeText(this, "", Toast.LENGTH_LONG)
//                    .show();
        }

    }


    public void LoadImage(){


        StringRequest request = new StringRequest(Request.Method.POST,"http://epay.cybussolutions.com/upload_image_mobile.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // loading.dismiss();
                ringProgressDialog.dismiss();
                if (!(response.equals(""))) {
                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pro_pic",response);
                    editor.apply();
                    updateProfile();

                } else {
                    ringProgressDialog.dismiss();
                    Toast.makeText(Profile.this, "Picture not uploaded", Toast.LENGTH_SHORT).show();
                }
            }

        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   loading.dismiss();
                ringProgressDialog.dismiss();
                String message = null;
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();

                                }
                            })
                            .show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("image", b64);
                map.put("name", formattedDate);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(request);

    }

    public  Bitmap rotateImageIfRequired(String imagePath) {
        int degrees = 0;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    //degrees = 180;

                    degrees = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    //degrees = 270;
                    degrees = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    //degrees = 360;

                    degrees = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e("ImageError", "Error in reading Exif data of " + imagePath, e);
        }

        BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
        decodeBounds.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
        int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
        int maxPixels = 2048 * 1536; // requires 12 MB heap

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;

        bitmap = BitmapFactory.decodeFile(imagePath, options);

        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

  /*public void Login()
    {

        //   loading = ProgressDialog.show(Login_Activity.this, "Please wait...", "Checking Credentails ...", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.POST_IMAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                // loading.dismiss();

                if (!(response.equals(""))) {

                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();





                } else {
                    Toast.makeText(Profile.this, "Incorrect User name or Password", Toast.LENGTH_SHORT).show();
                }
            }

        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   loading.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("image",imgDecodableString);
                map.put("name", dateAndTime);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(request);

    }*/

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMG);

    }
    private void camera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           /* uri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/
        startActivityForResult(intent, 1);
        //  imageView.mCurrentShape = DrawingView.SMOOTHLINE;
        //imageView.reset();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS || requestCode==REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                if(requestCode==0) {
                    gallery();
                }
                if(requestCode==1){
                    camera();
                }
            } else {


                Toast.makeText(Profile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
public void updateProfile(){
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final String img = preferences.getString("pro_pic","");

    //        if(number.matches()){}

    StringRequest request = new StringRequest(Request.Method.POST,End_Points.EDIT_profile, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            // loading.dismiss();

            if (!(response.equals(""))) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserEmail", etEmail.getText().toString());
                editor.putString("number",etNum.getText().toString());
                //editor.putString("email",email);
                editor.putString("name",etName.getText().toString());
                editor.apply();
                ringProgressDialog.dismiss();
                new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setConfirmText("OK").setContentText("Successful")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                Intent intent=new Intent(Profile.this,Profile_Detailed.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                SharedPreferences.Editor editor = preferences.edit();
//                                editor.putString("img",response);
//                                editor.apply();

            } else {
                Toast.makeText(Profile.this, "data not updated", Toast.LENGTH_SHORT).show();
            }
        }

    }
            , new Response.ErrorListener()

    {
        @Override
        public void onErrorResponse(VolleyError error) {
            //   loading.dismiss();
            ringProgressDialog.dismiss();
            String message = null;
            if (error instanceof NoConnectionError) {

                new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setConfirmText("OK").setContentText("No Internet Connection")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
            } else if (error instanceof TimeoutError) {

                new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();

                            }
                        })
                        .show();
            }

        }


    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            map.put("cust_id", id);
            map.put("cust_fname", name);
            map.put("cust_lname","");
            map.put("cust_phoneNum",etNum.getText().toString());
            map.put("cust_propic",img);
            map.put("cust_email",email);
            return map;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
    requestQueue.add(request);
}
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {
        Log.d("path", path);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

