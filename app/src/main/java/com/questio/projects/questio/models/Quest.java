package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by ning jittima on 2/4/2558.
 */
public class Quest {
    private static final String LOG_TAG = Quest.class.getSimpleName();

    @SerializedName("questid")
    private int questId;

    @SerializedName("questname")
    private String questName;

    @SerializedName("questdetails")
    private String questDetails;

    @SerializedName("questtypeid")
    private int questTypeId;

    @SerializedName("zoneid")
    private int zoneId;

    @SerializedName("diffid")
    private int diffId;

    @SerializedName("zonename")
    private String zoneName;

    @SerializedName("floorname")
    private String floorName;

    @SerializedName("buildingname")
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


    public static int[] countAttribute(ArrayList<Quest> arrayList) {
        int[] count = new int[3];
        HashSet<String> buildingSet = new HashSet<>();
        HashSet<String> floorSet = new HashSet<>();
        HashSet<String> zoneSet = new HashSet<>();
        for (Quest q : arrayList) {
            buildingSet.add(q.getBuildingName());
            floorSet.add(q.getFloorName());
            zoneSet.add(q.getZoneName());
        }

        count[0] = buildingSet.size();
        count[1] = floorSet.size();
        count[2] = zoneSet.size();
        return count;
    }

    public static String[] getBuildingNamesArray(String tag, ArrayList<Quest> arrayList) {
        if (tag.equalsIgnoreCase("building")) {
            String[] buildingNames;
            HashSet<String> buildingSet = new HashSet<>();
            for (Quest q : arrayList) {
                buildingSet.add(q.getBuildingName());
            }
            buildingNames = buildingSet.toArray(new String[buildingSet.size() + 1]);
            buildingNames[buildingSet.size()] = " ";
            return buildingNames;
        } else if (tag.equalsIgnoreCase("floor")) {
            String[] floorNames;
            HashSet<String> floorSet = new HashSet<>();
            for (Quest q : arrayList) {
                floorSet.add(q.getFloorName());
            }
            floorNames = floorSet.toArray(new String[floorSet.size() + 1]);
            floorNames[floorSet.size()] = " ";
            return floorNames;
        }else if (tag.equalsIgnoreCase("zone")) {
            String[] zoneNames;
            HashSet<String> zoneSet = new HashSet<>();
            for (Quest q : arrayList) {
                zoneSet.add(q.getZoneName());
            }
            zoneNames = zoneSet.toArray(new String[zoneSet.size() + 1]);
            zoneNames[zoneSet.size()] = " ";
            return zoneNames;
        }
        return null;
    }
//    public static ArrayList<Quest> getAllQuestByPlaceId(int placeId) {
//        ArrayList<Quest> arr = null;
//        final String URL = "http://52.74.64.61/api/select_all_quest_by_placeid_location_name.php?placeid=" + placeId;
//        try {
//            String response = new HttpHelper().execute(URL).get();
//            Log.d(LOG_TAG, "getAllQuestByPlaceId response:" + response);
//            JSONArray jsonArray = new JSONArray(response);
//            if (jsonArray.length() != 0) {
//                arr = getQuestsFromJSON(response);
//            }
//        } catch (InterruptedException | ExecutionException | JSONException e) {
//            e.printStackTrace();
//        }
//
//        return arr;
//    }

//    public static ArrayList<Quest> getAllQuestByZoneId(int zoneId) {
//        ArrayList<Quest> arr = null;
//        final String URL = "http://52.74.64.61/api/select_all_quest_by_zoneid.php?zoneid=" + zoneId;
//        try {
//            String response = new HttpHelper().execute(URL).get();
//            Log.d(LOG_TAG, "getAllQuestByZoneId response:" + response);
//            JSONArray jsonArray = new JSONArray(response);
//            if (jsonArray.length() != 0) {
//                arr =  getQuestsFromJSON(response);
//            }
//        } catch (InterruptedException | ExecutionException | JSONException e) {
//            e.printStackTrace();
//        }
//        return arr;
//    }


//    public static ArrayList<Quest> getQuestsFromJSON(String response) {
//        ArrayList<Quest> quests = new ArrayList<>();
//        Gson gson = new Gson();
//        Quest[] questsTemp = gson.fromJson(response, Quest[].class);
//        Collections.addAll(quests, questsTemp);
//        return quests;
//    }
}
