package com.questio.projects.questio.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by coad4u4ever on 22-Mar-15.
 */
public class HttpHelper extends AsyncTask<String, Void, String> {
    private static final String LOG_TAG = HttpHelper.class.getSimpleName();


    @Override
    protected String doInBackground(String... arg) {
        try{
            String link = arg[0];
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (is, "UTF-8") );
            String data = null;
            String webPage = "";
            while ((data = reader.readLine()) != null){
                webPage += data + "\n";
            }
            return webPage;
        }catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(LOG_TAG,"onPostExecute: " + result);
    }
}
