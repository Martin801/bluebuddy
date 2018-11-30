package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ParticipantListAdapter;
import com.bluebuddy.models.ParticipantsList;

import java.util.ArrayList;

/**
 * Created by USER on 5/7/2017.
 */

public class ParticipantsFragmentMyEvents extends Fragment {

    RecyclerView recyclerView;
//    String[] Pplname = {"Martin jones", "Steve Jobes", "Steve Jobes", "Martin jones", "Steve Jobes", "Steve Jobes", "Steve Jobes", "Steve Jobes"};
//    int[] Pplpic = {
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1,
//            R.drawable.user_icon1};
    private RecyclerView.LayoutManager mLayoutManager;
    private ParticipantListAdapter participantListAdapter;
    private Activity _activity;
    private ArrayList<ParticipantsList> ParticipantsList;
    private Context _context;

    public static ParticipantsFragmentMyEvents createInstance(int itemsCount) {
        ParticipantsFragmentMyEvents participantsFragment = new ParticipantsFragmentMyEvents();
        Bundle bundle = new Bundle();
        participantsFragment.setArguments(bundle);
        return participantsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myevent_participants, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        ParticipantsList = new ArrayList<>();
//        for (int i = 0; i < Pplname.length; i++) {
//            ParticipantsList participantsList = new ParticipantsList(Pplname[i], Pplpic[i]);
//            ParticipantsList.add(participantsList);
//        }
//        participantListAdapter = new ParticipantListAdapter(_activity, _context, null);
//        recyclerView.setAdapter(participantListAdapter);
//        participantListAdapter.notifyDataSetChanged();
    }

}
