package com.questio.projects.questio.adepters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.utilities.QuestListRowHolder;

import java.util.ArrayList;

public class QuestRecycleViewAdapter extends RecyclerView.Adapter<QuestListRowHolder> {
    private ArrayList<Quest> questItemList;
    Context mContext;

    public QuestRecycleViewAdapter(Context context, ArrayList<Quest> questItemList) {
        this.questItemList = questItemList;
        this.mContext = context;
    }

    @Override
    public QuestListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_list_quest, null);
        return new QuestListRowHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestListRowHolder questListRowHolder, int i) {
        Quest questItem = questItemList.get(i);

        switch (questItem.getDiffId()) {
            case 1:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#ff76ff53"));
                break;
            case 2:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#CCFF99"));
                break;
            case 3:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#fffbff61"));
                break;
            case 4:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#ffffce44"));
                break;
            case 5:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#ffff9022"));
                break;
            default:
                questListRowHolder.cardview_quest.setCardBackgroundColor(Color.parseColor("#ff362913"));
                break;
        }

        questListRowHolder.quest_list_questid.setText(Integer.toString(questItem.getQuestId()));
        questListRowHolder.quest_list_questname.setText(questItem.getQuestName());
        questListRowHolder.quest_list_questdetails.setText(questItem.getQuestDetails());
        switch (questItem.getQuestTypeId()) {
            case 1:
                questListRowHolder.quest_list_questtypeid.setImageResource(R.drawable.ic_icon_quiz);
                break;
            case 2:
                questListRowHolder.quest_list_questtypeid.setImageResource(R.drawable.ic_icon_riddle);
                break;
            case 3:
                questListRowHolder.quest_list_questtypeid.setImageResource(R.drawable.ic_icon_puzzle);
                //questListRowHolder.quest_list_questtypeid.setText("ทายรูปภาพ");
                break;
            default:
                questListRowHolder.quest_list_questtypeid.setImageResource(R.drawable.ic_icon_riddle);
                //questListRowHolder.quest_list_questtypeid.setText("ไม่สามารถระบุประเภทได้");
                break;
        }

        questListRowHolder.quest_list_zoneid.setText("ตำแหน่ง " + questItem.getZoneName() + ", " + questItem.getFloorName() + ", " + questItem.getBuildingName());
        String diff;
        switch (questItem.getDiffId()) {
            case 1:
                diff = "ง่ายมาก";
                break;
            case 2:
                diff = "ง่าย";
                break;
            case 3:
                diff = "ปานกลาง";
                break;
            case 4:
                diff = "ยาก";
                break;
            case 5:
                diff = "ยากมาก";
                break;
            default:
                diff = "ไม่สามารถระบุได้";
                break;
        }
        questListRowHolder.quest_list_diffid.setText("ระดับความยาก " + diff);


    }

    @Override
    public int getItemCount() {
        return (null != questItemList ? questItemList.size() : 0);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}