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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class BlueMarketPicEditActivity extends BuddyActivity implements View.OnClickListener {

    private static final String TAG = "BMPicEditActivity";
    Button BMpic;
    ImageView back;

    int imageCount = 0;

    String imagepath1 = "", imagepath2 = "", imagepath3 = "", imagepath4 = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    private Activity _myActivity;
    private Context _mContext;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private String image1, image2, image3, image4 = "";
    private String imagelist[];
    private SharedPreferences pref;
    private String token, productId;
    private String encoded_image = "";
    private String encodedImageList = "";
    private String img = "";
    private String pic1, pic2, pic3, pic4 = "";
    private String userProfile = "";
    private String tmpImg = "";
    private ProgressDialog mProgressDialog;
    private String category;
    private ImageView iv_removeImage1, iv_removeImage2, iv_removeImage3, iv_removeImage4;

    private List<ProductImage> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_pic);
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        Bundle bundle = getIntent().getExtras();

        productId = bundle.getString("AllProduct");
        category = bundle.getString("category");

        this._myActivity = this;
        this._mContext = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        //get product details.
        apiEbpp(productId);

        init();

//        BMpic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (encodedImageList.length() > 0) {
//                    //   Log.d("lost images", tmpImg);
//                    tmpImg = encodedImageList.substring(0, encodedImageList.length() - 1);
////                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, tmpImg, "");
////                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", false, _myActivity, "BlueMarketProductEditActivity", 0, tmpImg, "");
//
//                    Intent intent = new Intent(BlueMarketPicEditActivity.this, BlueMarketProductEditActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("encoded_image", productId);
//                    bundle.putString("DATA_VALUE", category);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else {
////                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, "", "");
////                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", false, _myActivity, "BlueMarketProductEditActivity", 0, "", "");
//
//                    Intent intent = new Intent(BlueMarketPicEditActivity.this, BlueMarketProductEditActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("encoded_image", productId);
//                    bundle.putString("DATA_VALUE", category);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            }
//        });

    }

    private void init() {

        imageView1 = (ImageView) findViewById(R.id.imgfrst);
        imageView2 = (ImageView) findViewById(R.id.imgscnd);
        imageView3 = (ImageView) findViewById(R.id.imgthrd);
        imageView4 = (ImageView) findViewById(R.id.imgfrth);

        iv_removeImage1 = (ImageView) findViewById(R.id.iv_removeImage1);
        iv_removeImage1.setOnClickListener(this);
        iv_removeImage1.setVisibility(View.GONE);

        iv_removeImage2 = (ImageView) findViewById(R.id.iv_removeImage2);
        iv_removeImage2.setOnClickListener(this);
        iv_removeImage2.setVisibility(View.GONE);

        iv_removeImage3 = (ImageView) findViewById(R.id.iv_removeImage3);
        iv_removeImage3.setOnClickListener(this);
        iv_removeImage3.setVisibility(View.GONE);

        iv_removeImage4 = (ImageView) findViewById(R.id.iv_removeImage4);
        iv_removeImage4.setOnClickListener(this);
        iv_removeImage4.setVisibility(View.GONE);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BMpic = (Button) findViewById(R.id.bmpic);
        BMpic.setOnClickListener(this);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE) {
//                userProfile = selectImage.onSelectFromGalleryResultSaquare(data, "");
//            } else if (requestCode == REQUEST_CAMERA) {
//                userProfile = selectImage.onCaptureImageResultSqaure(data, "");
//            }
//
//            if (!encoded_image.equals("")) {
//                encodedImageList += userProfile + ",";
//            }
//        }
//    }

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

//    public void frstpic(View view) {
//        selectImage = new SelectImage1(_myActivity, _mContext, (ImageView) view);
//        selectImage.selectImage();
//    }
//
//    public void scndpic(View view) {
//        selectImage = new SelectImage1(_myActivity, _mContext, (ImageView) view);
//        selectImage.selectImage();
//    }
//
//    public void thrdpic(View view) {
//        selectImage = new SelectImage1(_myActivity, _mContext, (ImageView) view);
//        selectImage.selectImage();
//    }
//
//    public void fourthpic(View view) {
//        selectImage = new SelectImage1(_myActivity, _mContext, (ImageView) view);
//        selectImage.selectImage();
//    }

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
                                ActivityCompat.requestPermissions(BlueMarketPicEditActivity.this,
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
                    Toast.makeText(BlueMarketPicEditActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(BlueMarketPicEditActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN, final String encodedImageList) {
        super.openDialog12(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN, encodedImageList);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void apiEbpp(String productId) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> userCall = service.productDt("Bearer " + token, productId);
        userCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {

                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                if (response.body().getStatus()) {

                    ProductDetail details = response.body().getDetails();

                    images = response.body().getDetails().getProductImages();

                    for (int i = 0; i < images.size(); i++) {
                        switch (i) {
                            case 0:
                                setImage(images.get(i).getImage(), imageView1);
                                iv_removeImage1.setVisibility(View.VISIBLE);
                                break;

                            case 1:
                                setImage(images.get(i).getImage(), imageView2);
                                iv_removeImage2.setVisibility(View.VISIBLE);
                                break;

                            case 2:
                                setImage(images.get(i).getImage(), imageView3);
                                iv_removeImage3.setVisibility(View.VISIBLE);
                                break;

                            case 3:
                                setImage(images.get(i).getImage(), imageView4);
                                iv_removeImage4.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
//                    Log.d("IMAGE:", response.body().getDetails().getImage());
//                    String images = response.body().getDetails().getImage();
//                    String img[] = images.split(",");
//
//                    int tmpLen = -1;
//
//                    if (img != null)
//                        tmpLen = img.length - 1;
//
//                    if (tmpLen >= 0) {
//                        setImage(img[0], imageView1);
//                    }
//
//                    if (tmpLen >= 1) {
//                        setImage(img[1], imageView2);
//                    }
//
//                    if (tmpLen >= 2) {
//                        setImage(img[2], imageView3);
//                    }
//
//                    if (tmpLen >= 3) {
//                        setImage(img[3], imageView4);
//                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    private void setImage(String img, ImageView iv) {
        Glide.with(BlueMarketPicEditActivity.this).asBitmap().load(IMG_URL + img).into(new BitmapImageViewTarget(iv));
        tmpImg = "";
        Log.d("empty images", tmpImg);
    }

    @Override
    public void onBackPressed() {
//        Intent i = new Intent(BlueMarketPicEditActivity.this, BlueMarketActivity42.class);
//        startActivity(i);
//
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
                Toast.makeText(BlueMarketPicEditActivity.this, "First Image Deleted", Toast.LENGTH_SHORT).show();

                if (images.size() >= 1) {
                    productImageDelete(images.get(0).getId());
                    images.remove(0);
                }
                iv_removeImage1.setVisibility(View.GONE);
                imagepath1 = "";
                Glide.with(BlueMarketPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView1));
                break;

            case R.id.iv_removeImage2:
                Toast.makeText(BlueMarketPicEditActivity.this, "Second Image Deleted", Toast.LENGTH_SHORT).show();

                if (images.size() >= 2) {
                    productImageDelete(images.get(1).getId());
                    images.remove(1);
                }
                iv_removeImage2.setVisibility(View.GONE);
                imagepath2 = "";
                Glide.with(BlueMarketPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView2));
                break;

            case R.id.iv_removeImage3:
                Toast.makeText(BlueMarketPicEditActivity.this, "Third Image Deleted", Toast.LENGTH_SHORT).show();

                if (images.size() >= 3) {
                    productImageDelete(images.get(2).getId());
                    images.remove(2);
                }
                iv_removeImage3.setVisibility(View.GONE);
                imagepath3 = "";
                Glide.with(BlueMarketPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView3));
                break;

            case R.id.iv_removeImage4:
                Toast.makeText(BlueMarketPicEditActivity.this, "Forth Image Deleted", Toast.LENGTH_SHORT).show();

                if (images.size() >= 4) {
                    productImageDelete(images.get(3).getId());
                    images.remove(3);
                }
                iv_removeImage4.setVisibility(View.GONE);
                imagepath4 = "";
                Glide.with(BlueMarketPicEditActivity.this).asBitmap().load(R.drawable.upldimg).into(new BitmapImageViewTarget(imageView4));
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

        if (encodedImageList.matches("") && images.size() < 1) {
            oDialog("Please select Product image", "Close", "", false, _myActivity, "", 0);
        } else {
            Intent intent = new Intent(BlueMarketPicEditActivity.this, BlueMarketProductEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("encoded_image", productId);
            bundle.putString("DATA_VALUE", encodedImageList);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

//            if (encodedImageList.length() > AppConfig.IMG_LENTH_CHECK) {
//                String tmpImg = encodedImageList.substring(0, encodedImageList.length() - 1);
//                // oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductActivity", 0, tmpImg, "");
//                oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, productId, encodedImageList);
//            } else {
//                oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, "BlueMarketProductEditActivity", 0, "", "");
//            }

        }

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private void productImageDelete(String id) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> userCall = service.productImageDelete("Bearer " + token, id);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                if (response.body() != null) {


                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    Toast.makeText(BlueMarketPicEditActivity.this, "" + msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
}
