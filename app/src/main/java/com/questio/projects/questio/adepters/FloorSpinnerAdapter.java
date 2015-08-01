package com.questio.projects.questio.adepters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.questio.projects.questio.models.Floor;

import java.util.ArrayList;


public class FloorSpinnerAdapter extends ArrayAdapter<Floor> {

    private Context context;
    private ArrayList<Floor> floors;

    public FloorSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Floor> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.floors = values;
    }

    public int getCount() {
        return floors.size();
    }

    public Floor getItem(int position) {
        return floors.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(floors.get(position).getFloorName());
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(floors.get(position).getFloorName());

        return label;
    }
}
