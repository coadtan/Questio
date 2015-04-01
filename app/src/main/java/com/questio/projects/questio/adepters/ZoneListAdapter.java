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

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class ZoneListAdapter extends ArrayAdapter<Zone> {
    public static final String LOG_TAG = ZoneListAdapter.class.getSimpleName();

    private static class ViewHolder {
        private ImageView zone_list_icon;
        private TextView zone_list_zoneId;
        private TextView zone_list_zoneName;
        private TextView zone_list_zoneDetail;
        private TextView zone_list_itemset;
        private TextView zone_list_reward;

        public ViewHolder(View view) {

            zone_list_icon = (ImageView) view.findViewById(R.id.zone_list_icon);
            zone_list_zoneId = (TextView) view.findViewById(R.id.zone_list_zoneId);
            zone_list_zoneName = (TextView) view.findViewById(R.id.zone_list_zoneName);
            zone_list_zoneDetail = (TextView) view.findViewById(R.id.zone_list_zoneDetail);
            zone_list_itemset = (TextView) view.findViewById(R.id.zone_list_itemset);
            zone_list_reward = (TextView) view.findViewById(R.id.zone_list_reward);

        }
    }

    public ZoneListAdapter(Context context, ArrayList<Zone> zones) {
        super(context, 0, zones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Zone items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_zone, parent, false);
        }
        // Lookup view for data population
        ViewHolder viewHolder = new ViewHolder(convertView);
        // Populate the data into the template view using the data object
        viewHolder.zone_list_icon.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_icon_science));
        viewHolder.zone_list_zoneId.setText(Integer.toString(items.getZoneId()));
        viewHolder.zone_list_zoneName.setText(items.getZoneName());
        viewHolder.zone_list_zoneDetail.setText(items.getZoneDetails());
        viewHolder.zone_list_itemset.setText(items.getItemSet());
        viewHolder.zone_list_reward.setText(Integer.toString(items.getRewardId()));
        // Return the completed view to render on screen
        return convertView;
    }
}
