package com.questio.projects.questio.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.questio.projects.questio.utilities.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by coad4u4ever on 04-Apr-15.
 */
public class PlaceDetail {
    /*
    placeid
    placedetails
    phonecontact1
    phonecontact2
    website
    email
    imageurl
    */
    private static final String LOG_TAG = PlaceDetail.class.getSimpleName();
    @SerializedName("placeid")
    private int placeId;

    @SerializedName("placedetails")
    private String placeDetails;

    @SerializedName("phonecontact1")
    private String phoneContact1;

    @SerializedName("phonecontact2")
    private String phoneContact2;

    @SerializedName("website")
    private String webSite;

    @SerializedName("email")
    private String eMail;

    @SerializedName("imageurl")
    private String imageUrl;

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(String placeDetails) {
        this.placeDetails = placeDetails;
    }

    public String getPhoneContact1() {
        return phoneContact1;
    }

    public void setPhoneContact1(String phoneContact1) {
        this.phoneContact1 = phoneContact1;
    }

    public String getPhoneContact2() {
        return phoneContact2;
    }

    public void setPhoneContact2(String phoneContact2) {
        this.phoneContact2 = phoneContact2;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "PlaceDetail{" +
                "placeId=" + placeId +
                ", placeDetails='" + placeDetails + '\'' +
                ", phoneContact1='" + phoneContact1 + '\'' +
                ", phoneContact2='" + phoneContact2 + '\'' +
                ", webSite='" + webSite + '\'' +
                ", eMail='" + eMail + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public static PlaceDetail getPlaceDetailByPlaceId(int placeId) {
        PlaceDetail pd= null;
        final String URL = "http://52.74.64.61/api/select_placedetail_by_placeid.php?placeid=" + placeId;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG, "getPlaceDetailByPlaceId response:" + response);
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() != 0) {
                pd = new PlaceDetail();
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                String placeid = jsonObject.get("placeid").toString();
                String placedetails = jsonObject.get("placedetails").toString();
                String phonecontact1 = jsonObject.get("phonecontact1").toString();
                String phonecontact2 = jsonObject.get("phonecontact2").toString();
                String website = jsonObject.get("website").toString();
                String email = jsonObject.get("email").toString();
                String imageurl = jsonObject.get("imageurl").toString();
                pd.setPlaceId(Integer.parseInt(placeid));
                pd.setPlaceDetails(placedetails);
                pd.setPhoneContact1(phonecontact1);
                pd.setPhoneContact2(phonecontact2);
                pd.setWebSite(website);
                pd.seteMail(email);
                pd.setImageUrl(imageurl);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        return pd;
    }
}
