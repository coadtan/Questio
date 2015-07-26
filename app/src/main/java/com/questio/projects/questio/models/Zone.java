package com.questio.projects.questio.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.questio.projects.questio.utilities.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class Zone {
    private static final String LOG_TAG = Zone.class.getSimpleName();

    @SerializedName("zoneid")
    private int zoneId;

    @SerializedName("floorid")
    private int floorId;

    @SerializedName("zonetypeid")
    private int zoneTypeId;

    @SerializedName("zonename")
    private String zoneName;

    @SerializedName("zonedetails")
    private String zoneDetails;

    @SerializedName("qrcode")
    private int qrCode;

    @SerializedName("sensorid")
    private int sensorId;

    @SerializedName("imageurl")
    private String imageUrl;

    @SerializedName("minimapurl")
    private String miniMapUrl;

    @SerializedName("itemset")
    private String itemSet;

    @SerializedName("rewardid")
    private int rewardId;

    @SerializedName("zonetypeimage")
    private String zoneTypeImage;

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getZoneTypeId() {
        return zoneTypeId;
    }

    public void setZoneTypeId(int zoneTypeId) {
        this.zoneTypeId = zoneTypeId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneDetails() {
        return zoneDetails;
    }

    public void setZoneDetails(String zoneDetails) {
        this.zoneDetails = zoneDetails;
    }

    public int getQrCode() {
        return qrCode;
    }

    public void setQrCode(int qrCode) {
        this.qrCode = qrCode;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMiniMapUrl() {
        return miniMapUrl;
    }

    public void setMiniMapUrl(String miniMapUrl) {
        this.miniMapUrl = miniMapUrl;
    }

    public String getItemSet() {
        return itemSet;
    }

    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public String getZoneTypeImage() {
        return zoneTypeImage;
    }

    public void setZoneTypeImage(String zoneTypeImage) {
        this.zoneTypeImage = zoneTypeImage;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "floorId=" + floorId +
                ", zoneId=" + zoneId +
                ", zoneTypeId=" + zoneTypeId +
                ", zoneName='" + zoneName + '\'' +
                ", zoneDetails='" + zoneDetails + '\'' +
                ", qrCode=" + qrCode +
                ", sensorId=" + sensorId +
                ", imageUrl='" + imageUrl + '\'' +
                ", miniMapUrl='" + miniMapUrl + '\'' +
                ", itemSet='" + itemSet + '\'' +
                ", rewardId=" + rewardId +
                ", zoneTypeImage='" + zoneTypeImage + '\'' +
                '}';
    }

    /*public static ArrayList<Zone> getAllZoneByFoorId(int floorId) {
        ArrayList<Zone> al = null;
        Zone zone;
        final String URL = "http://52.74.64.61/api/select_all_zone_by_floorid.php?floorid=" + floorId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                al = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    zone = new Zone();
                    JSONObject obj = (JSONObject) arr.get(i);
                    zone.setZoneId(Integer.parseInt(obj.get("zoneid").toString()));
                    zone.setZoneName(obj.get("zonename").toString());

                    if (!obj.get("zonetypeid").toString().equalsIgnoreCase("null")) {
                        zone.setZoneTypeId(Integer.parseInt(obj.get("zonetypeid").toString()));
                    }
                    if (!obj.get("zonedetails").toString().equalsIgnoreCase("null")) {
                        zone.setZoneDetails(obj.get("zonedetails").toString());
                    }
                    if (!obj.get("imageurl").toString().equalsIgnoreCase("null")) {
                        zone.setImageUrl(obj.get("imageurl").toString());
                    }
                    if (!obj.get("minimapurl").toString().equalsIgnoreCase("null")) {
                        zone.setMiniMapUrl(obj.get("minimapurl").toString());
                    }
                    if (!obj.get("floorid").toString().equalsIgnoreCase("null")) {
                        zone.setFloorId(Integer.parseInt(obj.get("floorid").toString()));
                    }
                    if (!obj.get("qrcode").toString().equalsIgnoreCase("null")) {
                        zone.setQrCode(Integer.parseInt(obj.get("qrcode").toString()));
                    }
                    if (!obj.get("sensorid").toString().equalsIgnoreCase("null")) {
                        zone.setSensorId(Integer.parseInt(obj.get("sensorid").toString()));
                    }
                    if (!obj.get("itemset").toString().equalsIgnoreCase("null")) {
                        zone.setItemSet(obj.get("itemset").toString());
                    }
                    if (!obj.get("rewardid").toString().equalsIgnoreCase("null")) {
                        zone.setRewardId(Integer.parseInt(obj.get("rewardid").toString()));
                    }


                    al.add(zone);
                }

            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        return al;
    }*/

    /*public static ArrayList<Zone> getAllZoneByPlaceId(int placeId) {
        ArrayList<Zone> al = null;
        Zone zone;
        final String URL = "http://52.74.64.61/api/select_all_zone_by_placeid.php?placeid=" + placeId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                al = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    zone = new Zone();
                    JSONObject obj = (JSONObject) arr.get(i);
                    String zoneid = obj.get("zoneid").toString();
                    String floorid = obj.get("floorid").toString();
                    String zonetypeid = obj.get("zonetypeid").toString();
                    String zonename = obj.get("zonename").toString();
                    String zonedetails = obj.get("zonedetails").toString();
                    String qrcode = obj.get("qrcode").toString();
                    String sensorid = obj.get("sensorid").toString();
                    String imageurl = obj.get("imageurl").toString();
                    String minimapurl = obj.get("minimapurl").toString();
                    String itemset = obj.get("itemset").toString();
                    String rewardid = obj.get("rewardid").toString();
                    zone.setZoneId(Integer.parseInt(zoneid));
                    zone.setFloorId(Integer.parseInt(floorid));
                    zone.setZoneTypeId(Integer.parseInt(zonetypeid));
                    zone.setZoneName(zonename);
                    if (!zonedetails.equalsIgnoreCase("null")) {
                        zone.setZoneDetails(zonedetails);
                    }
                    if (!qrcode.equalsIgnoreCase("null")) {
                        zone.setQrCode(Integer.parseInt(qrcode));
                    }
                    if (!sensorid.equalsIgnoreCase("null")) {
                        zone.setSensorId(Integer.parseInt(sensorid));

                    }
                    if (!imageurl.equalsIgnoreCase("null")) {
                        zone.setImageUrl(imageurl);

                    }
                    if (!minimapurl.equalsIgnoreCase("null")) {
                        zone.setMiniMapUrl(minimapurl);
                    }
                    if (!itemset.equalsIgnoreCase("null")) {
                        zone.setItemSet(itemset);
                    }
                    if (!rewardid.equalsIgnoreCase("null")) {
                        zone.setRewardId(Integer.parseInt(rewardid));
                    }

                    al.add(zone);
                }

            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        return al;
    }*/

    public static int findZoneIdByQRCode(int qrCode) {
        int zoneId = 0;
        final String URL = "http://52.74.64.61/api/select_zoneid_by_qrcode.php?qrcode=" + qrCode;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                JSONObject obj = (JSONObject) arr.get(0);
                zoneId = Integer.parseInt(obj.get("zoneid").toString());
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        return zoneId;
    }

    public static Zone getZoneByZoneId(int id) {
        Zone zone = null;
        final String URL = "http://52.74.64.61/api/select_zone_by_zoneid.php?zoneid=" + id;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                zone = new Zone();
                JSONObject obj = (JSONObject) arr.get(0);
                String zoneid = obj.get("zoneid").toString();
                String floorid = obj.get("floorid").toString();
                String zonetypeid = obj.get("zonetypeid").toString();
                String zonename = obj.get("zonename").toString();
                String zonedetails = obj.get("zonedetails").toString();
                String qrcode = obj.get("qrcode").toString();
                String sensorid = obj.get("sensorid").toString();
                String imageurl = obj.get("imageurl").toString();
                String minimapurl = obj.get("minimapurl").toString();
                String itemset = obj.get("itemset").toString();
                String rewardid = obj.get("rewardid").toString();
                zone.setZoneId(Integer.parseInt(zoneid));
                zone.setFloorId(Integer.parseInt(floorid));
                zone.setZoneTypeId(Integer.parseInt(zonetypeid));
                zone.setZoneName(zonename);
                if (!zonedetails.equalsIgnoreCase("null")) {
                    zone.setZoneDetails(zonedetails);
                }
                if (!qrcode.equalsIgnoreCase("null")) {
                    zone.setQrCode(Integer.parseInt(qrcode));
                }
                if (!sensorid.equalsIgnoreCase("null")) {
                    zone.setSensorId(Integer.parseInt(sensorid));

                }
                if (!imageurl.equalsIgnoreCase("null")) {
                    zone.setImageUrl(imageurl);

                }
                if (!minimapurl.equalsIgnoreCase("null")) {
                    zone.setMiniMapUrl(minimapurl);
                }
                if (!itemset.equalsIgnoreCase("null")) {
                    zone.setItemSet(itemset);
                }
                if (!rewardid.equalsIgnoreCase("null")) {
                    zone.setRewardId(Integer.parseInt(rewardid));
                }


            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        return zone;
    }
}
