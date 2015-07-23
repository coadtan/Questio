package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 23/7/2558.
 */
public class RewardHOF {
    //[{"rewardid":"1","rewardname":"TestRewards","description":"Description of Test Rewards","rewardtype":"3","rewardpic":"\/itempic\/head\/icon_rewards.png","datereceived":"2015-07-22 12:20:20","rankid":"2"}]
    @SerializedName("rewardid")
    private int rewardId;

    @SerializedName("rewardname")
    private String rewardName;

    @SerializedName("description")
    private String description;

    @SerializedName("rewardtype")
    private int rewardType;

    @SerializedName("rewardpic")
    private String rewardPic;

    @SerializedName("datereceived")
    private String dateReceived;

    @SerializedName("rankid")
    private int rankId;

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

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

    public String getRewardPic() {
        return rewardPic;
    }

    public void setRewardPic(String rewardPic) {
        this.rewardPic = rewardPic;
    }

    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }

    @Override
    public String toString() {
        return "RewardHOF{" +
                "dateReceived='" + dateReceived + '\'' +
                ", rewardId=" + rewardId +
                ", rewardName='" + rewardName + '\'' +
                ", description='" + description + '\'' +
                ", rewardType=" + rewardType +
                ", rewardPic='" + rewardPic + '\'' +
                ", rankId=" + rankId +
                '}';
    }
}
