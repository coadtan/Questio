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
import com.questio.projects.questio.utilities.HttpHelper;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by coad4u4ever on 08-Apr-15.
 */
public class QuizAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = QuizAction.class.getSimpleName();
    private final int FIRST_QUIZ = 0;
    Toolbar toolbar;
    int buttonId = 0;
    int quizCount;
    int quizFinished;

    ArrayList<Quiz> quizs;
    HashMap<String, String> quizStatusHashMap;

    TextView quiz_question;
    TextView quiz_sequence;


    Button button;

    Button quiz_answer_a;
    Button quiz_answer_b;
    Button quiz_answer_c;
    Button quiz_answer_d;

    int qid;
    long adventurerId;
    int zid;

    int currentQuiz;
    Quiz q;

    QuestioAPIService api;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quiz_question = (TextView) findViewById(R.id.quiz_question);
        quiz_sequence = (TextView) findViewById(R.id.quiz_sequence);
        quiz_answer_a = (Button) findViewById(R.id.quiz_answer_a);
        quiz_answer_b = (Button) findViewById(R.id.quiz_answer_b);
        quiz_answer_c = (Button) findViewById(R.id.quiz_answer_c);
        quiz_answer_d = (Button) findViewById(R.id.quiz_answer_d);

        quizFinished = 0;

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
        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);

        qid = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);
        // quizs = Quiz.getAllQuizByQuestId(Integer.parseInt(questId));
        requestQuizData(Integer.parseInt(questId));
        currentQuiz = 0;


        getSupportActionBar().setTitle(questName);


        quiz_answer_a.setOnClickListener(this);
        quiz_answer_b.setOnClickListener(this);
        quiz_answer_c.setOnClickListener(this);
        quiz_answer_d.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_answer_a:
                onAnswer(1);
                break;
            case R.id.quiz_answer_b:
                onAnswer(2);
                break;
            case R.id.quiz_answer_c:
                onAnswer(3);
                break;
            case R.id.quiz_answer_d:
                onAnswer(4);
                break;
        }
    }

    void onAnswer(final int answer) {

        final Dialog dialog = new Dialog(QuizAction.this);
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

    void onCorrect(int quizId) {
        Button b = (Button) findViewById(quizId - 1);
        b.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
        updateQuizStatus(QuestioConstants.QUEST_CORRECT);
        disableButton();
        quizFinished++;
        checkQuizFinished();

    }

    void onIncorrect(int quizId) {
        Log.d(LOG_TAG, "Before minus 1: " + quizId);
        Button b = (Button) findViewById(quizId - 1);
        b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));
        updateQuizStatus(QuestioConstants.QUEST_FAILED);
        disableButton();
        quizFinished++;
        checkQuizFinished();
    }

    void populateQuiz(int i) {
        q = quizs.get(i);
        quiz_question.setText(q.getQuestion());
        quiz_sequence.setText(Integer.toString(q.getSeqId()));
        quiz_answer_a.setText(q.getChoiceA());
        quiz_answer_b.setText(q.getChoiceB());
        quiz_answer_c.setText(q.getChoiceC());
        quiz_answer_d.setText(q.getChoiceD());
        currentQuiz = q.getQuizId();
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
            quizStatusHashMap = getRequestStatus(Integer.parseInt(Integer.toString(qid) + (int) adventurerId));
            String statusStr;
            int status;

            if(!quizStatusHashMap.isEmpty()) {
                    statusStr = quizStatusHashMap.get(Integer.toString(currentQuiz));
                    status = Integer.parseInt(statusStr);
                if(status == QuestioConstants.QUEST_CORRECT || status == QuestioConstants.QUEST_FAILED){
                    disableButton();
                }else{

                    enableButton();
                }


            }
        }
    }

    public void changeButtonIndicator(int selected) {
        View v = findViewById(android.R.id.content);
        Button b;
        for (int i = 0; i < quizs.size(); i++) {
            b = (Button) v.findViewById(i);
            if (i == selected) {
                quizStatusHashMap = getRequestStatus(Integer.parseInt(Integer.toString(qid) + (int) adventurerId));
                String statusStr;
                int status;

                if (!quizStatusHashMap.isEmpty()) {
                    statusStr = quizStatusHashMap.get(Integer.toString(currentQuiz));
                    status = Integer.parseInt(statusStr);
                    if (status == QuestioConstants.QUEST_CORRECT || status == QuestioConstants.QUEST_FAILED) {
                        disableButton();
                        b.setTextColor(getResources().getColor(R.color.white));
                        b.setText("?");
                    } else {
                        b.setBackgroundResource(R.color.yellow_quiz_unanswered);
                        b.setTextColor(getResources().getColor(R.color.white));
                        b.setText("?");
                        updateQuizStatus(QuestioConstants.QUEST_NOT_FINISHED);
                        updateQuestStatus(QuestioConstants.QUEST_NOT_FINISHED);
                        enableButton();
                    }
                }


                } else {
                    b.setText("");
                    //b.setBackgroundResource(R.color.grey_700);
                }
            }
        }


    private void requestQuizData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        api.getAllQuizByQuestId(id, new Callback<ArrayList<Quiz>>() {
            @Override
            public void success(ArrayList<Quiz> quizsTemp, Response response) {
                if (quizsTemp != null) {
                    quizs = quizsTemp;
                    requestProgressData();
                    quizCount = quizs.size();
                    Log.d(LOG_TAG, "ref: " + Integer.parseInt(Integer.toString(qid) + (int) adventurerId));
                    LinearLayout quizActionProgressLinerSection = (LinearLayout) findViewById(R.id.quiz_action_progress_liner_section);
                    quizActionProgressLinerSection.setWeightSum(quizCount);





                    for (int i = 0; i < quizCount; i++) {
                        button = new Button(QuizAction.this);
                        if (i == 0) {
                            button.setTextColor(getResources().getColor(R.color.white));
                            button.setText("?");
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                        );
                        button.setBackgroundColor(getResources().getColor(R.color.grey_700));
                        //params.setMargins(left, top, right, bottom);
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

                quizStatusHashMap = getRequestStatus(Integer.parseInt(Integer.toString(qid) + (int) adventurerId));
                if(!quizStatusHashMap.isEmpty()) {
                    Button bProgess;
                    String statusStr;
                    int status;
                    for (int i = 0; i < quizCount; i++) {
                        bProgess = (Button)findViewById(i);
                        String quizIdFromButton = bProgess.getContentDescription().toString();
                        statusStr = quizStatusHashMap.get(quizIdFromButton);
                        if(statusStr != null){
                            status = Integer.parseInt(statusStr);
                        }else{
                            status = QuestioConstants.QUEST_NOT_STARTED;
                        }
                        populateButtonQuizProgress(i, status);
                    }

                    statusStr = quizStatusHashMap.get(Integer.toString(currentQuiz));
                    status = Integer.parseInt(statusStr);
                    if (status == QuestioConstants.QUEST_CORRECT || status == QuestioConstants.QUEST_FAILED) {
                        disableButton();
                    } else {
                        enableButton();
                    }
                }

                checkQuizFinished();

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
//        api.getQuestProgressByQuestIdAndAdventurerId(qid, adventurerId, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//                if (QuestioHelper.responseToString(response).equalsIgnoreCase("null")){
        Log.d(LOG_TAG, "No Progress in Quest");
        api.addQuestProgress(qid, adventurerId, Integer.parseInt(Integer.toString(qid) + (int) adventurerId), zid, 1, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
//                if (QuestioHelper.getJSONStringValueByTag("status", questioStatus).equalsIgnoreCase("1")) {
//                Log.d(LOG_TAG, "Add Quest Progress + " + qid + "Successful");
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);
                for (Quiz q : quizs) {
                    api.addQuizProgress(Integer.parseInt(Integer.toString(qid) + (int) adventurerId), q.getQuizId(), new Callback<Response>() {
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

                        }
                    });
                }

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

    void populateButtonQuizProgress(int buttonId, int status) {
        Button b = (Button) findViewById(buttonId);
        if (status == QuestioConstants.QUEST_CORRECT) {
            b.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
        } else if (status == QuestioConstants.QUEST_FAILED) {
            b.setBackgroundColor(getResources().getColor(R.color.red_quiz_wrong));
        } else if (status == QuestioConstants.QUEST_NOT_FINISHED) {
            b.setBackgroundColor(getResources().getColor(R.color.yellow_quiz_unanswered));
        } else if (status == QuestioConstants.QUEST_NOT_STARTED){
            b.setBackgroundColor(getResources().getColor(R.color.grey_700));
        }

    }

    private HashMap<String, String> getRequestStatus(int ref) {
        HashMap<String,String> hashMap = new HashMap<>();
        final String URL =
                "http://52.74.64.61/api/select_all_quizprogress_by_ref.php?ref=" + ref;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    QuizStatus qs = new QuizStatus();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String quizid = jsonObject.get("quizid").toString();
                    String status = jsonObject.get("statusid").toString();
                    qs.setQuizId(Integer.parseInt(quizid));
                    qs.setStatus(Integer.parseInt(status));
                    hashMap.put(quizid, status);
                }

            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    private void updateQuizStatus(int status){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        api.updateStatusQuizProgressByRefAndQuizId(status, Integer.parseInt(Integer.toString(qid) + (int) adventurerId), q.getQuizId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
//                String questioStatus = QuestioHelper.responseToString(response);
//                if (QuestioHelper.getJSONStringValueByTag("status", questioStatus).equalsIgnoreCase("1")) {
//                    Log.d(LOG_TAG, "Update Successful");
//                } else {
//                    Log.d(LOG_TAG, "Update Failed: " + questioStatus);
//                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

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

    private void checkQuizFinished(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        api.getCountQuizProgressFinishedByRef(Integer.parseInt(Integer.toString(qid) + (int) adventurerId), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String countQuizFinishedStr = QuestioHelper.responseToString(response);
                quizFinished = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("count", countQuizFinishedStr));
                Log.d(LOG_TAG, "Quiz Finished Count: " + quizFinished);
                if (quizFinished == quizCount){
                    updateQuestStatus(QuestioConstants.QUEST_CORRECT);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private class QuizStatus {
        private int quizId;
        private int status;

        public int getQuizId() {
            return quizId;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "QuizStatus{" +
                    "quizId=" + quizId +
                    ", status=" + status +
                    '}';
        }
    }

    private void disableButton(){
        quiz_answer_a.setEnabled(false);
        quiz_answer_a.setClickable(false);
        quiz_answer_b.setEnabled(false);
        quiz_answer_b.setClickable(false);
        quiz_answer_c.setEnabled(false);
        quiz_answer_c.setClickable(false);
        quiz_answer_d.setEnabled(false);
        quiz_answer_d.setClickable(false);
    }

    private void enableButton(){
        quiz_answer_a.setEnabled(true);
        quiz_answer_a.setClickable(true);
        quiz_answer_b.setEnabled(true);
        quiz_answer_b.setClickable(true);
        quiz_answer_c.setEnabled(true);
        quiz_answer_c.setClickable(true);
        quiz_answer_d.setEnabled(true);
        quiz_answer_d.setClickable(true);
    }

}
