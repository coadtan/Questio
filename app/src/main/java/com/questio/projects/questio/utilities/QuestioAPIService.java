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
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.models.RewardHOF;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;


public interface QuestioAPIService {


    @GET("/select_all_quest_by_placeid_location_name.php")
    void getQuestsByPlaceId(@Query("placeid") int id, Callback<ArrayList<Quest>> response);


    @GET("/select_all_quest_by_zoneid.php")
    void getAllQuestByZoneId(@Query("zoneid") int id, Callback<ArrayList<Quest>> response);

    @GET("/select_all_quiz_by_questid.php")
    void getAllQuizByQuestId(@Query("questid") int id, Callback<ArrayList<Quiz>> response);

    @GET("/select_all_riddle_by_questid.php")
    void getRiddleByQuestId(@Query("questid") int id, Callback<Riddle[]> response);

    @GET("/select_all_picturepuzzle_by_puzzleid.php")
    void getPicturePuzzleByPuzzleId(@Query("puzzleid") int id, Callback<PicturePuzzle[]> response);

    @GET("/select_all_placenews_by_placeid.php")
    void getAllPlaceNewsByPlaceId(@Query("placeid") int id, Callback<ArrayList<PlaceNews>> response);

    @GET("/select_placedetail_by_placeid.php")
    void getPlaceDetailByPlaceId(@Query("placeid") int id, Callback<PlaceDetail[]> response);

    @GET("/select_zone_by_zoneid.php")
    void getZoneByZoneId(@Query("zoneid") int id, Callback<Zone[]> response);

    @GET("/select_count_adventurer.php")
    void getCountAdventurer(Callback<Response> response);

    @GET("/select_guserid_adventurer_from_guserid.php")
    void getGuserIdByGuserId(@Query("guserid") String gid, Callback<Response> response);

    @GET("/select_adventurerid_adventurer_from_guserid.php")
    void getAdventurerIdByGuserId(@Query("guserid") String gid, Callback<Response> response);


    @GET("/insert_adventurer.php")
    void addAdventurer(@Query("adventurerid") long id,
                       @Query("guserid") String gid,
                       @Query("email") String email,
                       @Query("avatarid") long aid,
                       @Query("detailid") long did,
                       Callback<Response> response);

    @GET("/insert_adventurerdetails.php")
    void addAdventurerDetails(@Query("detailid") long id,
                              @Query("displayname") String displayName,
                              @Query("birthdate") String birthDate,
                              Callback<Response> response);

