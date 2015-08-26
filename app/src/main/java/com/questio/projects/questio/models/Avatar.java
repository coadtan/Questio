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

}
