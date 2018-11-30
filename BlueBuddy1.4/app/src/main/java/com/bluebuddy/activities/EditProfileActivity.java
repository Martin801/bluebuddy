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
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponseCp6;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.ProfileDetails;
import com.bluebuddy.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
import static com.bluebuddy.app.AppConfig.API_KEY;
import static com.bluebuddy.app.AppConfig.IMG_URL;
import static com.bluebuddy.app.AppConfig.LOG_TAG;
import static com.bluebuddy.app.AppConfig.OUT_JSON;
import static com.bluebuddy.app.AppConfig.PLACES_API_BASE;
import static com.bluebuddy.app.AppConfig.TYPE_AUTOCOMPLETE;

public class EditProfileActivity extends BuddyActivity implements AdapterView.OnItemClickListener {

    EditText edt_frstnm, edt_lastname, edt_website, edt_abt;
    FrameLayout frmfb;
    String rc = "";
    MyTextView bell_counter;
    File fileImage = null;
    String latSubmit = "", longSubmit = "";
    private Button btn_edtprofile, slfbid, slinstid, sltwtid, slglid;
    private AutoCompleteTextView edt_location;
    private Activity _myActivity;
    private Context _mContext;
    private ImageView back, imageView;
    private SelectImage selectImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private SharedPreferences pref;
    private String token, bellcounter, fburl = "", twturl = "", insturl = "", gglurl = "";
    private String userProfile = "";
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;

    //2
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:USA");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_edit_profile);
        super.onCreate(savedInstanceState);
        super.initialize();

        super.notice();
        super.setImageResource1(5);
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        _mContext = this;

        super.setTitle("EDIT YOUR PROFILE");

