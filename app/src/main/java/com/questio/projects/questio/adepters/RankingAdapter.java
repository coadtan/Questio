package com.questio.projects.questio.adepters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Ranking;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.questio.projects.questio.utilities.QuestioConstants.ENDPOINT;

/**
 * Created by ning jittima on 17/2/2559.
 */
public class RankingAdapter extends BaseAdapter {
    public static final String LOG_TAG = RankingAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Ranking> rankingList;
    private Typeface tf;

    public static class ViewHolder{
        @Bind(R.id.rank_no)
        TextView rankNumber;

        @Bind(R.id.rank_name)
        TextView rankName;

        @Bind(R.id.rank_score)
        TextView rankScore;

        @Bind(R.id.rank_image)
        ImageView rankImage;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public RankingAdapter(Context context, ArrayList<Ranking> rankings){
        mContext = context;
        rankingList = rankings;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/CSPraJad.otf");
    }

    @Override
    public int getCount() {
        return rankingList.size();
    }

    @Override
    public Ranking getItem(int i) {
        return rankingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_ranking, viewGroup, false);
        }
        Ranking ranking = getItem(i);
        if(ranking != null){
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.rankNumber.setText(Integer.toString(ranking.getRank()));
            viewHolder.rankNumber.setTypeface(tf);
            viewHolder.rankName.setText(ranking.getDisplayName());
            viewHolder.rankName.setTypeface(tf);
            viewHolder.rankScore.setText(Integer.toString(ranking.getScore()));
            viewHolder.rankScore.setTypeface(tf);
            String gUserId = ranking.getgUserId();
//            Glide.with(mContext)
//                    .load(QuestioHelper.getProfileLink(gUserId))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(viewHolder.rankImage);

            //getProfileLink for update rankImage; use glide.
        }
        return view;
    }
}
