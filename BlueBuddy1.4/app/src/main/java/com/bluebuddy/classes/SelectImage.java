package com.bluebuddy.classes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bluebuddy.BuildConfig;
import com.bluebuddy.Utilities.FilePath;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 5/2/2017.
 */

public class SelectImage {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Activity activity;
    private Context context;
    String mCurrentPhotoPath = "";
    Uri mImageCaptureUri;
    private ImageView imageView;
    private String userChoosenTask;
    Handler handler = new Handler();

    public SelectImage() {

    }

    public SelectImage(Activity a, Context c, ImageView i) {
        this.activity = a;
        this.context = c;
        this.imageView = i;
    }

    public SelectImage(Activity a, Context c) {
        this.activity = a;
        this.context = c;
    }

    public void selectImage() {

        final CharSequence[] items = {"Use Camera Roll", "Use Gallery or Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);

                if (items[item].equals("Use Camera Roll")) {
                    userChoosenTask = "Take Photo";

                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Use Gallery or Library")) {
                    userChoosenTask = "Choose from Library";

                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });

        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
            //  f = mAlbumStorageDirFactory.setUpPhotoFile();
            mCurrentPhotoPath = destination.getAbsolutePath();
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                mImageCaptureUri = Uri.fromFile(destination);
            } else {
                mImageCaptureUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", destination);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void onCaptureImageResult(Intent data, String token) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(getCircleBitmap(thumbnail));
        byte[] imageBytes = baos.toByteArray();
        String profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ServerResponseCp7> userCall = service.uploadImage("Bearer " + token, "data:image/jpeg;base64," + profileimg, 9);
        userCall.enqueue(new Callback<ServerResponseCp7>() {
            @Override
            public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {
                //hidepDialog();
                //onSignupSuccess();
                //  Log.d("onResponse", "" + response.body().getMessage());

                //     BusProvider.getInstance().post(new ServerEvent(response.body()));
                // Log.d("onResponse2Return", "" + response.body().getMessage()+response.body().getEmail()+response.body().getPhone());

                if (response.body().getStatus()) {
                    Log.d("onResponse", "image uploaded");
                } else {
                    Log.d("onResponse", "" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                //   hidepDialog();
                Log.d("onFailure", t.toString());
            }
        });

    }

    public String onCaptureImageResultSquare(Intent data, String token) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(thumbnail);
        byte[] imageBytes = baos.toByteArray();
        String profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ServerResponseCp7> userCall = service.uploadImage("Bearer " + token, "data:image/jpeg;base64," + profileimg, 9);
        userCall.enqueue(new Callback<ServerResponseCp7>() {
            @Override
            public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {
                //hidepDialog();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());

                //     BusProvider.getInstance().post(new ServerEvent(response.body()));
                // Log.d("onResponse2Return", "" + response.body().getMessage()+response.body().getEmail()+response.body().getPhone());


                if (response.body().getStatus()) {
                    Log.d("onResponse", "image uploaded");
                } else {
                    Log.d("onResponse", "" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                //   hidepDialog();
                Log.d("onFailure", t.toString());
            }
        });
        return profileimg;
    }

    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(Intent data, String token) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(getCircleBitmap(bm));
        byte[] imageBytes = baos.toByteArray();
        String profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ServerResponseCp7> userCall = service.uploadImage("Bearer " + token, "data:image/jpeg;base64," + profileimg, 9);
        userCall.enqueue(new Callback<ServerResponseCp7>() {
            @Override
            public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {
                //hidepDialog();
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());

                //     BusProvider.getInstance().post(new ServerEvent(response.body()));
                // Log.d("onResponse2Return", "" + response.body().getMessage()+response.body().getEmail()+response.body().getPhone());

                if (response.body().getStatus()) {
                    Log.d("onResponse", "image uploaded");
                } else {
                    Log.d("onResponse", "" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                //   hidepDialog();
                Log.d("onFailure", t.toString());
            }
        });
    }

    public String getRealPathFromURI(Uri contentURI) {
        return FilePath.getPath(context, contentURI);
//        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            if (idx > 0) {
//                return cursor.getString(idx);
//            } else {
//                return "";
//            }
//        }
    }

    public String SaveImageAndReturnPath(Bitmap finalBitmap) {

        //String root = Environment.getExternalStorageDirectory();
        String filePath = "";
        File file = null;
        File myDir = new File(Environment.getExternalStorageDirectory() + "/bluebuddy");
        myDir.mkdirs();
        if (myDir.exists()) {

            String fname = "bluebuddy_" + System.currentTimeMillis() + ".jpg";

            file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                filePath = myDir + "/" + fname;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }

    public File SaveImage(Bitmap finalBitmap) {

        //String root = Environment.getExternalStorageDirectory();
        File file = null;
        File myDir = new File(Environment.getExternalStorageDirectory() + "/bluebuddy");
        myDir.mkdirs();
        if (myDir.exists()) {

            String fname = "bluebuddy_" + System.currentTimeMillis() + ".jpg";
            file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /* public String onSelectFromGalleryResultSquare(Intent data, String token) {

         File imageFile = null;
         Bitmap bm=null;
         if (data != null) {
             try {

                 imageFile= new File(getRealPathFromURI(data.getData()));
                 bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         imageView.setImageBitmap(bm);
         byte[] imageBytes = baos.toByteArray();
         String profileimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
         MultipartBody.Part body = null;
         RequestBody requestBodyNext_step = RequestBody.create(
                 MediaType.parse("multipart/form-data"), "9");
         RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
         if(imageFile !=null){

                 body = MultipartBody.Part.createFormData("profile_img", imageFile.getName().replace(" ", ""), requestBodyOrderDataFile);
         }

         ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
         Call<ServerResponseCp7> userCall = service.uploadImageMultipart("Bearer "+ token,requestBodyNext_step,body);
         userCall.enqueue(new Callback<ServerResponseCp7>() {
             @Override
             public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {

                *//* Log.d("onResponse", "" + response.body().getMessage());*//*


                if (response.body().getStatus() == "true") {
                    Log.d("onResponse", "image uploaded");
                } else{
                    Log.d("onResponse", "" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                //   hidepDialog();
                Log.d("onFailure", t.toString());
            }
        });
        return profileimg;
    }*/
    public Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

}