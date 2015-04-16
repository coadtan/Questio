package com.questio.projects.questio;

import android.app.Application;
import android.content.res.Configuration;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class QuestioApplication extends Application {
    private static final String LOG_TAG = QuestioApplication.class.getSimpleName();
    private static QuestioApplication singleton;
    private static boolean login = false;
    public static GoogleApiClient mGoogleApiClient;

    public QuestioApplication getInstance() {
        return singleton;
    }

    public static boolean isLogin() {
        return login;
    }


    public static void setLogin(boolean login) {
        QuestioApplication.login = login;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


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
