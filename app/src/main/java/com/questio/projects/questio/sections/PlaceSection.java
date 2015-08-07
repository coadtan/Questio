package com.questio.projects.questio.sections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.PlaceActivity;
import com.questio.projects.questio.activities.ZoneActivity;
import com.questio.projects.questio.adepters.PlaceListAdapter;
import com.questio.projects.questio.libraries.AndroidGoogleDirectionAndPlaceLibrary.AndroidGoogleDirectionAndPlaceLibrary.GoogleDirection;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import net.sourceforge.zbar.Symbol;

import org.w3c.dom.Document;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PlaceSection extends Fragment implements LocationListener, GoogleMap.OnCameraChangeListener {
    private static final String LOG_TAG = PlaceSection.class.getSimpleName();
    Context mContext;
    Place place;
    Location location;
    LocationManager locationManager;
    Boolean isGPSEnabled;
    Boolean isNetworkEnabled;
    Boolean canGetLocation;
    final long MIN_TIME_BW_UPDATES = 10000;
    final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    double currentLat = 0;
    double currentLng = 0;
    Reward reward;
    long adventurerId;

    @Bind(R.id.map)
    MapView mMapView;

    @Bind(R.id.enter_place)
    Button enterPlaceBtn;

    @Bind(R.id.listview_place)
    ListView listViewPlace;

    View rootView;
    GoogleMap googleMap;
    ArrayList<Place> placeListForDistance;
    Marker mMarker;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    QuestioAPIService api;
    RestAdapter adapter;


    Runnable runnable;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        place = new Place(mContext);
        placeListForDistance = place.getAllPlaceArrayList();
        location = getLocation();
        setHasOptionsMenu(true);
        //editor = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        prefs = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        editor = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                enterPlaceBtn.setVisibility(View.GONE);
                location = null;
            }
        };
        adapter = new RestAdapter.Builder().setEndpoint(QuestioConstants.ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.section_place, container, false);
        Bundle args = getArguments();
        ButterKnife.bind(this, rootView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(mContext.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        LatLng coordinate = new LatLng(currentLat, currentLng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraChangeListener(this);

        Cursor cursor = place.getAllPlacesCursor();

        PlaceListAdapter placeListAdapter = new PlaceListAdapter(mContext, cursor, 0);
        listViewPlace.setAdapter(placeListAdapter);
        listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvLat = ButterKnife.findById(view, R.id.placeLat);
                TextView tvLng = ButterKnife.findById(view, R.id.placeLng);
                LatLng fromPosition = new LatLng(currentLat, currentLng);
                LatLng toPosition = new LatLng(Double.parseDouble(tvLat.getText().toString()), Double.parseDouble(tvLng.getText().toString()));
                googleMap.clear();
                final ArrayList<Marker> markers = new ArrayList<>();
                Marker m1 = googleMap.addMarker(new MarkerOptions().position(fromPosition).title("คุณอยู่นี่").snippet("ชื่อตัวละคร").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                Marker m2 = googleMap.addMarker(new MarkerOptions().position(toPosition).title("Destination"));
                markers.add(m1);
                markers.add(m2);
                GoogleDirection gd = new GoogleDirection(mContext);
                gd.request(fromPosition, toPosition, GoogleDirection.MODE_DRIVING);
                gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
                    public void onResponse(String status, Document doc, GoogleDirection gd) {
                        googleMap.addPolyline(gd.getPolyline(doc, 3, Color.YELLOW));
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 64; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        googleMap.animateCamera(cu);
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(LOG_TAG, "getLocation(): Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            currentLat = location.getLatitude();
                            currentLng = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(LOG_TAG, "getLocation(): GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                currentLat = location.getLatitude();
                                currentLng = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void isEnterQuestMap(double currentLat, double currentLng, final Place p) {

        float[] results = new float[1];
        Location.distanceBetween(currentLat, currentLng,
                p.getLatitude(), p.getLongitude(), results);

        if (results[0] <= p.getRadius()) {

            int placeIDFromSharedPreferences = prefs.getInt(QuestioConstants.CURRENT_PLACE, 0);
            //long timeFromSharedPreferences = prefs.getLong(QuestioConstants.CURRENT_PLACE_TIMEOUT, 0);

            Log.d(LOG_TAG, "isEnterQuestMap: placeIDFromSharedPreferences = " + placeIDFromSharedPreferences);
            //Log.d(LOG_TAG,"isEnterQuestMap: timeFromSharedPreferences = "+ timeFromSharedPreferences);
            Log.d(LOG_TAG, "isEnterQuestMap: p.getPlaceId() = " + p.getPlaceId());
            if (placeIDFromSharedPreferences != p.getPlaceId()) {
                //QuestioHelper.isTimeDifferentLessThan3Hours(timeFromSharedPreferences, QuestioHelper.getTimeNow())
                editor.putInt(QuestioConstants.CURRENT_PLACE, p.getPlaceId());
                //editor.putLong(QuestioConstants.CURRENT_PLACE_TIMEOUT, QuestioHelper.getTimeNow());
                editor.apply();
                new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("เข้าสู่ " + p.getPlaceName() + "!")
                        .setMessage("ยืนยันการเข้าสู่สถานที่แห่งนี้หรือไม่ครับ")
                        .setPositiveButton("ยืนยัน!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                                        Intent intent = new Intent(mContext, PlaceActivity.class);
                                                        intent.putExtra("place", p);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    Log.d(LOG_TAG, "checkRewardData: failure");
                                                }
                                            });


                                        } else {
                                            Intent intent = new Intent(mContext, PlaceActivity.class);
                                            intent.putExtra("place", p);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                    }
                                });

                            }

                        })
                        .setNegativeButton("ไม่", null)
                        .show();
            } else {
                enterPlaceBtn.setVisibility(View.VISIBLE);
                enterPlaceBtn.setText("ENTER TO " + p.getPlaceName());
                enterPlaceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                Intent intent = new Intent(mContext, PlaceActivity.class);
                                                intent.putExtra("place", p);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.d(LOG_TAG, "checkRewardData: failure");
                                        }
                                    });


                                } else {
                                    Intent intent = new Intent(mContext, PlaceActivity.class);
                                    intent.putExtra("place", p);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });

                    }
                });
                handler.postDelayed(runnable, 3 * 60 * 60 * 1000);
            }


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "onLocationChanged: LocationChanged called!");
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        if (!placeListForDistance.isEmpty()) {
            for (Place po : placeListForDistance) {
                isEnterQuestMap(currentLat, currentLng, po);
            }
        }

        if (mMarker != null) {
            mMarker.remove();
        }
        LatLng coordinate = new LatLng(currentLat, currentLng);
        mMarker = googleMap.addMarker(new MarkerOptions().position(coordinate).title("คุณอยู่นี่").snippet("ชื่อตัวละคร").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Log.d(LOG_TAG, coordinate + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_place_section, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_kmutt_location:
                currentLat = 13.652621;
                currentLng = 100.493640;
                if (!placeListForDistance.isEmpty()) {
                    for (Place po : placeListForDistance) {
                        isEnterQuestMap(currentLat, currentLng, po);
                    }
                }
                return true;

            case R.id.action_delect_all_data:
                place.deleteAllPlace();
                return true;

            case R.id.action_qrcode_scan:
                Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_enter_zone68001:
                String[] qr = {"zone", "68001"};
                if (qr[0].equalsIgnoreCase("zone")) {
                    Intent intentToQuestAction = new Intent(mContext, ZoneActivity.class);
                    intentToQuestAction.putExtra("qrcode", qr[1]);
                    startActivity(intentToQuestAction);
                }
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode: " + requestCode);
        Log.d(LOG_TAG, "Activity.RESULT_OK: " + Activity.RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {
            // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
            // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
            //
            // Toast.makeText(getActivity(), "Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0), Toast.LENGTH_SHORT).show();
            // The value of type indicates one of the symbols listed in Advanced Options below.
            String[] qr = QuestioHelper.getDeQRCode(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            if (qr[0].equalsIgnoreCase(QuestioConstants.QRTYPE_ZONE)) {
                Intent intent = new Intent(getActivity(), ZoneActivity.class);
                intent.putExtra("qrcode", qr[1]);
                startActivity(intent);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void showObtainRewardDialog(int rank, final Place p) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reward_obtain_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        ImageView rewardPicture = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_picture);
        TextView tvRewardName = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_name);
        TextView tvRewardRank = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_rank);
        Button closeBtn = ButterKnife.findById(dialog, R.id.button_obtain_reward_close);

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
                    .bitmapTransform(new SepiaFilterTransformation(mContext, Glide.get(mContext).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_SILVER) {
            rewardRank = "ระดับเงิน";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(mContext).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_GOLD) {
            rewardRank = "ระดับทอง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new BrightnessFilterTransformation(mContext, Glide.get(mContext).getBitmapPool(), 0.5f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }

        tvRewardRank.setText(rewardRank);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRewardHOF(reward.getRewardId(), QuestioConstants.REWARD_RANK_NORMAL);
                Intent intent = new Intent(mContext, PlaceActivity.class);
                intent.putExtra("place", p);
                startActivity(intent);
                dialog.cancel();
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
}
