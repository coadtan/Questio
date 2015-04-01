package com.questio.projects.questio.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Spinner;

import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.FloorSpinnerAdapter;
import com.questio.projects.questio.adepters.ZoneListAdapter;
import com.questio.projects.questio.models.Floor;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;


/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class QuestBrowsing extends ActionBarActivity {
    private Toolbar toolbar;
    private Spinner floorSpinner;
    private FloorSpinnerAdapter floorSpinnerAdapter;
    private ArrayList<Floor> floors;
    private Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_browsing);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        place = (Place) getIntent().getSerializableExtra("place");
        floors = Floor.getAllFloorByPlaceId(place.getPlaceId());
        floorSpinnerAdapter = new FloorSpinnerAdapter(this, R.layout.spinner_item_list, floors);
        floorSpinner = (Spinner) findViewById(R.id.quest_browsing_spinner);
        floorSpinner.setAdapter(floorSpinnerAdapter);
        ArrayList<Zone> zones = Zone.getAllZoneByFoorId(1);
        ZoneListAdapter adapter = new ZoneListAdapter(this, zones);
        ListView listView = (ListView) findViewById(R.id.quest_browsing_zone_list);
        listView.setAdapter(adapter);

    }
}