    @GET("/select_all_questprogress_by_questid_and_adventurerid.php")
    void getQuestProgressByQuestIdAndAdventurerId(
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/select_all_questprogress_by_adventurerid.php")
    void getQuestProgressByAdventurerId(
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/select_all_questprogress_by_questid.php")
    void getQuestProgressByQuestId(
            @Query("questid") int questid,
            Callback<Response> response);

    @GET("/select_all_quizprogress_by_ref.php")
    void getQuizProgressByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<ArrayList<QuizProgress>> response);

    @GET("/update_score_questprogress_by_questid_and_adventurerid.php")
    void updateScoreQuestProgressByQuestIdAndAdventurerId(
            @Query("score") int score,
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/update_status_questprogress_by_questid_and_adventurerid.php")
    void updateStatusQuestProgressByQuestIdAndAdventurerId(
            @Query("statusid") int status,
            @Query("questid") int questid,
            @Query("adventurerid") long adventurerid,
            Callback<Response> response);

    @GET("/update_status_quizprogress_by_ref_and_quizid.php")
    void updateStatusQuizProgressByRefAndQuizId(
            @Query("statusid") int status,
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_score_quizprogress_by_ref_and_quizid.php")
    void updateScoreQuizProgressByRefAndQuizId(
            @Query("score") int score,
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_statuschoicea_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceAQuizByRefAndQuizId(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_statuschoiceb_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceBQuizByRefAndQuizId(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_statuschoicec_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceCQuizByRefAndQuizId(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/update_statuschoiced_quizprogress_by_ref_and_quizid.php")
    void updateStatusChoiceDQuizByRefAndQuizId(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            @Query("quizid") int quizid,
            Callback<Response> response);

    @GET("/insert_questprogress.php")
    void addQuestProgress(
            @Query("questid") int qid,
            @Query("adventurerid") long aid,
            @Query("zoneid") int zid,
            @Query("questtypeid") int qtid,
            Callback<Response> response);

//    @GET("/insert_questprogress.php")
//    void addQuestProgressNonQuiz(
//            @Query("questid") int qid,
//            @Query("adventurerid") long aid,
//            @Query("zoneid") int zid,
//            @Query("questtypeid") int qtid,
//            Callback<Response> response);

    @GET("/insert_quizprogress.php")
    void addQuizProgress(
            @Query("adventurerid") long aid,
            @Query("questid") int quid,
            @Query("quizid") int qid,
            Callback<Response> response);

    @GET("/select_count_quizprogress_by_ref_finished.php")
    void getCountQuizProgressFinishedByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response);

    @GET("/update_puzzleprogress_tlpiece_by_ref.php")
    void updatePuzzleProgressTopLeftPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_tmpiece_by_ref.php")
    void updatePuzzleProgressTopMidPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_trpiece_by_ref.php")
    void updatePuzzleProgressTopRightPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_mlpiece_by_ref.php")
    void updatePuzzleProgressMidLeftPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_mmpiece_by_ref.php")
    void updatePuzzleProgressMidMidPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_mrpiece_by_ref.php")
    void updatePuzzleProgressMidRightPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_blpiece_by_ref.php")
    void updatePuzzleProgressBottomLeftPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_bmpiece_by_ref.php")
    void updatePuzzleProgressBottomMidPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_puzzleprogress_brpiece_by_ref.php")
    void updatePuzzleProgressBottomRightPieceByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/select_all_puzzleprogress_by_ref.php")
    void getPuzzleProgressByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/insert_puzzleprogress.php")
    void addPuzzleProgress(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/select_all_riddleprogress_by_ref.php")
    void getRiddleProgressByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_riddleprogress_scanlimit_by_ref.php")
    void updateRiddleProgressScanLimitByRef(
            @Query("scanlimit") int scanLimit,
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );


    @GET("/select_current_points_puzzle_progress_by_ref.php")
    void getCurrentPointsByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/select_current_points_riddle_progress_by_ref.php")
    void getCurrentRiddlePointByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/insert_riddleprogress.php")
    void addRiddleProgress(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_riddleprogress_hint1_by_ref.php")
    void updateRiddleProgressHint1ByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_riddleprogress_hint2_by_ref.php")
    void updateRiddleProgressHint2ByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    @GET("/update_riddleprogress_hint3_by_ref.php")
    void updateRiddleProgressHint3ByRef(
            @Query("adventurerid") long adventurerId,
            @Query("questid") int questId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/update_questprogress_autoscore_quiz.php?questid=20&adventurerid=1&statusid=3

    @GET("/update_questprogress_autoscore_quiz.php")
    void updateQuestProgressAutoScoreQuiz(
            @Query("questid") int questId,
            @Query("adventurerid") long adventurerId,
            @Query("statusid") int statusId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_quest_status_and_score_by_zoneandadventurerid.php?zoneid=1&adventurerid=2
    @GET("/select_quest_status_and_score_by_zoneandadventurerid.php")
    void getQuestStatusAndScoreByZoneAdventurerid(
            @Query("zoneid") int zoneId,
            @Query("adventurerid") long adventurerId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_reward_by_zoneid.php?zoneid=1
    @GET("/select_all_reward_by_zoneid.php")
    void getRewardByZoneId(
            @Query("zoneid") int zoneId,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_all_item_by_zoneid.php?zoneid=1
    @GET("/select_all_item_by_zoneid.php")
    void getItemByZoneId(
            @Query("zoneid") int zoneId,
            Callback<Item[]> item
    );

    //http://52.74.64.61/api/insert_inventory.php?adventurerid=1&itemid=1
    @GET("/insert_inventory.php")
    void addInventory(
            @Query("adventurerid") long adventurerId,
            @Query("itemid") int itemId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_inventory_by_adventurerid_and_itemid.php?adventurerid=1&itemid=1
    @GET("/select_count_inventory_by_adventurerid_and_itemid.php")
    void getCountInventoryByAdventurerIdAndItemId(
            @Query("adventurerid") long adventurerId,
            @Query("itemid") int itemId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_hof_by_adventurerid_and_rewardid.php?adventurerid=2&rewardid=1
    @GET("/select_count_hof_by_adventurerid_and_rewardid.php")
    void getCountHOFByAdventurerIdAndRewardId(
            @Query("adventurerid") long adventurerId,
            @Query("rewardid") int rewardId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_item_inventory_by_adventurerid.php?adventurerid=2
    @GET("/select_all_item_inventory_by_adventurerid.php")
    void getAllItemInInventoryByAdventurerId(
            @Query("adventurerid") long adventurerId,
            Callback<ArrayList<ItemInInventory>> response
    );

    //http://52.74.64.61/api/select_all_rewards_halloffame_by_adventurerid.php?adventurerid=2
    @GET("/select_all_rewards_halloffame_by_adventurerid.php")
    void getAllRewardsInHalloffameByAdventurerId(
            @Query("adventurerid") long adventurerId,
            Callback<ArrayList<RewardHOF>> response
    );

    //http://52.74.64.61/api/insert_rewards.php?adventurerid=2&rewardid=1&rankid=4
    @GET("/insert_rewards.php")
    void addRewards(
            @Query("adventurerid") long adventurerId,
            @Query("rewardid") int rewardId,
            @Query("rankid") int rankId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_reward_by_questid.php?questid=22
    @GET("/select_all_reward_by_questid.php")
    void getRewardByQuestId(
            @Query("questid") int questId,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_all_reward_by_placeid.php?placeid=1
    @GET("/select_all_reward_by_placeid.php")
    void getRewardByPlaceId(
            @Query("placeid") int placeId,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/insert_explorerprogress.php?adventurerid=2&placeid=1&zoneid=1
    @GET("/insert_explorerprogress.php")
    void addExplorerProgress(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            @Query("zoneid") int zoneId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_all_zone_by_placeid.php?placeid=1
    @GET("/select_count_all_zone_by_placeid.php")
    void getCountZoneByPlaceId(
            @Query("placeid") int placeId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_count_explorerprogress_by_placeid_and_adventurerid.php?placeid=1&adventurerid=1
    @GET("/select_count_explorerprogress_by_placeid_and_adventurerid.php")
    void getCountExplorerProgressByAdventurerIdAndPlaceId(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_explorerprogress_by_adventurerid_placeid_and_zoneid.php?adventurerid=1&placeid=1&zoneid=1
    @GET("/select_all_explorerprogress_by_adventurerid_placeid_and_zoneid.php")
    void getExplorerProgressByAdventurerIdPlaceIdAndZoneId(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            @Query("zoneid") int zoneId,
            Callback<ExplorerProgress[]> response
    );

    //http://52.74.64.61/api/select_all_zone_by_placeid.php?placeid=1
    @GET("/select_all_zone_by_placeid.php")
    void getZoneByPlaceId(
            @Query("placeid") int placeId,
            Callback<ArrayList<Zone>> response
    );

    //http://52.74.64.61/api/update_explorerprogress_by_adventurerid_and_placeid_and_zoneid.php?adventurerid=2&placeid=1&zoneid=1
    @GET("/update_explorerprogress_by_adventurerid_and_placeid_and_zoneid.php")
    void updateExplorerProgressByAdventurerIdPlaceIdAndZoneId(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            @Query("zoneid") int zoneId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/insert_placeprogress.php?adventurerid=1&placeid=1
    @GET("/insert_placeprogress.php")
    void addPlaceProgress(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/update_placeprogress_by_adventurerid_and_placeid.php?adventurerid=1&placeid=1
    @GET("/update_placeprogress_by_adventurerid_and_placeid.php")
    void updatePlaceProgressByAdventurerIdAndPlaceId(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_placeprogress_by_adventurerid_and_placeid.php?adventurerid=1&placeid=1
    @GET("/select_all_placeprogress_by_adventurerid_and_placeid.php")
    void getAllPlaceProgressByAdventurerIdAndPlaceId(
            @Query("adventurerid") long adventurerId,
            @Query("placeid") int placeId,
            Callback<PlaceProgress[]> response
    );

    //http://52.74.64.61/api/select_all_place_by_zoneid.php?zoneid=1
    @GET("/select_all_place_by_zoneid.php")
    void getAllPlaceByZoneId(
            @Query("zoneid") int zoneId,
            Callback<Place[]> response
    );

    //http://52.74.64.61/api/select_all_explore_reward_by_placeid.php?placeid=1
    @GET("/select_all_explore_reward_by_placeid.php")
    void getAllExploreRewardByPlaceId(
            @Query("placeid") int placeId,
            Callback<Reward[]> reward
    );

    //http://52.74.64.61/api/select_avatar_count_by_avatarid.php?avatarid=1
    @GET("/select_avatar_count_by_avatarid.php")
    void getAvatarCountByAvatarId(
            @Query("avatarid") long avatarId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/insert_avatar.php?avatarid=1
    @GET("/insert_avatar.php")
    void insertNewAvatar(
            @Query("avatarid") long avatarId,
            Callback<Response> response
    );

    //http://52.74.64.61/api/select_all_avatar_by_avatarid.php?avatarid=1
    @GET("/select_all_avatar_by_avatarid.php")
    void getAvatarByAvatarId(
            @Query("avatarid") long avatarId,
            Callback<Avatar[]> avatar
    );

    //http://52.74.64.61/api/select_equipspritepath_by_itemid.php?itemid=1
    @GET("/select_equipspritepath_by_itemid.php")
    void getEquipSpritePathByItemId(
            @Query("itemid") long itemid,
            Response response
    );

    //http://52.74.64.61/api/select_all_item_by_itemid.php?headid=1&backgroundid=2&neckid=3&bodyid=4&handleftid=5&handrightid=6&armid=7&legid=8&footid=9&specialid=10
    @GET("/select_all_item_by_itemid.php")
    void getItemsBySetOfItemId(
            @Query("headid") long headid,
            @Query("backgroundid") long backgroundid,
            @Query("neckid") long neckid,
            @Query("bodyid") long bodyid,
            @Query("handleftid") long handleftid,
            @Query("handrightid") long handrightid,
            @Query("armid") long armid,
            @Query("legid") long legid,
            @Query("footid") long footid,
            @Query("specialid") long specialid,
            Callback<Item[]> items
    );
}
