package com.questio.projects.questio.adepters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.Item;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.models.QuestStatusAndScore;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

/**
 * Created by ning jittima on 17/7/2558.
 */
public class InventoryAdapter extends ArrayAdapter<ItemInInventory> {
    public static final String LOG_TAG = InventoryAdapter.class.getSimpleName();
    private Context mContext;
    ArrayList<ItemInInventory> itemInvList;

    private static class ViewHolder {
        private ImageView itemImage;

        public ViewHolder(View view){
            itemImage = (ImageView)view.findViewById(R.id.item_inventory);
        }
    }
    public InventoryAdapter(Context context, ArrayList<ItemInInventory> itemsInv) {
        super(context, 0);
        itemInvList = itemsInv;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemInInventory itemInv = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_inventory, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        Glide.with(mContext)
                .load(QuestioHelper.getImgLink(itemInv.getItemPicPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.itemImage);
        Log.d(LOG_TAG, "getView: item = " + itemInv.getItemName());
        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(LOG_TAG, "notifyDataSetChanged: called");

        super.notifyDataSetChanged();
    }
}