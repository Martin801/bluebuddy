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
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CourseDetail;
import com.bluebuddy.models.CourseDetailResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

public class BlueMarketCoursePicEditActivity extends BuddyActivity {

    ImageButton back;
    Button chrtpicupld;
    File fileImage = null;
    String img_url_From_server = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    // private Activity activity;
    private Activity _myActivity;
    private Context _mContext;
    private ImageView imageView;
    private SharedPreferences pref;
    private String token, id;
    private String encoded_image = "";
    private String userProfile = "";
    private String category = "";
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_charter_pic);
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("COURSE_ID");
        category = bundle.getString("category");
        back = (ImageButton) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(BlueMarketCoursePicEditActivity.this, BlueMarketActivity2.class);
                startActivity(i);*/
                backPressCall();
            }
        });
        TextView tx = (TextView) findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        _myActivity = this;
        _mContext = this;
        imageView = (ImageView) findViewById(R.id.imgcharpic);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        // Log.d("token in bceact:", token);

        // Bundle b = getIntent().getExtras();

        loadcourseimage();
        chrtpicupld = (Button) findViewById(R.id.charterpic);

        chrtpicupld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileImage != null && fileImage.length() > 0) {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(_myActivity);
                        progressDialog.setTitle(getString(R.string.loading_title));
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(getString(R.string.loading_message));
                    }
                    progressDialog.show();
                    MultipartBody.Part body = null;

                    RequestBody requestBodyId = RequestBody.create(
                            MediaType.parse("multipart/form-data"), id);


                    if (fileImage != null && fileImage.length() > 1) {
                        RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                        body = MultipartBody.Part.createFormData("co_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
                    }
                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<ServerResponsechrt> userCall = service.CourseimgupdateMultipart("Bearer " + token, requestBodyId, body);

                    userCall.enqueue(new Callback<ServerResponsechrt>() {
                        @Override
                        public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            if (response.body() != null) {
                                if (response.body().getStatus() == "true") {
                                    oDialog(response.body().getMessage(), "Next", "Cancel", true, _myActivity, "CourseEditActivity", 0, category, id);
                                } else {

                                    oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0, "", "");
                                    // Log.d("onResponse", "" + response.body().getStatus());
                                }
                            } else {
                                String msg = (String) getApplication().getText(R.string.goeswrong);
                                oDialog(msg, "Ok", "", false, _myActivity, "", 0, "", "");
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                            t.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    if ((img_url_From_server.matches("") || img_url_From_server == null) && (fileImage == null || fileImage.length() < 1)) {
                        oDialog("Please upload image ", "Ok", "", false, _myActivity, "", 0, "", "");
                        return;
                    } else {
                        Intent intent = new Intent(BlueMarketCoursePicEditActivity.this, CourseEditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("DATA_VALUE", id);
                        bundle.putString("encoded_image", category);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
              /*  ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                Call<AllCourse> userCall = service.Courseimgupdate("Bearer " + token, id, userProfile);

                userCall.enqueue(new Callback<AllCourse>() {
                    @Override
                    public void onResponse(Call<AllCourse> call, Response<AllCourse> response) {

                        if (response.body().getStatus() == true) {

                            Log.d("onResponse", "" + response.body().getStatus());
                            oDialog("Course Image updated Successfully.", "Submit", "Cancel", true, _myActivity, "CourseEditActivity", 0, category, id);
                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<AllCourse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });*/
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                String imagepath = selectImage.getRealPathFromURI(data.getData());
                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {
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
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;
                }
                // userProfile = "data:image/jpeg;base64," + selectImage.onSelectFromGalleryResultSaquare(data, "");
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                fileImage = selectImage.SaveImage(photo);
                imageView.setImageBitmap(photo);
                //userProfile = "data:image/jpeg;base64," + selectImage.onCaptureImageResultSqaure(data, "");
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
        new android.app.AlertDialog.Builder(BlueMarketCoursePicEditActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Some Permission is Denied", Toast.LENGTH_SHORT)
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
                                ActivityCompat.requestPermissions(BlueMarketCoursePicEditActivity.this,
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
      /*  selectImage = new SelectImage1(_myActivity, _mContext);
        selectImage.selectImage();*/
        checkforpermission();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String data) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, data);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void loadcourseimage() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CourseDetailResponse> userCall = service.courseDt("Bearer " + token, id);
        //  Log.d("find id", id);
        userCall.enqueue(new Callback<CourseDetailResponse>() {
            @Override
            public void onResponse(Call<CourseDetailResponse> call, Response<CourseDetailResponse> response) {
                if (response.body().getStatus() == true) {
                    String tOKEN = response.body().getToken();
                    CourseDetail details = response.body().getDetails();
                    //  Log.d("find id", id);
                    // Log.d("find img", details.getImage());
                    img_url_From_server = details.getImage();
                    Glide.with(BlueMarketCoursePicEditActivity.this).asBitmap().load(IMG_URL + details.getImage()).into(new BitmapImageViewTarget(imageView));
                    userProfile = "";

                    Log.d("onResponse end here", "" + tOKEN);
                } else {
                }
            }

            @Override
            public void onFailure(Call<CourseDetailResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private String decodeImageFromUrl(String img) {
        Bitmap bm = null;
        String image = "";

        if (!img.equals("")) {
            try {
                URL url = new URL(IMG_URL + img);
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                image = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return image;
    }

    @Override
    public void onBackPressed() {
        backPressCall();
      /*  Intent i = new Intent(BlueMarketCoursePicEditActivity.this, BlueMarketActivity2.class);
        startActivity(i);*/
    }

    void backPressCall() {
        if (category == null || category.matches("")) {
            Intent i = new Intent(BlueMarketCoursePicEditActivity.this, MyListingActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(BlueMarketCoursePicEditActivity.this, BlueMarketCourseActivityNew.class);
            i.putExtra("category", category);
            startActivity(i);
        }
    }

}
