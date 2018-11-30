package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponseCp7;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

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

public class CreateProfileActivity7 extends BuddyActivity {

    private static final int REQUEST_CODE_GALLERY = 2;
    private static final int ACTION_OPEN_CAMERA = 10;
    private static final String TAG = "CreateProfileActivity7";
    final String redirectActivity = "MyProfileActivity";
    Button crtprofpicupld;
    View passView = null;
    File imageFile = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SelectImage selectImage;
    private Activity activity;
    private Context context;
    private Activity _myActivity;
    private ImageView imageView, back;
    private SharedPreferences pref;
    private String encoded_image = "";
    private String token;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String email;
    private String password;
    private boolean isFacebook = false;
    private String firebaseUserId;
    private String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_create_profile7);
        super.onCreate(savedInstanceState);
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        this.activity = this;
        this.context = this;

        auth = FirebaseAuth.getInstance();
        firebaseToken = FirebaseInstanceId.getInstance().getToken();

        back = findViewById(R.id.imgNotification2);
        TextView tx = findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        imageView = findViewById(R.id.imgscnd);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        back.setOnClickListener(v -> onBackPressed());

        crtprofpicupld = findViewById(R.id.crtpropicid);
        crtprofpicupld.setOnClickListener(v -> {
            if (encoded_image == "") {
                oDialog("Please select Profile image", "Close", "", false, _myActivity, "", 0);
            } else {
                if (isFacebook) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        firebaseUserId = user.getUid();
                        signUpUser(firebaseUserId);
                    } else {
                        // No user is signed in
                    }
                } else
                    firebaseRegistration(email, password);
//                    oDialog("Are you sure you want to submit?", "Submit", "Cancel", true, _myActivity, redirectActivity, 0, token);
            }
        });
    }

    private void firebaseRegistration(final String email, final String password) {

        crtprofpicupld.setClickable(false);
        crtprofpicupld.setFocusable(false);

        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateProfileActivity7.this, task -> {
                    if (!task.isSuccessful()) {
                        createUser(email, password);
                    } else if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        firebaseUserId = user.getUid();
                        signUpUser(firebaseUserId);
                    }
                })
                .addOnFailureListener(e -> {
                    crtprofpicupld.setClickable(true);
                    crtprofpicupld.setFocusable(true);
                });

        /*AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            fbUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> fbUser.delete()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                }
                                authUtils.createUser(email, password);
                            }))
                    .addOnFailureListener(e -> {
                        crtprofpicupld.setClickable(true);
                        crtprofpicupld.setFocusable(true);
                    });
        } else {
            authUtils.createUser(email, password);
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                String imagepath = selectImage.getRealPathFromURI(data.getData());
                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imageView.setImageBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    onSelectFromGalleryResultSquare(imagepath);

                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (requestCode == REQUEST_CAMERA) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageView.setImageBitmap(photo);
                String imagepath = selectImage.SaveImageAndReturnPath(photo);
                if (imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {

                    onSelectFromGalleryResultSquare(imagepath);

                } else {

                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;

                }

            }

        }
    }

    /*  private String getRealPathFromURI(Uri contentURI) {
          Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
          if (cursor == null) { // Source is Dropbox or other similar local file path
              return contentURI.getPath();
          } else {
              cursor.moveToFirst();
              int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
              return cursor.getString(idx);
          }
      }*/

    public void onSelectFromGalleryResultSquare(String imagepath) {

        imageFile = new File(imagepath);

        MultipartBody.Part body = null;
//        RequestBody requestBodyNext_step = RequestBody.create(MediaType.parse("multipart/form-data"), "9");

        if (imageFile != null) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            body = MultipartBody.Part.createFormData("profile_img", imageFile.getName().replace(" ", ""), requestBodyOrderDataFile);
        }

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.setCancelable(false);
        }

        progressDialog.show();
        Call<ServerResponseCp7> userCall = service.uploadImageMultipart("Bearer " + token, /*requestBodyNext_step,*/ body);
        userCall.enqueue(new Callback<ServerResponseCp7>() {
            @Override
            public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {

                /* Log.d("onResponse", "" + response.body().getMessage());*/
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

//                if (response.body() != null && response.body().getStatus() == "true") {
                if (response.body() != null && response.body().getStatus()) {
                    encoded_image = "success";
//                    isFacebook = response.body().getUserDetails().getIsSocial();
                    email = response.body().getUserDetails().getEmail();
                    password = response.body().getUserDetails().getPassword();
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                }

            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }
        });

    }
   /* public void upldimg(View view) {

    }*/

    //first
    public void frstpic(View view) {
        selectImage = new SelectImage(activity, context, (ImageView) view);
        selectImage.selectImage();
    }

    // for image choose from Gallary
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CreateProfileActivity7.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utility.REQUEST_CODE_CAMERA) {
            /*if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this,"Can not",Toast.LENGTH_SHORT).show();
            }*/
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
                    if (passView != null) {
                        openPopupForImage();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(CreateProfileActivity7.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }/* else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // openGallery();
            } else {
                Toast.makeText(this, "Can not", Toast.LENGTH_SHORT).show();
            }
        } */ else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void scndpic(View view) {
        // check for permission first

        List<String> permissionsNeeded = new ArrayList<String>();

        passView = view;
        final List<String> permissionsList = new ArrayList<String>();
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
                                ActivityCompat.requestPermissions(CreateProfileActivity7.this,
                                        permissionsList.toArray(new String[permissionsList.size()]),
                                        Utility.REQUEST_CODE_CAMERA);
                            }
                        });

                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    Utility.REQUEST_CODE_CAMERA);

            return;
        }
        if (passView != null) {
            openPopupForImage();
        }

    }

    void openPopupForImage() {
        selectImage = new SelectImage(activity, context, (ImageView) passView);
        selectImage.selectImage();
    }

    //third
    public void thrdpic(View view) {
        selectImage = new SelectImage(activity, context, (ImageView) view);
        selectImage.selectImage();
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String tOKEN) {
        super.openDialog9(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, tOKEN);
    }

    /*public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }*/

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        setMyPref(ACCESS_STEP, "7");
//        Intent i = new Intent(CreateProfileActivity7.this, LocationActivity.class);
//        startActivity(i);
        finish();
    }

    void createUser(final String email, final String password) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(CreateProfileActivity7.this);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.setCancelable(false);
        }

        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateProfileActivity7.this, task -> {
                    if (!task.isSuccessful()) {

                        oDialog("The email address is already in use by another account!", "Ok", "", false, _myActivity, "", 0);
                        crtprofpicupld.setClickable(true);
                        crtprofpicupld.setFocusable(true);

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                    } else if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        firebaseUserId = user.getUid();
                        signUpUser(firebaseUserId);
//                        signUpUser(task, isFacebook);
                    }
                })
                .addOnFailureListener(e -> {
                    crtprofpicupld.setClickable(true);
                    crtprofpicupld.setFocusable(true);

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                });

    }

    //    private void signUpUser(@NonNull Task<AuthResult> task, boolean isFacebook) {
    private void signUpUser(String firebaseUserId) {

        crtprofpicupld.setClickable(false);
        crtprofpicupld.setFocusable(false);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<ServerResponseCp7> userCall = service.registerFirebase("Bearer " + token,
                firebaseToken,
                firebaseUserId,
                "9");
        userCall.enqueue(new Callback<ServerResponseCp7>() {
            @Override
            public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {
                if (Boolean.valueOf(response.body().getStatus())) {
                    initNewUserInfo(response.body().getUserDetails());
                } else {
                    oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    crtprofpicupld.setClickable(true);
                    crtprofpicupld.setFocusable(true);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                oDialog(t.toString(), "Ok", "", false, _myActivity, "", 0);

                crtprofpicupld.setClickable(true);
                crtprofpicupld.setFocusable(true);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    void initNewUserInfo(UserDetails userDetails) {
        user = auth.getCurrentUser();

        HashMap<String, String> updateUser = new HashMap<>();
//            updateUser.put("email", user.getEmail());
//            updateUser.put("name", user.getEmail().substring(0, user.getEmail().indexOf("@")));
//            updateUser.put("image", StaticConfig.STR_DEFAULT_BASE64);

        updateUser.put("email", userDetails.getEmail());
        updateUser.put("name", userDetails.getFirst_name() + " " + userDetails.getLast_name());
        updateUser.put("image", userDetails.getProfile_pic());

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(user.getUid())
                .child("credentials")
                .setValue(updateUser);

        oDialog("Profile updated successfully", "Ok", "", true, _myActivity, redirectActivity, R.layout.custom_popup_layout, token);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        crtprofpicupld.setClickable(true);
        crtprofpicupld.setFocusable(true);
    }

}
