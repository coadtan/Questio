package com.questio.projects.questio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.slidingtabs.SlidingTabsBasicFragment;
import com.questio.projects.questio.models.Place;

/*
 * Created by coad4u4ever on 01-Apr-15.
 * This will be the Main Class for the application.
 */
public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!QuestioApplication.isLogin()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        setContentView(R.layout.main_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        Place place = new Place(getApplicationContext());
        Log.d(LOG_TAG,"count: " + place.getPlaceCount());
    }
}
