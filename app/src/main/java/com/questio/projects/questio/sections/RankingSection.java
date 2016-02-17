package com.questio.projects.questio.sections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.ZoneActivity;
import com.questio.projects.questio.adepters.RankingAdapter;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Ranking;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import net.sourceforge.zbar.Symbol;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RankingSection extends Fragment {
    private static final String LOG_TAG = RankingSection.class.getSimpleName();
    Context mContext;
    View view;
    RestAdapter adapter;
    QuestioAPIService api;
    ArrayList<Ranking> rankingList;
    RankingAdapter rankingAdapter;
    SharedPreferences prefs;
    long adventurerId;

    @Bind(R.id.ranking)
    ListView ranking;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        prefs = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_ranking, container, false);
        ButterKnife.bind(this, view);
        requestRankingData(adventurerId);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_place_section, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_qrcode_scan:
                Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_enter_zone68001:
                String[] qr = {"zone", "68001"};
                if (qr[0].equalsIgnoreCase("zone")) {
                    Intent intentToQuestAction = new Intent(getActivity(), ZoneActivity.class);
                    intentToQuestAction.putExtra("qrcode", qr[1]);
                    startActivity(intentToQuestAction);
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private void requestRankingData(long adventurerId){
        api.getRankingTopTen(adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<ArrayList<Ranking>>() {
            @Override
            public void success(ArrayList<Ranking> rankings, Response response) {

                if(rankings != null){
                    rankingList = rankings;
                    rankingAdapter = new RankingAdapter(mContext, rankingList);
                    ranking.setAdapter(rankingAdapter);

                }else{
                    Log.d(LOG_TAG, "ranking is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}