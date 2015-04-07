package com.questio.projects.questio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.FloorSpinnerAdapter;
import com.questio.projects.questio.adepters.QuestRecycleViewAdapter;
import com.questio.projects.questio.fragments.QuestRecycleView;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Floor;
import com.questio.projects.questio.models.Place;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.utilities.DownloadImageHelper;

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
    private ArrayList<Quest> quests;
    private ImageView quest_browsing_picture;
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
        quest_browsing_picture = (ImageView)findViewById(R.id.quest_browsing_picture);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Log.d(LOG_TAG, "oncreate");
        place = (Place) getIntent().getSerializableExtra("place");
        if(place!=null) {
            Log.d(LOG_TAG, Integer.toString(place.getPlaceId()) + place.getImageurl());
            new DownloadImageHelper(quest_browsing_picture).execute("http://52.74.64.61" + place.getImageurl());
            ArrayList<Quest> quests = Quest.getAllQuestByPlaceId(place.getPlaceId());
            if (quests != null) {
                setQuests(quests);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                QuestRecycleView fragment = new QuestRecycleView();
                transaction.replace(R.id.quest_browsing_null, fragment);
                transaction.commit();
            }else{
                Log.d(LOG_TAG,"quests: is null");
            }
        }else{
            Log.d(LOG_TAG,"place: is null");
        }


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
