package com.questio.projects.questio.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import net.sourceforge.zbar.Symbol;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ning jittima on 12/4/2558.
 */
public class RiddleAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = RiddleAction.class.getSimpleName();
    Toolbar toolbar;
    TextView riddle;
    Button hint1Btn;
    Button hint2Btn;
    Button hint3Btn;
    ImageButton scanHere;
    TextView hintReveal1;
    TextView hintReveal2;
    TextView hintReveal3;


    TextView scanTV;
    int scanLimit;

    int qid;
    int zid;
    long adventurerId;

    QuestioAPIService api;

    Riddle r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        scanTV = (TextView) toolbar.findViewById(R.id.toolbar_limit);

        riddle = (TextView)findViewById(R.id.riddle_riddle);
        hint1Btn = (Button)findViewById(R.id.riddle_hint1Btn);
        hint2Btn = (Button)findViewById(R.id.riddle_hint2Btn);
        hint3Btn = (Button)findViewById(R.id.riddle_hint3Btn);
        scanHere = (ImageButton)findViewById(R.id.riddle_scanHere);
        hintReveal1 = (TextView)findViewById(R.id.riddle_hintReveal1);
        hintReveal2 = (TextView)findViewById(R.id.riddle_hintReveal2);
        hintReveal3 = (TextView)findViewById(R.id.riddle_hintReveal3);






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String questId;
        String questName;
        String zoneId;


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                questId = null;
                questName = null;
                zoneId = null;
            } else {
                questId = extras.getString(QuestioConstants.QUEST_ID);
                questName = extras.getString(QuestioConstants.QUEST_NAME);
                zoneId = extras.getString(QuestioConstants.QUEST_ZONE_ID);
            }
        } else {
            questId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ID);
            questName = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_NAME);
            zoneId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ZONE_ID);
        }
        Log.d(LOG_TAG, "questid: " + questId + " questName: " + questName);

        getSupportActionBar().setTitle(questName);

        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        qid = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);

        //r = Riddle.getAllRiddleByRiddleId((Integer.parseInt(questId)));
        requestRiddleData(Integer.parseInt(questId));

        requestProgressData();
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.riddle_scanHere:
                Intent intent = new Intent(this, ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);
                updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                break;
            case R.id.riddle_hint1Btn:
                hintReveal1.setText(r.getHint1());
                updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                break;
            case R.id.riddle_hint2Btn:
                hintReveal2.setText(r.getHint2());
                updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                break;
            case R.id.riddle_hint3Btn:
                hintReveal3.setText(r.getHint3());
                updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode: " + requestCode);
        Log.d(LOG_TAG, "Activity.RESULT_OK: " + Activity.RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {
            // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
            // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
            //
            // Toast.makeText(getActivity(), "Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0), Toast.LENGTH_SHORT).show();
            // The value of type indicates one of the symbols listed in Advanced Options below.
            String[] qr = QuestioHelper.getDeQRCode(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            if (qr[0].equalsIgnoreCase(QuestioConstants.QRTYPE_RIDDLE_ANSWER)) {
                onAnswer(qr[1]);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void onAnswer(String answer){

        if(scanLimit != 0){
            if(answer.equalsIgnoreCase(Long.toString(r.getQrCode()))){
                riddle.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                updateQuestStatus(QuestioConstants.QUEST_CORRECT);
                scanHere.setEnabled(false);
                scanHere.setClickable(false);
            }else{
                scanLimit--;
                scanTV.setText(Integer.toString(scanLimit));
                updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                if(scanLimit == 0){
                    updateQuestStatus(QuestioConstants.QUEST_FAILED);
                    scanHere.setEnabled(false);
                    scanHere.setClickable(false);
                }
            }
        }

    }

    private void requestRiddleData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getRiddleByQuestId(id, new Callback<Riddle[]>() {
            @Override
            public void success(Riddle[] riddleTemp, Response response) {
                if(riddleTemp != null){
                    r = riddleTemp[0];
                    Log.d(LOG_TAG, r.toString());
                    if(r.getHint1().equalsIgnoreCase("")){
                        hint1Btn.setEnabled(false);
                        hint1Btn.setClickable(false);
                        hint1Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal1.setVisibility(View.INVISIBLE);
                    }

                    if(r.getHint2().equalsIgnoreCase("")){
                        hint2Btn.setEnabled(false);
                        hint2Btn.setClickable(false);
                        hint2Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal2.setVisibility(View.INVISIBLE);
                    }
                    if(r.getHint3().equalsIgnoreCase("")){
                        hint3Btn.setEnabled(false);
                        hint3Btn.setClickable(false);
                        hint3Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal3.setVisibility(View.INVISIBLE);
                    }

                    riddle.setText(r.getRidDetails());

                    scanLimit = r.getScanLimit();

                    hint1Btn.setOnClickListener(RiddleAction.this);
                    hint2Btn.setOnClickListener(RiddleAction.this);
                    hint3Btn.setOnClickListener(RiddleAction.this);
                    scanHere.setOnClickListener(RiddleAction.this);

                    scanTV.setText(Integer.toString(scanLimit));
                }else{
                    Log.d(LOG_TAG, "Riddle is null");
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getStackTrace());
            }
        });
    }

    private void requestProgressData() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
//        api.getQuestProgressByQuestIdAndAdventurerId(questId, adventurerId, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//                if (QuestioHelper.responseToString(response).equalsIgnoreCase("null")){
        Log.d(LOG_TAG, "No Progress in Quest");
        api.addQuestProgressNonQuiz(qid, adventurerId, zid, 2, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);


//                } else {
//                    Log.d(LOG_TAG, "Add Quest Progress Failed: " + questioStatus);
//                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
//                }else{
//                    Log.d(LOG_TAG, "Quiz Progress Exists");
//                }
    }

//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//    }

    private void updateQuestStatus(int status){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        api.updateStatusQuestProgressByQuestIdAndAdventurerId(status, qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
