package com.questio.projects.questio.utilities;

import com.questio.projects.questio.models.Avatar;
import com.questio.projects.questio.models.ExplorerProgress;
import com.questio.projects.questio.models.Item;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.models.PicturePuzzle;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.PlaceDetail;
import com.questio.projects.questio.models.PlaceNews;
import com.questio.projects.questio.models.PlaceProgress;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.Quiz;
import com.questio.projects.questio.models.QuizProgress;
import com.questio.projects.questio.models.Ranking;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.models.RewardHOF;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;


public interface QuestioAPIService {


    @FormUrlEncoded
    @POST("/select_all_quest_by_placeid_location_name.php")
    void getQuestsByPlaceId(@Field("placeid") int id,
                            @Field("key") String key,
                            Callback<ArrayList<Quest>> response);


    @FormUrlEncoded
    @POST("/select_all_quest_by_zoneid.php")
    void getAllQuestByZoneId(@Field("zoneid") int id,
                             @Field("key") String key,
                             Callback<ArrayList<Quest>> response);

    @FormUrlEncoded
    @POST("/select_all_quiz_by_questid.php")
    void getAllQuizByQuestId(@Field("questid") int id,
                             @Field("key") String key,
                             Callback<ArrayList<Quiz>> response);

    @FormUrlEncoded
    @POST("/select_all_riddle_by_questid.php")
    void getRiddleByQuestId(@Field("questid") int id,
                            @Field("key") String key,
                            Callback<Riddle[]> response);

    @FormUrlEncoded
    @POST("/select_all_picturepuzzle_by_puzzleid.php")
    void getPicturePuzzleByPuzzleId(@Field("puzzleid") int id,
                                    @Field("key") String key,
                                    Callback<PicturePuzzle[]> response);

    @FormUrlEncoded
    @POST("/select_all_placenews_by_placeid.php")
    void getAllPlaceNewsByPlaceId(@Field("placeid") int id,
                                  @Field("key") String key,
                                  Callback<ArrayList<PlaceNews>> response);

    @FormUrlEncoded
    @POST("/select_placedetail_by_placeid.php")
    void getPlaceDetailByPlaceId(@Field("placeid") int id,
                                 @Field("key") String key,
                                 Callback<PlaceDetail[]> response);

    @GET("/select_zone_by_zoneid.php")
    void getZoneByZoneId(@Query("zoneid") int id,
                         Callback<Zone[]> response);

