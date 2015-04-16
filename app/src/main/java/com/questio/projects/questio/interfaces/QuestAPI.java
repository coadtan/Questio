package com.questio.projects.questio.interfaces;

import com.questio.projects.questio.models.Quest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by coad4u4ever on 16-Apr-15.
 */
public interface QuestAPI {


    @GET("/select_all_quest_by_placeid_location_name.php")
    public void getQuestsByPlaceId(@Query("placeid") int id, Callback<ArrayList<Quest>> response);


    @GET("/select_all_quest_by_zoneid.php")
    public void getAllQuestByZoneId(@Query("zoneid") int id, Callback<ArrayList<Quest>> response);

}