        frmfb = (FrameLayout) findViewById(R.id.frmfb);
        slfbid = (Button) findViewById(R.id.slfbid);
        slglid = (Button) findViewById(R.id.slglid);
        slinstid = (Button) findViewById(R.id.slinstid);
        sltwtid = (Button) findViewById(R.id.sltwtid);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        edt_frstnm = (EditText) findViewById(R.id.edt_frstnm);
        edt_lastname = (EditText) findViewById(R.id.edt_lastname);
        edt_location = (AutoCompleteTextView) findViewById(R.id.edt_location);
        edt_location.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_for_map_address));
        edt_location.setOnItemClickListener(this);

        edt_website = (EditText) findViewById(R.id.edt_website);
        edt_abt = (EditText) findViewById(R.id.edt_abt);
        imageView = (ImageView) findViewById(R.id.edt_profile_view);
        btn_edtprofile = (Button) findViewById(R.id.btn_edtprofile);
        back = (ImageView) findViewById(R.id.imgNotification2);

        auth = FirebaseAuth.getInstance();

        apiEPA();
        bellcount();

        //==============================================
        slglid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_myActivity);
                LayoutInflater inflater = _myActivity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                final String url = "https://www.youtube.com/channel/";
                if (gglurl.trim().equalsIgnoreCase("")) {
                    edt.setText(url);
                } else {
                    edt.setText(gglurl);
                }
                edt.setHint(url);

                // dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Enter Your YouTube Url");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String googlepluslink = url;
                        if (edt.getText().toString().trim().contains(url))
                            googlepluslink = edt.getText().toString().trim();
                        else
                            googlepluslink = url + edt.getText().toString().trim();

                        slglid.setBackgroundColor(0xff0000ff);
                        int white = Color.parseColor("#ffffffff");
                        slglid.setTextColor(white);
                        Log.d("YouTubelink input", googlepluslink);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<ServerResponseCp6> userCall = service.socialurl4("Bearer " + token, "put", googlepluslink, 9);
                        //  Call<LoginStatus> userCall = service.login("post",email,password);

                        userCall.enqueue(new Callback<ServerResponseCp6>() {
                            @Override
                            public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("ggl msg", "" + response.body().getStatus().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                //
            }
        });

        slinstid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_myActivity);
                LayoutInflater inflater = _myActivity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                final String url = "https://www.instagram.com/";
                if (insturl.trim().equalsIgnoreCase("")) {
                    edt.setText(url);
                } else {
                    edt.setText(insturl);
                }

                edt.setHint(url);

                // dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Enter Your Instagram Url");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String instlink = url;
                        if (edt.getText().toString().trim().contains(url))
                            instlink = edt.getText().toString().trim();
                        else
                            instlink = url + edt.getText().toString().trim();
                        slinstid.setBackgroundColor(Color.GRAY);
                        int white = Color.parseColor("#ffffffff");
                        slinstid.setTextColor(white);
                        Log.d("instlink input", instlink);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<ServerResponseCp6> userCall = service.socialurl3("Bearer " + token, "put", instlink, 9);
                        //  Call<LoginStatus> userCall = service.login("post",email,password);

                        userCall.enqueue(new Callback<ServerResponseCp6>() {
                            @Override
                            public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("twt msg", "" + response.body().getStatus().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                //
            }
        });

        sltwtid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_myActivity);
                LayoutInflater inflater = _myActivity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                final String url = "https://www.twitter.com/";
                if (twturl.trim().equalsIgnoreCase("")) {
                    edt.setText(url);
                } else {
                    edt.setText(twturl);
                }

                edt.setHint(url);
                // dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Enter Your Twitter Url");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String twtlink = url;
                        if (edt.getText().toString().trim().contains(url))
                            twtlink = edt.getText().toString().trim();
                        else
                            twtlink = url + edt.getText().toString().trim();
                        sltwtid.setBackgroundColor(0xff0000ff);
                        int white = Color.parseColor("#ffffffff");
                        sltwtid.setTextColor(white);
                        Log.d("twt input", twtlink);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<ServerResponseCp6> userCall = service.socialurl2("Bearer " + token, "put", twtlink, 9);
                        //  Call<LoginStatus> userCall = service.login("post",email,password);

                        userCall.enqueue(new Callback<ServerResponseCp6>() {
                            @Override
                            public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("twt msg", "" + response.body().getStatus().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                //
            }
        });
        //==============================================


        slfbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_myActivity);
                LayoutInflater inflater = _myActivity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                final String url = "https://www.facebook.com/";
                if (fburl.trim().equalsIgnoreCase("")) {
                    edt.setText(url);
                } else {
                    edt.setText(fburl);
                }

                edt.setHint(url);
                // dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Enter Your Facebook Url");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fblink = url;
                        if (edt.getText().toString().trim().contains(url))
                            fblink = edt.getText().toString().trim();
                        else
                            fblink = url + edt.getText().toString().trim();
                        slfbid.setBackgroundColor(0xff0000ff);
                        int white = Color.parseColor("#ffffffff");
                        slfbid.setTextColor(white);
                        Log.d("fb input", fblink);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<ServerResponseCp6> userCall = service.socialurl("Bearer " + token, "put", fblink, 9);
                        //  Call<LoginStatus> userCall = service.login("post",email,password);

                        userCall.enqueue(new Callback<ServerResponseCp6>() {
                            @Override
                            public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("fb msg", "" + response.body().getStatus().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                //
            }
        });
        //==================================================
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(EditProfileActivity.this, MyAccountMenu.class);
//                startActivity(i);

                onBackPressed();
            }
        });

        btn_edtprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rc = "MyProfileActivity";
                updateUserProfile();
            }
        });
    }

    /* private  File SaveImage(Bitmap finalBitmap) {

         //String root = Environment.getExternalStorageDirectory();
         File myDir = new File(Environment.getExternalStorageDirectory() + "/bluebuddy");
         myDir.mkdirs();
         if(myDir.exists()){
             Log.d("nipaerror","exist");
         }else {
             Log.d("nipaerror","not exist");
         }
         String fname = "Image-"+System.currentTimeMillis() +".jpg";
         File file = new File (myDir, fname);
         if (file.exists ()) file.delete ();
         try {
             FileOutputStream out = new FileOutputStream(file);
             finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
             out.flush();
             out.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
         return file;
     }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                // userProfile = "data:image/jpeg;base64," + selectImage.onSelectFromGalleryResult(data, token);
                String imagepath = selectImage.getRealPathFromURI(data.getData());
                fileImage = null;
                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {
                    fileImage = new File(imagepath);
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imageView.setImageBitmap(selectImage.getCircleBitmap(bm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CAMERA) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                fileImage = selectImage.SaveImage(photo);
                // imageView.setImageBitmap(photo);
                imageView.setImageBitmap(selectImage.getCircleBitmap(photo));
            }
            //userProfile = "data:image/jpeg;base64," + selectImage.onCaptureImageResult(data, token);
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog10(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    void initNewUserInfo() {
        User newUser = new User();
        user = auth.getCurrentUser();

        newUser.email = user.getEmail();
        newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        newUser.avata = StaticConfig.STR_DEFAULT_BASE64;

        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("credentials").setValue(newUser);
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
        new android.app.AlertDialog.Builder(EditProfileActivity.this)
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
                    Toast.makeText(EditProfileActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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
                                ActivityCompat.requestPermissions(EditProfileActivity.this,
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

    public void scndpic(View view) {
        checkforpermission();
    }

    private void selectImages() {
        selectImage = new SelectImage(_myActivity, _mContext, imageView);
        selectImage.selectImage();
    }

    private void apiEPA() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.mpa("Bearer " + token);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (response.body().getStatus() == true) {

                    String tOKEN = response.body().getToken();
                    ProfileDetails p = response.body().getProfile_details();
                    if (p != null) {
                        // Log.d("image123:", p.getProfile_pic());
                        //    userProfile = decodeImageFromUrl(p.getProfile_pic());

                        Glide.with(EditProfileActivity.this).asBitmap().load(IMG_URL + p.getProfile_pic()).into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(EditProfileActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        latSubmit = p.getLocation_lat();
                        longSubmit = p.getLocation_long();

                        edt_frstnm.setText(p.getFirst_name());
                        edt_lastname.setText(p.getLast_name());
                        edt_location.setText(p.getLocation());

                        edt_website.setText(p.getWebsite_link() + "");
                        edt_abt.setText(p.getAbout());

                        if (p.getFb_url().trim() != "") {
                            fburl = p.getFb_url();
                            int greenColorValue = Color.parseColor("#FF112FA6");
                            slfbid.setTextColor(greenColorValue);
                        }

                        if (p.getTwtr_url().trim() != "") {
                            twturl = p.getTwtr_url();
                            int greenColorValue = Color.parseColor("#FF112FA6");
                            sltwtid.setTextColor(greenColorValue);
                        }

                        if (p.getIngm_url().trim() != "") {
                            insturl = p.getIngm_url();
                            int greenColorValue = Color.parseColor("#FF112FA6");
                            slinstid.setTextColor(greenColorValue);
                        }

                        if (p.getGgle_url().trim() != "") {
                            gglurl = p.getGgle_url();
                            int greenColorValue = Color.parseColor("#FF112FA6");
                            slglid.setTextColor(greenColorValue);
                        }

                    }

                    // Log.d("onResponse end here", "" + tOKEN);
                } else {
                    Toast.makeText(EditProfileActivity.this, "" + response.body().getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                t.printStackTrace();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateUserProfile() {

        String website = edt_website.getText().toString().trim();
        String lastname = edt_lastname.getText().toString().trim();
        String frstnm = edt_frstnm.getText().toString().trim();
        String location = edt_location.getText().toString().trim();
        String abt = edt_abt.getText().toString().trim();
        if (edt_frstnm.getText().toString().trim().equals("")) {
            oDialog("Enter your first name", "Close", "", false, _myActivity, "", 0);
            return;
        } else if (lastname.equals("")) {
            oDialog("Enter your last name", "Close", "", false, _myActivity, "", 0);
            return;
        }/*else if (website.equals("")) {
            oDialog("Enter your website link", "Close", "", false, _myActivity, "", 0);
            return;
        } */ else if (abt.trim().equals("")) {
            oDialog("Enter about your self.", "Close", "", false, _myActivity, "", 0);
            return;
        } else if (edt_location.getText().toString().trim().equals("")) {
            oDialog("Enter your location", "Close", "", false, _myActivity, "", 0);
            return;
        } else {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(_myActivity);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading_message));
            }
            progressDialog.show();
            MultipartBody.Part body = null;

            String[] add = getLocationFromAddress(_mContext, location);

            if (add[0] != null) {
                latSubmit = add[0];
            }
            if (add[1] != null) {
                longSubmit = add[1];
            }
            if (website == null) {
                website = "";
            }


            RequestBody requestBodyfirst_name = RequestBody.create(
                    MediaType.parse("multipart/form-data"), frstnm);
            RequestBody requestBodylast_name = RequestBody.create(
                    MediaType.parse("multipart/form-data"), lastname);
            RequestBody requestBodyuser_location = RequestBody.create(
                    MediaType.parse("multipart/form-data"), location);
            RequestBody requestBodyuser_lat = RequestBody.create(
                    MediaType.parse("multipart/form-data"), latSubmit);
            RequestBody requestBodyuser_long = RequestBody.create(
                    MediaType.parse("multipart/form-data"), longSubmit);
            RequestBody requestBodyabout = RequestBody.create(
                    MediaType.parse("multipart/form-data"), abt);
            RequestBody requestBodywebsite_link = RequestBody.create(
                    MediaType.parse("multipart/form-data"), website);
            RequestBody requestBodyNext_step = RequestBody.create(
                    MediaType.parse("multipart/form-data"), "9");


            if (fileImage != null && fileImage.length() > AppConfig.IMG_LENTH_CHECK) {
                RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                body = MultipartBody.Part.createFormData("profile_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
            }

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            //Call<CommonResponse> proUpdate = service.updateMyProfile("Bearer " + token, userProfile, frstnm, lastname, location, add[0], add[1], abt, website + "", "9");
            Call<CommonResponse> proUpdate = service.updateProfileMultipart("Bearer " + token, requestBodyfirst_name, requestBodylast_name, requestBodyuser_location, requestBodyuser_lat, requestBodyuser_long, requestBodyabout, requestBodywebsite_link, requestBodyNext_step, body);
            proUpdate.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getStatus() == true) {

                        oDialog("Profile details updated successfully", "Close", "", true, _myActivity, "MyProfileActivity", 0);
                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        String sMesage = "";
                        if (response.body().getMessage() != null) {
                            sMesage = response.body().getMessage();
                        }
                        oDialog(msg + " " + sMesage, "Ok", "", false, _myActivity, "", 0, "");
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private String decodeImageFromUrl(String img) {
        Bitmap bm = null;
        String image = "";

        if (!img.equals("")) {
            try {
                URL url = new URL(img);
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                image = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Log.d("decodeimg", image);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }

        return image;
    }

    //1
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
    }

    public String[] getLocationFromAddress(Context context, String location) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Address location1;
        String[] l;
        String[] myStringArray = new String[2];

        try {
            // May throw an IOException
            address = coder.getFromLocationName(location, 5);
            if (address == null) {

            }

            location1 = address.get(0);
            location1.getLatitude();
            location1.getLongitude();

            double Lat = location1.getLatitude();
            double Long = location1.getLongitude();

            Log.d("lat:", String.valueOf(location1.getLatitude()));
            Log.d("long:", String.valueOf(location1.getLongitude()));

            myStringArray[0] = String.valueOf(location1.getLatitude());
            myStringArray[1] = String.valueOf(location1.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return myStringArray;
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(EditProfileActivity.this, MyAccountMenu.class);
//        startActivity(i);
        finish();
    }

    //3
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }

}