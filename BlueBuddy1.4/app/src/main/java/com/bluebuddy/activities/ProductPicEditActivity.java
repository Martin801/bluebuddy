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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.ProductDetail;
import com.bluebuddy.models.ProductDetailResponse;
import com.bluebuddy.models.ProductImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class ProductPicEditActivity extends BuddyActivity implements View.OnClickListener {

    private static final String TAG = "ProductPicEditActivity";
    public static String pass_from = "";
    ImageButton back;
    Button chrtpicupld;
    String myimage, gotimg;
    File fileImage = null;
    String img_url_From_server = "";
    String pass = "";
    int imageCount = 0;
    String imagepath1 = "", imagepath2 = "", imagepath3 = "", imagepath4 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    // private Activity activity;
    private Activity _myActivity;
    private Context _mContext;
    private ImageView imageView;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private SharedPreferences pref;
    private String token, id, category;
    private String encoded_image = "";
    private String userProfile = "";
    private String encodedImageList = "";
    private String img = "";
    private String pic1, pic2, pic3, pic4 = "";
    private String tmpImg = "";
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;
    private Button BMpic;
    private ImageView iv_removeImage1, iv_removeImage2, iv_removeImage3, iv_removeImage4;
    private List<ProductImage> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        super.setLayout(R.layout.activity_blue_market_charter_pic);

        super.setLayout(R.layout.activity_blue_market_pic);
        super.onCreate(savedInstanceState);
        super.loader();

        init();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("AllProduct");
        category = bundle.getString("category");
        pass = bundle.getString("pass");

        if (pass == null || pass.matches("")) {
            pass = pass_from;
        } else {
            pass_from = pass;
        }

        back = (ImageButton) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(ProductPicEditActivity.this, BlueMarketActivity4.class);
                startActivity(i);*/
                onBackPressed();
            }
        });

        TextView tx = (TextView) findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        _myActivity = this;
        _mContext = this;
        final String rc = "BoatCharterFormActivity";
        imageView = (ImageView) findViewById(R.id.imgcharpic);
        pref = super.getPref();
        //  Log.d("token in bceact:", token);
        apiEbpp(id);
