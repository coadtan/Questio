package com.questio.projects.questio.models;

import android.util.Log;

import com.questio.projects.questio.utilities.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ning jittima on 2/4/2558.
 */
public class Quest {
    private static final String LOG_TAG = Quest.class.getSimpleName();
    private int questId;
    private String questName;
    private String questDetails;
    private int questTypeId;
    private int zoneId;
    private int diffId;
    private String zoneName;
    private String floorName;
    private String buildingName;


    public static String getLogTag() {
        return LOG_TAG;
    }


    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getQuestDetails() {
        return questDetails;
    }

    public void setQuestDetails(String questDetails) {
        this.questDetails = questDetails;
    }

    public int getQuestTypeId() {
        return questTypeId;
    }

    public void setQuestTypeId(int questTypeId) {
        this.questTypeId = questTypeId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getDiffId() {
        return diffId;
    }

    public void setDiffId(int diffId) {
        this.diffId = diffId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "buildingName='" + buildingName + '\'' +
                ", floorName='" + floorName + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", diffId=" + diffId +
                ", zoneId=" + zoneId +
                ", questTypeId=" + questTypeId +
                ", questDetails='" + questDetails + '\'' +
                ", questName='" + questName + '\'' +
                ", questId=" + questId +
                '}';
    }

    public static ArrayList<Quest> getAllQuestByPlaceId(int placeId){
        Quest q;
        ArrayList<Quest> arr = null;
        final String URL = "http://52.74.64.61/api/select_all_quest_by_placeid_location_name.php?placeid=" + placeId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG,"getAllQuestByPlaceId response:" +response);
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length() != 0){
                arr = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    q = new Quest();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    String questid = jsonObject.get("questid").toString();
                    String questname = jsonObject.get("questname").toString();
                    String questdetails = jsonObject.get("questdetails").toString();
                    String questtypeid = jsonObject.get("questtypeid").toString();
                    String zoneid = jsonObject.get("zoneid").toString();
                    String diffid = jsonObject.get("diffid").toString();
                    String zonename = jsonObject.get("zonename").toString();
                    String floorname = jsonObject.get("floorname").toString();
                    String buildingname = jsonObject.get("buildingname").toString();
                    q.setQuestId(Integer.parseInt(questid));
                    q.setQuestName(questname);
                    q.setQuestDetails(questdetails);
                    q.setQuestTypeId(Integer.parseInt(questtypeid));
                    q.setDiffId(Integer.parseInt(diffid));
                    q.setZoneId(Integer.parseInt(zoneid));
                    q.setZoneName(zonename);
                    q.setFloorName(floorname);
                    q.setBuildingName(buildingname);
                    arr.add(q);
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    public static ArrayList<Quest> getAllQuestByZoneId(int zoneId){
        Quest q;
        ArrayList<Quest> arr = null;
        final String URL = "http://52.74.64.61/api/select_all_quest_by_zoneid.php?zoneid=" + zoneId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG,"getAllQuestByZoneId response:" +response);
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length() != 0){
                arr = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    q = new Quest();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    String questid = jsonObject.get("questid").toString();
                    String questname = jsonObject.get("questname").toString();
                    String questdetails = jsonObject.get("questdetails").toString();
                    String questtypeid = jsonObject.get("questtypeid").toString();
                    String zoneid = jsonObject.get("zoneid").toString();
                    String diffid = jsonObject.get("diffid").toString();
                    q.setQuestId(Integer.parseInt(questid));
                    q.setQuestName(questname);
                    q.setQuestDetails(questdetails);
                    q.setQuestTypeId(Integer.parseInt(questtypeid));
                    q.setDiffId(Integer.parseInt(diffid));
                    q.setZoneId(Integer.parseInt(zoneid));
                    arr.add(q);
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
