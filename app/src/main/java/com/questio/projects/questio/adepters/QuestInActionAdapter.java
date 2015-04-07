package com.questio.projects.questio.adepters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Quest;

import java.util.ArrayList;

/**
 * Created by coad4u4ever on 05-Apr-15.
 */
public class QuestInActionAdapter extends ArrayAdapter<Quest> {

    public static final String LOG_TAG = NewsListAdapter.class.getSimpleName();

    private static class ViewHolder {
        private ImageView questtype;
        private ImageView difficulty;
        private ImageView status;

        private TextView questname;
        private TextView questid;
        private TextView questdetails;

        public ViewHolder(View view) {

            questtype = (ImageView) view.findViewById(R.id.questtype);
            difficulty = (ImageView) view.findViewById(R.id.difficulty);
            status = (ImageView) view.findViewById(R.id.status);
            questname = (TextView) view.findViewById(R.id.questname);
            questdetails = (TextView) view.findViewById(R.id.questdetails);
            questid = (TextView) view.findViewById(R.id.questid);

        }

    }

    public QuestInActionAdapter(Context context, ArrayList<Quest> feed) {
        super(context, 0, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Quest items = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_quest, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.questid.setText(Integer.toString(items.getQuestId()));
        viewHolder.questname.setText(items.getQuestName());
        viewHolder.questdetails.setText(items.getQuestDetails());
        switch (items.getQuestTypeId()) {
            case 1:
                viewHolder.questtype.setImageResource(R.drawable.ic_icon_quiz);
                break;
            case 2:
                viewHolder.questtype.setImageResource(R.drawable.ic_icon_riddle);
                break;
            case 3:
                viewHolder.questtype.setImageResource(R.drawable.ic_icon_puzzle);
                break;
        }

//        viewHolder.questtype.setImageDrawable();
//        viewHolder.difficulty.setImageDrawable();
//        viewHolder.status.setImageDrawable();
        return convertView;
    }
}