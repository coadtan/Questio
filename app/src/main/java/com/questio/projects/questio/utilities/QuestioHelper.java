package com.questio.projects.questio.utilities;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.questio.projects.questio.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import retrofit.client.Response;

public class QuestioHelper {

    private static final String LOG_TAG = QuestioHelper.class.getSimpleName();

    // passing "questio:zone:123:questio" to paremater qrCode will return "123"
    public static String[] getDeQRCode(String qrCode) {
        String[] deCodeForReturn = {"failed", "failed"};
        final String BEGIN_TYPE_1 = "questio:zone:";
        final String BEGIN_TYPE_2 = "questio:place:";
        final String BEGIN_TYPE_3 = "questio:floor:";
        final String BEGIN_TYPE_4 = "questio:building:";
        final String BEGIN_TYPE_5 = "questio:riddleanswer:";
        final String END = ":questio";
        // 1 Step: check size
        if (qrCode.length() < (BEGIN_TYPE_2.length() + END.length())) {
            return deCodeForReturn;
        }
        // 2 Step: removeBegin
        String type = getQRType(qrCode);

        if (type.equalsIgnoreCase(QuestioConstants.QRTYPE_ZONE)) {
            qrCode = qrCode.replace(BEGIN_TYPE_1, "");
        } else if (type.equalsIgnoreCase(QuestioConstants.QRTYPE_FLOOR)) {
            qrCode = qrCode.replace(BEGIN_TYPE_3, "");
        } else if (type.equalsIgnoreCase(QuestioConstants.QRTYPE_BUILDING)) {
            qrCode = qrCode.replace(BEGIN_TYPE_4, "");
        } else if (type.equalsIgnoreCase(QuestioConstants.QRTYPE_PLACE)) {
            qrCode = qrCode.replace(BEGIN_TYPE_2, "");
        } else if (type.equalsIgnoreCase(QuestioConstants.QRTYPE_RIDDLE_ANSWER)) {
            qrCode = qrCode.replace(BEGIN_TYPE_5, "");
        }
        // 3 Step: removeEnd
        qrCode = qrCode.replace(END, "");
        deCodeForReturn[0] = type;
        deCodeForReturn[1] = qrCode;
        questioLog(LOG_TAG, "deCodeForReturn[0]: " + deCodeForReturn[0]);
        questioLog(LOG_TAG, "deCodeForReturn[1]: " + deCodeForReturn[1]);
        return deCodeForReturn;
    }

    public static String getQRType(String qrCode) {
        String type = qrCode.substring(8, 9);
        if (type.equalsIgnoreCase("z")) {
            type = QuestioConstants.QRTYPE_ZONE;
        } else if (type.equalsIgnoreCase("f")) {
            type = QuestioConstants.QRTYPE_FLOOR;
        } else if (type.equalsIgnoreCase("b")) {
            type = QuestioConstants.QRTYPE_BUILDING;
        } else if (type.equalsIgnoreCase("p")) {
            type = QuestioConstants.QRTYPE_PLACE;
        } else if (type.equalsIgnoreCase("r")) {
            type = QuestioConstants.QRTYPE_RIDDLE_ANSWER;
        } else {
            type = "N/A";
        }
        return type;
    }

    public static long getPlaceCountFromJson(String response) {
        JSONArray arr;
        Log.d(LOG_TAG, "getPlaceCountFromJson: " + response);
        long result = 0;
        try {
            arr = new JSONArray(response);
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    result = Long.parseLong(obj.get("count").toString());

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getImgLink(String path) {
        return QuestioConstants.BASE_QUESTIO_MANAGEMENT + path;
    }

    public static long getAdventurerCountFromJson(String response) {
        JSONArray arr;
        Log.d(LOG_TAG, "getAdventurerCountFromJson: " + response);
        long result = 0;
        try {
            arr = new JSONArray(response);
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    result = Long.parseLong(obj.get("count").toString());

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String responseToString(Response response) {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getJSONStringValueByTag(String tag, String inputResponese) {

        JSONArray arr;
        Log.d(LOG_TAG, "getJSONStringValueByTag: " + inputResponese);
        String resultForReturn = null;
        try {
            arr = new JSONArray(inputResponese);
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    resultForReturn = obj.get(tag).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultForReturn;
    }

    public static String getJSONStringValueByTag(String tag, Response inputResponese) {
        JSONArray arr;
        Log.d(LOG_TAG, "getJSONStringValueByTag(Response): " + responseToString(inputResponese));
        String resultForReturn = null;
        try {
            arr = new JSONArray(QuestioHelper.responseToString(inputResponese));
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    resultForReturn = obj.get(tag).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultForReturn;
    }

    public static String[] moveBackToFront(String[] items) {
        String temp[] = new String[items.length];
        for (int i = items.length - 1; i >= 0; i--) {
            if (i == items.length - 1) {
                temp[0] = items[items.length - 1];
                continue;
            }
            temp[i + 1] = items[i];
        }
        return temp;
    }

    public static int getPercentFrom2ValueAsInt(double a, double b) {
        double temp = a / b * 100;
        Log.d(LOG_TAG, "getPercentFrom2ValueAsInt: " + (int) temp);
        if (a == b) {
            Log.d(LOG_TAG, "getPercentFrom2ValueAsInt: " + 100);
            return 100;
        }
        return (int) temp;
    }

    public static void questioLog(String tag, String message) {
        Log.d(tag, message);
    }
}
