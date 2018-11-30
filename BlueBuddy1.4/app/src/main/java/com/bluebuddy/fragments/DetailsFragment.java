package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 5/7/2017.
 */

public class DetailsFragment extends Fragment {
    private static String token1;
    private Button button4;
    private TextView detailfragid;
    private EventDetails allEventsDetails;
    private Activity _activity;
    private String token;


    public static DetailsFragment createInstance(int i) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public static DetailsFragment createInstance(String about) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DETAILS", about);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public static DetailsFragment createInstance(EventDetails details) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DETAILS", details);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public static DetailsFragment createInstance(String token, EventDetails details) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DETAILS", details);

       // bundle.putString(token, token);
        token1=bundle.getString("token", token);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public static DetailsFragment newInstance(){
        DetailsFragment detailsFragment = new DetailsFragment();
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle bundle = getArguments();

        if(bundle != null){
            if(bundle.containsKey("DETAILS")) {
                allEventsDetails = (EventDetails) bundle.getSerializable("DETAILS");
            }

            if(bundle.containsKey("token")) {
                token = bundle.getString("token");
            }

            button4 = (Button) rootView.findViewById(R.id.button4);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getevtid(allEventsDetails.getEvent_id(), allEventsDetails.getCreator_firebase_api_key());
                }
            });

            if(Integer.valueOf(allEventsDetails.getIs_editable()) > 0 || Integer.valueOf(allEventsDetails.getParticipated()) > 0){
                button4.setVisibility(View.GONE);
            }else if(Integer.valueOf(allEventsDetails.getParticipated()) > 0){
                button4.setVisibility(View.GONE);
            }

            detailfragid = (TextView) rootView.findViewById(R.id.detailfragid);
            String evtdesc = allEventsDetails.getDescription();
            detailfragid.setText(evtdesc);
        }

        return rootView;
    }

    public void getevtid(String event_id, final String accessToken){
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.participateToEvent("Bearer "+token1,  event_id);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Log.d("TOKEN:", "" + token1);

                button4.setVisibility(View.GONE);

                HashMap<String, String> notification = new HashMap<String, String>();
                notification.put("body", "A new user wanted to participate");
                notification.put("title", "Participation Request");
                notification.put("icon", "appicon");

//                sendNotification(new PushRequest(accessToken, notification));
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendNotification(PushRequest notification){
        String serverKey = _activity.getResources().getString(R.string.firebase_server_key);

        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);

        pushCall.enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                Log.d("SUCCESS_REGISTER", "SENT");
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Log.d("FAILURE_REGISTER", "HERE I M");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this._activity = getActivity();
    }
}
