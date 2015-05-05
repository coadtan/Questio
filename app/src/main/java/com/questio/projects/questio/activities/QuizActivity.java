package com.questio.projects.questio.activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Quiz;
import com.questio.projects.questio.models.QuizProgress;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by coad4u4ever on 05-May-15.
 * This Class will act as Quiz Manager
 */


public class QuizActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();
    Toolbar toolbar;


    // These 4 values have value after handleInstanceState() called
    int qid;
    int zid;
    int ref;
    long adventurerId;


    // View Zone
    TextView score;
    TextView quizQuestion;
    TextView quizSequence;
    Button quizChoice1;
    Button quizChoice2;
    Button quizChoice3;
    Button quizChoice4;

    // Misc
    private final int FIRST_QUIZ = 0;
    int currentQuiz;
    int currentSeq;
    int currentScore;
    int totalQuiz;

    // API zone
    QuestioAPIService api;
    RestAdapter adapter;

    // Quiz Data Zone
    ArrayList<Quiz> quizs;
    ArrayList<QuizProgress> quizProgresses;
    HashMap<String, String> quizStatusHashMap;
    Quiz q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        handleToolbar();
        handleView();
        handleInstanceState(savedInstanceState);
        adapter = new RestAdapter.Builder().setEndpoint(QuestioConstants.ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        currentQuiz = 0;
        currentScore = 0;
        requestQuizData(qid);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_choice_1:
                onAnswer(1);
                break;
            case R.id.quiz_choice_2:
                onAnswer(2);
                break;
            case R.id.quiz_choice_3:
                onAnswer(3);
                break;
            case R.id.quiz_choice_4:
                onAnswer(4);
                break;
        }
    }


    private void handleView() {
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        quizSequence = (TextView) findViewById(R.id.quiz_sequence);
        score = (TextView) findViewById(R.id.quiz_score);
        quizChoice1 = (Button) findViewById(R.id.quiz_choice_1);
        quizChoice2 = (Button) findViewById(R.id.quiz_choice_2);
        quizChoice3 = (Button) findViewById(R.id.quiz_choice_3);
        quizChoice4 = (Button) findViewById(R.id.quiz_choice_4);
        quizChoice1.setOnClickListener(this);
        quizChoice2.setOnClickListener(this);
        quizChoice3.setOnClickListener(this);
        quizChoice4.setOnClickListener(this);
    }

    private void handleInstanceState(Bundle savedInstanceState) {
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
        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        qid = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);
        ref = Integer.parseInt(Integer.toString(qid) + (int) adventurerId);
    }

    private void handleToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void requestQuizData(int qid) {

        api.getAllQuizByQuestId(qid, new Callback<ArrayList<Quiz>>() {
            @Override
            public void success(ArrayList<Quiz> quizsTemp, Response response) {
                if (quizsTemp != null) {
                    quizs = quizsTemp;
                    totalQuiz = quizs.size();
                    requestQuizProgresses();
                    LinearLayout quizActionProgressLinerSection = (LinearLayout) findViewById(R.id.quiz_progress_bar);
                    quizActionProgressLinerSection.setWeightSum(totalQuiz);
                    int buttonId = 0;
                    for (int i = 0; i < totalQuiz; i++) {
                        Button button = new Button(QuizActivity.this);
                        if (i == 0) {
                            button.setBackgroundColor(getResources().getColor(R.color.yellow_quiz_unanswered));
                            button.setText("?");
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        button.setTextColor(getResources().getColor(R.color.white));
                        button.setBackgroundColor(getResources().getColor(R.color.grey_700));
                        params.setMargins(5, 5, 5, 5);
                        button.setId(buttonId);
                        button.setContentDescription(Integer.toString(quizs.get(i).getQuizId()));
                        button.setOnClickListener(new ButtonProgressListener(buttonId));
                        button.setLayoutParams(params);
                        quizActionProgressLinerSection.addView(button);
                        buttonId++;
                    }
                    populateQuiz(FIRST_QUIZ);

                } else {
                    Log.d(LOG_TAG, "Quiz is null");
                }


            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
            }
        });
    }


    private void insertProgressData() {
        api.addQuestProgress(qid, adventurerId, ref, zid, 1, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);
                for (Quiz q : quizs) {
                    api.addQuizProgress(ref, q.getQuizId(), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            String questioStatus = QuestioHelper.responseToString(response);
                            if (QuestioHelper.getJSONStringValueByTag("status", questioStatus).equalsIgnoreCase("1")) {
                                Log.d(LOG_TAG, "Add Quiz Progress Successful");
                            } else {
                                Log.d(LOG_TAG, "Add Quiz Progress Failed: " + questioStatus);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(LOG_TAG, "addQuizProgress FAILURE");
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    void populateQuiz(int i) {
        q = quizs.get(i);
        currentSeq = i;
        quizQuestion.setText(q.getQuestion());
        quizSequence.setText(Integer.toString(q.getSeqId()));
        quizChoice1.setText(q.getChoiceA());
        quizChoice2.setText(q.getChoiceB());
        quizChoice3.setText(q.getChoiceC());
        quizChoice4.setText(q.getChoiceD());
        currentQuiz = q.getQuizId();
    }

    public void changeButtonIndicator(int selected) {
        View v = findViewById(android.R.id.content);
        Button b;
        for (int i = 0; i < quizs.size(); i++) {
            b = (Button) v.findViewById(i);
            if (i == selected) {
                b.setTextColor(getResources().getColor(R.color.white));
                b.setText("?");

            } else {
                b.setText("");
            }
        }
    }


    private void requestQuizProgresses() {
        Log.d(LOG_TAG, "requestQuizProgress: called");
        api.getQuizProgressByRef(ref, new Callback<ArrayList<QuizProgress>>() {
            @Override
            public void success(ArrayList<QuizProgress> quizProgressesTemp, Response response) {
                quizProgresses = quizProgressesTemp;
                Log.d(LOG_TAG, "requestQuizProgress: success");
                if (null == quizProgresses) {
                    Log.d(LOG_TAG, "requestQuizProgress: success but quizProgresses is null");
                    insertProgressData();
                } else {
                    changeButtonProgressColor();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuizProgress: failure");
            }
        });
    }


    private void changeButtonProgressColor() {
        View v = findViewById(android.R.id.content);
        Button b;
        for (int i = 0; i < quizs.size(); i++) {
            b = (Button) v.findViewById(i);
            int quizIDFromButton = Integer.parseInt(b.getContentDescription().toString());
            for (QuizProgress qp : quizProgresses) {
                if (qp.getQuizId() == quizIDFromButton) {
                    switch (qp.getStatusId()) {
                        case 1:
                            // do nothing
                            break;
                        case 2:
                            b.setBackgroundColor(getResources().getColor(R.color.yellow_quiz_unanswered));
                            break;
                        case 3:
                            b.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                            break;
                        case 4:
                            b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));
                            break;
                    }
                }
            }
        }
    }

    void onAnswer(final int answer) {
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.setCancelable(true);
        TextView answerTV = (TextView) dialog.findViewById(R.id.confirm_answer);
        switch (answer) {
            case 1:
                answerTV.setText(q.getChoiceA());
                break;
            case 2:
                answerTV.setText(q.getChoiceB());
                break;
            case 3:
                answerTV.setText(q.getChoiceC());
                break;
            case 4:
                answerTV.setText(q.getChoiceD());
                break;
        }
        ImageButton btnNo = (ImageButton) dialog.findViewById(R.id.confirm_no);
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        ImageButton btnYes = (ImageButton) dialog.findViewById(R.id.confirm_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                updateStateAnswerButton(answer);

                if (q.getAnswerId().equalsIgnoreCase(Integer.toString(answer))) {
                    onCorrect(q.getSeqId());

                } else {
                    onIncorrect(q.getSeqId());
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    void onCorrect(int seqId) {
        Button b = (Button) findViewById(seqId - 1);
        //    b.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
        api.updateScoreQuizProgressByRefAndQuizId(3, ref, Integer.parseInt(b.getContentDescription().toString()),
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    void onIncorrect(int seqId) {

//        Log.d(LOG_TAG, "Before minus 1: " + seqId);
//        Button b = (Button) findViewById(seqId - 1);
//        b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));

    }
    private void updateStateAnswerButton(int answer){
        switch (answer){
            case 1:
          //      api.updateStatusChoiceAQuizByRefAndQuizId()
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
    private class ButtonProgressListener implements Button.OnClickListener {
        int pos;

        public ButtonProgressListener(int position) {
            pos = position;
        }

        @Override
        public void onClick(View v) {
            populateQuiz(v.getId());
            changeButtonIndicator(v.getId());
            currentQuiz = q.getQuizId();
        }
    }
}
