package com.questio.projects.questio.adepters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.questio.projects.questio.models.Floor;

import java.util.ArrayList;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
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


//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
//        TextView label = new TextView(context);
//        label.setTextColor(Color.BLACK);
//        // Then you can get the current item using the values array (Users array) and the current position
//        // You can NOW reference each method you has created in your bean object (User class)
//        label.setText(floors.get(position).getFloorName());
//
//        // And finally return your dynamic (or custom) view for each spinner item
//        return label;
//    }
//
//    // And here is when the "chooser" is popped up
//    // Normally is the same view, but you can customize it if you want
//    @Override
//    public View getDropDownView(int position, View convertView,
//                                ViewGroup parent) {
//        TextView label = new TextView(context);
//        label.setText(floors.get(position).getFloorName());
//
//        return label;
//    }
}
