package com.questio.projects.questio.models;

import android.util.Log;

import com.questio.projects.questio.utilities.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by ning jittima on 11/4/2558.
 */
public class PicturePuzzle {
    private static final String LOG_TAG = PicturePuzzle.class.getSimpleName();
    /*
    puzzleid
    imageurl
    helperanswer
    correctanswer
     */
    private int puzzleId;
    private String imageUrl;
    private String helperAnswer;
    private String correctAnswer;

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHelperAnswer() {
        return helperAnswer;
    }

    public void setHelperAnswer(String helperAnswer) {
        this.helperAnswer = helperAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "PicturePuzzle{" +
                "puzzleId=" + puzzleId +
                ", imageUrl='" + imageUrl + '\'' +
                ", helperAnswer='" + helperAnswer + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }

    public static PicturePuzzle getAllPicturePuzzleByQuestId(int id){
       // ArrayList<PicturePuzzle> arr = null;
        PicturePuzzle pp = null;
        final String URL = "http://52.74.64.61/api/select_all_picturepuzzle_by_puzzleid.php?puzzleid="+id;
        try {
            String response = new HttpHelper().execute(URL).get();
            Log.d(LOG_TAG,"getAllPicturePuzzleByQuestId response: " + response);
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length()!=0){
                //arr = new ArrayList<>();
               // for (int i = 0; i < jsonArray.length(); i++) {
                    pp = new PicturePuzzle();
                    JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                    String puzzleid = jsonObject.get("puzzleid").toString();
                    String imageurl = jsonObject.get("imageurl").toString();
                    String helperanswer = jsonObject.get("helperanswer").toString();
                    String correctanswer = jsonObject.get("correctanswer").toString();
                    if(!helperanswer.equalsIgnoreCase("null")){
                        pp.setHelperAnswer(helperanswer);
                    }
                    pp.setCorrectAnswer(correctanswer);
                    pp.setImageUrl(imageurl);
                    pp.setPuzzleId(Integer.parseInt(puzzleid));
                //    arr.add(pp);
              //  }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        //return arr;
        return pp;
    }
}
