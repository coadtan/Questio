package com.questio.projects.questio.activities;

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
public class PicturePuzzleAction extends ActionBarActivity implements View.OnClickListener {
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
    int points = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pointTV = (TextView) toolbar.findViewById(R.id.toolbar_points);
        pointTV.setText(Integer.toString(points));

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


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                questId = null;
                questName = null;
            } else {
                questId = extras.getString(QuestioConstants.QUEST_ID);
                questName = extras.getString(QuestioConstants.QUEST_NAME);
            }
        } else {
            questId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ID);
            questName = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_NAME);
        }
        Log.d(LOG_TAG, "questid: " + questId + " questName: " + questName);

        getSupportActionBar().setTitle(questName);

        requestPicturePuzzleData(Integer.parseInt(questId));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topLeft:
                topLeft.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.topMiddle:
                topMiddle.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.topRight:
                topRight.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.middleLeft:
                middleLeft.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.middleMiddle:
                middleMiddle.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.middleRight:
                middleRight.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.bottomLeft:
                bottomLeft.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.bottomMiddle:
                bottomMiddle.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
            case R.id.bottomRight:
                bottomRight.setVisibility(View.INVISIBLE);
                onUnMask();
                break;
        }

    }

    void onUnMask() {
        points--;
        pointTV.setText(Integer.toString(points));
        //
    }

    private void requestPicturePuzzleData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getPicturePuzzleByPuzzleId(id, new Callback<PicturePuzzle[]>() {
            @Override
            public void success(PicturePuzzle[] picturePuzzleTemp, Response response) {
                if(picturePuzzleTemp[0]!= null){
                    pp = picturePuzzleTemp[0];

                    Log.d(LOG_TAG,QuestioHelper.getImgLink(pp.getImageUrl()));
                    Glide.with(PicturePuzzleAction.this)
                            .load(QuestioHelper.getImgLink(pp.getImageUrl()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(picturePuzzleQuestion);

                    picturePuzzleHint.setHint(pp.getHelperAnswer());

                    picturePuzzleAnswer.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            currentAnswer = picturePuzzleAnswer.getText().toString();
                            if (currentAnswer.equalsIgnoreCase(pp.getCorrectAnswer())) {
                                picturePuzzleAnswer.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                            }
                        }
                    });

                    topLeft.setOnClickListener(PicturePuzzleAction.this);
                    topMiddle.setOnClickListener(PicturePuzzleAction.this);
                    topRight.setOnClickListener(PicturePuzzleAction.this);
                    middleLeft.setOnClickListener(PicturePuzzleAction.this);
                    middleMiddle.setOnClickListener(PicturePuzzleAction.this);
                    middleRight.setOnClickListener(PicturePuzzleAction.this);
                    bottomLeft.setOnClickListener(PicturePuzzleAction.this);
                    bottomMiddle.setOnClickListener(PicturePuzzleAction.this);
                    bottomRight.setOnClickListener(PicturePuzzleAction.this);
                }else{
                    Log.d(LOG_TAG,"Picture Puzzle is null");
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
}
