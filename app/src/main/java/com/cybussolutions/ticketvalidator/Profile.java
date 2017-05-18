package com.cybussolutions.ticketvalidator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cybussolutions.ticketvalidator.Activities.History;
import com.cybussolutions.ticketvalidator.Activities.Login_Activity;
import com.cybussolutions.ticketvalidator.Activities.MainScreen;
import com.cybussolutions.ticketvalidator.Activities.Payment_Method;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class Profile extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

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
    //StringEntity se;
    String entityContents="";
//

    AlertDialog myalertdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        //   AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        context = getApplicationContext();


        userImg = (ImageView) findViewById(R.id.imageBtn);
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
                    startActivityForResult(intent, 188);


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


                }


                else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == 188 && resultCode == Activity.RESULT_OK) {

                Toast.makeText(getApplicationContext(),"ASDFGH",Toast.LENGTH_LONG).show();



                Uri selectedImage = data.getData();
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        userImg.setImageBitmap(bitmap);

              ///userImg.setImageBitmap(rotateImageIfRequired(selectedImage.getPath()));


                b64 = encodeImage(bitmap);


            }



        }

        catch(Exception e){
            e.printStackTrace();
            Log.e(e.getLocalizedMessage(), "this was the behan yakki");
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


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







        }