//        chrtpicupld = (Button) findViewById(R.id.charterpic);
//
//        chrtpicupld.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (fileImage != null && fileImage.length() > 0) {
//                    if (progressDialog == null) {
//                        progressDialog = new ProgressDialog(_myActivity);
//                        progressDialog.setTitle(getString(R.string.loading_title));
//                        progressDialog.setMessage(getString(R.string.loading_message));
//                    }
//                    progressDialog.show();
//                    MultipartBody.Part body = null;
//
//                    RequestBody requestBodyId = RequestBody.create(
//                            MediaType.parse("multipart/form-data"), id);
//                    if (fileImage != null && fileImage.length() > 1) {
//
//                        RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
//
//                        body = MultipartBody.Part.createFormData("pr_img1", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
//                    }
//                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//
//                    //Call<ServerResponsechrt> userCall = service.Charterimgupdate("Bearer " + token, id, userProfile);
//                    Call<ServerResponsechrt> userCall = service.ProductimgupdateMultipart("Bearer " + token, requestBodyId, body);
//
//                    userCall.enqueue(new Callback<ServerResponsechrt>() {
//                        @Override
//                        public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                            }
//
//                            //  Log.d("onResponse", "" + response.body().getMessage());
//                            if (response.body() != null) {
//                                if (response.body().getStatus() == "true") {
//                                    Log.d("onResponse", "" + response.body().getStatus());
//                                    oDialog("Product Image updated Successfully.", "Ok", "", true, _myActivity, "BlueMarketProductEditActivity", 0, id, category);
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
//
//                } else {
//
//                    if ((img_url_From_server.matches("") || img_url_From_server == null) && (fileImage == null || fileImage.length() < 1)) {
//                        oDialog("Please upload image ", "Ok", "", false, _myActivity, "", 0, "");
//                        return;
//                    } else {
//                        Intent intent = new Intent(ProductPicEditActivity.this, BlueMarketProductEditActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("encoded_image", id);
//                        bundle.putString("DATA_VALUE", category);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                }
//
//                // Log.d("got image", userProfile);
//             /*   ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//
//                Call<ServerResponsechrt> userCall = service.Productimgupdate("Bearer " + token, id, userProfile);
//
//                userCall.enqueue(new Callback<ServerResponsechrt>() {
//                    @Override
//                    public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
//
//                       // Log.d("onResponse", "" + response.body().getMessage());
//
//                        if (response.body().getStatus() == "true") {
//                         //   Log.d("onResponse", "" + response.body().getStatus());
//                            oDialog("Product Image updated Successfully.", "Submit", "", true, _myActivity, "BlueMarketProductEditActivity", 0, id, category);
//
//                        } else {
//                           // Log.d("onResponse", "" + response.body().getStatus());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
//                        if (mProgressDialog.isShowing()) {
//                            mProgressDialog.dismiss();
//                        }
//                        t.printStackTrace();
//                    }
//                });*/
//
//            }
//        });
    }

    private void init() {
        Toast.makeText(this, "Inside init",Toast.LENGTH_SHORT).show();
        imageView1 = (ImageView) findViewById(R.id.imgfrst);
        imageView2 = (ImageView) findViewById(R.id.imgscnd);
        imageView3 = (ImageView) findViewById(R.id.imgthrd);
        imageView4 = (ImageView) findViewById(R.id.imgfrth);

        iv_removeImage1 = (ImageView) findViewById(R.id.iv_removeImage1);
        iv_removeImage1.setOnClickListener(this);
        iv_removeImage1.setVisibility(View.GONE);
        Toast.makeText(this, iv_removeImage1.getVisibility()+"",Toast.LENGTH_SHORT).show();

        iv_removeImage2 = (ImageView) findViewById(R.id.iv_removeImage2);
        iv_removeImage2.setOnClickListener(this);
        iv_removeImage2.setVisibility(View.GONE);

        iv_removeImage3 = (ImageView) findViewById(R.id.iv_removeImage3);
        iv_removeImage3.setOnClickListener(this);
        iv_removeImage3.setVisibility(View.GONE);

        iv_removeImage4 = (ImageView) findViewById(R.id.iv_removeImage4);
        iv_removeImage4.setOnClickListener(this);
        iv_removeImage4.setVisibility(View.GONE);

        BMpic = (Button) findViewById(R.id.bmpic);
        BMpic.setOnClickListener(this);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE) {
//                String imagepath = selectImage.getRealPathFromURI(data.getData());
//                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {
//                    fileImage = null;
//                    fileImage = new File(imagepath);
//                    Bitmap bm = null;
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        imageView.setImageBitmap(bm);
//                        //  imageView.setImageBitmap(selectImage.getCircleBitmap(bm));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // userProfile = "data:image/jpeg;base64," + selectImage.onSelectFromGalleryResultSaquare(data, "");
//            } else if (requestCode == REQUEST_CAMERA) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                fileImage = selectImage.SaveImage(photo);
//                imageView.setImageBitmap(photo);
//                // userProfile = "data:image/jpeg;base64," + selectImage.onCaptureImageResultSqaure(data, "");
//            }
//        }
//
//    }

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
        new android.app.AlertDialog.Builder(ProductPicEditActivity.this)
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
                    Toast.makeText(ProductPicEditActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                                ActivityCompat.requestPermissions(ProductPicEditActivity.this,
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
                            iv_removeImage2.setVisibility(View.VISIBLE);
                            imageView2.setImageBitmap(bm);
                            imagepath2 = imagepathFromSelect;
                        } else if (imageCount == 3) {
                            iv_removeImage3.setVisibility(View.VISIBLE);
                            imageView3.setImageBitmap(bm);
                            imagepath3 = imagepathFromSelect;
                        } else if (imageCount == 4) {
                            iv_removeImage4.setVisibility(View.VISIBLE);
                            imageView4.setImageBitmap(bm);
                            imagepath4 = imagepathFromSelect;
                        } else {
                            iv_removeImage1.setVisibility(View.VISIBLE);
                            imageView1.setImageBitmap(bm);
                            imagepath1 = imagepathFromSelect;
                            iv_removeImage1.setVisibility(View.VISIBLE);
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
                    iv_removeImage2.setVisibility(View.VISIBLE);
                    imageView2.setImageBitmap(photo);
                    imagepath2 = selectImage.SaveImageAndReturnPath(photo);
                } else if (imageCount == 3) {
                    iv_removeImage3.setVisibility(View.VISIBLE);
                    imageView3.setImageBitmap(photo);
                    imagepath3 = selectImage.SaveImageAndReturnPath(photo);
                } else if (imageCount == 4) {
                    iv_removeImage4.setVisibility(View.VISIBLE);
                    imageView4.setImageBitmap(photo);
                    imagepath4 = selectImage.SaveImageAndReturnPath(photo);
                } else {
                    iv_removeImage1.setVisibility(View.VISIBLE);
                    imageView1.setImageBitmap(photo);
                    imagepath1 = selectImage.SaveImageAndReturnPath(photo);
                }
                //   encoded_image = selectImage.onCaptureImageResultSqaure(data, "");
            }

        }
    }

