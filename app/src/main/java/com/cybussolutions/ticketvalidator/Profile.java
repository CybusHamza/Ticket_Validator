package com.cybussolutions.ticketvalidator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.cybussolutions.ticketvalidator.Network.End_Points;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mikepenz.materialdrawer.Drawer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Profile extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;


    EditText etEmail, etName, etNum;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Context context;

    ImageView userImg;
    Drawer result;

//    PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
//    SecondaryDrawerItem payment = new SecondaryDrawerItem()
//            .withIdentifier(2).withName("Payment");
//    SecondaryDrawerItem your_trips = new SecondaryDrawerItem()
//            .withIdentifier(3).withName("Your Trips");
//
//    SecondaryDrawerItem EditProfile = new SecondaryDrawerItem()
//            .withIdentifier(4).withName("Edit Profile");
//
//
//    SecondaryDrawerItem logout = new SecondaryDrawerItem()
//            .withIdentifier(5).withName("Logout");
   String b64;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    String email,name,number,id,fname,lname;
    //StringEntity se;
    String entityContents="";
//



    public static Phonenumber.PhoneNumber phonenumberProto;
    PhoneNumberUtil phoneNumberUtil;
    Boolean isValid;




    String dateAndTime;
    AlertDialog myalertdialog;
    Calendar calender;
    int seconds;
    Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        //   AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        etEmail = (EditText) findViewById(R.id.userEmail);
        etName = (EditText) findViewById(R.id.userName);
        etNum = (EditText) findViewById(R.id.userNumber);
        btnUpdate = (Button)findViewById(R.id.update);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            email = preferences.getString("UserEmail","");
        number = preferences.getString("number","");
      name =  preferences.getString("name","");
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


                    String[] names = name.split(" ");
                    fname = names[0];
                    lname = names[2];


                    if (number.length() > 0) {

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
                        if (isValid && email.matches(EMAIL_PATTERN)) {

                            Toast.makeText(getApplicationContext(), "is Valid", Toast.LENGTH_LONG).show();




                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            final String img = preferences.getString("img","");




                            //        if(number.matches()){}


                            StringRequest request = new StringRequest(Request.Method.POST,End_Points.EDIT_profile, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    // loading.dismiss();

                                    if (!(response.equals(""))) {
                                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                                    map.put("cust_id", id);
                                    map.put("cust_fname", fname);
                                    map.put("cust_lname",lname);
                                    map.put("cust_phoneNum",number);
                                    map.put("cust_propic",img);
                                    map.put("cust_email",email);
                                    return map;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
                            requestQueue.add(request);



                            if (b64 != null){
                                LoadImage();

                            }

                        }





                        if (isValid && !(email.matches(EMAIL_PATTERN))) {

                            Toast.makeText(getApplicationContext(), "Invalid Pattern", Toast.LENGTH_LONG).show();


                        } else if (number.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Mobile Number is required", Toast.LENGTH_SHORT).show();

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
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


                    }
                });
                camera.setOnClickListener(new View.OnClickListener() {
                @Override
              public void onClick(View view) {


                    myalertdialog.dismiss();

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);


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


                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                if (!picturePath.equals("")) {
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//added lines
                    bm.recycle();
                    bm = null;
                    userImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//added lines
                    byte[] b = baos.toByteArray();
                    b64 = Base64.encodeToString(b, Base64.DEFAULT);

                 //   Login(b64,dateAndTime);



             //   LoadImage();



            }


                else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

               // Toast.makeText(getApplicationContext(),"ASDFGH",Toast.LENGTH_LONG).show();



                Uri selectedImage = data.getData();
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        userImg.setImageBitmap(bitmap);

              ///userImg.setImageBitmap(rotateImageIfRequired(selectedImage.getPath()));


                b64 = encodeImage(bitmap);
               // LoadImage();
            }



        }

        catch(Exception e){
            e.printStackTrace();
            Log.e(e.getLocalizedMessage(), "this was the behan yakki");
//            Toast.makeText(this, "", Toast.LENGTH_LONG)
//                    .show();
        }

        }


public void LoadImage(){


    StringRequest request = new StringRequest(Request.Method.POST,"http://epay.cybussolutions.com/upload_image_mobile.php", new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            // loading.dismiss();

            if (!(response.equals(""))) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("img",response);
                editor.apply();





            } else {
                Toast.makeText(Profile.this, "Picture not uploaded", Toast.LENGTH_SHORT).show();
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
            map.put("image", b64);
            map.put("name", dateAndTime);
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







    public void Login()
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

    }






        }

