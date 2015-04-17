package com.questio.projects.questio.utilities;

import com.questio.projects.questio.models.PicturePuzzle;
import com.questio.projects.questio.models.PlaceDetail;
import com.questio.projects.questio.models.PlaceNews;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.Quiz;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by coad4u4ever on 16-Apr-15.
 */
public interface QuestioAPIService {


    @GET("/select_all_quest_by_placeid_location_name.php")
    public void getQuestsByPlaceId(@Query("placeid") int id, Callback<ArrayList<Quest>> response);


    @GET("/select_all_quest_by_zoneid.php")
    public void getAllQuestByZoneId(@Query("zoneid") int id, Callback<ArrayList<Quest>> response);

    @GET("/select_all_quiz_by_questid.php")
    public void getAllQuizByQuestId(@Query("questid") int id, Callback<ArrayList<Quiz>> response);

    @GET("/select_all_riddle_by_questid.php")
    public void getRiddleByQuestId(@Query("questid") int id, Callback<Riddle[]> response);

    @GET("/select_all_picturepuzzle_by_puzzleid.php")
    public void getPicturePuzzleByPuzzleId(@Query("puzzleid") int id, Callback<PicturePuzzle[]> response);

    @GET("/select_all_placenews_by_placeid.php")
    public void getAllPlaceNewsByPlaceId(@Query("placeid") int id, Callback<ArrayList<PlaceNews>> response);

    @GET("/select_placedetail_by_placeid.php")
    public void getPlaceDetailByPlaceId(@Query("placeid") int id, Callback<PlaceDetail[]> response);

    @GET("/select_zone_by_zoneid.php")
    public void getZoneByZoneId(@Query("zoneid") int id, Callback<Zone[]> response);
}
