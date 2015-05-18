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
    private static final int I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_CORRECT = 1;
    private static final int I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_INCORRECT = 2;
    Toolbar toolbar;


    // These 4 values have value after handleInstanceState() called
    int questId;
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
    HashMap<Integer, AnswerState> answerStatesMap;
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
        requestQuizData(questId);
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
        this.questId = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);
        ref = Integer.parseInt(Integer.toString(this.questId) + (int) adventurerId);
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
                            //   button.setBackgroundColor(getResources().getColor(R.color.yellow_quiz_unanswered));
                            button.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                            button.setText("?");
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        button.setTextColor(getResources().getColor(R.color.black));
                        //  button.setBackgroundColor(getResources().getColor(R.color.grey_700));
                        button.setBackground(getResources().getDrawable(R.drawable.corners_button));
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
        api.addQuestProgress(questId, adventurerId, ref, zid, 1, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + questId + " " + questioStatus);
                answerStatesMap = new HashMap<>();
                for (Quiz q : quizs) {
                    answerStatesMap.put(q.getQuizId(), new AnswerState());
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
        swapButtonColorByState(Integer.parseInt(q.getAnswerId()), 0);
    }

    private void swapButtonColorByState(int answerPosition, int flag) {
        Log.d(LOG_TAG, "swapButtonColorByState: called");
        Log.d(LOG_TAG, "swapButtonColorByState: answerPosition = " + answerPosition);
        quizChoice1.setBackground(getResources().getDrawable(R.drawable.answer_button));
        quizChoice2.setBackground(getResources().getDrawable(R.drawable.answer_button));
        quizChoice3.setBackground(getResources().getDrawable(R.drawable.answer_button));
        quizChoice4.setBackground(getResources().getDrawable(R.drawable.answer_button));
        if (flag == I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_CORRECT) {
            if (answerStatesMap != null) {
                AnswerState as = answerStatesMap.get(q.getQuizId());
                as.setStatus(QuestioConstants.QUEST_FINISHED);
                answerStatesMap.put(q.getQuizId(), as);
            } else {
                Log.d(LOG_TAG, "swapButtonColorByState: answerStatesMap is null");
            }
        } else if (flag == I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_INCORRECT) {
            if (answerStatesMap != null) {
                AnswerState as = answerStatesMap.get(q.getQuizId());
                as.setStatus(QuestioConstants.QUEST_FAILED);
                answerStatesMap.put(q.getQuizId(), as);
            }
        }
        if (answerStatesMap != null) {
            AnswerState as = answerStatesMap.get(q.getQuizId());
            if (as.getStatus() == QuestioConstants.QUEST_FINISHED) {
                quizChoice1.setEnabled(false);
                quizChoice1.setClickable(false);
                quizChoice2.setEnabled(false);
                quizChoice2.setClickable(false);
                quizChoice3.setEnabled(false);
                quizChoice3.setClickable(false);
                quizChoice4.setEnabled(false);
                quizChoice4.setClickable(false);
                switch (answerPosition) {
                    case 1:
                        //quizChoice1.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                        quizChoice1.setBackground(getResources().getDrawable(R.drawable.answer_button_correct));
                        break;
                    case 2:
                        //quizChoice2.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                        quizChoice2.setBackground(getResources().getDrawable(R.drawable.answer_button_correct));
                        break;
                    case 3:
                        //quizChoice3.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                        quizChoice3.setBackground(getResources().getDrawable(R.drawable.answer_button_correct));
                        break;
                    case 4:
                        //quizChoice4.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                        quizChoice4.setBackground(getResources().getDrawable(R.drawable.answer_button_correct));
                        break;
                }
            } else if (as.getStatus() == QuestioConstants.QUEST_FAILED) {
                quizChoice1.setEnabled(false);
                quizChoice1.setClickable(false);
                quizChoice2.setEnabled(false);
                quizChoice2.setClickable(false);
                quizChoice3.setEnabled(false);
                quizChoice3.setClickable(false);
                quizChoice4.setEnabled(false);
                quizChoice4.setClickable(false);
                switch (answerPosition) {
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
                Log.d(LOG_TAG, "swapButtonColorByState: QuestioConstants is not QUEST_FINISHED");
                if (as.isA()) {
                    disableButtonAnswer(quizChoice1);
                } else {
                    enableButtonAnswer(quizChoice1);
                }
                if (as.isB()) {
                    disableButtonAnswer(quizChoice2);
                } else {
                    enableButtonAnswer(quizChoice2);
                }
                if (as.isC()) {
                    disableButtonAnswer(quizChoice3);
                } else {
                    enableButtonAnswer(quizChoice3);
                }
                if (as.isD()) {
                    disableButtonAnswer(quizChoice4);
                } else {
                    enableButtonAnswer(quizChoice4);
                }
            }
        } else {
            Log.d(LOG_TAG, "swapButtonColorByState: answerStatesMap is null");
        }
    }


    public void changeButtonIndicator(int selected) {
        View v = findViewById(android.R.id.content);
        Button b;
        for (int i = 0; i < quizs.size(); i++) {
            b = (Button) v.findViewById(i);
            if (i == selected) {
                // b.setTextColor(getResources().getColor(R.color.white));
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
                if (quizProgresses == null || quizProgresses.size() != totalQuiz) {
                    Log.d(LOG_TAG, "requestQuizProgress: success but quizProgresses is null");
                    insertProgressData();
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
                    swapButtonColorByState(Integer.parseInt(quizs.get(FIRST_QUIZ).getAnswerId()), 0);
                    changeButtonProgressColor();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuizProgress: failure");
            }
        });
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
                            //b.setBackgroundColor(getResources().getColor(R.color.yellow_quiz_unanswered));
                            b.setBackground(getResources().getDrawable(R.drawable.corners_button_yellow));
                            break;
                        case 3:

                            //b.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                            b.setBackground(getResources().getDrawable(R.drawable.corners_button_green));
                            break;
                        case 4:
                            //b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));
                            b.setBackground(getResources().getDrawable(R.drawable.corners_button_red));
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

                if (answerStatesMap.get(q.getQuizId()).getAnswerTime() > 2) {
                    onLimitAnswer(q.getSeqId());
                } else {
                    updateStateAnswerButton(answer, q.getQuizId());
                    if (q.getAnswerId().equalsIgnoreCase(Integer.toString(answer))) {
                        onCorrect(q.getSeqId(), q.getQuizId());

                    } else {
                        onIncorrect(q.getSeqId(), answer);
                    }
                }

                dialog.cancel();
            }
        });

        dialog.show();
    }

    void onCorrect(int seqId, int quizId) {
        Log.d(LOG_TAG, "onCorrect: called");
        Button b = (Button) findViewById(seqId - 1);
        int answerTime = answerStatesMap.get(quizId).getAnswerTime();
        int score = 3;
        for (int i = 0; i < answerTime; i++) {
            score--;
        }
        api.updateScoreQuizProgressByRefAndQuizId(score, ref, Integer.parseInt(b.getContentDescription().toString()),
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


        updateQuizProgressStatus(QuestioConstants.QUEST_FINISHED, quizId);
        Log.d(LOG_TAG, "onCorrect: seqId = " + seqId);
        swapButtonColorByState(Integer.parseInt(quizs.get(seqId - 1).getAnswerId()), I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_CORRECT);
    }

    void onIncorrect(int seqId, int answer) {

        Button b = (Button) findViewById(seqId - 1);
        int quizId = Integer.parseInt(b.getContentDescription().toString());
        AnswerState as = answerStatesMap.get(quizId);

        switch (answer) {
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
        if (answerTime >= 3) {
            api.updateScoreQuizProgressByRefAndQuizId(0, ref, Integer.parseInt(b.getContentDescription().toString()),
                    new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
            updateQuizProgressStatus(QuestioConstants.QUEST_FAILED, quizId);
            Log.d(LOG_TAG, "onCorrect: seqId = " + seqId);
            swapButtonColorByState(Integer.parseInt(quizs.get(seqId - 1).getAnswerId()), I_DO_NOT_CARE_JUST_CHANGE_IT_IMMEDIATELY_TO_INCORRECT);

        }

//        Log.d(LOG_TAG, "Before minus 1: " + seqId);
//        Button b = (Button) findViewById(seqId - 1);
//        b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));

    }


    void onLimitAnswer(int seqId) {
        Log.d(LOG_TAG, "onLimitAnswer: called");
    }

    void updateQuizProgressStatus(int status, int quizId) {
        Log.d(LOG_TAG, "updateQuizProgressStatus: called");
        Log.d(LOG_TAG, "updateQuizProgressStatus status: " + status);
        Log.d(LOG_TAG, "updateQuizProgressStatus ref: " + ref);
        Log.d(LOG_TAG, "updateQuizProgressStatus quizId: " + quizId);

        api.updateStatusQuizProgressByRefAndQuizId(status, ref, quizId, new Callback<Response>() {
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

    void enableButtonAnswer(Button b) {
        b.setBackground(getResources().getDrawable(R.drawable.answer_button));
        b.setTextColor(getResources().getColor(R.color.grey_900));
        b.setEnabled(true);
        b.setClickable(true);
    }

    private void updateStateAnswerButton(int answer, int qid) {
        Log.d(LOG_TAG, "updateStateAnswerButton: called");
        Log.d(LOG_TAG, "updateStateAnswerButton: " + "ref: " + ref + " quizid: " + qid);
        switch (answer) {
            case 1:
                api.updateStatusChoiceAQuizByRefAndQuizId(ref, qid, new Callback<Response>() {
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
                api.updateStatusChoiceBQuizByRefAndQuizId(ref, qid, new Callback<Response>() {
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
                api.updateStatusChoiceCQuizByRefAndQuizId(ref, qid, new Callback<Response>() {
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
                api.updateStatusChoiceDQuizByRefAndQuizId(ref, qid, new Callback<Response>() {
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

    // check if user finish every quiz in quest; return true if it is, false if it was not.
    private boolean isQuestFinish(){
        boolean finish = false;
        // step 1: check all quiz by ref + quest id if all of those status is not 0
        // if it is then return true

        return finish;
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

    }
}
