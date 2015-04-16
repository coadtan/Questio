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
 * Created by coad4u4ever on 04-Apr-15.
 */
public class PlaceNews {
    /*
    newsid
    placeid
    newsheader
    newsdetails
    datestarted
    dateended
    */
    private static final String LOG_TAG = PlaceNews.class.getSimpleName();
    @SerializedName("newsid")
    private int newsId;

    @SerializedName("placeid")
    private int placeId;

    @SerializedName("newsheader")
    private String newsHeader;

    @SerializedName("newsdetails")
    private String newsDetails;

    @SerializedName("datestarted")
    private String dateStarted;

    @SerializedName("dateended")
    private String dateEnded;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getNewsHeader() {
        return newsHeader;
    }

    public void setNewsHeader(String newsHeader) {
        this.newsHeader = newsHeader;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(String newsDetails) {
        this.newsDetails = newsDetails;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(String dateEnded) {
        this.dateEnded = dateEnded;
    }

    @Override
    public String toString() {
        return "PlaceNews{" +
                "newsId=" + newsId +
                ", placeId=" + placeId +
                ", newsHeader='" + newsHeader + '\'' +
                ", newsDetails='" + newsDetails + '\'' +
                ", dateStarted='" + dateStarted + '\'' +
                ", dateEnded='" + dateEnded + '\'' +
                '}';
    }

    public static ArrayList<PlaceNews> getAllPlaceNewsByPlaceId(int placeId){
        PlaceNews news;
        ArrayList<PlaceNews> arr = null;
        final String URL = "http://52.74.64.61/api/select_all_placenews_by_placeid.php?placeid=" + placeId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "getAllPlaceNewsByPlaceId response:" + response);
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length() != 0){
                arr = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    news = new PlaceNews();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    String newsid = jsonObject.get("newsid").toString();
                    String placeid = jsonObject.get("placeid").toString();
                    String newsheader = jsonObject.get("newsheader").toString();
                    String newsdetails = jsonObject.get("newsdetails").toString();
                    String datestarted = jsonObject.get("datestarted").toString();
                    String dateended = jsonObject.get("dateended").toString();
                    news.setNewsId(Integer.parseInt(newsid));
                    news.setPlaceId(Integer.parseInt(placeid));
                    news.setNewsHeader(newsheader);
                    news.setNewsDetails(newsdetails);
                    news.setDateStarted(datestarted);
                    news.setDateEnded(dateended);

                    arr.add(news);
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
