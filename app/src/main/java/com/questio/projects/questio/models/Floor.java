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
public class Floor {

    private static final String LOG_TAG = Floor.class.getSimpleName();

    @SerializedName("floorid")
    private int floorId;

    @SerializedName("placeid")
    private int placeId;

    @SerializedName("floorname")
    private String floorName;

    @SerializedName("imageurl")
    private String imageUrl;

    @SerializedName("qrcode")
    private long qrCode;

    @SerializedName("sensorid")
    private long sensorId;


    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getQrCode() {
        return qrCode;
    }

    public void setQrCode(long qrCode) {
        this.qrCode = qrCode;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "floorId=" + floorId +
                ", placeId=" + placeId +
                ", floorName='" + floorName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", qrCode=" + qrCode +
                ", sensorId=" + sensorId +
                '}';
    }

    public static ArrayList<Floor> getAllFloorByPlaceId(int placeId) {
        ArrayList<Floor> al = null;
        Floor floor;
        final String URL = "http://52.74.64.61/api/select_all_floor_by_placeid.php?placeid=" + placeId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "response: " + response);
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                al = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    floor = new Floor();
                    JSONObject obj = (JSONObject) arr.get(i);
                    floor.setFloorId(Integer.parseInt(obj.get("floorid").toString()));
                    floor.setFloorName(obj.get("floorname").toString());
                    floor.setPlaceId(Integer.parseInt(obj.get("placeid").toString()));
                    if (!obj.get("imageurl").toString().equalsIgnoreCase("null")) {
                        floor.setImageUrl(obj.get("imageurl").toString());
                    }
                    if (!obj.get("qrcode").toString().equalsIgnoreCase("null")) {
                        floor.setQrCode(Long.parseLong(obj.get("qrcode").toString()));
                    }
                    if (!obj.get("longitude").toString().equalsIgnoreCase("null")) {
                        floor.setSensorId(Long.parseLong(obj.get("sensorid").toString()));
                    }
                    al.add(floor);
                }

            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        return al;
    }


}
