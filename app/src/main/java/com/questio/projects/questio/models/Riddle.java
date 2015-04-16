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
 * Created by ning jittima on 11/4/2558.
 */
public class Riddle{
    private static final String LOG_TAG = Riddle.class.getSimpleName();
    /*
    * ridid
    * riddetails
    * qrcode
    * sensorid
    * scanlimit
    * hint1
    * hint2
    * hint3
    * */
    @SerializedName("ridid")
    private int ridId;

    @SerializedName("riddetails")
    private String ridDetails;

    @SerializedName("qrcode")
    private long qrCode;

    @SerializedName("sensorid")
    private long sensorId;

    @SerializedName("scanlimit")
    private int scanLimit;
    private String hint1;
    private String hint2;
    private String hint3;

    public int getRidId() {
        return ridId;
    }

    public void setRidId(int ridId) {
        this.ridId = ridId;
    }

    public String getRidDetails() {
        return ridDetails;
    }

    public void setRidDetails(String ridDetails) {
        this.ridDetails = ridDetails;
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

    public int getScanLimit() {
        return scanLimit;
    }

    public void setScanLimit(int scanLimit) {
        this.scanLimit = scanLimit;
    }

    public String getHint1() {
        return hint1;
    }

    public void setHint1(String hint1) {
        this.hint1 = hint1;
    }

    public String getHint2() {
        return hint2;
    }

    public void setHint2(String hint2) {
        this.hint2 = hint2;
    }

    public String getHint3() {
        return hint3;
    }

    public void setHint3(String hint3) {
        this.hint3 = hint3;
    }

    @Override
    public String toString() {
        return "Riddle{" +
                "ridId=" + ridId +
                ", ridDetails='" + ridDetails + '\'' +
                ", qrCode=" + qrCode +
                ", sensorId=" + sensorId +
                ", scanLimit=" + scanLimit +
                ", hint1='" + hint1 + '\'' +
                ", hint2='" + hint2 + '\'' +
                ", hint3='" + hint3 + '\'' +
                '}';
    }

    public static Riddle getAllRiddleByRiddleId(int id){
        Riddle r = null;
        final String URL = "http://52.74.64.61/api/select_all_riddle_by_questid.php?questid=" + id;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG,"getAllRiddleByQuestId response: " + response);
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length()!=0){
                    r = new Riddle();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                    String ridid = jsonObject.get("ridid").toString();
                    String riddetails = jsonObject.get("riddetails").toString();
                    String qrcode = jsonObject.get("qrcode").toString();
                    String sensorid = jsonObject.get("sensorid").toString();
                    String scanlimit = jsonObject.get("scanlimit").toString();
                    String hint1 = jsonObject.get("hint1").toString();
                    String hint2 = jsonObject.get("hint2").toString();
                    String hint3 = jsonObject.get("hint3").toString();
                    if(!hint1.equalsIgnoreCase("null")){
                        r.setHint1(hint1);
                    }
                    if(!hint2.equalsIgnoreCase("null")){
                        r.setHint2(hint2);
                    }
                    if(!hint3.equalsIgnoreCase("null")){
                        r.setHint3(hint3);
                    }
                    if(!qrcode.equalsIgnoreCase("null")){
                        r.setQrCode(Long.parseLong(qrcode));
                    }
                    if(!sensorid.equalsIgnoreCase("null")){
                        r.setSensorId(Long.parseLong(sensorid));
                    }
                    r.setRidDetails(riddetails);
                    r.setRidId(Integer.parseInt(ridid));
                    r.setScanLimit(Integer.parseInt(scanlimit));
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return r;
    }
}
