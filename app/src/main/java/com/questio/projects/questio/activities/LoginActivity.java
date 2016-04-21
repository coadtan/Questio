package com.questio.projects.questio.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 0;
    GoogleApiClient mGoogleApiClient;
    boolean mIntentInProgress;
    boolean mSignInClicked;
    ConnectionResult mConnectionResult;

    @Bind(R.id.sign_in_button)
    SignInButton btnSignIn;

    Person currentPerson;
    QuestioAPIService api;
    Long aId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
//        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        QuestioApplication.mGoogleApiClient = mGoogleApiClient;
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signInWithGplus();
                break;
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;

        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                Toast.makeText(this, "ยินดีต้อนรับ: " + personName, Toast.LENGTH_LONG).show();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);


                Log.d(LOG_TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                Log.d(LOG_TAG, "" + currentPerson.getId());
                Log.d(LOG_TAG, "" + currentPerson.getBirthday());


                // step 1: isNewAdventurer
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(QuestioConstants.ENDPOINT)
                        .build();
                api = adapter.create(QuestioAPIService.class);

                api.getGuserIdByGuserId(currentPerson.getId(), QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "s:getGuserIdByGuserId");
                        String result = QuestioHelper.responseToString(response);
                        if (result.equalsIgnoreCase("null")) {
                            api.getCountAdventurer(QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {

                                    String result = QuestioHelper.responseToString(response);
                                    Log.d(LOG_TAG, "result: " + result);

                                    aId = (QuestioHelper.getAdventurerCountFromJson(result) + 1);
                                    Log.d(LOG_TAG, "aId: " + aId);
                                    // add to SharedPreferences
                                    SharedPreferences.Editor editor = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE).edit();
                                    editor.putLong(QuestioConstants.ADVENTURER_ID, aId);
                                    editor.putString(QuestioConstants.ADVENTURER_DISPLAYNAME, currentPerson.getDisplayName());
                                    editor.apply();
                                    // end of - add to SharedPreferences

                                    SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
                                    String displayName = prefs.getString(QuestioConstants.ADVENTURER_DISPLAYNAME, null);
                                    long id = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);

                                    Log.d(LOG_TAG, "displayName: " + displayName + " id: " + id);

                                    api.addAdventurerDetails(aId, currentPerson.getDisplayName(), currentPerson.getBirthday()
                                            ,QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response response2) {

                                            String result = QuestioHelper.responseToString(response);
                                            Log.d(LOG_TAG, "addAdDetail: " + result);

                                            api.addAdventurer(aId, currentPerson.getId(), Plus.AccountApi.getAccountName(mGoogleApiClient),
                                                    aId, aId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                                                        @Override
                                                        public void success(Response response, Response response2) {

                                                            String result = QuestioHelper.responseToString(response);
                                                            Log.d(LOG_TAG, "addAdventurer: " + result);
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {

                                                        }
                                                    });

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {

                                        }
                                    });


                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
                        } else {
                            Log.d(LOG_TAG, "gid: " + result + " is already exists.");

                            api.getAdventurerIdByGuserId(currentPerson.getId(), QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {

                                    String idStr = QuestioHelper.getJSONStringValueByTag("adventurerid", QuestioHelper.responseToString(response));
                                    SharedPreferences.Editor editor = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE).edit();
                                    editor.putLong(QuestioConstants.ADVENTURER_ID, Long.parseLong(idStr));
                                    editor.putString(QuestioConstants.ADVENTURER_DISPLAYNAME, currentPerson.getDisplayName());
                                    editor.apply();
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });


                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "f:getGuserIdByGuserId");
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        QuestioApplication.setLogin(true);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(LOG_TAG, "User access revoked!");
                            mGoogleApiClient.connect();

                        }

                    });
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }


}