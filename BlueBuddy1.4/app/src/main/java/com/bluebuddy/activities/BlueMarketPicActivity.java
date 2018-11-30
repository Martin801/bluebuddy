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

public class BlueMarketPicActivity extends BuddyActivity {

    Activity _myActivity;
    Button BMpic;
    ImageView back;
    String imagepath1 = "", imagepath2 = "", imagepath3 = "", imagepath4 = "";
    int imageCount = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    private Context context;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private SharedPreferences pref;
    private String token, id, backpass, ract, categor;
    private String encoded_image = "";
    private String encodedImageList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_pic);
        super.onCreate(savedInstanceState);

        _myActivity = this;
        this.context = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        imageView1 = (ImageView) findViewById(R.id.imgfrst);
        imageView2 = (ImageView) findViewById(R.id.imgscnd);
        imageView3 = (ImageView) findViewById(R.id.imgthrd);
        imageView4 = (ImageView) findViewById(R.id.imgfrth);

        /*Bundle b = getIntent().getExtras();
        final String categor = b.getString("category");*/
        Bundle b = getIntent().getExtras();
        categor = b.getString("category");
        backpass = b.getString("bmproduct");
        String BP = backpass.toString().trim();
        if (BP.equals("1")) {
            ract = "BlueMarketProductActivityNew";
        }
        //  category3 = bundle.getString("category2");
        else if (BP.equals("2")) {
            ract = "Categories_bluemarketActivity2";
        } else if (BP.equals("0")) {
            /*ract = "Categories_bluemarketActivity2";*/
            ract = "Categories_bluemarketActivity";
        }
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(BlueMarketPicActivity.this,BlueMarketProductActivityNew.class);
            //   i.putExtra("category",categor);
                startActivity(i);*/

                onBackPressed();
            }
        });

        BMpic = (Button) findViewById(R.id.bmpic);
        //  TextView tx = (TextView) findViewById(R.id.idtxt1);
        // Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        //  tx.setTypeface(custom_font);
        BMpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  boolean hasDrawable = (imageView1.getDrawable() != null || imageView2.getDrawable() != null || imageView3.getDrawable() != null || imageView4.getDrawable() != null || encodedImageList != "");

                if (!imagepath1.matches("")) {
                    encodedImageList = imagepath1;
                }

                if (!imagepath2.matches("")) {
                    if (encodedImageList.matches("")) {
                        encodedImageList = imagepath2;
                    } else {
                        encodedImageList += "," + imagepath2;
                    }
                }

                if (!imagepath3.matches("")) {
                    if (encodedImageList.matches("")) {
                        encodedImageList = imagepath3;
                    } else {
                        encodedImageList += "," + imagepath3;
                    }
                }

                if (!imagepath4.matches("")) {
                    if (encodedImageList.matches("")) {
                        encodedImageList = imagepath4;
                    } else {
                        encodedImageList += "," + imagepath4;
                    }
                }

                if (encodedImageList.matches("")) {
                    oDialog("Please select Product image", "Close", "", false, _myActivity, "", 0);
                } else {
                    if (encodedImageList.length() > AppConfig.IMG_LENTH_CHECK) {
                        String tmpImg = encodedImageList.substring(0, encodedImageList.length() - 1);
                        // oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductActivity", 0, tmpImg, "");
                        oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductActivity", 0, encodedImageList, "");
                    } else {
                        oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductActivity", 0, "", "");
                    }
                }
            }
        });
    }

    public void frstpic(View view) {
        imageCount = 1;
        //   selectImage = new SelectImage1(activity, context, (ImageView) view);
        checkforpermission();
    }

    public void scndpic(View view) {
        imageCount = 2;
        // selectImage = new SelectImage1(activity, context, (ImageView) view);
        checkforpermission();
    }

    //third
    public void thrdpic(View view) {
        imageCount = 3;
        // selectImage = new SelectImage1(activity, context, (ImageView) view);
        checkforpermission();
    }

    public void fourthpic(View view) {
        imageCount = 4;
        //  selectImage = new SelectImage1(activity, context, (ImageView) view);
        checkforpermission();
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
        new android.app.AlertDialog.Builder(BlueMarketPicActivity.this)
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
                    Toast.makeText(BlueMarketPicActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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
                                ActivityCompat.requestPermissions(BlueMarketPicActivity.this,
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
        selectImage = new SelectImage(_myActivity, context);
        selectImage.selectImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                String imagepathFromSelect = selectImage.getRealPathFromURI(data.getData());
                if (imagepathFromSelect != null && imagepathFromSelect.trim().length() > AppConfig.IMG_LENTH_CHECK) {
                    Bitmap bm = null;
                    try {

                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                        if (imageCount == 2) {
                            imageView2.setImageBitmap(bm);
                            imagepath2 = imagepathFromSelect;
                        } else if (imageCount == 3) {
                            imageView3.setImageBitmap(bm);
                            imagepath3 = imagepathFromSelect;
                        } else if (imageCount == 4) {
                            imageView4.setImageBitmap(bm);
                            imagepath4 = imagepathFromSelect;
                        } else {
                            imageView1.setImageBitmap(bm);
                            imagepath1 = imagepathFromSelect;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                if (imageCount == 2) {
                    imageView2.setImageBitmap(photo);
                    imagepath2 = selectImage.SaveImageAndReturnPath(photo);
                } else if (imageCount == 3) {
                    imageView3.setImageBitmap(photo);
                    imagepath3 = selectImage.SaveImageAndReturnPath(photo);
                } else if (imageCount == 4) {
                    imageView4.setImageBitmap(photo);
                    imagepath4 = selectImage.SaveImageAndReturnPath(photo);
                } else {
                    imageView1.setImageBitmap(photo);
                    imagepath1 = selectImage.SaveImageAndReturnPath(photo);
                }
                //   encoded_image = selectImage.onCaptureImageResultSqaure(data, "");
            }
        }
    }

// end all permission

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String encodedImageList) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, encodedImageList);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    @Override
    public void onBackPressed() {
        Intent i = null;
        try {
            i = new Intent(BlueMarketPicActivity.this, Class.forName("com.bluebuddy.activities." + ract));
            i.putExtra("category", categor);
            startActivity(i);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
