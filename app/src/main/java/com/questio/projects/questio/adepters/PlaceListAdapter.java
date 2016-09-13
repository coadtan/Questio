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

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaceListAdapter extends CursorAdapter {
    public static final String LOG_TAG = PlaceListAdapter.class.getSimpleName();
    Typeface tf;

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
        viewHolder.placeDetail.setText(placeFullNameString);
        String placeLatString = cursor.getString(5);
        viewHolder.placeLat.setText(placeLatString);
        String placeLngString = cursor.getString(6);
        viewHolder.placeLng.setText(placeLngString);
        String placeType = cursor.getString(8);
        viewHolder.placeRadius.setText(cursor.getString(7) + " m.");
        if (!placeType.equalsIgnoreCase("null")) {
            viewHolder.iconView.setImageResource(R.drawable.ic_logo_museum);
        }
        view.setContentDescription(viewHolder.placeName.getText());


    }

    public static class ViewHolder {
        @BindView(R.id.list_item_icon)
        ImageView iconView;

        @BindView(R.id.placeId)
        TextView placeId;

        @BindView(R.id.placeName)
        TextView placeName;

        @BindView(R.id.place_detail)
        TextView placeDetail;

        @BindView(R.id.placeLat)
        TextView placeLat;

        @BindView(R.id.placeLng)
        TextView placeLng;

        @BindView(R.id.placeRadius)
        TextView placeRadius;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
