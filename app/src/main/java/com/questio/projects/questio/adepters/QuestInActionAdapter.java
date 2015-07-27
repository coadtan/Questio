package com.questio.projects.questio.adepters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.QuestStatusAndScore;
import com.questio.projects.questio.utilities.QuestioConstants;


public class QuestInActionAdapter extends BaseAdapter {
    public static final String LOG_TAG = QuestInActionAdapter.class.getSimpleName();
    private Context mContext;
    private Typeface tf;
    ArrayList<QuestStatusAndScore> statusList;
    ArrayList<Quest> quests;

    private static class ViewHolder {
        private ImageView questtype;
        private ImageView difficulty;
        private ImageView status;
        private LinearLayout questListItem;
        private TextView zoneid;
        private TextView questname;
        private TextView questid;
        private TextView questdetails;
        private TextView questTypeInvisible;

        public ViewHolder(View view) {
            questTypeInvisible = (TextView) view.findViewById(R.id.questTypeInvisible);
            questtype = (ImageView) view.findViewById(R.id.questtype);
            difficulty = (ImageView) view.findViewById(R.id.difficulty);
            status = (ImageView) view.findViewById(R.id.status);
            questname = (TextView) view.findViewById(R.id.questname);
            questdetails = (TextView) view.findViewById(R.id.questdetails);
            questid = (TextView) view.findViewById(R.id.questid);
            zoneid = (TextView) view.findViewById(R.id.quest_zoneId);
            questListItem = (LinearLayout) view.findViewById(R.id.quest_list_item);

        }

        @Override
        public String toString() {
            return questid.getText().toString();
        }
    }

    public QuestInActionAdapter(Context context, ArrayList<Quest> quests, ArrayList<QuestStatusAndScore> statusList) {
        this.quests = quests;
        this.statusList = statusList;
        mContext = context;
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/CSPraJad.otf");
    }

    @Override
    public int getCount() {
        return this.quests.size();
    }

    @Override
    public Quest getItem(int position) {
        return this.quests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.quests.get(position).getQuestId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_quest, parent, false);
        }
        Quest items = getItem(position);
        if (items != null) {
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.questid.setText(Integer.toString(items.getQuestId()));
            viewHolder.questid.setTypeface(tf);
            viewHolder.questname.setText(items.getQuestName());
            viewHolder.questname.setTypeface(tf);
            viewHolder.questdetails.setText(items.getQuestDetails());
            viewHolder.questTypeInvisible.setText(Integer.toString(items.getQuestTypeId()));
            viewHolder.zoneid.setText(Integer.toString(items.getZoneId()));
            viewHolder.questListItem.setBackgroundColor(mContext.getResources().getColor(R.color.grey_500));
            viewHolder.questListItem.setBackgroundResource(R.drawable.touch_selector);
            viewHolder.status.setImageResource(R.drawable.ic_quest_unfinish);

            if (this.statusList != null) {
                for (QuestStatusAndScore q : this.statusList) {
                    if (items.getQuestId() == q.getQuestId()) {
                        if (q.getStatus() == QuestioConstants.QUEST_FINISHED || q.getStatus() == QuestioConstants.QUEST_FAILED) {
                            viewHolder.questListItem.setBackgroundColor(mContext.getResources().getColor(R.color.grey_500));
                            viewHolder.status.setImageResource(R.drawable.ic_quest_finish);
                        }
                    }
                }
            }
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

            switch (items.getDiffId()) {
                case 1:
                    viewHolder.difficulty.setBackgroundColor(mContext.getResources().getColor(R.color.quest_veryeasy));
                    break;
                case 2:
                    viewHolder.difficulty.setBackgroundColor(mContext.getResources().getColor(R.color.quest_easy));
                    break;
                case 3:
                    viewHolder.difficulty.setBackgroundColor(mContext.getResources().getColor(R.color.quest_normal));
                    break;
                case 4:
                    viewHolder.difficulty.setBackgroundColor(mContext.getResources().getColor(R.color.quest_hard));
                    break;
                case 5:
                    viewHolder.difficulty.setBackgroundColor(mContext.getResources().getColor(R.color.quest_veryhard));
                    break;
            }
        }

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}