package com.questio.projects.questio.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.PlaceActivity;
import com.questio.projects.questio.adepters.QuestRecycleViewAdapter;

/**
 * Created by coad4u4ever on 05-Apr-15.
 */
public class QuestRecycleView extends Fragment {
    private static final String LOG_TAG = QuestRecycleView.class.getSimpleName();
    RecyclerView questBrowsingRecyclerView;
    QuestRecycleViewAdapter adapterRecycleView;
    View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "QuestRecycleView called");
        rootView = inflater.inflate(R.layout.quest_recycle_fragment, container, false);
        Bundle args = getArguments();
        questBrowsingRecyclerView = (RecyclerView)rootView.findViewById(R.id.quest_browsing_recycler_view);
        questBrowsingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterRecycleView = new QuestRecycleViewAdapter(getActivity(), ((PlaceActivity)getActivity()).getQuests() );
        questBrowsingRecyclerView.setAdapter(adapterRecycleView);

        return rootView;
    }
}
