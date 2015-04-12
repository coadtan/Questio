package com.questio.projects.questio.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.questio.projects.questio.R;

/**
 * Created by ning jittima on 12/4/2558.
 */
public class RiddleAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = RiddleAction.class.getSimpleName();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {

    }
}
