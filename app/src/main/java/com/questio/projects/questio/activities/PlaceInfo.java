package com.questio.projects.questio.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.NewsListAdapter;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.PlaceDetail;
import com.questio.projects.questio.models.PlaceNews;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by coad4u4ever on 04-Apr-15.
 */
public class PlaceInfo extends ActionBarActivity {
    Toolbar toolbar;
    ImageView quest_browsing_picture;
    ImageView place_detail;
    TextView place_name;
    TextView place_fullname;
    TextView place_contact1;
    TextView place_contact2;
    TextView place_www;
    TextView place_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_info);
        // init
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        quest_browsing_picture = (ImageView) findViewById(R.id.quest_browsing_picture);
        place_detail = (ImageView) findViewById(R.id.place_detail);
        place_name = (TextView) findViewById(R.id.place_name);
        place_fullname = (TextView) findViewById(R.id.place_fullname);
        place_contact1 = (TextView) findViewById(R.id.place_contact1);
        place_contact2 = (TextView) findViewById(R.id.place_contact2);
        place_www = (TextView) findViewById(R.id.place_www);
        place_email = (TextView) findViewById(R.id.place_email);

        // init-end

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Place place = (Place) getIntent().getSerializableExtra("place");
        final PlaceDetail placeDetail = PlaceDetail.getPlaceDetailByPlaceId(place.getPlaceId());

        // set value
        place_name.setText(place.getPlaceName());
        place_fullname.setText(place.getPlaceFullName());
        place_contact1.setText(placeDetail.getPhoneContact1());
        place_contact2.setText(placeDetail.getPhoneContact2());
        place_www.setText(placeDetail.getWebSite());
        place_email.setText(placeDetail.geteMail());
        place_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PlaceInfo.this);
                builder1.setTitle("ข้อมูลสถานที่");
                builder1.setMessage(placeDetail.getPlaceDetails());
                builder1.setCancelable(true);
                builder1.setNeutralButton("ขอบคุณ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.show();
            }
        });
        ArrayList<PlaceNews> newsList = PlaceNews.getAllPlaceNewsByPlaceId(place.getPlaceId());
        NewsListAdapter adapter = new NewsListAdapter(this, newsList);
        ListView listView = (ListView) findViewById(R.id.place_list_feed);
        listView.setAdapter(adapter);

        new DownloadImageTask(quest_browsing_picture).execute("http://52.74.64.61" + placeDetail.getImageUrl());
        // set value-end
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
