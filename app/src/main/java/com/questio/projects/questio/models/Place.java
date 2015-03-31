package com.questio.projects.questio.models;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.questio.projects.questio.utilities.DatabaseHelper;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class Place {
    private static final String LOG_TAG = Place.class.getSimpleName();
    private int placeId;
    private String placeName;
    private String placeFullName;
    private long qrCode;
    private long sensorId;
    private double latitude;
    private double longitude;
    private double radius;
    private String placetype;

    Context mContext;

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

    public String getPlacetype() {
        return placetype;
    }

    public void setPlacetype(String placetype) {
        this.placetype = placetype;
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
                ", placetype='" + placetype + '\'' +
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
        // 8 placetype
        Cursor cursor;
        String selectQuery = "SELECT  placeid as _id, placename, placefullname, qrcode, sensorid, latitude, longitude, radius, placetype FROM place";
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public  long getPlaceCount() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, "place");
        databaseHelper.close();
        database.close();
        return count;
    }
}
