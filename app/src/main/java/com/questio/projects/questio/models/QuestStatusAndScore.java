package com.questio.projects.questio.models;

import com.questio.projects.questio.utilities.QuestioHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.client.Response;


public class QuestStatusAndScore {
    private int questId;
    private int status;
    private int score;

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "QuestStatusAndScore{" +
                "questId=" + questId +
                ", status=" + status +
                ", score=" + score +
                '}';
    }

    public static ArrayList<QuestStatusAndScore> createStatusList(Response response) {
        ArrayList<QuestStatusAndScore> statusList = null;
        JSONArray jsonArray = null;
        QuestStatusAndScore q;
        try {
            jsonArray = new JSONArray(QuestioHelper.responseToString(response));
            if (jsonArray.length() != 0) {
                statusList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    q = new QuestStatusAndScore();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String questid = jsonObject.get("questid").toString();
                    String status = jsonObject.get("statusid").toString();
                    String score = jsonObject.get("score").toString();
                    q.setQuestId(Integer.parseInt(questid));
                    q.setStatus(Integer.parseInt(status));
                    q.setScore(Integer.parseInt(score));

                    statusList.add(q);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return statusList;
    }


}
