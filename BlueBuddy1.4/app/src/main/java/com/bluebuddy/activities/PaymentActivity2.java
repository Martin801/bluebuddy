package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class PaymentActivity2 extends BuddyActivity {
    private static final String EXTRA_PAYMENT = "";
    private static final String TAG = "paymentBluebuddy";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    //Ae2a81sOb7xFxpv7bLCl2F5jLK3e3ajyMye5-8-cmogQhAg_A7fC-wrwkbB21HFBhWNhzM6V1xhwSG8t
    private static final String CONFIG_CLIENT_ID = "Ae2a81sOb7xFxpv7bLCl2F5jLK3e3ajyMye5-8-cmogQhAg_A7fC-wrwkbB21HFBhWNhzM6V1xhwSG8t";
    //  private static final String CONFIG_CLIENT_ID = "AfqU1qkJh31qXvGmema-uV9Ws7xpx2gPaun-5BK2yliaBpNmw73addvaNQci29V-c2MpgsA8g3ZOTr__";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    String token;
    String Payprice, Forname, Id, category, category2, Payprice2;
    String category3 = "";

    // private static final String CONFIG_CLIENT_ID = "AQNCTXEi3IXY3s61fM9Pd2AtpkOQVjt31Jh8JmPlUpu3EgakZapeKVMPxHDdwYIQ6yLiM5fcEReh6Uv7";

    Button onBuyPressed;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_payment);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("PAYMENT");
        Bundle bundle = getIntent().getExtras();
        Payprice = bundle.getString("price");
        //
        if (Payprice.contains(" ")) {
            String[] separated = Payprice.split(" ");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            Payprice2 = separated[1].toString().trim();
            //  holder.tvevtName.setText(separated[1]);
        } else if (!Payprice.contains(" ")) {
            Toast.makeText(PaymentActivity2.this, "Entered price ", Toast.LENGTH_LONG).show();
        }
        //
        if (bundle.containsKey(category2)) {
            category3 = bundle.getString("category2");
            if (!category3.equals("")) {
                category = category3;
            } else {
                category = " ";
            }
        }
        Id = bundle.getString("ID");
        Forname = bundle.getString("name");
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    public void onBuyPressed(View pressed) {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PaymentActivity2.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(Payprice2), "USD", Forname, paymentIntent);
    }

    private PayPalPayment getStuffToBuy(String paymentIntent) {
        PayPalItem[] items =
                {
                        new PayPalItem("sample item #1", 2, new BigDecimal("87.50"), "USD",
                                "sku-12345678"),
                        new PayPalItem("free sample item #2", 1, new BigDecimal("0.00"),
                                "USD", "sku-zero-price"),
                        new PayPalItem("sample item #3 with a longer name", 6, new BigDecimal("37.99"),
                                "USD", "sku-33333")
                };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("7.21");
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "sample item", paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PaymentActivity2.this, PayPalFuturePaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(PaymentActivity2.this, PayPalProfileSharingActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }

    private PayPalOAuthScopes getOauthScopes() {

        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    }

    protected void displayResultText(String result) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        if (Forname.equals("Bluebuddy Charter")) {
            Call<ServerResponsechrt> userCall = service.Charterfeature("Bearer " + token, Id, "1");
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        Log.d("onResponse", "" + response.body().getStatus());
                        Intent i = new Intent(PaymentActivity2.this, MyListingActivity.class);

                        startActivity(i);

                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        } else if (Forname.equals("Bluebuddy Product")) {
            Call<ServerResponsechrt> userCall = service.Productfeature("Bearer " + token, Id, "1");
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        Log.d("onResponse", "" + response.body().getStatus());
                        Intent i = new Intent(PaymentActivity2.this, MyListingActivity.class);

                        startActivity(i);

                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        } else if (Forname.equals("Bluebuddy Course")) {
            Call<ServerResponsechrt> userCall = service.coursesfeature("Bearer " + token, Id, "1");
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        Log.d("onResponse", "" + response.body().getStatus());
                        Intent i = new Intent(PaymentActivity2.this, MyListingActivity.class);

                        startActivity(i);

                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.d("mydetails:", confirm.toJSONObject().toString().trim());
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        displayResultText("PaymentConfirmation info received from PayPal");


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Future Payment code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        displayResultText("Profile Sharing code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyListingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