//    public void Charterpic(View view) {
//        /*selectImage = new SelectImage1(_myActivity, _mContext);
//        selectImage.selectImage();*/
//        checkforpermission();
//    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String data) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, data);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void apiEbpp(String id) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> userCall = service.productDt("Bearer " + token, id);
        userCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                if (response.body() != null && response.body().getStatus() == true) {

                    ProductDetail details = response.body().getDetails();
                    // Log.d("Got status", "" + response.body().getStatus());
                    // Log.d("IMAGE:", response.body().getDetails().getImage());
                    //  img_url_From_server = response.body().getDetails().getImage();
                    images = response.body().getDetails().getProductImages();

                    for (int i = 0; i < images.size(); i++) {
                        switch (i) {
                            case 0:
                                Glide.with(ProductPicEditActivity.this).asBitmap().load(IMG_URL + images.get(i).getImage()).into(new BitmapImageViewTarget(imageView1));
//                                iv_removeImage1.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: " + IMG_URL + images.get(i).getImage());
                                break;

                            case 1:
                                Glide.with(ProductPicEditActivity.this).asBitmap().load(IMG_URL + images.get(i).getImage()).into(new BitmapImageViewTarget(imageView2));
//                                iv_removeImage2.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: " + IMG_URL + images.get(i).getImage());
                                break;

                            case 2:
                                Glide.with(ProductPicEditActivity.this).asBitmap().load(IMG_URL + images.get(i).getImage()).into(new BitmapImageViewTarget(imageView3));
//                                iv_removeImage3.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: " + IMG_URL + images.get(i).getImage());
                                break;

                            case 3:
                                Glide.with(ProductPicEditActivity.this).asBitmap().load(IMG_URL + images.get(i).getImage()).into(new BitmapImageViewTarget(imageView4));
//                                iv_removeImage4.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: " + IMG_URL + images.get(i).getImage());
                                break;
                        }
                    }
//                    String img[] = images.split(",");


//                    img_url_From_server = img[0];
//                    // Glide.with(ProductPicEditActivity.this).load(IMG_URL + img[0]).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView));
//                    Glide.with(ProductPicEditActivity.this).load(IMG_URL + img_url_From_server).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView));
                    userProfile = "";
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    Toast.makeText(ProductPicEditActivity.this, "" + response.body().getStatus() + msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }


    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog10(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(ProductPicEditActivity.this, BlueMarketActivity4.class);
        startActivity(i);*/
//        pass_from = "";
//        if (pass != null && pass.matches("2")) {
//            Intent i = new Intent(ProductPicEditActivity.this, MyListingActivity.class);
//            startActivity(i);
//        } else {
//            Intent i = new Intent(ProductPicEditActivity.this, BlueMarketProductActivityNew.class);
//            startActivity(i);
//        }
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_removeImage1:
                Toast.makeText(ProductPicEditActivity.this, "First Image Deleted", Toast.LENGTH_SHORT).show();
                productImageDelete(images.get(0).getId());
                iv_removeImage1.setVisibility(View.GONE);
                imagepath1 = "";
                Glide.with(ProductPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView1));
                break;

            case R.id.iv_removeImage2:
                Toast.makeText(ProductPicEditActivity.this, "Second Image Deleted", Toast.LENGTH_SHORT).show();
                productImageDelete(images.get(1).getId());
                iv_removeImage2.setVisibility(View.GONE);
                imagepath2 = "";
                Glide.with(ProductPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView2));
                break;

            case R.id.iv_removeImage3:
                Toast.makeText(ProductPicEditActivity.this, "Third Image Deleted", Toast.LENGTH_SHORT).show();
                productImageDelete(images.get(2).getId());
                iv_removeImage3.setVisibility(View.GONE);
                imagepath3 = "";
                Glide.with(ProductPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView3));
                break;

            case R.id.iv_removeImage4:
                Toast.makeText(ProductPicEditActivity.this, "Forth Image Deleted", Toast.LENGTH_SHORT).show();
                productImageDelete(images.get(3).getId());
                iv_removeImage4.setVisibility(View.GONE);
                imagepath4 = "";
                Glide.with(ProductPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView4));
                break;

            case R.id.bmpic:
                onClickNext();
                break;

        }
    }

    private void onClickNext() {

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
                oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, encodedImageList, "");
            } else {
                oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, "", "");
            }
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private void productImageDelete(String id) {
//        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//        Call<Response> userCall = service.productImageDelete("Bearer " + token, id);
//        userCall.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, Response<Response> response) {
//                if (mProgressDialog != null)
//                    mProgressDialog.dismiss();
//                if (response.body() != null) {
//
//
//                } else {
//                    String msg = (String) getApplication().getText(R.string.goeswrong);
//                    Toast.makeText(ProductPicEditActivity.this, "" + msg, Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                Log.d("onFailure", t.toString());
//            }
//        });
    }
}
