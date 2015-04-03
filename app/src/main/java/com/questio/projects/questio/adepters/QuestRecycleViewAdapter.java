package com.questio.projects.questio.adepters;

import android.content.Context;
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
    private Context mContext;

    public QuestRecycleViewAdapter(Context context, ArrayList<Quest> questItemList) {
        this.questItemList = questItemList;
        this.mContext = context;
    }

    @Override
    public QuestListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_list_quest, null);
        QuestListRowHolder qr = new QuestListRowHolder(v);
        return qr;
    }

    @Override
    public void onBindViewHolder(QuestListRowHolder questListRowHolder, int i) {
        Quest questItem = questItemList.get(i);
        questListRowHolder.quest_list_questid.setText(Integer.toString(questItem.getQuestId()));
        questListRowHolder.quest_list_questname.setText(questItem.getQuestName());
        questListRowHolder.quest_list_questdetails.setText(questItem.getQuestDetails());
        switch (questItem.getQuestTypeId()) {
            case 1:
                questListRowHolder.quest_list_questtypeid.setText("ถาม-ตอบ");
                break;
            case 2:
                questListRowHolder.quest_list_questtypeid.setText("ปริศนา");
                break;
            case 3:
                questListRowHolder.quest_list_questtypeid.setText("ทายรูปภาพ");
                break;
            default:
                questListRowHolder.quest_list_questtypeid.setText("ไม่สามารถระบุประเภทได้");
                break;
        }

        questListRowHolder.quest_list_zoneid.setText("ตำแหน่ง "+ Integer.toString(questItem.getZoneId()));
        String diff;
        switch (questItem.getDiffId()){
            case 1 : diff = "ง่ายมาก"; break;
            case 2 : diff = "ง่าย"; break;
            case 3 : diff = "ปานกลาง"; break;
            case 4 : diff = "ยาก"; break;
            case 5 : diff = "ยากมาก"; break;
            default:
                diff = "ไม่สามารถระบุได้"; break;
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