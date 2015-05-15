package com.questio.projects.questio.utilities;

import com.questio.projects.questio.models.PicturePuzzle;
import com.questio.projects.questio.models.PlaceDetail;
import com.questio.projects.questio.models.PlaceNews;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.Quiz;
import com.questio.projects.questio.models.QuizProgress;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
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

    @GET("/select_count_adventurer.php")
    public void getCountAdventurer(Callback<Response> response);

    @GET("/select_guserid_adventurer_from_guserid.php")
    public void getGuserIdByGuserId(@Query("guserid") String gid, Callback<Response> response);

    @GET("/select_adventurerid_adventurer_from_guserid.php")
    public void getAdventurerIdByGuserId(@Query("guserid") String gid, Callback<Response> response);



    @GET("/insert_adventurer.php")
    public void addAdventurer(@Query("adventurerid") long id,
                              @Query("guserid") String gid,
                              @Query("email") String email,
                              @Query("avatarid") long aid,
                              @Query("detailid") long did,
                              Callback<Response> response);

    @GET("/insert_adventurerdetails.php")
    public void addAdventurerDetails(@Query("detailid") long id,
                              @Query("displayname") String displayName,
                              @Query("birthdate") String birthDate,
                              Callback<Response> response);

    @GET("/select_all_questprogress_by_questid_and_adventurerid.php")
    public void getQuestProgressByQuestIdAndAdventurerId(
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/select_all_questprogress_by_adventurerid.php")
    public void getQuestProgressByAdventurerId(
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/select_all_questprogress_by_questid.php")
    public void getQuestProgressByQuestId(
            @Query("questid") int questid,
            Callback<Response> response);

    @GET("/select_all_quizprogress_by_ref.php")
    public void getQuizProgressByRef(
            @Query("ref") int ref,
            Callback<ArrayList<QuizProgress>> response);

    @GET("/update_score_questprogress_by_questid_and_adventurerid.php")
    public void updateScoreQuestProgressByQuestIdAndAdventurerId(
            @Query("score") int score,
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/update_status_questprogress_by_questid_and_adventurerid.php")
    public void updateStatusQuestProgressByQuestIdAndAdventurerId(
            @Query("statusid") int status,
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/update_status_quizprogress_by_ref_and_quizid.php")
    public void updateStatusQuizProgressByRefAndQuizId(
            @Query("statusid") int status,
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_score_quizprogress_by_ref_and_quizid.php")
    public void updateScoreQuizProgressByRefAndQuizId(
            @Query("score") int score,
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);
    @GET("/update_statuschoicea_quizprogress_by_ref_and_quizid.php")
    public void updateStatusChoiceAQuizByRefAndQuizId(
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_statuschoiceb_quizprogress_by_ref_and_quizid.php")
    public void updateStatusChoiceBQuizByRefAndQuizId(
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);
    @GET("/update_statuschoicec_quizprogress_by_ref_and_quizid.php")
    public void updateStatusChoiceCQuizByRefAndQuizId(
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);
    @GET("/update_statuschoiced_quizprogress_by_ref_and_quizid.php")
    public void updateStatusChoiceDQuizByRefAndQuizId(
            @Query("ref") int ref,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/insert_questprogress.php")
    public void addQuestProgress(
            @Query("questid") int qid,
            @Query("adventurerid") long aid,
            @Query("ref") int ref,
            @Query("zoneid") int zid,
            @Query("questtypeid") int qtid,
            Callback<Response> response);

    @GET("/insert_questprogress.php")
    public void addQuestProgressNonQuiz(
            @Query("questid") int qid,
            @Query("adventurerid") long aid,
            @Query("zoneid") int zid,
            @Query("questtypeid") int qtid,
            Callback<Response> response);

    @GET("/insert_quizprogress.php")
    public void addQuizProgress(
            @Query("ref") int ref,
            @Query("quizid") int aid,
            Callback<Response> response);

    @GET("/select_count_quizprogress_by_ref_finished.php")
    public void getCountQuizProgressFinishedByRef(
            @Query("ref") int ref,
            Callback<Response> response);

    @GET("/update_puzzleprogress_puzzlepiece_by_ref.php")
    public void updatePuzzleProgressPieceByRef(
            @Query("tlstatus") int topLeftStatus,
            @Query("tmstatus") int topMidStatus,
            @Query("trstatus") int topRightStatus,
            @Query("mlstatus") int midLeftStatus,
            @Query("mmstatus") int midMidStatus,
            @Query("mrstatus") int midRightStatus,
            @Query("blstatus") int bottomLeftStatus,
            @Query("bmstatus") int bottomMidStatus,
            @Query("brstatus") int bottomRightStatus,
            @Query("ref") int ref,
            Callback<Response> response
    );
    @GET("/select_all_puzzleprogress_by_ref.php")
    public void getPuzzleProgressByRef(
            @Query("ref") int ref,
            Callback<Response> response
    );

    @GET("/insert_puzzleprogress.php")
    public void addPuzzleProgress(
            @Query("ref") int ref,
            @Query("puzzleid") int puzzleid,
            Callback<Response> response
    );

    @GET("/select_all_riddleprogress_by_ref.php")
    public void getRiddleProgressByRef(
            @Query("ref") int ref,
            Callback<Response> response
    );
    @GET("/update_riddleprogress_scanlimit_by_ref.php")
    public void updateRiddleProgressScanLimitByRef(
            @Query("scanlimit") int scanLimit,
            @Query("ref") int ref,
            Callback<Response> response
    );

    @GET("/insert_riddleprogress.php")
    public void addRiddleProgress(
            @Query("ref") int ref,
            @Query("riddleid") int riddleId,
            Callback<Response> response
    );

    @GET("/update_riddleprogress_hintpiece_by_ref.php")
    public void updateRiddleProgressHintPieceByRef(
            @Query("hint1status") int hint1Status,
            @Query("hint2status") int hint2Status,
            @Query("hint3status") int hint3Status,
            @Query("ref") int ref,
            Callback<Response> response
    );
}
