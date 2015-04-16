package com.questio.projects.questio;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.utilities.HttpHelper;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.concurrent.ExecutionException;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class QuestioApplication extends Application {
    private static final String LOG_TAG = QuestioApplication.class.getSimpleName();
    private static QuestioApplication singleton;

    public QuestioApplication getInstance() {
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        Place place = new Place(getApplicationContext());
        try {
            String res = new HttpHelper().execute("http://52.74.64.61/api/select_all_place_count.php").get();

            Log.d(LOG_TAG, "count: " + res);
            long placeServerCount = QuestioHelper.getPlaceCountFromJson(res);
            long placeSQLiteCount = place.getPlaceCount();
            Log.d(LOG_TAG, "placeServerCount: " + placeServerCount + " placeSQLiteCount: " + placeSQLiteCount);
            if (placeServerCount != placeSQLiteCount) {
                place.delectAllPlace();
                new PlaceSync(getApplicationContext()).execute("http://52.74.64.61/api/select_all_place.php");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
