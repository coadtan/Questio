package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 15/8/2558.
 */
public class PlaceProgress {
    @SerializedName("adventurerid")
    private long adventurerId;

    @SerializedName("placeid")
    private int placeId;

    @SerializedName("queststatus")
    private int questStatus;

    public long getAdventurerId() {
        return adventurerId;
    }

    public void setAdventurerId(long adventurerId) {
        this.adventurerId = adventurerId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getQuestStatus() {
        return questStatus;
    }

    public void setQuestStatus(int questStatus) {
        this.questStatus = questStatus;
    }

    @Override
    public String toString() {
        return "PlaceProgress{" +
                "adventurerId=" + adventurerId +
                ", placeId=" + placeId +
                ", questStatus=" + questStatus +
                '}';
    }
}
