package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;


public class Avatar {
    private static final String LOG_TAG = Avatar.class.getSimpleName();

    @SerializedName("avatarid")
    private long avatarId;

    @SerializedName("headid")
    private long headId;

    @SerializedName("backgroundid")
    private long backgroundId;

    @SerializedName("neckid")
    private long neckId;

    @SerializedName("bodyid")
    private long bodyId;

    @SerializedName("handleftid")
    private long handleftId;

    @SerializedName("handrightid")
    private long handrightId;

    @SerializedName("armid")
    private long armId;

    @SerializedName("legid")
    private long legId;

    @SerializedName("footid")
    private long footId;

    @SerializedName("specialid")
    private long specialId;

    public long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(long avatarId) {
        this.avatarId = avatarId;
    }

    public long getHeadId() {
        return headId;
    }

    public void setHeadId(long headId) {
        this.headId = headId;
    }

    public long getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(long backgroundId) {
        this.backgroundId = backgroundId;
    }

    public long getNeckId() {
        return neckId;
    }

    public void setNeckId(long neckId) {
        this.neckId = neckId;
    }

    public long getBodyId() {
        return bodyId;
    }

    public void setBodyId(long bodyId) {
        this.bodyId = bodyId;
    }

    public long getHandleftId() {
        return handleftId;
    }

    public void setHandleftId(long handleftId) {
        this.handleftId = handleftId;
    }

    public long getHandrightId() {
        return handrightId;
    }

    public void setHandrightId(long handrightId) {
        this.handrightId = handrightId;
    }

    public long getArmId() {
        return armId;
    }

    public void setArmId(long armId) {
        this.armId = armId;
    }

    public long getLegId() {
        return legId;
    }

    public void setLegId(long legId) {
        this.legId = legId;
    }

    public long getFootId() {
        return footId;
    }

    public void setFootId(long footId) {
        this.footId = footId;
    }

    public long getSpecialId() {
        return specialId;
    }

    public void setSpecialId(long specialId) {
        this.specialId = specialId;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "avatarId=" + avatarId +
                ", headId=" + headId +
                ", backgroundId=" + backgroundId +
                ", neckId=" + neckId +
                ", bodyId=" + bodyId +
                ", handleftId=" + handleftId +
                ", handrightId=" + handrightId +
                ", armId=" + armId +
                ", legId=" + legId +
                ", footId=" + footId +
                ", specialId=" + specialId +
                '}';
    }
}
