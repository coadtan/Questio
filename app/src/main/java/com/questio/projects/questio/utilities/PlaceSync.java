package com.questio.projects.questio.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class PlaceSync extends AsyncTask<String, Void, String> {
    private static final String LOG_TAG = PlaceSync.class.getSimpleName();
    private final Context mContext;

    public PlaceSync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg) {
        try {
            URL url = new URL(arg[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (is, "UTF-8"));
            String data;
            String webPage = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            return webPage;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(LOG_TAG, "onPostExecute: " + result);
        updateSQLite(result);

    }

    public void updateSQLite(String response) {
        Log.d(LOG_TAG, response);
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        try {
            JSONArray arr = new JSONArray(response);
            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    HashMap<String, String> queryValues = new HashMap<>();
                    queryValues.put(QuestioConstants.TAG_PLACE_ID, obj.get(QuestioConstants.TAG_PLACE_ID).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_NAME, obj.get(QuestioConstants.TAG_PLACE_NAME).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_FULL_NAME, obj.get(QuestioConstants.TAG_PLACE_FULL_NAME).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_QR_CODE, obj.get(QuestioConstants.TAG_PLACE_QR_CODE).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_SENSOR_ID, obj.get(QuestioConstants.TAG_PLACE_SENSOR_ID).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_LATITUDE, obj.get(QuestioConstants.TAG_PLACE_LATITUDE).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_LONGITUDE, obj.get(QuestioConstants.TAG_PLACE_LONGITUDE).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_RADIUS, obj.get(QuestioConstants.TAG_PLACE_RADIUS).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_TYPE, obj.get(QuestioConstants.TAG_PLACE_TYPE).toString());
                    queryValues.put(QuestioConstants.TAG_PLACE_IMAGEURL, obj.get(QuestioConstants.TAG_PLACE_IMAGEURL).toString());
                    ContentValues values = new ContentValues();
                    values.put(QuestioConstants.TAG_PLACE_ID, queryValues.get(QuestioConstants.TAG_PLACE_ID));
                    values.put(QuestioConstants.TAG_PLACE_NAME, queryValues.get(QuestioConstants.TAG_PLACE_NAME));
                    values.put(QuestioConstants.TAG_PLACE_FULL_NAME, queryValues.get(QuestioConstants.TAG_PLACE_FULL_NAME));
                    values.put(QuestioConstants.TAG_PLACE_QR_CODE, queryValues.get(QuestioConstants.TAG_PLACE_QR_CODE));
                    values.put(QuestioConstants.TAG_PLACE_SENSOR_ID, queryValues.get(QuestioConstants.TAG_PLACE_SENSOR_ID));
                    values.put(QuestioConstants.TAG_PLACE_LATITUDE, queryValues.get(QuestioConstants.TAG_PLACE_LATITUDE));
                    values.put(QuestioConstants.TAG_PLACE_LONGITUDE, queryValues.get(QuestioConstants.TAG_PLACE_LONGITUDE));
                    values.put(QuestioConstants.TAG_PLACE_RADIUS, queryValues.get(QuestioConstants.TAG_PLACE_RADIUS));
                    values.put(QuestioConstants.TAG_PLACE_TYPE, queryValues.get(QuestioConstants.TAG_PLACE_TYPE));
                    values.put(QuestioConstants.TAG_PLACE_IMAGEURL, queryValues.get(QuestioConstants.TAG_PLACE_IMAGEURL));
                    database.insert("place", null, values);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            databaseHelper.close();
            database.close();
        }

    }


}
