package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 14/8/2558.
 */
public class ExplorerProgress {
 /*adventurerid
 * placeid
 * zoneid
 * isentered*/
    @SerializedName("adventurerid")
    private long adventurerId;

    @SerializedName("placeid")
    private int placeId;

    @SerializedName("zoneid")
    private int zoneId;

    @SerializedName("isentered")
    private int isEntered;

    public long getAdventurerId() {
        return adventurerId;
    }

    public void setAdventurerId(long adventurerId) {
        this.adventurerId = adventurerId;
    }

    public int getIsEntered() {
        return isEntered;
    }

    public void setIsEntered(int isEntered) {
        this.isEntered = isEntered;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return "ExplorerProgress{" +
                "adventurerId=" + adventurerId +
                ", placeId=" + placeId +
                ", zoneId=" + zoneId +
                ", isEntered=" + isEntered +
                '}';
    }
}
