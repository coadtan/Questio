package com.questio.projects.questio.models;

import android.util.Log;

import com.questio.projects.questio.utilities.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by coad4u4ever on 07-Apr-15.
 */
public class Quiz {
    private static final String LOG_TAG = Quiz.class.getSimpleName();
    /*
    quizid
    questid
    seqid
    question
    choicea
    choiceb
    choicec
    choiced
    answerid
    */

    private int quizId;
    private int questId;
    private int seqId;
    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String answerId;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId=" + quizId +
                ", questId=" + questId +
                ", seqId=" + seqId +
                ", question='" + question + '\'' +
                ", choiceA='" + choiceA + '\'' +
                ", choiceB='" + choiceB + '\'' +
                ", choiceC='" + choiceC + '\'' +
                ", choiceD='" + choiceD + '\'' +
                ", answerId='" + answerId + '\'' +
                '}';
    }



    public static ArrayList<Quiz> getAllQuizByQuestId(int questId) {
        Quiz q;
        ArrayList<Quiz> arr = null;
        final String URL = "http://52.74.64.61/api/select_all_quiz_by_questid.php?questid=" + questId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "getAllQuizByQuestId response:" + response);
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() != 0) {
                arr = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    q = new Quiz();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String quizid = jsonObject.get("quizid").toString();
                    String questid = jsonObject.get("questid").toString();
                    String seqid = jsonObject.get("seqid").toString();
                    String question = jsonObject.get("question").toString();
                    String choicea = jsonObject.get("choicea").toString();
                    String choiceb = jsonObject.get("choiceb").toString();
                    String choicec = jsonObject.get("choicec").toString();
                    String choiced = jsonObject.get("choiced").toString();
                    String answerid = jsonObject.get("answerid").toString();
                    q.setQuizId(Integer.parseInt(quizid));
                    q.setQuestId(Integer.parseInt(questid));
                    if(!seqid.equalsIgnoreCase("null")) {
                        q.setSeqId(Integer.parseInt(seqid));
                    }
                    q.setQuestion(question);
                    q.setChoiceA(choicea);
                    q.setChoiceB(choiceb);
                    q.setChoiceC(choicec);
                    q.setChoiceD(choiced);
                    q.setAnswerId(answerid);

                    arr.add(q);
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
