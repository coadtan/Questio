package com.questio.projects.questio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;

import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.FloorSpinnerAdapter;
import com.questio.projects.questio.adepters.QuestRecycleViewAdapter;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Floor;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Quest;

import net.sourceforge.zbar.Symbol;

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
    private static final String LOG_TAG = QuestBrowsing.class.getSimpleName();
    private RecyclerView questBrowsingRecyclerView;
    private QuestRecycleViewAdapter adapterRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_browsing);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        place = (Place) getIntent().getSerializableExtra("place");
        if(place!=null) {
            Log.d(LOG_TAG, Integer.toString(place.getPlaceId()));
            ArrayList<Quest> quests = Quest.getAllQuestByPlaceId(place.getPlaceId());
            if (quests != null) {
                questBrowsingRecyclerView = (RecyclerView) findViewById(R.id.quest_browsing_recycler_view);
                questBrowsingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapterRecycleView = new QuestRecycleViewAdapter(QuestBrowsing.this, quests);
                questBrowsingRecyclerView.setAdapter(adapterRecycleView);
            }else{
                Log.d(LOG_TAG,"quests: is null");
            }
        }else{
            Log.d(LOG_TAG,"place is null sad");
        }



//        floors = Floor.getAllFloorByPlaceId(place.getPlaceId());
//        floorSpinnerAdapter = new FloorSpinnerAdapter(this, R.layout.spinner_item_list, floors);
//        floorSpinner = (Spinner) findViewById(R.id.quest_browsing_spinner);
//        floorSpinner.setAdapter(floorSpinnerAdapter);
//        ArrayList<Zone> zones = Zone.getAllZoneByFoorId(1);
//        ZoneListAdapter adapter = new ZoneListAdapter(this, zones);
//        ListView listView = (ListView) findViewById(R.id.quest_browsing_zone_list);
//        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
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
}
