package com.questio.projects.questio.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.PicturePuzzle;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ning jittima on 11/4/2558.
 */
public class PicturePuzzleAction extends ActionBarActivity implements View.OnClickListener, TextWatcher, Callback<Response> {
    private static final String LOG_TAG = PicturePuzzleAction.class.getSimpleName();
    private ImageView picturePuzzleQuestion;
    private ImageView topLeft;
    private ImageView topMiddle;
    private ImageView topRight;
    private ImageView middleLeft;
    private ImageView middleMiddle;
    private ImageView middleRight;
    private ImageView bottomLeft;
    private ImageView bottomMiddle;
    private ImageView bottomRight;
    private EditText picturePuzzleAnswer;
    private EditText picturePuzzleHint;
    String currentAnswer;
    TextView pointTV;
    Toolbar toolbar;
    PicturePuzzle pp;
    int points;
    int ref;
    int qid;
    int zid;
    long adventurerId;


    QuestioAPIService api;
    RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pointTV = (TextView) toolbar.findViewById(R.id.toolbar_points);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        picturePuzzleQuestion = (ImageView) findViewById(R.id.picture_puzzle_question);
        topLeft = (ImageView) findViewById(R.id.topLeft);
        topMiddle = (ImageView) findViewById(R.id.topMiddle);
        topRight = (ImageView) findViewById(R.id.topRight);
        middleLeft = (ImageView) findViewById(R.id.middleLeft);
        middleMiddle = (ImageView) findViewById(R.id.middleMiddle);
        middleRight = (ImageView) findViewById(R.id.middleRight);
        bottomLeft = (ImageView) findViewById(R.id.bottomLeft);
        bottomMiddle = (ImageView) findViewById(R.id.bottomMiddle);
        bottomRight = (ImageView) findViewById(R.id.bottomRight);
        picturePuzzleAnswer = (EditText) findViewById(R.id.picture_puzzle_answer);
        picturePuzzleHint = (EditText) findViewById(R.id.picture_puzzle_hint);


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

        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);


        requestPicturePuzzleData(Integer.parseInt(questId));

        qid = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);

        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);

        ref = Integer.parseInt(Integer.toString(qid) + (int) adventurerId);
        getCurrentPoints();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topLeft:
                topLeft.setVisibility(View.INVISIBLE);
                onUnMask(1);
                break;
            case R.id.topMiddle:
                topMiddle.setVisibility(View.INVISIBLE);
                onUnMask(2);
                break;
            case R.id.topRight:
                topRight.setVisibility(View.INVISIBLE);
                onUnMask(3);
                break;
            case R.id.middleLeft:
                middleLeft.setVisibility(View.INVISIBLE);
                onUnMask(4);
                break;
            case R.id.middleMiddle:
                middleMiddle.setVisibility(View.INVISIBLE);
                onUnMask(5);
                break;
            case R.id.middleRight:
                middleRight.setVisibility(View.INVISIBLE);
                onUnMask(6);
                break;
            case R.id.bottomLeft:
                bottomLeft.setVisibility(View.INVISIBLE);
                onUnMask(7);
                break;
            case R.id.bottomMiddle:
                bottomMiddle.setVisibility(View.INVISIBLE);
                onUnMask(8);
                break;
            case R.id.bottomRight:
                bottomRight.setVisibility(View.INVISIBLE);
                onUnMask(9);
                break;
        }

    }

    void onUnMask(int position) {
        points--;
        pointTV.setText(Integer.toString(points));
        switch (position) {
            case 1:
                api.updatePuzzleProgressTopLeftPieceByRef(ref, this);
                break;
            case 2:
                api.updatePuzzleProgressTopMidPieceByRef(ref, this);
                break;
            case 3:
                api.updatePuzzleProgressTopRightPieceByRef(ref, this);
                break;
            case 4:
                api.updatePuzzleProgressMidLeftPieceByRef(ref, this);
                break;
            case 5:
                api.updatePuzzleProgressMidMidPieceByRef(ref, this);
                break;
            case 6:
                api.updatePuzzleProgressMidRightPieceByRef(ref, this);
                break;
            case 7:
                api.updatePuzzleProgressBottomLeftPieceByRef(ref, this);
                break;
            case 8:
                api.updatePuzzleProgressBottomMidPieceByRef(ref, this);
                break;
            case 9:
                api.updatePuzzleProgressBottomRightPieceByRef(ref, this);
                break;

        }
    }

    private void requestPicturePuzzleData(int id) {
        api.getPicturePuzzleByPuzzleId(id, new Callback<PicturePuzzle[]>() {
            @Override
            public void success(PicturePuzzle[] picturePuzzleTemp, Response response) {
                if (picturePuzzleTemp[0] != null) {
                    pp = picturePuzzleTemp[0];

                    Log.d(LOG_TAG, QuestioHelper.getImgLink(pp.getImageUrl()));
                    Glide.with(PicturePuzzleAction.this)
                            .load(QuestioHelper.getImgLink(pp.getImageUrl()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(picturePuzzleQuestion);
                    picturePuzzleHint.setHint(pp.getHelperAnswer());
                    picturePuzzleAnswer.addTextChangedListener(PicturePuzzleAction.this);
                    topLeft.setOnClickListener(PicturePuzzleAction.this);
                    topMiddle.setOnClickListener(PicturePuzzleAction.this);
                    topRight.setOnClickListener(PicturePuzzleAction.this);
                    middleLeft.setOnClickListener(PicturePuzzleAction.this);
                    middleMiddle.setOnClickListener(PicturePuzzleAction.this);
                    middleRight.setOnClickListener(PicturePuzzleAction.this);
                    bottomLeft.setOnClickListener(PicturePuzzleAction.this);
                    bottomMiddle.setOnClickListener(PicturePuzzleAction.this);
                    bottomRight.setOnClickListener(PicturePuzzleAction.this);
                    requestQuestProgress();

                } else {
                    Log.d(LOG_TAG, "Picture Puzzle is null");
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


    private void requestQuestProgress(){
        api.getQuestProgressByQuestIdAndAdventurerId(qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (QuestioHelper.responseToString(response).equalsIgnoreCase("null")) {
                    insertProgressData();
                    insertPuzzleProgress();
                } else {
                    String statusStr = QuestioHelper.getJSONStringValueByTag("statusid", response);
                    int status = Integer.parseInt(statusStr);
                    if(status==QuestioConstants.QUEST_FINISHED){
                        topLeft.setVisibility(View.INVISIBLE);
                        topRight.setVisibility(View.INVISIBLE);
                        topMiddle.setVisibility(View.INVISIBLE);
                        middleLeft.setVisibility(View.INVISIBLE);
                        middleMiddle.setVisibility(View.INVISIBLE);
                        middleRight.setVisibility(View.INVISIBLE);
                        bottomLeft.setVisibility(View.INVISIBLE);
                        bottomMiddle.setVisibility(View.INVISIBLE);
                        bottomRight.setVisibility(View.INVISIBLE);
                        disableAll();
                        picturePuzzleHint.setVisibility(View.INVISIBLE);

                        picturePuzzleAnswer.setText(pp.getCorrectAnswer());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    private void insertProgressData() {
        Log.d(LOG_TAG, "No Progress in Quest");
        api.addQuestProgress(qid, adventurerId, ref, zid, 3, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void insertPuzzleProgress() {
        api.addPuzzleProgress(ref, qid, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateQuestStatus(int status) {
        Log.d(LOG_TAG, "updateQuestStatus: called");
        api.updateStatusQuestProgressByQuestIdAndAdventurerId(status, qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateScore(){
        api.updateScoreQuestProgressByQuestIdAndAdventurerId(points, qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updateScore: success with points = " + points);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "updateScore: failed");
            }
        });
    }
    private void disableAll() {
        topRight.setClickable(false);
        topMiddle.setClickable(false);
        topLeft.setClickable(false);
        middleRight.setClickable(false);
        middleMiddle.setClickable(false);
        middleLeft.setClickable(false);
        bottomRight.setClickable(false);
        bottomMiddle.setClickable(false);
        bottomLeft.setClickable(false);
        topRight.setEnabled(false);
        topMiddle.setEnabled(false);
        topLeft.setEnabled(false);
        bottomRight.setEnabled(false);
        bottomMiddle.setEnabled(false);
        bottomLeft.setEnabled(false);
        middleRight.setEnabled(false);
        middleMiddle.setEnabled(false);
        middleLeft.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        currentAnswer = picturePuzzleAnswer.getText().toString();
        if (currentAnswer.equalsIgnoreCase(pp.getCorrectAnswer())) {
            onCorrect();
        }
    }

    private void onCorrect() {
        picturePuzzleAnswer.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
        updateQuestStatus(QuestioConstants.QUEST_FINISHED);
        updateScore();
        topLeft.setVisibility(View.INVISIBLE);
        topRight.setVisibility(View.INVISIBLE);
        topMiddle.setVisibility(View.INVISIBLE);
        middleLeft.setVisibility(View.INVISIBLE);
        middleMiddle.setVisibility(View.INVISIBLE);
        middleRight.setVisibility(View.INVISIBLE);
        bottomLeft.setVisibility(View.INVISIBLE);
        bottomMiddle.setVisibility(View.INVISIBLE);
        bottomRight.setVisibility(View.INVISIBLE);
        disableAll();
    }


    private void getCurrentPoints(){
        api.getCurrentPointsByRef(ref, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                points = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("points", QuestioHelper.responseToString(response)));
                pointTV.setText(Integer.toString(points));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void success(Response response, Response response2) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
