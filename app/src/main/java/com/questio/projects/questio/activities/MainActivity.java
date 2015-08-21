package com.questio.projects.questio.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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
    private GoogleApiClient googleApiClient;
    public LocationRequest locationRequest;
    // Estimote zone
    private static final String ESTIMOTE_PROXIMITY_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, 28521, 47387);
    private BeaconManager beaconManager = new BeaconManager(this);
    static final Region region = new Region("myRegion", ESTIMOTE_PROXIMITY_UUID, 28521, 47387);
    public Location location;
    SharedPreferences prefs;
    QuestioAPIService api;
    RestAdapter adapter;
    Reward reward;
    long adventurerId;
    Place place;
    ArrayList<Place> placeListForDistance;

    @Bind(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Estimote zone
        if (beaconManager.hasBluetooth()) {
            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                    Log.d(LOG_TAG, "Ranged beacons: " + beacons);
                }
            });

            try {
                beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                    @Override
                    public void onEnteredRegion(Region region, List<Beacon> beacons) {
                        Beacon beacon = beacons.get(0);
                        Log.d("Beacon", beacon.getMajor() + ": " + beacon.getMinor());
                    }

                    @Override
                    public void onExitedRegion(Region region) {
                        Log.d("Beacon", "exit Region");
                    }
                });
                beaconManager.startMonitoring(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        buildGoogleApiClient();
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
        // Should be invoked in #onStart.
        if (beaconManager.hasBluetooth()) {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "Cannot start ranging", e);
                    }
                }
            });

        }
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Should be invoked in #onStop.
        if (beaconManager.hasBluetooth()) {
            try {
                beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Cannot stop but it does not matter now", e);
            }

        }
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconManager.hasBluetooth()) {
            beaconManager.disconnect();
        }
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
        if (locationAvailability.isLocationAvailable()) {
            locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(QuestioConstants.LOCATION_INTERVAL_TIME_IN_MILLISEC);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient,
                    locationRequest,
                    this
            );
        } else {
            Log.d(LOG_TAG, "locationAvailability: false");
        }
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
//            final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(this);
//            dialog
//                    .withTitle("เข้าสู่ " + p.getPlaceName() + "!")
//                    .withIcon(android.R.drawable.ic_dialog_info)
//                    .withTitleColor("#FFFFFF")
//                    .withDividerColor("#11000000")
//                    .withMessage("ยืนยันการเข้าสู่สถานที่แห่งนี้หรือไม่ครับ")
//                    .withMessageColor("#FFFFFFFF")
//                    .withDialogColor("#FFE74C3C")
//                    .withDuration(300)
//                    .withEffect(Effectstype.Slidetop)
//                    .withButton1Text("ยืนยัน!")
//                    .setButton1Click(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    })
//                    .withButton2Text("ไม่")
//                    .setButton2Click(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .isCancelableOnTouchOutside(true);
//            dialog.show();

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
                                    showObtainRewardDialog(QuestioConstants.REWARD_RANK_NORMAL, p);
                                } else {
                                    insertExplorerProgress(p);
                                }
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
            Intent intent = new Intent(this, PlaceActivity.class);
            intent.putExtra("place", p);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(PlaceActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Enter new Place!")
                            .setContentText("You enter " + p.getPlaceFullName())
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1000, notification);
        }
    }

    void showObtainRewardDialog(int rank, final Place p) {
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
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.reward_obtain_dialog);
//        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
//        dialog.setCancelable(true);
        ImageView rewardPicture = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_picture);
        TextView tvRewardName = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_name);
        TextView tvRewardRank = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_rank);
        //Button closeBtn = ButterKnife.findById(dialog, R.id.button_obtain_reward_close);

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
                insertExplorerProgress(p);
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
                if (!zones.isEmpty()) {
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
}
