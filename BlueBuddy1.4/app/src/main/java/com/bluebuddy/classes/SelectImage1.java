package com.bluebuddy.classes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bluebuddy.Utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 5/2/2017.
 */

public class SelectImage1 {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Activity activity;
    private Context context;

    private ImageView imageView,imageView1,imageView2,imageView3,imageView4;
    private String userChoosenTask;
    private String chrtimg, option;
    private int position = 0;
    private  String mCurrentPhotoPath;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SelectImage1(){

    }

    public SelectImage1(Activity a, Context c, ImageView i, int position){
        this.activity = a;
        this.context = c;
        this.imageView = i;
        setPosition(position);
    }

    public SelectImage1(Activity a, Context c, ImageView i){
        this.activity = a;
        this.context = c;
        this.imageView = i;
    }

    public SelectImage1(Activity a, Context c){
        this.activity = a;
        this.context = c;
    }

    public SelectImage1(Activity a, Context c, ImageView i, String option){
        this.activity = a;
        this.context = c;
        this.imageView = i;
        this.option = option;
    }

    public void selectImage() {
        final CharSequence[] items = { "Use Camera Roll", "Use Gallery or Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(activity);

                if (items[item].equals("Use Camera Roll")) {
                    userChoosenTask = "Take Photo";

                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Use Gallery or Library")) {
                    userChoosenTask = "Choose from Library";

                    if(result)
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
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "blue_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("nipaerror","file path "+mCurrentPhotoPath);
        return image;
    }
    public String getCurrentPhotopath(){
        return mCurrentPhotoPath;
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /*// if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
              //  Log.i(TAG, "IOException");
                Toast.makeText(activity,"IOException, please try again",Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
           *//* if (photoFile != null) {
                Log.d("nipaerror","file path in activity");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(intent, REQUEST_CAMERA);
            }*//*
       // }*/
        activity.startActivityForResult(intent, REQUEST_CAMERA);


    }
    public File onCaptureImageFileResult(Intent data, String token) {
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        return destination;
    }
    public String onCaptureImageResult(Intent data, String token) {
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

        imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(getCircleBitmap(thumbnail));
        byte[] imageBytes = baos.toByteArray();
        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return  chrtimg;
    }
    public String onCaptureImageResultSqaure(Intent data, String token) {
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

        imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(thumbnail);
        byte[] imageBytes = baos.toByteArray();
        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return  chrtimg;
    }

    public String onCaptureImageResultSqaure11(Intent data, String token) {
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

        imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(thumbnail);
        byte[] imageBytes = baos.toByteArray();
        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return  chrtimg;
    }

    @SuppressWarnings("deprecation")
    public String onSelectFromGalleryResult(Intent data, String token) {
        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
              //  Log.d("simg0", String.valueOf(bm));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //imageView.setImageBitmap(bm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(getCircleBitmap(bm));
        byte[] imageBytes = baos.toByteArray();

        long lengthbmp = imageBytes.length/1024;
        Log.d("image size:", String.valueOf(lengthbmp));

        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        /*if(option.equals("")){
            return  chrtimg;
        }else{
            return  chrtimg + "||||" + option;
        }*/
      //  Log.d("simg",chrtimg);
        return  chrtimg;

    }
    @SuppressWarnings("deprecation")
    public String onSelectFromGalleryResultSaquare(Intent data, String token) {
        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageView.setImageBitmap(bm);
        byte[] imageBytes = baos.toByteArray();
        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return  chrtimg;
    }

    public String onSelectFromGalleryResultSaquare11(Intent data, String token) {
        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
       imageView.setImageBitmap(bm);
        byte[] imageBytes = baos.toByteArray();
        chrtimg = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return  chrtimg;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
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