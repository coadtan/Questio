package com.questio.projects.questio.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Avatar;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AvatarActivity extends AppCompatActivity {
    private static final String LOG_TAG = AvatarActivity.class.getSimpleName();
    QuestioAPIService api;
    RestAdapter adapter;
    long adventurerId;

    @Bind(R.id.avatar_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RestAdapter.Builder().setEndpoint(QuestioConstants.ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        setContentView(R.layout.activity_avatar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        handleInstanceState(savedInstanceState);

        api.getAvatarCountByAvatarId(adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String avatarCountStr = QuestioHelper.getJSONStringValueByTag("avatarcount", response);
                int avatarCount = Integer.parseInt(avatarCountStr);
                boolean hasAvatar = (avatarCount == 1);
                if (hasAvatar) {
                    populateAvatar();
                } else {
                    insertNewAvatar();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                adventurerId = 0;
            } else {
                adventurerId = extras.getLong(QuestioConstants.ADVENTURER_ID);
            }
        } else {
            adventurerId = (long) savedInstanceState.getSerializable(QuestioConstants.ADVENTURER_ID);
        }
        Log.d(LOG_TAG, QuestioConstants.ADVENTURER_ID + ": " + adventurerId);
        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Avatar");
        }
    }

    private void insertNewAvatar() {
        api.insertNewAvatar(adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "insert new avatar successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "insert new avatar failure");
            }
        });
    }

    private void populateAvatar() {
        api.getAvatarByAvatarId(adventurerId, new Callback<Avatar[]>() {
            @Override
            public void success(Avatar[] avatars, Response response) {
                Log.d(LOG_TAG, "get avatar successfully");
                Log.d(LOG_TAG, avatars[0].toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "get avatar failure");
            }
        });
    }
}
