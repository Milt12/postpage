package com.example.milt.profilepage;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface;



public class MainActivity extends AppCompatActivity implements OnClickListener {
    private ImageButton mDoneButton;
    private ImageView imageView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView)findViewById(R.id.imageview);
        mDoneButton = (ImageButton) findViewById(R.id.imagebutton1);
        mDoneButton.setOnClickListener(MainActivity.this);

        locationpermission();

    }

    @Override
    public void onClick(View v) {

            List<String> mAnimals = new ArrayList<String>();
            mAnimals.add("Camera");
            mAnimals.add("Gallery");


            //Create sequence of items
            final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Animals");
            dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    String selectedText = Animals[item].toString();
                      //Selected item in listview
                    if(selectedText=="Gallery"){
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                    else if (selectedText=="Camera"){
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }
                }
            });
            //Create alert dialog object via builder
            AlertDialog alertDialogObject = dialogBuilder.create();
            //Show the dialog
            alertDialogObject.show();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                try{
                    Bitmap d = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageReturnedIntent.getData());
                    int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );

                    Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
                    imageView.setImageBitmap(scaled);
                //    Toast.makeText(getApplicationContext(),"Image Resolution is too high.Take screenshot and then upload.",Toast.LENGTH_LONG).show();

                   /* Uri selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();


                        Bitmap d = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
                        Toast.makeText(getApplicationContext(),"there",Toast.LENGTH_LONG).show();
                        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);

                        imageView.setImageBitmap(scaled);*/
                    } catch (Exception e) {
                       Toast.makeText(getApplicationContext(),"If image doesn't load,take screenshot and upload again.",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                  //  Bitmap d = new BitmapDrawable(getApplicationContext().getResources() , selectedImage.getPath()).getBitmap();

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
    }


    private void locationpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this
                ,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)     {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}