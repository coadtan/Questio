package com.questio.projects.questio.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.slidingtabs.SlidingTabsBasicFragment;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.models.Zone;
import com.questio.projects.questio.utilities.HttpHelper;
import com.questio.projects.questio.utilities.PlaceSync;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static GoogleApiClient googleApiClient;
    public static LocationRequest locationRequest;

    public Location location;
    static SharedPreferences prefs;
    static QuestioAPIService api;
    RestAdapter adapter;
    Reward reward;
    long adventurerId;
    Place place;
    ArrayList<Place> placeListForDistance;
    static int zoneCount;
    //    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static Context context;

    @Bind(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        String displayName = prefs.getString(QuestioConstants.ADVENTURER_DISPLAYNAME, null);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        long id = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        Log.d(LOG_TAG, "displayName: " + displayName + " id: " + id);
        adapter = new RestAdapter.Builder().setEndpoint(QuestioConstants.ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        place = new Place(this);
        placeListForDistance = place.getAllPlaceArrayList();
        Place place = new Place(getApplicationContext());
        try {
            String res = new HttpHelper().execute("http://52.74.64.61/api/select_all_place_count.php").get();

            Log.d(LOG_TAG, "count: " + res);
            long placeServerCount = QuestioHelper.getPlaceCountFromJson(res);
            long placeSQLiteCount = place.getPlaceCount();
            Log.d(LOG_TAG, "placeServerCount: " + placeServerCount + " placeSQLiteCount: " + placeSQLiteCount);
            if (placeServerCount != placeSQLiteCount) {
                place.deleteAllPlace();
                new PlaceSync(getApplicationContext()).execute("http://52.74.64.61/api/select_all_place.php");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        Log.d(LOG_TAG, "count: " + place.getPlaceCount());


        editor = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE).edit();
        buildGoogleApiClient();
        Log.d(LOG_TAG, "Default Place ID: " + prefs.getInt(QuestioConstants.PLACE_ID, 0));
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt(QuestioConstants.PLACE_ID, 0);
        editor.apply();
        locationRequest.setInterval(QuestioConstants.DEFAULT_LOCATION_INTERVAL_TIME);

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putInt(QuestioConstants.PLACE_ID, 0);
        editor.apply();

    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        /**  Open GPS setting */
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Log.d(LOG_TAG, "gps: false");
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//        }
//        if (locationAvailability.isLocationAvailable()) {
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(QuestioConstants.DEFAULT_LOCATION_INTERVAL_TIME);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this
        );
//        } else {
//            Log.d(LOG_TAG, "locationAvailability: false");
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d(LOG_TAG, "This is the Google location service " + location.getLatitude() + " " + location.getLongitude());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        Intent intent = new Intent(QuestioConstants.LOCATION_UPDATE_ACTION);
        intent.putExtra("currentLatitude", currentLatitude);
        intent.putExtra("currentLongitude", currentLongitude);
        this.sendBroadcast(intent);
//        Log.d(LOG_TAG, "Current Place ID: " + sharedPreferences.getInt(QuestioConstants.PLACE_ID, 0));
//        Log.d(LOG_TAG, "Current Place Interval: " + locationRequest.getInterval());

        if (!placeListForDistance.isEmpty()) {
            for (Place po : placeListForDistance) {
                isEnterQuestMap(currentLatitude, currentLongitude, po);
            }
        }
    }

    public void isEnterQuestMap(double currentLat, double currentLng, final Place p) {

        float[] results = new float[1];
        Location.distanceBetween(currentLat, currentLng,
                p.getLatitude(), p.getLongitude(), results);
        if (results[0] <= p.getRadius()) {

            Log.d(LOG_TAG, "isEnterQuestMap: p.getPlaceId() = " + p.getPlaceId());

            api.getRewardByPlaceId(p.getPlaceId(), new Callback<Reward[]>() {
                @Override
                public void success(Reward[] rewards, Response response) {
                    if (rewards != null) {
                        reward = rewards[0];
                        api.getCountHOFByAdventurerIdAndRewardId(adventurerId, reward.getRewardId(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                int rewardCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hofcount", response));
                                Log.d(LOG_TAG, "Reward count: " + rewardCount);
                                if (rewardCount == 0) {
                                    showObtainRewardDialog(QuestioConstants.REWARD_RANK_NORMAL);
                                }
                                insertExplorerProgress(p);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(LOG_TAG, "checkRewardData: failure");
                            }
                        });
                    } else {
                        insertExplorerProgress(p);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            if (prefs.getInt(QuestioConstants.PLACE_ID, 0) != p.getPlaceId()) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.custom_place_notification);
                Intent intent = new Intent(this, EnterPlace.class);
                intent.putExtra("place", p);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_icon_quest)
                                .setAutoCancel(true)
                                .setContent(remoteViews)
                                .build();
                remoteViews.setTextViewText(R.id.enter_place_text, p.getPlaceFullName());
                remoteViews.setOnClickPendingIntent(R.id.enter_place_notification, pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1000, notification);
            }
        } else {
            editor.putInt(QuestioConstants.PLACE_ID, 0);
            editor.apply();
            locationRequest.setInterval(QuestioConstants.DEFAULT_LOCATION_INTERVAL_TIME);
        }

    }

    void showObtainRewardDialog(int rank) {
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(this);
        dialog
                .withTitle("Obtain Reward")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("You got reward:")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("Close")
                .isCancelableOnTouchOutside(false)
                .setCustomView(R.layout.reward_obtain_dialog, this);
        ImageView rewardPicture = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_picture);
        TextView tvRewardName = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_name);
        TextView tvRewardRank = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_rank);

        String rewardName = reward.getRewardName();
        tvRewardName.setText(rewardName);
        String rewardRank = "";

        if (rank == QuestioConstants.REWARD_RANK_NORMAL) {
            rewardRank = "ระดับปกติ";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_BRONZE) {
            rewardRank = "ระดับทองแดง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new SepiaFilterTransformation(this, Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_SILVER) {
            rewardRank = "ระดับเงิน";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_GOLD) {
            rewardRank = "ระดับทอง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new BrightnessFilterTransformation(this, Glide.get(this).getBitmapPool(), 0.5f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }

        tvRewardRank.setText(rewardRank);

        dialog.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRewardHOF(reward.getRewardId(), QuestioConstants.REWARD_RANK_NORMAL);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addRewardHOF(int rewardId, int rank) {
        api.addRewards(adventurerId, rewardId, rank, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void insertExplorerProgress(final Place p) {
        api.addPlaceProgress(adventurerId, p.getPlaceId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        api.getZoneByPlaceId(p.getPlaceId(), new Callback<ArrayList<Zone>>() {
            @Override
            public void success(ArrayList<Zone> zones, Response response) {
                Log.d(LOG_TAG, "explorer progress: p.getPlaceId() " + p.getPlaceId());
                if (zones == null) {
                    Log.d(LOG_TAG, "explorer progress: zone is null");
                }

                if (zones != null) {
                    for (Zone z : zones) {
                        api.addExplorerProgress(adventurerId, p.getPlaceId(), z.getZoneId(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void getZoneCount(Place p) {
        api.getCountZoneByPlaceId(p.getPlaceId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                zoneCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("zonecount", response));
                Log.d(LOG_TAG, "Zonecount = " + Integer.toString(zoneCount));
                if (zoneCount > 0) {
                    locationRequest.setInterval(zoneCount * 5 * 60 * 1000);
                }
                Log.d(LOG_TAG, "Current Place ID: " + prefs.getInt(QuestioConstants.PLACE_ID, 0));
                Log.d(LOG_TAG, "Current Place Interval: " + locationRequest.getInterval());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    public static class EnterPlace extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Place p = (Place) intent.getSerializableExtra("place");
            Log.d(LOG_TAG, "Place - " + p.toString());
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
            getZoneCount(p);
            Intent placeIntent = new Intent(context, PlaceActivity.class);
            placeIntent.putExtra("place", p);
            placeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(placeIntent);
        }
    }
}
