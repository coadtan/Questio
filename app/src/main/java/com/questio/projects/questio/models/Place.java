package com.questio.projects.questio.models;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;
import com.questio.projects.questio.utilities.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class Place implements Serializable {
    private static final String LOG_TAG = Place.class.getSimpleName();
    @SerializedName("placeid")
    private int placeId;

    @SerializedName("placename")
    private String placeName;

    @SerializedName("placefullname")
    private String placeFullName;

    @SerializedName("qrcode")
    private long qrCode;

    @SerializedName("sensorid")
    private long sensorId;

    private double latitude;

    private double longitude;

    private double radius;

    @SerializedName("placetype")
    private String placeType;

    @SerializedName("imageurl")
    private String imageUrl;

    Context mContext;

    public Place() {
    }

    public Place(Context m) {
        mContext = m;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceFullName() {
        return placeFullName;
    }

    public void setPlaceFullName(String placeFullName) {
        this.placeFullName = placeFullName;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", placeName='" + placeName + '\'' +
                ", placeFullName='" + placeFullName + '\'' +
                ", qrCode=" + qrCode +
                ", sensorId=" + sensorId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", placetype='" + placeType + '\'' +
                ", imageurl='" + imageUrl + '\'' +
                '}';
    }

    public Cursor getAllPlacesCursor() {
        // 0 placeid as _id
        // 1 placename
        // 2 placefullname
        // 3 qrcode
        // 4 sensorid
        // 5 latitude
        // 6 longitude
        // 7 radius
        // 8 placeType
        Cursor cursor;
        String selectQuery = "SELECT  placeid as _id, placename, placefullname, qrcode, sensorid, latitude, longitude, radius, placetype, imageurl FROM place";
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public ArrayList<Place> getAllPlaceArrayList() {
        ArrayList<Place> list = new ArrayList<>();
        Place po;
        String selectQuery = "SELECT  * FROM place";
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                po = new Place();
                po.setPlaceId(Integer.parseInt(cursor.getString(0)));
                po.setPlaceName(cursor.getString(1));
                po.setPlaceFullName(cursor.getString(2));
                if (!cursor.getString(3).equalsIgnoreCase("null")) {
                    po.setQrCode(Integer.parseInt(cursor.getString(3)));
                }
                if (!cursor.getString(4).equalsIgnoreCase("null")) {
                    po.setSensorId(Integer.parseInt(cursor.getString(4)));
                }
                po.setLatitude(Double.parseDouble(cursor.getString(5)));
                po.setLongitude(Double.parseDouble(cursor.getString(6)));
                po.setRadius(Double.parseDouble(cursor.getString(7)));
                po.setPlaceType(cursor.getString(8));
                po.setImageUrl(cursor.getString(9));
                list.add(po);
            } while (cursor.moveToNext());
        }
        databaseHelper.close();
        database.close();
        return list;
    }

    public void delectAllPlace() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete("place", null, null);
        databaseHelper.close();
        database.close();
    }

    public long getPlaceCount() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, "place");
        databaseHelper.close();
        database.close();
        return count;
    }

}
