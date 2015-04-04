package com.questio.projects.questio.adepters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.PlaceNews;

import java.util.ArrayList;

/**
 * Created by coad4u4ever on 04-Apr-15.
 */
public class NewsListAdapter extends ArrayAdapter<PlaceNews> {

public static final String LOG_TAG= NewsListAdapter.class.getSimpleName();

private static class ViewHolder {
    private TextView newsId;
    private TextView news_date;
    private TextView news_header;
    private TextView news_content;

    public ViewHolder(View view) {

        newsId = (TextView) view.findViewById(R.id.newsId);
        news_date = (TextView) view.findViewById(R.id.news_date);
        news_header = (TextView) view.findViewById(R.id.news_header);
        news_content = (TextView) view.findViewById(R.id.news_content);

    }

}

    public NewsListAdapter(Context context, ArrayList<PlaceNews> feed) {
        super(context, 0, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PlaceNews items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);
        }
        // Lookup view for data population
        ViewHolder viewHolder = new ViewHolder(convertView);
        // Populate the data into the template view using the data object
        viewHolder.newsId.setText(Integer.toString(items.getNewsId()));
        viewHolder.news_date.setText(items.getDateStarted());
        viewHolder.news_header.setText(items.getNewsHeader());
        viewHolder.news_content.setText(items.getNewsDetails());
        // Return the completed view to render on screen
        return convertView;
    }
}
