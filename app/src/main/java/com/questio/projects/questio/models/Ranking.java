package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 17/2/2559.
 */
public class Ranking {
    @SerializedName("rank")
    private int rank;

    @SerializedName("adventurerid")
    private long adventurerId;

    @SerializedName("displayname")
    private String displayName;

    @SerializedName("score")
    private int score;

    public long getAdventurerId() {
        return adventurerId;
    }

    public void setAdventurerId(long adventurerId) {
        this.adventurerId = adventurerId;
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
                "adventurerId=" + adventurerId +
                ", rank=" + rank +
                ", displayName='" + displayName + '\'' +
                ", score=" + score +
                '}';
    }
}
