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

import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.QuestInActionAdapter;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.Zone;
import com.questio.projects.questio.utilities.DownloadImageHelper;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.util.ArrayList;

/**
 * Created by ning jittima on 4/4/2558.
 */
public class ZoneActivity extends ActionBarActivity {
    private static final String LOG_TAG = ZoneActivity.class.getSimpleName();
    Toolbar toolbar;
    ArrayList<Quest> quests;
    ListView quest_action_listview;
    Zone zone;
    ImageView quest_action_picture;
    ImageView quest_action_minimap;
    TextView zonename;
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
        zonename = (TextView) findViewById(R.id.zonename);
        item = (TextView) findViewById(R.id.item);
        reward = (TextView) findViewById(R.id.reward);
        zonetype = (TextView) findViewById(R.id.zonetype);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        Log.d(LOG_TAG,"qrcode: " + qrcode);
        Log.d(LOG_TAG,"savedInstanceState: " + savedInstanceState);
        int zoneIdFromQRCode = Zone.findZoneIdByQRCode(Integer.parseInt(qrcode));
        zone = Zone.getZoneByZoneId(zoneIdFromQRCode);
        quests = Quest.getAllQuestByZoneId(zoneIdFromQRCode);
        QuestInActionAdapter adapter = new QuestInActionAdapter(this, quests);
        quest_action_listview = (ListView) findViewById(R.id.quest_action_listview);
        quest_action_listview.setAdapter(adapter);
        quest_action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView questId = (TextView) view.findViewById(R.id.questid);
                TextView questName = (TextView) view.findViewById(R.id.questname);
                TextView questTypeInvisible = (TextView) view.findViewById(R.id.questTypeInvisible);
                String questIdForIntent = questId.getText().toString();
                String questNameForIntent = questName.getText().toString();
                switch (Integer.parseInt(questTypeInvisible.getText().toString())) {
                    case 1:
                        Intent intentToQuiz = new Intent(ZoneActivity.this, QuizAction.class);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        startActivity(intentToQuiz);
                        break;
                    case 2:
                        Intent intentToRiddle = new Intent(ZoneActivity.this, RiddleAction.class);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        startActivity(intentToRiddle);
                        break;
                    case 3:
                        Intent intentToPuzzle = new Intent(ZoneActivity.this, PicturePuzzleAction.class);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        startActivity(intentToPuzzle);
                        break;
                }


            }
        });
        zonename.setText(zone.getZoneName());
        item.setText(zone.getItemSet());
        reward.setText(Integer.toString(zone.getRewardId()));
        zonetype.setText(Integer.toString(zone.getZoneTypeId()));

        if (!(zone.getImageUrl() == null || zone.getMiniMapUrl() == null)) {
            new DownloadImageHelper(quest_action_picture).execute("http://52.74.64.61" + zone.getImageUrl());
            new DownloadImageHelper(quest_action_minimap).execute("http://52.74.64.61" + zone.getMiniMapUrl());
        }

    }

}
