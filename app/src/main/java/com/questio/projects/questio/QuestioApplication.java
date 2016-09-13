package com.questio.projects.questio;

import android.app.Application;
import android.content.res.Configuration;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;


public class QuestioApplication extends Application  {
    private static final String LOG_TAG = QuestioApplication.class.getSimpleName();
    public static GoogleApiClient mGoogleApiClient;
    public static GoogleSignInAccount mGoogleSignInAccount;
    private static QuestioApplication singleton;
    private static boolean login = false;

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        QuestioApplication.login = login;
    }

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
