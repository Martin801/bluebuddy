package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BlueMarketCharterPicActivity extends BuddyActivity {
    Activity _myActivity;
    ImageButton back;
    Button chrtpicupld;
    String backpass, ract;
    String imagepath = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    // private SelectImage1 selectImage;
    private SelectImage selectImage;
    private Activity activity;
    private Context context;
    private ImageView imageView;
    private SharedPreferences pref;
    private String token;
    private String encoded_image = "";
    private Context _mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_charter_pic);
        super.onCreate(savedInstanceState);
        _mContext = this;
        Bundle bundle = getIntent().getExtras();
        backpass = bundle.getString("bmcharter");
        String BP = backpass.toString().trim();
        if (BP.equals("1")) {
            ract = "BlueMarketCharterActivityNew";
        }
        //  category3 = bundle.getString("category2");
        else if (BP.equals("2")) {
            /*  ract = "BlueMarketActivity22";*/
            ract = "Categories_bluemarketActivity2";
        } else if (BP.equals("0")) {
            /*  ract = "BlueMarketActivity22";*/
            //ract = "Categories_bluemarketActivity2";
            ract = "Categories_bluemarketActivity";
        }


        back = (ImageButton) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = null;
//                try {
//                    i = new Intent(BlueMarketCharterPicActivity.this, Class.forName("com.bluebuddy.activities." + ract));
//                    startActivity(i);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }

                onBackPressed();

            }
        });


        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        this.activity = this;
        this.context = this;

        final String rc = "BoatCharterFormActivity";
        imageView = (ImageView) findViewById(R.id.imgcharpic);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        chrtpicupld = (Button) findViewById(R.id.charterpic);

        chrtpicupld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable() == null || imagepath == "") {
                    oDialog("Please select charter image", "Close", "", false, _myActivity, "", 0);
                }
                boolean hasDrawable = (imageView.getDrawable() != null || !imagepath.matches(""));
                if (hasDrawable) {
                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BoatCharterFormActivity", 0, imagepath, "");
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                imagepath = selectImage.getRealPathFromURI(data.getData());
                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imageView.setImageBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;
                }
                //  encoded_image = selectImage.onSelectFromGalleryResultSaquare(data, "");
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imagepath = selectImage.SaveImageAndReturnPath(photo);
                // fileImage=selectImage.SaveImage(photo);
                imageView.setImageBitmap(photo);
                // encoded_image = selectImage.onCaptureImageResultSqaure(data, "");
            }

        }
    }

    public void Charterpic(View view) {
        checkforpermission();
      /*  selectImage = new SelectImage1(activity, context, (ImageView) view);
        selectImage.selectImage();*/
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(BlueMarketCharterPicActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utility.RECORD_REQUEST_CODE) {

            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    selectImages();
                } else {
                    // Permission Denied
                    Toast.makeText(BlueMarketCharterPicActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void checkforpermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BlueMarketCharterPicActivity.this,
                                        permissionsList.toArray(new String[permissionsList.size()]),
                                        Utility.RECORD_REQUEST_CODE);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    Utility.RECORD_REQUEST_CODE);
            return;
        }
        selectImages();

    }

    private void selectImages() {
        selectImage = new SelectImage(_myActivity, _mContext, imageView);
        selectImage.selectImage();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String data) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, data);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {
//        Intent i = new Intent(BlueMarketCharterPicActivity.this, BlueMarketActivity2.class);
//        startActivity(i);
//
//        try {
//            i = new Intent(BlueMarketCharterPicActivity.this, Class.forName("com.bluebuddy.activities." + ract));
//            startActivity(i);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        finish();
    }
}
