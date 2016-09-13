package com.questio.projects.questio.sections;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.RankingAdapter;
import com.questio.projects.questio.models.Ranking;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.util.ArrayList;

import butterknife.BindView;
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
    Person currentPerson;
    @BindView(R.id.ranking)
    ListView ranking;
    @BindView(R.id.self_rank_no)
    TextView selfRankNumber;
    @BindView(R.id.self_rank_name)
    TextView selfRankName;
    @BindView(R.id.self_rank_score)
    TextView selfRankScore;
    @BindView(R.id.self_rank_image)
    ImageView selfRankImage;
    private Typeface tf;

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

    private void requestRankingData(long adventurerId) {
        api.getRankingTopTen(adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<ArrayList<Ranking>>() {
            @Override
            public void success(ArrayList<Ranking> rankings, Response response) {

                if (rankings != null) {
                    rankingList = rankings;
                    rankingAdapter = new RankingAdapter(mContext, rankingList);
                    ranking.setAdapter(rankingAdapter);

                } else {
                    Log.d(LOG_TAG, "ranking is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getCurrentAdventurerRank() {
        Log.d(LOG_TAG, "adventurerId: " + adventurerId);
        api.getCurrentRankByAdventurerId(adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Ranking[]>() {
            @Override
            public void success(Ranking[] rankings, Response response) {
                if (null != rankings) {
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
                } else {
                    Log.d(LOG_TAG, "current ranking is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}