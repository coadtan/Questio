package com.questio.projects.questio.sections;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questio.projects.questio.R;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class SearchSection extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_search, container, false);
        Bundle args = getArguments();

        return view;
    }
}
