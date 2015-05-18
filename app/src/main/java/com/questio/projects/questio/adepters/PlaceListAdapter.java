package com.questio.projects.questio.adepters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.questio.projects.questio.R;


/**
 * Created by coad4u4ever on 07-Mar-15.
 */
public class PlaceListAdapter extends CursorAdapter {
    public static final String LOG_TAG = PlaceListAdapter.class.getSimpleName();
    Typeface tf;

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView placeId;
        public final TextView placeName;
        public final TextView place_detail;
        public final TextView placeLat;
        public final TextView placeLng;


        public ViewHolder(View view) {

            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            placeId = (TextView) view.findViewById(R.id.placeId);
            placeName = (TextView) view.findViewById(R.id.placeName);
            place_detail = (TextView) view.findViewById(R.id.place_detail);
            placeLat = (TextView) view.findViewById(R.id.placeLat);
            placeLng = (TextView) view.findViewById(R.id.placeLng);
        }
    }

    public PlaceListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/supermarket.ttf");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_place, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String placeIdString = cursor.getString(0);
        viewHolder.placeId.setText(placeIdString);
        String placeNameString = cursor.getString(1);
        viewHolder.placeName.setText(placeNameString);
        viewHolder.placeName.setTypeface(tf);
        String placeFullNameString = cursor.getString(2);
        viewHolder.place_detail.setText(placeFullNameString);
        String placeLatString = cursor.getString(5);
        viewHolder.placeLat.setText(placeLatString);
        String placeLngString = cursor.getString(6);
        viewHolder.placeLng.setText(placeLngString);
        String placeType = cursor.getString(8);
        if (!placeType.equalsIgnoreCase("null")) {
            viewHolder.iconView.setImageResource(R.drawable.ic_logo_museum);
        }


    }
}
