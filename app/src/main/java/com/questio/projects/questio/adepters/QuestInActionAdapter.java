package com.questio.projects.questio.adepters;

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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class QuestInActionAdapter extends BaseAdapter {
    public static final String LOG_TAG = QuestInActionAdapter.class.getSimpleName();
    private Context mContext;
    private Typeface tf;
    ArrayList<QuestStatusAndScore> statusList;
    ArrayList<Quest> quests;

    public static class ViewHolder {
        @Bind(R.id.questtype)
        ImageView questtype;

        @Bind(R.id.difficulty)
        ImageView difficulty;

        @Bind(R.id.status)
        ImageView status;

        @Bind(R.id.quest_list_item)
        LinearLayout questListItem;

        @Bind(R.id.quest_zoneId)
        TextView zoneid;

        @Bind(R.id.questname)
        TextView questname;

        @Bind(R.id.questid)
        TextView questid;

        @Bind(R.id.questdetails)
        TextView questdetails;

        @Bind(R.id.questTypeInvisible)
        TextView questTypeInvisible;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
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
            viewHolder.status.setContentDescription(Integer.toString(QuestioConstants.QUEST_NOT_STARTED));
            if (this.statusList != null) {
                for (QuestStatusAndScore q : this.statusList) {
                    if (items.getQuestId() == q.getQuestId()) {
                        if (q.getStatus() == QuestioConstants.QUEST_FINISHED || q.getStatus() == QuestioConstants.QUEST_FAILED) {
                            viewHolder.questListItem.setBackgroundColor(mContext.getResources().getColor(R.color.grey_500));
                            viewHolder.status.setImageResource(R.drawable.ic_quest_finish);
                            viewHolder.status.setContentDescription(Integer.toString(QuestioConstants.QUEST_FINISHED));
                        }
                        if (q.getStatus() == QuestioConstants.QUEST_FAILED) {
                            viewHolder.status.setContentDescription(Integer.toString(QuestioConstants.QUEST_FAILED));
                        }
                        if (q.getStatus() == QuestioConstants.QUEST_NOT_FINISHED) {
                            viewHolder.status.setContentDescription(Integer.toString(QuestioConstants.QUEST_NOT_FINISHED));
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