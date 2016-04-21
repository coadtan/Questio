package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 17/2/2559.
 */
public class Ranking {
    @SerializedName("rank")
    private int rank;

    @SerializedName("guserid")
    private String gUserId;

    @SerializedName("displayname")
    private String displayName;

    @SerializedName("score")
    private int score;

    public String getgUserId() {
        return gUserId;
    }

    public void setgUserId(String gUserId) {
        this.gUserId = gUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "displayName='" + displayName + '\'' +
                ", rank=" + rank +
                ", gUserId='" + gUserId + '\'' +
                ", score=" + score +
                '}';
    }
}
