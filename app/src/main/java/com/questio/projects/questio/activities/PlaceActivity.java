package com.questio.projects.questio.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.fragments.QuestRecycleView;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import net.sourceforge.zbar.Symbol;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class PlaceActivity extends ActionBarActivity {
    Toolbar toolbar;
    Spinner buildingSpinner;
    Spinner floorSpinner;
    Spinner zoneSpinner;
    Button placeBtnTrigerFilter;
    Button placeBtnTrigerMap;
    private Place place;
    private static final String LOG_TAG = PlaceActivity.class.getSimpleName();
    private ArrayList<Quest> quests;
    ImageView quest_browsing_picture;
    LinearLayout place_activity_filter;
    boolean isFilterVisable = false;
    FrameLayout quest_browsing_top_frame;
    boolean isMapVisable = true;

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_browsing);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        quest_browsing_picture = (ImageView) findViewById(R.id.quest_browsing_picture);
        buildingSpinner = (Spinner) findViewById(R.id.place_building_spinner);
        floorSpinner = (Spinner) findViewById(R.id.place_floor_spinner);
        zoneSpinner = (Spinner) findViewById(R.id.place_zone_spinner);
        placeBtnTrigerFilter = (Button) findViewById(R.id.place_btn_triger_filter);
        place_activity_filter = (LinearLayout) findViewById(R.id.place_activity_filter);
        placeBtnTrigerMap = (Button) findViewById(R.id.place_btn_triger_map);
        quest_browsing_top_frame = (FrameLayout) findViewById(R.id.quest_browsing_top_frame);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/THSarabunNew.ttf");
        placeBtnTrigerFilter.setTypeface(custom_font);
        placeBtnTrigerMap.setTypeface(custom_font);

        place = (Place) getIntent().getSerializableExtra("place");
        place_activity_filter.setVisibility(View.GONE);
        placeBtnTrigerFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                if (isFilterVisable) {
                    isFilterVisable = false;

                    placeBtnTrigerFilter.setText("แสดงคัดกรอง ภารกิจ");
                    place_activity_filter.setVisibility(View.GONE);
                } else {
                    isFilterVisable = true;
                    placeBtnTrigerFilter.setText("ซ่อนคัดกรอง ภารกิจ");
                    place_activity_filter.setVisibility(View.VISIBLE);
                    //   place_activity_filter.setAlpha(1.0f);
                    place_activity_filter.startAnimation(fadeIn);
                }

            }
        });

        placeBtnTrigerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                if (isMapVisable) {
                    isMapVisable = false;
                    placeBtnTrigerMap.setText("แสดง แผนที่");
                    quest_browsing_top_frame.setVisibility(View.GONE);
                } else {
                    isMapVisable = true;
                    placeBtnTrigerMap.setText("ซ่อน แผนที่");
                    quest_browsing_top_frame.setVisibility(View.VISIBLE);
                    //   place_activity_filter.setAlpha(1.0f);
                    quest_browsing_top_frame.startAnimation(fadeIn);
                }
            }
        });
        if (place != null) {
            Log.d(LOG_TAG, Integer.toString(place.getPlaceId()) + place.getImageUrl());
            Glide.with(this)
                    .load("http://52.74.64.61" + place.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(quest_browsing_picture);

            requestQuestData(place.getPlaceId());

        } else {
            Log.d(LOG_TAG, "place: is null");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_quest_browsing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_place_details:
                Intent intentDetail = new Intent(this, PlaceInfo.class);
                intentDetail.putExtra("place", place);
                startActivity(intentDetail);
                return true;
            case R.id.action_qrcode_scan:
                Intent intent = new Intent(this, ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);
                return true;

            default:
                break;
        }
        return false;
    }

    private void requestQuestData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getQuestsByPlaceId(id, new Callback<ArrayList<Quest>>() {
            @Override
            public void success(final ArrayList<Quest> quests, Response response) {
                if (quests != null) {
                    setQuests(quests);

                    final String[] buildingNames = Quest.getBuildingNamesArray("building", quests);
                    final String[] floorNames = Quest.getBuildingNamesArray("floor", quests);
                    String[] zoneNames = Quest.getBuildingNamesArray("zone", quests);

                    ArrayAdapter<String> adapterBuilding = new ArrayAdapter<>(PlaceActivity.this,
                            R.layout.spinner_item_list, buildingNames);
                    adapterBuilding.setDropDownViewResource(R.layout.spinner_item_list);

                    final ArrayAdapter<String> adapterFloor = new ArrayAdapter<>(PlaceActivity.this,
                            R.layout.spinner_item_list, floorNames);
                    adapterBuilding.setDropDownViewResource(R.layout.spinner_item_list);

                    final ArrayAdapter<String> adapterZone = new ArrayAdapter<>(PlaceActivity.this,
                            R.layout.spinner_item_list, zoneNames);
                    adapterBuilding.setDropDownViewResource(R.layout.spinner_item_list);

                    final HashSet<String> floorFilter = new HashSet<>();
                    final HashSet<String> zoneFilter = new HashSet<>();

                    buildingSpinner.setAdapter(adapterBuilding);
                    floorSpinner.setAdapter(adapterFloor);
                    zoneSpinner.setAdapter(adapterZone);

                    buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String buildingItem = buildingSpinner.getSelectedItem().toString();
                            if (!buildingItem.equalsIgnoreCase(" ")) {
                                floorFilter.clear();
                                for (Quest q : quests) {
                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                        floorFilter.add(q.getFloorName());
                                    }
                                }
                                String[] floorNameFilter = floorFilter.toArray(new String[floorFilter.size() + 1]);
                                floorNameFilter[floorFilter.size()] = " ";
                                ArrayAdapter<String> adapterFloorFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                        R.layout.spinner_item_list, floorNameFilter);
                                floorSpinner.setAdapter(adapterFloorFiltered);

                                floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        String floorItem = floorSpinner.getSelectedItem().toString();
                                        if (!floorItem.equalsIgnoreCase(" ")) {
                                            zoneFilter.clear();
                                            for (Quest q : quests) {
                                                if (q.getFloorName().equalsIgnoreCase(floorItem)) {
                                                    zoneFilter.add(q.getZoneName());
                                                }
                                            }
                                            String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                            zoneNameFilter[zoneFilter.size()] = " ";
                                            ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                    R.layout.spinner_item_list, zoneNameFilter);
                                            zoneSpinner.setAdapter(adapterZoneFiltered);
                                        }else{
                                            String buildingItem = buildingSpinner.getSelectedItem().toString();
                                            if (!buildingItem.equalsIgnoreCase(" ")){
                                                zoneFilter.clear();
                                                for (Quest q : quests) {
                                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                                        zoneFilter.add(q.getZoneName());
                                                    }
                                                }
                                                String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                                zoneNameFilter[zoneFilter.size()] = " ";
                                                ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                        R.layout.spinner_item_list, zoneNameFilter);
                                                zoneSpinner.setAdapter(adapterZoneFiltered);
                                            }else{
                                                zoneFilter.clear();
                                                zoneSpinner.setAdapter(adapterZone);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }else{

                                floorFilter.clear();
                                floorSpinner.setAdapter(adapterFloor);
                                floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        String floorItem = floorSpinner.getSelectedItem().toString();
                                        if (!floorItem.equalsIgnoreCase(" ")) {
                                            zoneFilter.clear();
                                            for (Quest q : quests) {
                                                if (q.getFloorName().equalsIgnoreCase(floorItem)) {
                                                    zoneFilter.add(q.getZoneName());
                                                }
                                            }
                                            String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                            zoneNameFilter[zoneFilter.size()] = " ";
                                            ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                    R.layout.spinner_item_list, zoneNameFilter);
                                            zoneSpinner.setAdapter(adapterZoneFiltered);
                                        }else{
                                            String buildingItem = buildingSpinner.getSelectedItem().toString();
                                            if (!buildingItem.equalsIgnoreCase(" ")){
                                                zoneFilter.clear();
                                                for (Quest q : quests) {
                                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                                        zoneFilter.add(q.getZoneName());
                                                    }
                                                }
                                                String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                                zoneNameFilter[zoneFilter.size()] = " ";
                                                ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                        R.layout.spinner_item_list, zoneNameFilter);
                                                zoneSpinner.setAdapter(adapterZoneFiltered);
                                            }else{
                                                zoneFilter.clear();
                                                zoneSpinner.setAdapter(adapterZone);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    QuestRecycleView fragment = new QuestRecycleView();
                    transaction.replace(R.id.quest_browsing_null, fragment);
                    transaction.commit();
                } else {
                    Log.d(LOG_TAG, "quests: is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuestData: failure");
            }
        });
    }
}
