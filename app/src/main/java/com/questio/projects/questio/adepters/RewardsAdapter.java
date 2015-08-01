package com.questio.projects.questio.adepters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.models.RewardHOF;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;

public class RewardsAdapter extends BaseAdapter {
    public static final String LOG_TAG = RewardsAdapter.class.getSimpleName();
    private Context mContext;
    ArrayList<RewardHOF> rewardHOFsList;

    private static class ViewHolder {
        private ImageView rewardsImage;

        public ViewHolder(View view) {
            rewardsImage = (ImageView) view.findViewById(R.id.reward_hof);
        }
    }

    public RewardsAdapter(Context context, ArrayList<RewardHOF> rewards) {
        rewardHOFsList = rewards;
        mContext = context;
    }

    @Override
    public int getCount() {
        return rewardHOFsList.size();
    }

    @Override
    public RewardHOF getItem(int position) {
        return rewardHOFsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RewardHOF reward = getItem(position);
        Log.d(LOG_TAG, reward.toString());
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_hof, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);

        if(reward.getRankId() == QuestioConstants.REWARD_RANK_BRONZE){
            Glide.with(mContext)
                    .load(QuestioHelper.getImgLink(reward.getRewardPic()))
                    .bitmapTransform(new SepiaFilterTransformation(mContext, Glide.get(mContext).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.rewardsImage);
        }else if(reward.getRankId() == QuestioConstants.REWARD_RANK_SILVER){
            Glide.with(mContext)
                    .load(QuestioHelper.getImgLink(reward.getRewardPic()))
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(mContext).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.rewardsImage);
        }else if(reward.getRankId() == QuestioConstants.REWARD_RANK_GOLD){
            Glide.with(mContext)
                    .load(QuestioHelper.getImgLink(reward.getRewardPic()))
                    .bitmapTransform(new BrightnessFilterTransformation(mContext, Glide.get(mContext).getBitmapPool(), 0.5f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.rewardsImage);
        }else{
            Glide.with(mContext)
                    .load(QuestioHelper.getImgLink(reward.getRewardPic()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.rewardsImage);
        }


        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}