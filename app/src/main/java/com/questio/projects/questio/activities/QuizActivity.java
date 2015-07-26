package com.questio.projects.questio.activities;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
 * Created by coad4u4ever on 16-May-15.
 */
public class QuizActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();
    private static final int I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_CORRECT = 1;
    private static final int I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_INCORRECT = 2;
    Toolbar toolbar;


    // These 4 values have value after handleInstanceState() called
    int questId;
    int zid;
    //int ref;
    long adventurerId;


    // View Zone
    TextView score;
    TextView quizQuestion;
    TextView quizSequence;
    TextView quizCurrentTv;
    TextView quizTotalNumberTv;
    Button quizChoice1;
    Button quizChoice2;
    Button quizChoice3;
    Button quizChoice4;
    Button quizPreBtn;
    Button quizNextBtn;

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
    HashMap<Integer, AnswerState> answerStatesMap;
    Quiz q;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doquiz_activity);
        handleToolbar();
        handleView();
        handleInstanceState(savedInstanceState);

        adapter = new RestAdapter.Builder().setEndpoint(QuestioConstants.ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        currentQuiz = 0;
        currentScore = 0;
        requestQuizData(questId);
    }


    private void handleView() {
        quizQuestion = (TextView) findViewById(R.id.quiz_question);
        quizSequence = (TextView) findViewById(R.id.quiz_sequence);
        score = (TextView) findViewById(R.id.quiz_score);
        quizChoice1 = (Button) findViewById(R.id.quiz_choice_1);
        quizChoice2 = (Button) findViewById(R.id.quiz_choice_2);
        quizChoice3 = (Button) findViewById(R.id.quiz_choice_3);
        quizChoice4 = (Button) findViewById(R.id.quiz_choice_4);
        quizPreBtn = (Button) findViewById(R.id.quiz_pre_btn);
        quizNextBtn = (Button) findViewById(R.id.quiz_next_btn);
        quizCurrentTv = (TextView) findViewById(R.id.quiz_current_tv);
        quizTotalNumberTv = (TextView) findViewById(R.id.quiz_total_number_tv);
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
        this.questId = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);
        //ref = Integer.parseInt(Integer.toString(this.questId) + (int) adventurerId);
        // set title of toolbar to questname
        getSupportActionBar().setTitle(questName);
    }

    private void handleToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
            case R.id.quiz_pre_btn:
                currentSeq--;
                if (currentSeq >= 0) {
                    populateQuiz(currentSeq);
                } else {
                    populateQuiz(totalQuiz - 1);
                }
                break;
            case R.id.quiz_next_btn:
                currentSeq++;
                if (currentSeq == (totalQuiz)) {
                    populateQuiz(FIRST_QUIZ);
                } else {
                    populateQuiz(currentSeq);
                }
                break;
        }
    }

    private void requestQuizData(int qid) {
        api.getAllQuizByQuestId(qid, new Callback<ArrayList<Quiz>>() {
            @Override
            public void success(ArrayList<Quiz> quizsTemp, Response response) {
                if (quizsTemp != null) {
                    quizs = quizsTemp;
                    totalQuiz = quizs.size();
                    requestQuizProgresses();
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

    void populateQuiz(int i) {
        q = quizs.get(i);
        Log.d(LOG_TAG, q.toString());
        currentSeq = i;
        quizCurrentTv.setText(Integer.toString(i + 1));
        quizTotalNumberTv.setText(Integer.toString(quizs.size()));

        // clear old background
//        quizChoice1.setBackground(getResources().getDrawable(R.drawable.answer_button));
//        quizChoice2.setBackground(getResources().getDrawable(R.drawable.answer_button));
//        quizChoice3.setBackground(getResources().getDrawable(R.drawable.answer_button));
//        quizChoice4.setBackground(getResources().getDrawable(R.drawable.answer_button));
        quizChoice1.setBackground(getResources().getDrawable(R.drawable.quiz_btn));
        quizChoice2.setBackground(getResources().getDrawable(R.drawable.quiz_btn));
        quizChoice3.setBackground(getResources().getDrawable(R.drawable.quiz_btn));
        quizChoice4.setBackground(getResources().getDrawable(R.drawable.quiz_btn));
        enableAllChoices();
        if (answerStatesMap != null) {
            AnswerState as = answerStatesMap.get(q.getQuizId());
            Log.d(LOG_TAG, "answerstate: " + as.toString());
            if (as.getStatus() == QuestioConstants.QUEST_FINISHED) {
                disableAllChoices();
                switch (Integer.parseInt(q.getAnswerId())) {
                    case 1:
                        quizChoice1.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                        break;
                    case 2:
                        quizChoice2.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                        break;
                    case 3:
                        quizChoice3.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                        break;
                    case 4:
                        quizChoice4.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                        break;
                }
            } else if (as.getStatus() == QuestioConstants.QUEST_FAILED) {
                disableAllChoices();
                switch (Integer.parseInt(q.getAnswerId())) {
                    case 1:
                        quizChoice1.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                        break;
                    case 2:
                        quizChoice2.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                        break;
                    case 3:
                        quizChoice3.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                        break;
                    case 4:
                        quizChoice4.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                        break;
                }
            } else {
                if (as.isA()) {
                    disableButtonAnswer(quizChoice1);
                }
                if (as.isB()) {
                    disableButtonAnswer(quizChoice2);
                }
                if (as.isC()) {
                    disableButtonAnswer(quizChoice3);
                }
                if (as.isD()) {
                    disableButtonAnswer(quizChoice4);
                }
            }
        }

        quizQuestion.setText(q.getQuestion());
        quizSequence.setText(Integer.toString(q.getSeqId()));
        quizChoice1.setText(q.getChoiceA());
        quizChoice2.setText(q.getChoiceB());
        quizChoice3.setText(q.getChoiceC());
        quizChoice4.setText(q.getChoiceD());
        currentQuiz = q.getQuizId();
    }

    private AnswerState createAnswerState(QuizProgress qp) {
        AnswerState as = new AnswerState();
        as.setQuizId(qp.getQuizId());
        if (qp.getaAnswered() == 1) {
            as.setA(true);
        }
        if (qp.getbAnswered() == 1) {
            as.setB(true);
        }
        if (qp.getcAnswered() == 1) {
            as.setC(true);
        }
        if (qp.getdAnswered() == 1) {
            as.setD(true);
        }
        return as;
    }

    private void requestQuizProgresses() {
        Log.d(LOG_TAG, "requestQuizProgress: called");
        api.getQuizProgressByRef(adventurerId, questId, new Callback<ArrayList<QuizProgress>>() {
            @Override
            public void success(ArrayList<QuizProgress> quizProgressesTemp, Response response) {
                quizProgresses = quizProgressesTemp;
                Log.d(LOG_TAG, "requestQuizProgress: success");
                if (quizProgresses == null || quizProgresses.size() != totalQuiz) {
                    Log.d(LOG_TAG, "requestQuizProgress: success but quizProgresses is null");
                    insertProgressData();
                    populateQuiz(FIRST_QUIZ);
                } else {
                    answerStatesMap = new HashMap<>();
                    for (QuizProgress qp : quizProgresses) {
                        AnswerState as = createAnswerState(qp);
                        if (qp.getStatusId() == QuestioConstants.QUEST_FINISHED) {
                            as.setStatus(QuestioConstants.QUEST_FINISHED);
                        } else if (qp.getStatusId() == QuestioConstants.QUEST_FAILED) {
                            as.setStatus(QuestioConstants.QUEST_FAILED);
                        }
                        answerStatesMap.put(qp.getQuizId(), as);
                    }
                    populateQuiz(FIRST_QUIZ);
                }
                quizChoice1.setOnClickListener(QuizActivity.this);
                quizChoice2.setOnClickListener(QuizActivity.this);
                quizChoice3.setOnClickListener(QuizActivity.this);
                quizChoice4.setOnClickListener(QuizActivity.this);
                quizPreBtn.setOnClickListener(QuizActivity.this);
                quizNextBtn.setOnClickListener(QuizActivity.this);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuizProgress: failure");
            }
        });

    }

    private void insertProgressData() {
        api.addQuestProgress(questId, adventurerId, zid, 1, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + questId + " " + questioStatus);
                answerStatesMap = new HashMap<>();
                for (Quiz q : quizs) {
                    answerStatesMap.put(q.getQuizId(), new AnswerState());
                    api.addQuizProgress(adventurerId, questId, q.getQuizId(), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Log.d(LOG_TAG, QuestioHelper.responseToString(response));
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


    void onAnswer(final int choiceSelected) {
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        TextView answerTV = (TextView) dialog.findViewById(R.id.confirm_answer);
        switch (choiceSelected) {
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

                if (answerStatesMap.get(q.getQuizId()).getAnswerTime() > 1) {
                    onLimitAnswer(q.getSeqId(), q.getQuizId());
                } else {
                    updateStateAnswerButton(choiceSelected, q.getQuizId());

                    if (q.getAnswerId().equalsIgnoreCase(Integer.toString(choiceSelected))) {
                        onCorrect(q.getSeqId(), q.getQuizId(), choiceSelected);

                    } else {
                        onIncorrect(q.getSeqId(), q.getQuizId(), choiceSelected);
                    }
                }

                dialog.cancel();
            }
        });

        dialog.show();
    }

    void onCorrect(int seqId, int quizId, int choiceSelected) {
        Log.d(LOG_TAG, "onCorrect: called");
        AnswerState as = answerStatesMap.get(quizId);
        int answerTime = as.getAnswerTime();
        int score = 2;
        for (int i = 0; i < answerTime; i++) {
            score--;
        }

        as.setStatus(3);
        answerStatesMap.put(quizId, as);
        disableAllChoices();
        switch (choiceSelected) {
            case 1:
                as.setA(true);
                answerStatesMap.put(quizId, as);
                quizChoice1.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                break;
            case 2:
                as.setB(true);
                answerStatesMap.put(quizId, as);
                quizChoice2.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                break;
            case 3:
                as.setC(true);
                answerStatesMap.put(quizId, as);
                quizChoice3.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                break;
            case 4:
                as.setD(true);
                answerStatesMap.put(quizId, as);
                quizChoice4.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                break;
        }


        api.updateScoreQuizProgressByRefAndQuizId(score, adventurerId, questId, quizId,
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


        updateQuizProgressStatus(QuestioConstants.QUEST_FINISHED, quizId);
        //populateQuiz(getPossibleNextQuizSeqFromCurrentSeq(seqId - 1));
        showCorrectAnswer(seqId, true);
        Log.d(LOG_TAG, "onCorrect: seqId = " + seqId);

    }

    void onIncorrect(int seqId, int quizId, int choiceSelected) {

        AnswerState as = answerStatesMap.get(quizId);

        switch (choiceSelected) {
            case 1:
                as.setA(true);
                answerStatesMap.put(quizId, as);
                disableButtonAnswer(quizChoice1);
                break;
            case 2:
                as.setB(true);
                answerStatesMap.put(quizId, as);
                disableButtonAnswer(quizChoice2);
                break;
            case 3:
                as.setC(true);
                answerStatesMap.put(quizId, as);
                disableButtonAnswer(quizChoice3);
                break;
            case 4:
                as.setD(true);
                answerStatesMap.put(quizId, as);
                disableButtonAnswer(quizChoice4);
                break;
        }
        int answerTime = as.getAnswerTime();
        Log.d(LOG_TAG, "onIncorrect: answerTime = " + answerTime);
        if (answerTime >= 2) {
            onLimitAnswer(seqId, quizId);
        }

    }


    void onLimitAnswer(int seqId, int quizId) {
        disableAllChoices();
        AnswerState as = answerStatesMap.get(quizId);
        as.setStatus(QuestioConstants.QUEST_FAILED);
        answerStatesMap.put(quizId, as);
        api.updateScoreQuizProgressByRefAndQuizId(0, adventurerId, questId, quizId,
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
        updateQuizProgressStatus(QuestioConstants.QUEST_FAILED, quizId);
        Log.d(LOG_TAG, "onLimitAnswer: called");

        showCorrectAnswer(seqId, false);
    }


    int getPossibleNextQuizSeqFromCurrentSeq(int currentSeq) {
        Log.d(LOG_TAG, "getPossibleNextQuizSeqFromCurrentSeq: called");
        Log.d(LOG_TAG, "getPossibleNextQuizSeqFromCurrentSeq: ");

        AnswerState as;
        int i = 0;
        for (Quiz q : quizs) {
            as = answerStatesMap.get(q.getQuizId());
            if (as.getStatus() == QuestioConstants.QUEST_FINISHED || as.getStatus() == QuestioConstants.QUEST_FAILED || i == currentSeq) {
                i++;
                continue;
            }

            Log.d(LOG_TAG, "getPossibleNextQuizSeqFromCurrentSeq: return: " + i);
            return i;
        }
        Log.d(LOG_TAG, "getPossibleNextQuizSeqFromCurrentSeq: return: " + 0);
        return 0;
    }

    void updateQuizProgressStatus(int status, int quizId) {
        Log.d(LOG_TAG, "updateQuizProgressStatus: called");
        Log.d(LOG_TAG, "updateQuizProgressStatus status: " + status);
        //Log.d(LOG_TAG, "updateQuizProgressStatus ref: " + ref);
        Log.d(LOG_TAG, "updateQuizProgressStatus quizId: " + quizId);
        api.updateStatusQuizProgressByRefAndQuizId(status, adventurerId, questId, quizId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updateQuizProgressStatus: success");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "updateQuizProgressStatus: failure");
            }
        });
    }

    void disableButtonAnswer(Button b) {
        //b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));
        b.setBackground(getResources().getDrawable(R.drawable.corners_button_red));
        //b.setTextColor(getResources().getColor(R.color.white));
        b.setEnabled(false);
        b.setClickable(false);
    }

    void disableAllChoices() {
        quizChoice1.setEnabled(false);
        quizChoice1.setClickable(false);
        quizChoice2.setEnabled(false);
        quizChoice2.setClickable(false);
        quizChoice3.setEnabled(false);
        quizChoice3.setClickable(false);
        quizChoice4.setEnabled(false);
        quizChoice4.setClickable(false);

    }

    void enableAllChoices() {
        quizChoice1.setEnabled(true);
        quizChoice1.setClickable(true);
        quizChoice2.setEnabled(true);
        quizChoice2.setClickable(true);
        quizChoice3.setEnabled(true);
        quizChoice3.setClickable(true);
        quizChoice4.setEnabled(true);
        quizChoice4.setClickable(true);
    }


    private void updateStateAnswerButton(int answer, int qid) {
        Log.d(LOG_TAG, "updateStateAnswerButton: called");
        //Log.d(LOG_TAG, "updateStateAnswerButton: " + "ref: " + ref + " quizid: " + qid);
        switch (answer) {
            case 1:
                api.updateStatusChoiceAQuizByRefAndQuizId(adventurerId, questId, qid, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "updateStateAnswerButton success: " + QuestioHelper.responseToString(response));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "updateStateAnswerButton failure: " + error.getUrl());
                    }
                });
                break;
            case 2:
                api.updateStatusChoiceBQuizByRefAndQuizId(adventurerId, questId, qid, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "updateStateAnswerButton success: " + QuestioHelper.responseToString(response));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "updateStateAnswerButton failure: " + error.getUrl());

                    }
                });
                break;
            case 3:
                api.updateStatusChoiceCQuizByRefAndQuizId(adventurerId, questId, qid, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "updateStateAnswerButton success: " + QuestioHelper.responseToString(response));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "updateStateAnswerButton failure: " + error.getUrl());
                    }
                });
                break;
            case 4:
                api.updateStatusChoiceDQuizByRefAndQuizId(adventurerId, questId, qid, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "updateStateAnswerButton success: " + QuestioHelper.responseToString(response));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "updateStateAnswerButton failure: " + error.getUrl());
                    }
                });
                break;
        }
    }

    private void updateProgressStatusAndScore(int status) {

        Log.d(LOG_TAG, "updateProgressStatusAndScore: called");
        api.updateQuestProgressAutoScoreQuiz(questId, adventurerId, status, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    void checkQuestFinish() {
        if(isAllQuizFinish()){
            updateProgressStatusAndScore(QuestioConstants.QUEST_FINISHED);
            onBackPressed();
        }
    }

    boolean isAllQuizFinish() {
        AnswerState as;

        for (int i = 0; i < quizs.size(); i++) {
            as = answerStatesMap.get(quizs.get(i).getQuizId());
            if (as.getStatus() != QuestioConstants.QUEST_FINISHED &&
                    as.getStatus() != QuestioConstants.QUEST_FAILED) {
                return false;
            }

        }
        return true;
    }

    private class AnswerState {
        private int status;
        private int quizId;
        private boolean a;
        private boolean b;
        private boolean c;
        private boolean d;

        AnswerState() {
            status = 0;
            quizId = 0;
            a = false;
            b = false;
            c = false;
            d = false;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public void setA(boolean a) {
            this.a = a;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public void setC(boolean c) {
            this.c = c;
        }

        public void setD(boolean d) {
            this.d = d;
        }

        public int getQuizId() {
            return quizId;
        }

        public boolean isA() {
            return a;
        }

        public boolean isB() {
            return b;
        }

        public boolean isC() {
            return c;
        }

        public boolean isD() {
            return d;
        }

        int getAnswerTime() {
            int i = 0;
            if (a) {
                i++;
            }
            if (b) {
                i++;
            }
            if (c) {
                i++;
            }
            if (d) {
                i++;
            }
            return i;
        }

        @Override
        public String toString() {
            return "AnswerState{" +
                    "status=" + status +
                    ", quizId=" + quizId +
                    ", a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    '}';
        }
    }

    void showCorrectAnswer(final int seqId, boolean isCorrect){
        final Dialog dialog = new Dialog(QuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.correct_answer_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        TextView correctMsg = (TextView)dialog.findViewById(R.id.dialog_correctanswer_msg);
        TextView correctAns = (TextView)dialog.findViewById(R.id.dialog_correctanswer_answer);
        if(q.getAnswerId().equalsIgnoreCase("1")){
            correctAns.setText(q.getChoiceA());
        }else if(q.getAnswerId().equalsIgnoreCase("2")){
            correctAns.setText(q.getChoiceB());
        }else if(q.getAnswerId().equalsIgnoreCase("3")){
            correctAns.setText(q.getChoiceC());
        }else if(q.getAnswerId().equalsIgnoreCase("4")){
            correctAns.setText(q.getChoiceD());
        }

        if(isCorrect){
            correctMsg.setText("ถูกต้องนะครับ");
        }else{
            correctMsg.setText("ผิดครับ");
        }
        Button nextQuestion = (Button)dialog.findViewById(R.id.button_correctanswer_next_question);
        if(!isAllQuizFinish()) {
            nextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateQuiz(getPossibleNextQuizSeqFromCurrentSeq(seqId - 1));
                    dialog.cancel();
                }

            });
        }else{
            nextQuestion.setText("กลับไปยังหน้าจอภารกิจ");
            nextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkQuestFinish();
                    dialog.cancel();
                }

            });
        }
        dialog.show();
    }
}
