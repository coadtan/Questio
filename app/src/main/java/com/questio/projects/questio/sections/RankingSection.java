package com.questio.projects.questio.sections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.ZoneActivity;
import com.questio.projects.questio.adepters.RankingAdapter;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Ranking;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

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
    private Typeface tf;
    public static GoogleApiClient mGoogleApiClient;
    Person currentPerson;


    @Bind(R.id.ranking)
    ListView ranking;

    @Bind(R.id.self_rank_no)
    TextView selfRankNumber;

    @Bind(R.id.self_rank_name)
    TextView selfRankName;

    @Bind(R.id.self_rank_score)
    TextView selfRankScore;

    @Bind(R.id.self_rank_image)
    ImageView selfRankImage;


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
        currentPerson = Plus.PeopleApi
                .getCurrentPerson(QuestioApplication.mGoogleApiClient);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_ranking, container, false);
        ButterKnife.bind(this, view);
        tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/CSPraJad.otf");
        requestRankingData(adventurerId);
        getCurrentAdventurerRank();

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

    public void getCurrentAdventurerRank(){
        api.getCurrentRankByAdventurerId(adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Ranking[]>() {
            @Override
            public void success(Ranking[] rankings, Response response) {
                if(rankings[0] != null){
                    Ranking ranking = rankings[0];
                    selfRankNumber.setText(Integer.toString(ranking.getRank()));
                    selfRankNumber.setTypeface(tf);
                    selfRankName.setText(ranking.getDisplayName());
                    selfRankName.setTypeface(tf);
                    selfRankScore.setText(Integer.toString(ranking.getScore()));
                    selfRankScore.setTypeface(tf);
                    if (QuestioApplication.mGoogleApiClient.isConnected()) {
                        Glide.with(mContext)
                                .load(currentPerson.getImage().getUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(selfRankImage);
                    }
                }else{
                    Log.d(LOG_TAG, "current ranking is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}