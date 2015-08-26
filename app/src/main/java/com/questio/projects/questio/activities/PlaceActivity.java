package com.questio.projects.questio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.location.LocationRequest;
import com.questio.projects.questio.R;
import com.questio.projects.questio.fragments.QuestRecycleView;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import net.sourceforge.zbar.Symbol;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PlaceActivity extends AppCompatActivity {
    @Bind(R.id.app_bar)
    Toolbar toolbar;

    @Bind(R.id.place_building_spinner)
    Spinner buildingSpinner;

    @Bind(R.id.place_floor_spinner)
    Spinner floorSpinner;

    @Bind(R.id.place_zone_spinner)
    Spinner zoneSpinner;

    @Bind(R.id.place_btn_triger_filter)
    Button placeBtnTrigerFilter;

    @Bind(R.id.place_btn_triger_map)
    Button placeBtnTrigerMap;

    @Bind(R.id.quest_browsing_picture)
    ImageView questBrowsingPicture;

    @Bind(R.id.place_activity_filter)
    LinearLayout placeActivityFilter;

    @Bind(R.id.quest_browsing_top_frame)
    FrameLayout questBrowsingTopFrame;

    private Place place;
    private static final String LOG_TAG = PlaceActivity.class.getSimpleName();
    private ArrayList<Quest> quests;
    private ArrayList<Quest> questsTemp;
    boolean isFilterVisable = false;
    boolean isMapVisable = true;
    String buildingItem = " ";
    String floorItem = " ";
    String zoneItem = " ";
    QuestioAPIService api;
    RestAdapter adapter;
    int zoneCount;
    LocationRequest locationRequest;

    public ArrayList<Quest> getQuestsTemp() {
        return questsTemp;
    }

    public void setQuestsTemp(ArrayList<Quest> questsTemp) {
        this.questsTemp = questsTemp;
    }

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
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        placeActivityFilter.setVisibility(View.GONE);
        placeBtnTrigerFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                if (isFilterVisable) {
                    isFilterVisable = false;

                    placeBtnTrigerFilter.setText("แสดงคัดกรอง ภารกิจ");
                    placeActivityFilter.setVisibility(View.GONE);
                } else {
                    isFilterVisable = true;
                    placeBtnTrigerFilter.setText("ซ่อนคัดกรอง ภารกิจ");
                    placeActivityFilter.setVisibility(View.VISIBLE);
                    placeActivityFilter.startAnimation(fadeIn);
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
                    questBrowsingTopFrame.setVisibility(View.GONE);
                } else {
                    isMapVisable = true;
                    placeBtnTrigerMap.setText("ซ่อน แผนที่");
                    questBrowsingTopFrame.setVisibility(View.VISIBLE);
                    questBrowsingTopFrame.startAnimation(fadeIn);
                }
            }
        });
        if (place != null) {
            Log.d(LOG_TAG, Integer.toString(place.getPlaceId()) + place.getImageUrl());
            Glide.with(this)
                    .load("http://52.74.64.61" + place.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(questBrowsingPicture);

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
                            buildingItem = buildingSpinner.getSelectedItem().toString();
                            if (!buildingItem.equalsIgnoreCase(" ")) {
                                reTempArray();
                                floorFilter.clear();
                                for (Quest q : quests) {
                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                        floorFilter.add(q.getFloorName());
                                    }
                                }
                                String[] floorNameFilter = floorFilter.toArray(new String[floorFilter.size() + 1]);
                                floorNameFilter[floorFilter.size()] = " ";

                                ArrayAdapter<String> adapterFloorFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                        R.layout.spinner_item_list, QuestioHelper.moveBackToFront(floorNameFilter));
                                floorSpinner.setAdapter(adapterFloorFiltered);
                                floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        floorItem = floorSpinner.getSelectedItem().toString();

                                        if (!floorItem.equalsIgnoreCase(" ")) {
                                            reTempArray();
                                            zoneFilter.clear();
                                            for (Quest q : quests) {
                                                if (q.getFloorName().equalsIgnoreCase(floorItem)) {
                                                    zoneFilter.add(q.getZoneName());
                                                }
                                            }
                                            String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                            zoneNameFilter[zoneFilter.size()] = " ";

                                            ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                    R.layout.spinner_item_list, QuestioHelper.moveBackToFront(zoneNameFilter));
                                            zoneSpinner.setAdapter(adapterZoneFiltered);
                                            zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    zoneItem = zoneSpinner.getSelectedItem().toString();
                                                    reTempArray();
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });
                                        } else {
                                            buildingItem = buildingSpinner.getSelectedItem().toString();
                                            if (!buildingItem.equalsIgnoreCase(" ")) {
                                                reTempArray();
                                                zoneFilter.clear();
                                                for (Quest q : quests) {
                                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                                        zoneFilter.add(q.getZoneName());
                                                    }
                                                }
                                                String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                                zoneNameFilter[zoneFilter.size()] = " ";

                                                Log.d(LOG_TAG, "Zone Name Filter" + Arrays.toString(zoneNameFilter));
                                                Log.d(LOG_TAG, "Temp Zone Name Filter" + Arrays.toString(QuestioHelper.moveBackToFront(zoneNameFilter)));
                                                ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                        R.layout.spinner_item_list, QuestioHelper.moveBackToFront(zoneNameFilter));
                                                zoneSpinner.setAdapter(adapterZoneFiltered);

                                                zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                        zoneItem = zoneSpinner.getSelectedItem().toString();
                                                        reTempArray();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }
                                                });
                                            } else {
                                                reTempArray();
                                                zoneFilter.clear();
                                                zoneSpinner.setAdapter(adapterZone);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            } else {
                                reTempArray();
                                floorFilter.clear();
                                floorSpinner.setAdapter(adapterFloor);
                                floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        floorItem = floorSpinner.getSelectedItem().toString();
                                        if (!floorItem.equalsIgnoreCase(" ")) {
                                            reTempArray();
                                            zoneFilter.clear();
                                            for (Quest q : quests) {
                                                if (q.getFloorName().equalsIgnoreCase(floorItem)) {
                                                    zoneFilter.add(q.getZoneName());
                                                }
                                            }
                                            String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                            zoneNameFilter[zoneFilter.size()] = " ";

                                            ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                    R.layout.spinner_item_list, QuestioHelper.moveBackToFront(zoneNameFilter));
                                            zoneSpinner.setAdapter(adapterZoneFiltered);

                                            zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    zoneItem = zoneSpinner.getSelectedItem().toString();
                                                    reTempArray();
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });
                                        } else {
                                            buildingItem = buildingSpinner.getSelectedItem().toString();
                                            if (!buildingItem.equalsIgnoreCase(" ")) {
                                                reTempArray();
                                                zoneFilter.clear();
                                                for (Quest q : quests) {
                                                    if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                                                        zoneFilter.add(q.getZoneName());
                                                    }
                                                }
                                                String[] zoneNameFilter = zoneFilter.toArray(new String[zoneFilter.size() + 1]);
                                                zoneNameFilter[zoneFilter.size()] = " ";

                                                ArrayAdapter<String> adapterZoneFiltered = new ArrayAdapter<>(PlaceActivity.this,
                                                        R.layout.spinner_item_list, QuestioHelper.moveBackToFront(zoneNameFilter));
                                                zoneSpinner.setAdapter(adapterZoneFiltered);

                                                zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        zoneItem = zoneSpinner.getSelectedItem().toString();
                                                        reTempArray();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } else {
                                                reTempArray();
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

    private void reTempArray() {
        questsTemp = new ArrayList<>();
        Iterator<Quest> iter = quests.iterator();
        Log.d(LOG_TAG, "buildingItem: " + buildingItem);
        Log.d(LOG_TAG, "floorItem: " + floorItem);
        Log.d(LOG_TAG, "zoneItem: " + zoneItem);

        if (buildingItem.equalsIgnoreCase(" ")) {
            if (floorItem.equalsIgnoreCase(" ")) {
                if (zoneItem.equalsIgnoreCase(" ")) {
                    //All Empty
                    questsTemp = quests;
                } else {
                    //Zone not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getZoneName().equalsIgnoreCase(zoneItem)) {
                            questsTemp.add(q);
                        }
                    }
                }
            } else {
                if (zoneItem.equalsIgnoreCase(" ")) {
                    //Floor not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getFloorName().equalsIgnoreCase(floorItem)) {
                            questsTemp.add(q);
                        }
                    }
                } else {
                    //Zone and Floor not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getZoneName().equalsIgnoreCase(zoneItem) && q.getFloorName().equalsIgnoreCase(floorItem)) {
                            questsTemp.add(q);
                        }
                    }
                }
            }
        } else {
            if (floorItem.equalsIgnoreCase(" ")) {
                if (zoneItem.equalsIgnoreCase(" ")) {
                    //Building not Empty

                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                            questsTemp.add(q);
                        }
                    }
                } else {
                    //Building and Zone not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getBuildingName().equalsIgnoreCase(buildingItem) && q.getZoneName().equalsIgnoreCase(zoneItem)) {
                            questsTemp.add(q);
                        }
                    }
                }
            } else {
                if (zoneItem.equalsIgnoreCase(" ")) {
                    //Building and Floor not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getBuildingName().equalsIgnoreCase(buildingItem) && q.getFloorName().equalsIgnoreCase(floorItem)) {
                            questsTemp.add(q);
                        }
                    }
                } else {
                    //All not Empty
                    while (iter.hasNext()) {
                        Quest q = iter.next();
                        if (q.getZoneName().equalsIgnoreCase(zoneItem) && q.getFloorName().equalsIgnoreCase(floorItem) && q.getBuildingName().equalsIgnoreCase(buildingItem)) {
                            questsTemp.add(q);
                        }
                    }
                }
            }
        }

        // END OF CREATE NEW TEMP QUESTS
        QuestRecycleView fragment = (QuestRecycleView) getSupportFragmentManager().findFragmentById(R.id.quest_browsing_null);
        fragment.reCreateRecyclerView();
    }


}