    @FormUrlEncoded
    @POST("/select_count_adventurer.php")
    void getCountAdventurer(@Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_guserid_adventurer_from_guserid.php")
    void getGuserIdByGuserId(@Field("guserid") String gid,
                             @Field("key") String key,
                             Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_adventurerid_adventurer_from_guserid.php")
    void getAdventurerIdByGuserId(@Field("guserid") String gid,
                                  @Field("key") String key,
                                  Callback<Response> response);


    @FormUrlEncoded
    @POST("/insert_adventurer.php")
    void addAdventurer(@Field("adventurerid") long id,
                       @Field("guserid") String gid,
                       @Field("email") String email,
                       @Field("avatarid") long aid,
                       @Field("detailid") long did,
                       @Field("key") String key,
                       Callback<Response> response);

    @FormUrlEncoded
    @POST("/insert_adventurerdetails.php")
    void addAdventurerDetails(@Field("detailid") long id,
                              @Field("displayname") String displayName,
                              @Field("birthdate") String birthDate,
                              @Field("key") String key,
                              Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_all_questprogress_by_questid_and_adventurerid.php")
    void getQuestProgressByQuestIdAndAdventurerId(
            @Field("questid") int questid,
            @Field("adventurerid") long adventurerid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_all_questprogress_by_adventurerid.php")
    void getQuestProgressByAdventurerId(
            @Field("adventurerid") long adventurerid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_all_questprogress_by_questid.php")
    void getQuestProgressByQuestId(
            @Field("questid") int questid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_all_quizprogress_by_ref.php")
    void getQuizProgressByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<ArrayList<QuizProgress>> response);

    @FormUrlEncoded
    @POST("/update_score_questprogress_by_questid_and_adventurerid.php")
    void updateScoreQuestProgressByQuestIdAndAdventurerId(
            @Field("score") int score,
            @Field("questid") int questid,
            @Field("adventurerid") long adventurerid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_status_questprogress_by_questid_and_adventurerid.php")
    void updateStatusQuestProgressByQuestIdAndAdventurerId(
            @Field("statusid") int status,
            @Field("questid") int questid,
            @Field("adventurerid") long adventurerid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_status_quizprogress_by_ref_and_quizid.php")
    void updateStatusQuizProgressByRefAndQuizId(
            @Field("statusid") int status,
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_score_quizprogress_by_ref_and_quizid.php")
    void updateScoreQuizProgressByRefAndQuizId(
            @Field("score") int score,
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_statuschoicea_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceAQuizByRefAndQuizId(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_statuschoiceb_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceBQuizByRefAndQuizId(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_statuschoicec_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceCQuizByRefAndQuizId(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_statuschoiced_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceDQuizByRefAndQuizId(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("quizid") int quizid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/insert_questprogress.php")
    void addQuestProgress(
            @Field("questid") int qid,
            @Field("adventurerid") long aid,
            @Field("zoneid") int zid,
            @Field("questtypeid") int qtid,
            @Field("key") String key,
            Callback<Response> response);

//
// @POST("/insert_questprogress.php")
//    void addQuestProgressNonQuiz(
//            @Field("questid") int qid,
//            @Field("adventurerid") long aid,
//            @Field("zoneid") int zid,
//            @Field("questtypeid") int qtid,
//            Callback<Response> response);

    @FormUrlEncoded
    @POST("/insert_quizprogress.php")
    void addQuizProgress(
            @Field("adventurerid") long aid,
            @Field("questid") int quid,
            @Field("quizid") int qid,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/select_count_quizprogress_by_ref_finished.php")
    void getCountQuizProgressFinishedByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response);

    @FormUrlEncoded
    @POST("/update_puzzleprogress_tlpiece_by_ref.php")
    void updatePuzzleProgressTopLeftPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_tmpiece_by_ref.php")
    void updatePuzzleProgressTopMidPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_trpiece_by_ref.php")
    void updatePuzzleProgressTopRightPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_mlpiece_by_ref.php")
    void updatePuzzleProgressMidLeftPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_mmpiece_by_ref.php")
    void updatePuzzleProgressMidMidPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_mrpiece_by_ref.php")
    void updatePuzzleProgressMidRightPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_blpiece_by_ref.php")
    void updatePuzzleProgressBottomLeftPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_bmpiece_by_ref.php")
    void updatePuzzleProgressBottomMidPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_puzzleprogress_brpiece_by_ref.php")
    void updatePuzzleProgressBottomRightPieceByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/select_all_puzzleprogress_by_ref.php")
    void getPuzzleProgressByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/insert_puzzleprogress.php")
    void addPuzzleProgress(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/select_all_riddleprogress_by_ref.php")
    void getRiddleProgressByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_riddleprogress_scanlimit_by_ref.php")
    void updateRiddleProgressScanLimitByRef(
            @Field("scanlimit") int scanLimit,
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );


    @FormUrlEncoded
    @POST("/select_current_points_puzzle_progress_by_ref.php")
    void getCurrentPointsByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/select_current_points_riddle_progress_by_ref.php")
    void getCurrentRiddlePointByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/insert_riddleprogress.php")
    void addRiddleProgress(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_riddleprogress_hint1_by_ref.php")
    void updateRiddleProgressHint1ByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_riddleprogress_hint2_by_ref.php")
    void updateRiddleProgressHint2ByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/update_riddleprogress_hint3_by_ref.php")
    void updateRiddleProgressHint3ByRef(
            @Field("adventurerid") long adventurerId,
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/update_questprogress_autoscore_quiz.php?questid=20&adventurerid=1&statusid=3

    @FormUrlEncoded
    @POST("/update_questprogress_autoscore_quiz.php")
    void updateQuestProgressAutoScoreQuiz(
            @Field("questid") int questId,
            @Field("adventurerid") long adventurerId,
            @Field("statusid") int statusId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_quest_status_and_score_by_zoneandadventurerid.php?zoneid=1&adventurerid=2
    @FormUrlEncoded
    @POST("/select_quest_status_and_score_by_zoneandadventurerid.php")
    void getQuestStatusAndScoreByZoneAdventurerid(
            @Field("zoneid") int zoneId,
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_reward_by_zoneid.php?zoneid=1
    @FormUrlEncoded
    @POST("/select_all_reward_by_zoneid.php")
    void getRewardByZoneId(
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_all_item_by_zoneid.php?zoneid=1
    @FormUrlEncoded
    @POST("/select_all_item_by_zoneid.php")
    void getItemByZoneId(
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<Item[]> item
    );

    //http://52.74.64.61/api/insert_inventory.php?adventurerid=1&itemid=1
    @FormUrlEncoded
    @POST("/insert_inventory.php")
    void addInventory(
            @Field("adventurerid") long adventurerId,
            @Field("itemid") int itemId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_inventory_by_adventurerid_and_itemid.php?adventurerid=1&itemid=1
    @FormUrlEncoded
    @POST("/select_count_inventory_by_adventurerid_and_itemid.php")
    void getCountInventoryByAdventurerIdAndItemId(
            @Field("adventurerid") long adventurerId,
            @Field("itemid") int itemId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_hof_by_adventurerid_and_rewardid.php?adventurerid=2&rewardid=1
    @FormUrlEncoded
    @POST("/select_count_hof_by_adventurerid_and_rewardid.php")
    void getCountHOFByAdventurerIdAndRewardId(
            @Field("adventurerid") long adventurerId,
            @Field("rewardid") int rewardId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_item_inventory_by_adventurerid.php?adventurerid=2
    @FormUrlEncoded
    @POST("/select_all_item_inventory_by_adventurerid.php")
    void getAllItemInInventoryByAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<ArrayList<ItemInInventory>> response
    );

    //http://52.74.64.61/api/select_all_rewards_halloffame_by_adventurerid.php?adventurerid=2
    @FormUrlEncoded
    @POST("/select_all_rewards_halloffame_by_adventurerid.php")
    void getAllRewardsInHalloffameByAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<ArrayList<RewardHOF>> response
    );

    //http://52.74.64.61/api/insert_rewards.php?adventurerid=2&rewardid=1&rankid=4
    @FormUrlEncoded
    @POST("/insert_rewards.php")
    void addRewards(
            @Field("adventurerid") long adventurerId,
            @Field("rewardid") int rewardId,
            @Field("rankid") int rankId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_reward_by_questid.php?questid=22
    @FormUrlEncoded
    @POST("/select_all_reward_by_questid.php")
    void getRewardByQuestId(
            @Field("questid") int questId,
            @Field("key") String key,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_all_reward_by_placeid.php?placeid=1
    @FormUrlEncoded
    @POST("/select_all_reward_by_placeid.php")
    void getRewardByPlaceId(
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/insert_explorerprogress.php?adventurerid=2&placeid=1&zoneid=1
    @FormUrlEncoded
    @POST("/insert_explorerprogress.php")
    void addExplorerProgress(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_all_zone_by_placeid.php?placeid=1
    @FormUrlEncoded
    @POST("/select_count_all_zone_by_placeid.php")
    void getCountZoneByPlaceId(
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_explorerprogress_by_placeid_and_adventurerid.php?placeid=1&adventurerid=1
    @FormUrlEncoded
    @POST("/select_count_explorerprogress_by_placeid_and_adventurerid.php")
    void getCountExplorerProgressByAdventurerIdAndPlaceId(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_explorerprogress_by_adventurerid_placeid_and_zoneid.php?adventurerid=1&placeid=1&zoneid=1
    @FormUrlEncoded
    @POST("/select_all_explorerprogress_by_adventurerid_placeid_and_zoneid.php")
    void getExplorerProgressByAdventurerIdPlaceIdAndZoneId(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<ExplorerProgress[]> response
    );

    //http://52.74.64.61/api/select_all_zone_by_placeid.php?placeid=1
    @FormUrlEncoded
    @POST("/select_all_zone_by_placeid.php")
    void getZoneByPlaceId(
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<ArrayList<Zone>> response
    );

    //http://52.74.64.61/api/update_explorerprogress_by_adventurerid_and_placeid_and_zoneid.php?adventurerid=2&placeid=1&zoneid=1
    @FormUrlEncoded
    @POST("/update_explorerprogress_by_adventurerid_and_placeid_and_zoneid.php")
    void updateExplorerProgressByAdventurerIdPlaceIdAndZoneId(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/insert_placeprogress.php?adventurerid=1&placeid=1
    @FormUrlEncoded
    @POST("/insert_placeprogress.php")
    void addPlaceProgress(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/update_placeprogress_by_adventurerid_and_placeid.php?adventurerid=1&placeid=1
    @FormUrlEncoded
    @POST("/update_placeprogress_by_adventurerid_and_placeid.php")
    void updatePlaceProgressByAdventurerIdAndPlaceId(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_placeprogress_by_adventurerid_and_placeid.php?adventurerid=1&placeid=1
    @FormUrlEncoded
    @POST("/select_all_placeprogress_by_adventurerid_and_placeid.php")
    void getAllPlaceProgressByAdventurerIdAndPlaceId(
            @Field("adventurerid") long adventurerId,
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<PlaceProgress[]> response
    );

    //http://52.74.64.61/api/select_all_place_by_zoneid.php?zoneid=1
    @FormUrlEncoded
    @POST("/select_all_place_by_zoneid.php")
    void getAllPlaceByZoneId(
            @Field("zoneid") int zoneId,
            @Field("key") String key,
            Callback<Place[]> response
    );

    //http://52.74.64.61/api/select_all_explore_reward_by_placeid.php?placeid=1
    @FormUrlEncoded
    @POST("/select_all_explore_reward_by_placeid.php")
    void getAllExploreRewardByPlaceId(
            @Field("placeid") int placeId,
            @Field("key") String key,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_avatar_count_by_avatarid.php?avatarid=1
    @FormUrlEncoded
    @POST("/select_avatar_count_by_avatarid.php")
    void getAvatarCountByAvatarId(
            @Field("avatarid") long avatarId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/insert_avatar.php?avatarid=1
    @FormUrlEncoded
    @POST("/insert_avatar.php")
    void insertNewAvatar(
            @Field("avatarid") long avatarId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_avatar_by_avatarid.php?avatarid=1
    @FormUrlEncoded
    @POST("/select_all_avatar_by_avatarid.php")
    void getAvatarByAvatarId(
            @Field("avatarid") long avatarId,
            @Field("key") String key,
            Callback<Avatar[]> avatar
    );

    //http://52.74.64.61/api/select_equipspritepath_by_itemid.php?itemid=1
    @FormUrlEncoded
    @POST("/select_equipspritepath_by_itemid.php")
    void getEquipSpritePathByItemId(
            @Field("itemid") long itemid,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_item_by_itemid.php?headid=1&backgroundid=2&neckid=3&bodyid=4&handleftid=5&handrightid=6&armid=7&legid=8&footid=9&specialid=10
    @FormUrlEncoded
    @POST("/select_all_item_by_itemid.php")
    void getItemsBySetOfItemId(
            @Field("headid") long headid,
            @Field("backgroundid") long backgroundid,
            @Field("neckid") long neckid,
            @Field("bodyid") long bodyid,
            @Field("handleftid") long handleftid,
            @Field("handrightid") long handrightid,
            @Field("armid") long armid,
            @Field("legid") long legid,
            @Field("footid") long footid,
            @Field("specialid") long specialid,
            @Field("key") String key,
            Callback<Item[]> items
    );

    //http://52.74.64.61/api/select_all_item_inventory_by_adventurerid_and_positionid.php?adventurerid=2&positionid=1
    @FormUrlEncoded
    @POST("/select_all_item_inventory_by_adventurerid_and_positionid.php")
    void getAllItemInInventoryByAdventurerIdAndPositionId(
            @Field("adventurerid") long adventurerId,
            @Field("positionid") int positionId,
            @Field("key") String key,
            Callback<ArrayList<ItemInInventory>> items
    );

    //http://52.74.64.61/api/equip_new_item_by_partid_itemid_and_avatarid.php?part=1&itemid=2&avatarid=2&olditemid=1
    @FormUrlEncoded
    @POST("/equip_new_item_by_partid_itemid_and_avatarid.php")
    void equipNewItem(
            @Field("part") int partId,
            @Field("itemid") long itemId,
            @Field("olditemid") long oldItemId,
            @Field("avatarid") long avatarId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/unequip_by_partid_itemid_and_avatarid.php?part=1&itemid=1&avatarid=2
    @FormUrlEncoded
    @POST("/unequip_by_partid_itemid_and_avatarid.php")
    void unequipItem(
            @Field("part") int partId,
            @Field("itemid") long itemId,
            @Field("avatarid") long avatarId,
            @Field("key") String key,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_rewards_halloffame_place_by_adventurerid.php?adventurerid=2
    @FormUrlEncoded
    @POST("/select_rewards_halloffame_place_by_adventurerid.php")
    void getAllPlaceRewardsInHalloffameByAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<ArrayList<RewardHOF>> response
    );

    @FormUrlEncoded
    @POST("/select_rewards_halloffame_nonplace_by_adventurerid.php")
    void getNonPlaceRewardsInHalloffameByAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<ArrayList<RewardHOF>> response
    );

    //select_topten_rank.php
    @FormUrlEncoded
                               @POST("/select_topten_rank.php")
      void getRankingTopTen(
                    @Field("adventurerid") long adventurerId,
                    @Field("key") String key,
                    Callback<ArrayList<Ranking>> response
            );

    @FormUrlEncoded
    @POST("/select_guserid_adventurer_from_adventurerid.php")
    void getGUserIdFromAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/select_current_rank_by_adventurerid.php")
    void getCurrentRankByAdventurerId(
            @Field("adventurerid") long adventurerId,
            @Field("key") String key,
            Callback<Ranking[]> response
    );
}
