package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.EmptyRecyclerView;
import com.bluebuddy.adapters.ParticipantListAdapter;
import com.bluebuddy.models.Participant;

import java.util.ArrayList;

/**
 * Created by USER on 5/7/2017.
 */

public class ParticipantsFragment extends Fragment {

    private EmptyRecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ParticipantListAdapter participantListAdapter;
    private Activity _activity;
    private Context _context;
    private ArrayList<Participant> participantsList;
    private LinearLayout emptyParticipantView;
    private String token1 = "";
    private Boolean isRemoveVisible;
    private String eventId;

    public static ParticipantsFragment createInstance(int itemsCount) {
        ParticipantsFragment participantsFragment = new ParticipantsFragment();
        Bundle bundle = new Bundle();
        participantsFragment.setArguments(bundle);
        return participantsFragment;
    }

    public static ParticipantsFragment createInstance(String eventId, String token, boolean isRemoveVisible, ArrayList<Participant> participation_list) {
        ParticipantsFragment participantsFragment = new ParticipantsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("participationList", participation_list);
        bundle.putString("token", token);
        bundle.putString("eventId",eventId);
        bundle.putBoolean("isRemoveVisible", isRemoveVisible);
        participantsFragment.setArguments(bundle);

        return participantsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_participants, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            participantsList = (ArrayList<Participant>) bundle.getSerializable("participationList");
            token1 = (String) bundle.getString("token","");
            isRemoveVisible = (Boolean) bundle.getBoolean("isRemoveVisible", false);
            eventId = (String) bundle.getString("eventId", "");
            participantListAdapter = new ParticipantListAdapter(eventId, token1, isRemoveVisible, _activity, _context, participantsList);
        } else {
            participantListAdapter = new ParticipantListAdapter(eventId, token1, isRemoveVisible, _activity, _context, null);
            Log.d("tripDetail", "Trip detail list is empty");
        }

        participantListAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyParticipantView = (LinearLayout) view.findViewById(R.id.emptyParticipantView);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setEmptyView(emptyParticipantView);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(participantListAdapter);

        participantListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this._context = context;
        this._activity = getActivity();
    }

}
