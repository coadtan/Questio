package com.questio.projects.questio.sections;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questio.projects.questio.R;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class HOFSection extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_halloffame, container, false);
        Bundle args = getArguments();

        return rootView;
    }
}
