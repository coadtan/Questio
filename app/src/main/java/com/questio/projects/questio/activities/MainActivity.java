package com.questio.projects.questio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.slidingtabs.SlidingTabsBasicFragment;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.utilities.HttpHelper;
import com.questio.projects.questio.utilities.PlaceSync;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.concurrent.ExecutionException;

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

        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        String displayName = prefs.getString(QuestioConstants.ADVENTURER_DISPLAYNAME, null);
        long id = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);

        Log.d(LOG_TAG, "displayName: " + displayName + " id: " + id);


        if (!QuestioApplication.isLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        Place place = new Place(getApplicationContext());
        try {
            String res = new HttpHelper().execute("http://52.74.64.61/api/select_all_place_count.php").get();

            Log.d(LOG_TAG, "count: " + res);
            long placeServerCount = QuestioHelper.getPlaceCountFromJson(res);
            long placeSQLiteCount = place.getPlaceCount();
            Log.d(LOG_TAG, "placeServerCount: " + placeServerCount + " placeSQLiteCount: " + placeSQLiteCount);
            if (placeServerCount != placeSQLiteCount) {
                place.deleteAllPlace();
                new PlaceSync(getApplicationContext()).execute("http://52.74.64.61/api/select_all_place.php");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.main_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        Log.d(LOG_TAG, "count: " + place.getPlaceCount());
    }
}
