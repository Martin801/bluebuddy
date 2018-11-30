package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CharterDetail;
import com.bluebuddy.models.CharterDetailResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class BlueMarketCharterPicEditActivity extends BuddyActivity {

    public static String pass_from = "";
    ImageButton back;
    Button chrtpicupld;
    String myimage, gotimg;
    File fileImage = null;
    String img_url_From_server = "";
    String pass = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    private Activity _myActivity;
    private Context _mContext;
    private ImageView imageView;
    private SharedPreferences pref;
    private String token, id;
    private String encoded_image = "";
    private String userProfile = "";
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;
    private String imagepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_charter_pic);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("AllCharter");
        pass = bundle.getString("pass");
        // Log.d("nipaerror",pass_from);
        if (pass == null || pass.matches("")) {
            pass = pass_from;
        } else {
            pass_from = pass;
        }

        back = (ImageButton) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redict changes
                // Intent i = new Intent(BlueMarketCharterPicEditActivity.this, BlueMarketActivity2.class);
                /*Intent i = new Intent(BlueMarketCharterPicEditActivity.this, MyListingActivity.class);
                startActivity(i);*/

                backPressCall();
            }
        });

        MyTextView tx = (MyTextView) findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        _myActivity = this;
        _mContext = this;
        final String rc = "BoatCharterFormActivity";
        imageView = (ImageView) findViewById(R.id.imgcharpic);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        apiEbcp();
        chrtpicupld = (Button) findViewById(R.id.charterpic);

        chrtpicupld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileImage != null && fileImage.length() > 0) {

                    Intent intent = new Intent(BlueMarketCharterPicEditActivity.this, BoatCharterFormEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("encoded_image", imagepath);
                    bundle.putString("DATA_VALUE", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

//                    if (progressDialog == null) {
//                        progressDialog = new ProgressDialog(_myActivity);
//                        progressDialog.setTitle(getString(R.string.loading_title));
//                        progressDialog.setCancelable(false);
//                        progressDialog.setMessage(getString(R.string.loading_message));
//                    }
//                    progressDialog.show();
//                    MultipartBody.Part body = null;
//
//                    RequestBody requestBodyId = RequestBody.create(
//                            MediaType.parse("multipart/form-data"), id);
//
//
//                    if (fileImage != null && fileImage.length() > 1) {
//                        RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
//                        body = MultipartBody.Part.createFormData("ch_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
//                    }
//                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//
//                    //Call<ServerResponsechrt> userCall = service.Charterimgupdate("Bearer " + token, id, userProfile);
//                    Call<ServerResponsechrt> userCall = service.CharterimgupdateMultipart("Bearer " + token, requestBodyId, body);
//
//                    userCall.enqueue(new Callback<ServerResponsechrt>() {
//                        @Override
//                        public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                            }
//
//                            if (response.body() != null) {
//                                if (response.body().getStatus() == "true") {
//                                    Log.d("onResponse", "" + response.body().getStatus());
//
//                                    oDialog(response.body().getMessage(), "Next", "", true, _myActivity, "BoatCharterFormEditActivity", 0, id);
//                                } else {
//
//                                    oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0, "");
//                                    // Log.d("onResponse", "" + response.body().getStatus());
//                                }
//                            } else {
//                                String msg = (String) getApplication().getText(R.string.goeswrong);
//                                oDialog(msg, "Ok", "", false, _myActivity, "", 0, "");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
//                            t.printStackTrace();
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                            }
//                            Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
//                        }
//                    });

                } else {
                    if ((img_url_From_server.matches("") || img_url_From_server == null) && (fileImage == null || fileImage.length() < 1)) {
                        oDialog("Please upload image ", "Ok", "", false, _myActivity, "", 0, "");
                        return;
                    } else {
                        Intent intent = new Intent(BlueMarketCharterPicEditActivity.this, BoatCharterFormEditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("DATA_VALUE", id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                // userProfile = "data:image/jpeg;base64," + selectImage.onSelectFromGalleryResultSaquare(data, "");
                imagepath = selectImage.getRealPathFromURI(data.getData());
                fileImage = null;
                fileImage = new File(imagepath);
                Bitmap bm = null;
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageView.setImageBitmap(bm);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                fileImage = selectImage.SaveImage(photo);
                imageView.setImageBitmap(photo);

            }
        }
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
        new android.app.AlertDialog.Builder(BlueMarketCharterPicEditActivity.this)
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
                    Toast.makeText(BlueMarketCharterPicEditActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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
                                ActivityCompat.requestPermissions(BlueMarketCharterPicEditActivity.this,
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
        selectImage = new SelectImage(_myActivity, _mContext);
        selectImage.selectImage();
    }

    public void Charterpic(View view) {
        checkforpermission();

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String data) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, data);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void apiEbcp() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CharterDetailResponse> userCall = service.chrtDt("Bearer " + token, id);

        userCall.enqueue(new Callback<CharterDetailResponse>() {
            @Override
            public void onResponse(Call<CharterDetailResponse> call, Response<CharterDetailResponse> response) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (response.body().getStatus() == true) {
                    mProgressDialog.dismiss();
                    String tOKEN = response.body().getToken();
                    img_url_From_server = response.body().getDetails().getImage();

                    CharterDetail details = response.body().getDetails();
                    myimage = details.getImage();

                    Glide.with(BlueMarketCharterPicEditActivity.this).asBitmap().load(IMG_URL + img_url_From_server).into(new BitmapImageViewTarget(imageView));
                    userProfile = "";

                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CharterDetailResponse> call, Throwable t) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog10(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(BlueMarketCharterPicEditActivity.this, BlueMarketActivity2.class);
        startActivity(i);*/
        backPressCall();
    }

    void backPressCall() {

        pass_from = "";
        if (pass.matches("2")) {
            Intent i = new Intent(BlueMarketCharterPicEditActivity.this, MyListingActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(BlueMarketCharterPicEditActivity.this, BlueMarketCharterActivityNew.class);
            startActivity(i);
        }
    }
}
