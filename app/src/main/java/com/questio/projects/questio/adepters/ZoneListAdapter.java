package com.questio.projects.questio.adepters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Zone;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ZoneListAdapter extends ArrayAdapter<Zone> {
    public static final String LOG_TAG = ZoneListAdapter.class.getSimpleName();

    public ZoneListAdapter(Context context, ArrayList<Zone> zones) {
        super(context, 0, zones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zone items = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_zone, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.zoneIcon.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_icon_science));
        viewHolder.zoneId.setText(Integer.toString(items.getZoneId()));
        viewHolder.zoneName.setText(items.getZoneName());
        viewHolder.zoneDetail.setText(items.getZoneDetails());
        viewHolder.zoneItem.setText(items.getItemSet());
        viewHolder.zoneReward.setText(Integer.toString(items.getRewardId()));

        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.zone_list_icon)
        ImageView zoneIcon;

        @BindView(R.id.zone_list_zoneId)
        TextView zoneId;

        @BindView(R.id.zone_list_zoneName)
        TextView zoneName;

        @BindView(R.id.zone_list_zoneDetail)
        TextView zoneDetail;

        @BindView(R.id.zone_list_itemset)
        TextView zoneItem;

        @BindView(R.id.zone_list_reward)
        TextView zoneReward;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
