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

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsListAdapter extends ArrayAdapter<PlaceNews> {
    public static final String LOG_TAG = NewsListAdapter.class.getSimpleName();

    public static class ViewHolder {
        @Bind(R.id.newsId)
        TextView newsId;
        @Bind(R.id.news_date)
        TextView newsDate;
        @Bind(R.id.news_header)
        TextView newsHeader;
        @Bind(R.id.news_content)
        TextView newsContent;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public NewsListAdapter(Context context, ArrayList<PlaceNews> feed) {
        super(context, 0, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceNews items = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.newsId.setText(Integer.toString(items.getNewsId()));
        viewHolder.newsDate.setText(items.getDateStarted());
        viewHolder.newsHeader.setText(items.getNewsHeader());
        viewHolder.newsContent.setText(items.getNewsDetails());

        return convertView;
    }
}
