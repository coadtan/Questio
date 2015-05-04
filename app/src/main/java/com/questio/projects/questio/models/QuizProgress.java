package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 4/5/2558.
 */
public class QuizProgress {
/*  ref
    quizid
    statusid
    score
    aanswered
    banswered
    canswered
    danswered*/

    private int ref;

    @SerializedName("quizid")
    private int quizId;

    @SerializedName("statusid")
    private int statusId;

    private int score;

    @SerializedName("aanswered")
    private int aAnswered;

    @SerializedName("banswered")
    private int bAnswered;

    @SerializedName("canswered")
    private int cAnswered;

    @SerializedName("danswered")
    private int dAnswered;

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getaAnswered() {
        return aAnswered;
    }

    public void setaAnswered(int aAnswered) {
        this.aAnswered = aAnswered;
    }

    public int getbAnswered() {
        return bAnswered;
    }

    public void setbAnswered(int bAnswered) {
        this.bAnswered = bAnswered;
    }

    public int getcAnswered() {
        return cAnswered;
    }

    public void setcAnswered(int cAnswered) {
        this.cAnswered = cAnswered;
    }

    public int getdAnswered() {
        return dAnswered;
    }

    public void setdAnswered(int dAnswered) {
        this.dAnswered = dAnswered;
    }

    @Override
    public String toString() {
        return "QuizProgress{" +
                "ref=" + ref +
                ", quizId=" + quizId +
                ", statusId=" + statusId +
                ", score=" + score +
                ", aAnswered=" + aAnswered +
                ", bAnswered=" + bAnswered +
                ", cAnswered=" + cAnswered +
                ", dAnswered=" + dAnswered +
                '}';
    }
}
