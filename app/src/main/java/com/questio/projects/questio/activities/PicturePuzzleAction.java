package com.questio.projects.questio.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.PicturePuzzle;

/**
 * Created by ning jittima on 11/4/2558.
 */
public class PicturePuzzleAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = PicturePuzzleAction.class.getSimpleName();
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
        topLeft.setOnClickListener(this);
        topMiddle.setOnClickListener(this);
        topRight.setOnClickListener(this);
        middleLeft.setOnClickListener(this);
        middleMiddle.setOnClickListener(this);
        middleRight.setOnClickListener(this);
        bottomLeft.setOnClickListener(this);
        bottomMiddle.setOnClickListener(this);
        bottomRight.setOnClickListener(this);

        String questId;
        String questName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                questId = null;
                questName = null;
            } else {
                questId = extras.getString("questid");
                questName = extras.getString("questname");
            }
        } else {
            questId = (String) savedInstanceState.getSerializable("questid");
            questName = (String) savedInstanceState.getSerializable("questname");
        }
        Log.d(LOG_TAG, "questid: " + questId + " questName: " + questName);

        getSupportActionBar().setTitle(questName);

        pp = PicturePuzzle.getAllPicturePuzzleByQuestId(Integer.parseInt(questId));

        picturePuzzleAnswer.setHint(pp.getHelperAnswer());
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
}
