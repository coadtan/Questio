package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 4/7/2558.
 */
public class Reward {

    private static final String LOG_TAG = Reward.class.getSimpleName();

    /*rewardid
    rewardname
    description
    rewardtype
     */

    @SerializedName("rewardid")
    private int rewardId;

    @SerializedName("rewardname")
    private String rewardName;

    @SerializedName("description")
    private String description;

    @SerializedName("rewardtype")
    private int rewardType;

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "rewardId=" + rewardId +
                ", rewardName='" + rewardName + '\'' +
                ", description='" + description + '\'' +
                ", rewardType=" + rewardType +
                '}';
    }
}
