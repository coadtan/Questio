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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.NewsListAdapter;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.PlaceDetail;
import com.questio.projects.questio.models.PlaceNews;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PlaceInfo extends ActionBarActivity {
    private static final String LOG_TAG = PlaceInfo.class.getSimpleName();
    @Bind(R.id.app_bar)
    Toolbar toolbar;

    @Bind(R.id.place_info_picture)
    ImageView placeInfoPicture;

    @Bind(R.id.place_detail)
    ImageView placeDetail;

    @Bind(R.id.place_name)
    TextView placeName;

    @Bind(R.id.place_fullname)
    TextView placeFullname;

    @Bind(R.id.place_contact1)
    TextView placeContact1;

    @Bind(R.id.place_contact2)
    TextView placeContact2;

    @Bind(R.id.place_www)
    TextView placeWww;

    @Bind(R.id.place_email)
    TextView placeEmail;

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        place = (Place) getIntent().getSerializableExtra("place");
        requestPlaceInfoData(place.getPlaceId());

        // set value
        requestPlaceNewsData(place.getPlaceId());
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

    private void requestPlaceNewsData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getAllPlaceNewsByPlaceId(id, new Callback<ArrayList<PlaceNews>>() {
            @Override
            public void success(ArrayList<PlaceNews> placeNews, Response response) {
                if (placeNews != null) {
                    ArrayList<PlaceNews> newsList = placeNews;
                    NewsListAdapter adapter = new NewsListAdapter(PlaceInfo.this, newsList);
                    ListView listView = ButterKnife.findById(PlaceInfo.this, R.id.place_list_feed);
                    listView.setAdapter(adapter);
                } else {
                    Log.d(LOG_TAG, "place news is null");
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getStackTrace());
            }
        });
    }

    private void requestPlaceInfoData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getPlaceDetailByPlaceId(id, new Callback<PlaceDetail[]>() {
            @Override
            public void success(PlaceDetail[] placeDetails, Response response) {
                if (placeDetails[0] != null) {
                    final PlaceDetail placeDetail = placeDetails[0];
                    placeName.setText(place.getPlaceName());
                    placeFullname.setText(place.getPlaceFullName());
                    placeContact1.setText(placeDetail.getPhoneContact1());
                    placeContact2.setText(placeDetail.getPhoneContact2());
                    placeWww.setText(placeDetail.getWebSite());
                    placeEmail.setText(placeDetail.geteMail());
                    PlaceInfo.this.placeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(PlaceInfo.this);
                            dialog
                                    .withTitle("ข้อมูลสถานที่")
                                    .withTitleColor("#FFFFFF")
                                    .withDividerColor("#11000000")
                                    .withMessage(placeDetail.getPlaceDetails())
                                    .withMessageColor("#FFFFFFFF")
                                    .withDialogColor("#FFE74C3C")
                                    .withDuration(300)
                                    .withEffect(Effectstype.Slidetop)
                                    .withButton1Text("ขอบคุณ")
                                    .setButton1Click(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .isCancelableOnTouchOutside(true);
                            dialog.show();
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PlaceInfo.this);
//                            builder1.setTitle("ข้อมูลสถานที่");
//                            builder1.setMessage(placeDetail.getPlaceDetails());
//                            builder1.setCancelable(true);
//                            builder1.setNeutralButton("ขอบคุณ",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            builder1.show();
                        }
                    });
                    Glide.with(PlaceInfo.this)
                            .load(QuestioHelper.getImgLink(placeDetail.getImageUrl()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(placeInfoPicture);
                } else {
                    Log.d(LOG_TAG, "Place details is null");
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getStackTrace());
            }
        });
    }
}
