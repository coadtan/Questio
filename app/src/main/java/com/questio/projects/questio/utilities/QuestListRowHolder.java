package com.questio.projects.questio.utilities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.questio.projects.questio.R;

/**
 * Created by coad4u4ever on 03-Apr-15.
 */
public class QuestListRowHolder extends RecyclerView.ViewHolder {
    public TextView quest_list_questid;
    public TextView quest_list_questname;
    public TextView quest_list_questdetails;
    public TextView quest_list_questtypeid;
    public TextView quest_list_zoneid;
    public TextView quest_list_diffid;

    public QuestListRowHolder(View view) {
        super(view);
        //this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.quest_list_questid = (TextView) view.findViewById(R.id.quest_list_questid);
        this.quest_list_questname = (TextView) view.findViewById(R.id.quest_list_questname);
        this.quest_list_questdetails = (TextView) view.findViewById(R.id.quest_list_questdetails);
        this.quest_list_questtypeid = (TextView) view.findViewById(R.id.quest_list_questtypeid);
        this.quest_list_zoneid = (TextView) view.findViewById(R.id.quest_list_zoneid);
        this.quest_list_diffid = (TextView) view.findViewById(R.id.quest_list_diffid);

    }

}