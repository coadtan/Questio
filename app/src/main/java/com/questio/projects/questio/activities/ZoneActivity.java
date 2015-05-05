package com.questio.projects.questio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.QuestInActionAdapter;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.Zone;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ning jittima on 4/4/2558.
 */
public class ZoneActivity extends ActionBarActivity {
    private static final String LOG_TAG = ZoneActivity.class.getSimpleName();
    Toolbar toolbar;
    ArrayList<Quest> quests;
    private ListView quest_action_listview;
    Zone zone;
    ImageView quest_action_picture;
    ImageView quest_action_minimap;
    //    TextView zonename;
    TextView item;
    TextView reward;
    TextView zonetype;
    String qrcode;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("qrcode", qrcode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_action);

        quest_action_picture = (ImageView) findViewById(R.id.quest_action_picture);
        quest_action_minimap = (ImageView) findViewById(R.id.quest_action_minimap);
//        zonename = (TextView) findViewById(R.id.zonename);
        item = (TextView) findViewById(R.id.item);
        reward = (TextView) findViewById(R.id.reward);
        zonetype = (TextView) findViewById(R.id.zonetype);
        quest_action_listview = (ListView) findViewById(R.id.quest_action_listview);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("กำลังโหลด....");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            Log.d(LOG_TAG, "savedInstanceState: null");
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                qrcode = null;
            } else {
                qrcode = extras.getString("qrcode");
            }
        } else {
            Log.d(LOG_TAG, "savedInstanceState: !null");
            //qrcode = (String) savedInstanceState.getSerializable("qrcode");
            qrcode = savedInstanceState.getString("qrcode");
        }
        Log.d(LOG_TAG, "qrcode: " + qrcode);
        Log.d(LOG_TAG, "savedInstanceState: " + savedInstanceState);
        int zoneIdFromQRCode = Zone.findZoneIdByQRCode(Integer.parseInt(qrcode));
        requestZoneData(zoneIdFromQRCode);


        requestQuestData(zoneIdFromQRCode);

        quest_action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView questId = (TextView) view.findViewById(R.id.questid);
                TextView questName = (TextView) view.findViewById(R.id.questname);
                TextView questTypeInvisible = (TextView) view.findViewById(R.id.questTypeInvisible);
                TextView zoneId = (TextView) view.findViewById(R.id.quest_zoneId);
                String questIdForIntent = questId.getText().toString();
                String questNameForIntent = questName.getText().toString();
                String zoneIdForIntent = zoneId.getText().toString();
                switch (Integer.parseInt(questTypeInvisible.getText().toString())) {
                    case 1:
//                        Intent intentToQuiz = new Intent(ZoneActivity.this, QuizAction.class);
                        Intent intentToQuiz = new Intent(ZoneActivity.this, QuizActivity.class);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);

                        startActivity(intentToQuiz);
                        break;
                    case 2:
                        Intent intentToRiddle = new Intent(ZoneActivity.this, RiddleAction.class);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);
                        startActivity(intentToRiddle);
                        break;
                    case 3:
                        Intent intentToPuzzle = new Intent(ZoneActivity.this, PicturePuzzleAction.class);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);
                        startActivity(intentToPuzzle);
                        break;
                }


            }
        });


    }

    private void requestQuestData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getAllQuestByZoneId(id, new Callback<ArrayList<Quest>>() {
            @Override
            public void success(ArrayList<Quest> quests, Response response) {
                if (quests != null) {
                    QuestInActionAdapter adapter = new QuestInActionAdapter(ZoneActivity.this, quests);
                    quest_action_listview.setAdapter(adapter);
                } else {
                    Log.d(LOG_TAG, "quests: is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuestData: failure");
            }
        });
    }

    private void requestZoneData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getZoneByZoneId(id, new Callback<Zone[]>() {
            @Override
            public void success(Zone[] zones, Response response) {
                if (zones != null) {
                    zone = zones[0];
//                    zonename.setText(zone.getZoneName());
                    getSupportActionBar().setTitle(zone.getZoneName());
                    item.setText(zone.getItemSet());
                    reward.setText(Integer.toString(zone.getRewardId()));
                    zonetype.setText(Integer.toString(zone.getZoneTypeId()));

                    if (!(zone.getImageUrl() == null)) {
                        Glide.with(ZoneActivity.this)
                                .load(QuestioHelper.getImgLink(zone.getImageUrl()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(quest_action_picture);
                    }
                    if (!(zone.getMiniMapUrl() == null)) {
                        Glide.with(ZoneActivity.this)
                                .load(QuestioHelper.getImgLink(zone.getMiniMapUrl()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(quest_action_minimap);
                    }
                } else {
                    Log.d(LOG_TAG, "Zone is null");
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }


}
