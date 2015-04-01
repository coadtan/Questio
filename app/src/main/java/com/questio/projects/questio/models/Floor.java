package com.questio.projects.questio.models;

import android.util.Log;

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
    private int floorId;
    private int placeId;
    private String floorName;
    private String imageUrl;
    private double latitude;
    private double longitude;


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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
//        return "Floor{" +
//                "floorId=" + floorId +
//                ", placeId=" + placeId +
//                ", floorName='" + floorName + '\'' +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", latitude=" + latitude +
//                ", longitude=" + longitude +
//                '}';
        return floorId +": "+floorName;
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
                    floor.setImageUrl(obj.get("imageurl").toString());
                    if (!obj.get("latitude").toString().equalsIgnoreCase("null")) {
                        floor.setLatitude(Double.parseDouble(obj.get("latitude").toString()));
                    }
                    if (!obj.get("longitude").toString().equalsIgnoreCase("null")) {
                        floor.setLongitude(Double.parseDouble(obj.get("longitude").toString()));
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
