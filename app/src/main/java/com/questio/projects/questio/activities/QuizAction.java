package com.questio.projects.questio.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Quiz;

import java.util.ArrayList;

/**
 * Created by coad4u4ever on 08-Apr-15.
 */
public class QuizAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = QuizAction.class.getSimpleName();
    Toolbar toolbar;
    ArrayList<Quiz> quizs;
    TextView quiz_question;
    TextView quiz_sequence;

    Button quiz_answer_a;
    Button quiz_answer_b;
    Button quiz_answer_c;
    Button quiz_answer_d;


    Button quiz_prequiz;
    Button quiz_next;
    TextView quiz_current_seq;
    TextView quiz_count_quiz;


    int currentQuiz;
    Quiz q;

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

        quiz_prequiz = (Button) findViewById(R.id.quiz_prequiz);
        quiz_next = (Button) findViewById(R.id.quiz_next);
        quiz_current_seq = (TextView) findViewById(R.id.quiz_current_seq);
        quiz_count_quiz = (TextView) findViewById(R.id.quiz_count_quiz);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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


        quizs = Quiz.getAllQuizByQuestId(Integer.parseInt(questId));
        currentQuiz = 0;
        int quizCount = quizs.size();

        q = quizs.get(currentQuiz);
        quiz_current_seq.setText(Integer.toString(currentQuiz + 1));
        quiz_count_quiz.setText(Integer.toString(quizCount));
        getSupportActionBar().setTitle(questName);

        pupulateQuiz();

        quiz_prequiz.setOnClickListener(this);
        quiz_next.setOnClickListener(this);
        quiz_answer_a.setOnClickListener(this);
        quiz_answer_b.setOnClickListener(this);
        quiz_answer_c.setOnClickListener(this);
        quiz_answer_d.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_prequiz:
                if (currentQuiz > 0) {
                    currentQuiz--;
                    quiz_current_seq.setText(Integer.toString(currentQuiz + 1));
                    q = quizs.get(currentQuiz);
                    pupulateQuiz();
                }
                break;
            case R.id.quiz_next:
                if (currentQuiz < quizs.size() - 1) {
                    currentQuiz++;
                    quiz_current_seq.setText(Integer.toString(currentQuiz + 1));
                    q = quizs.get(currentQuiz);
                    pupulateQuiz();
                }
                break;
            case R.id.quiz_answer_a:
                if (q.getAnswerId().equalsIgnoreCase("1")) {
                    Toast.makeText(this, "ถูกค้องนะครับ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ยังไม่ถูกนะครับ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.quiz_answer_b:
                if (q.getAnswerId().equalsIgnoreCase("2")) {
                    Toast.makeText(this, "ถูกค้องนะครับ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ยังไม่ถูกนะครับ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.quiz_answer_c:
                if (q.getAnswerId().equalsIgnoreCase("3")) {
                    Toast.makeText(this, "ถูกค้องนะครับ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ยังไม่ถูกนะครับ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.quiz_answer_d:
                if (q.getAnswerId().equalsIgnoreCase("4")) {
                    Toast.makeText(this, "ถูกค้องนะครับ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ยังไม่ถูกนะครับ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void pupulateQuiz() {
        quiz_question.setText(q.getQuestion());
        quiz_sequence.setText(Integer.toString(q.getSeqId()));
        quiz_answer_a.setText(q.getChoiceA());
        quiz_answer_b.setText(q.getChoiceB());
        quiz_answer_c.setText(q.getChoiceC());
        quiz_answer_d.setText(q.getChoiceD());
    }
}
