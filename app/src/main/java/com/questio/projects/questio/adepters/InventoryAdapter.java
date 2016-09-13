package com.questio.projects.questio.adepters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryAdapter extends BaseAdapter {
    public static final String LOG_TAG = InventoryAdapter.class.getSimpleName();
    ArrayList<ItemInInventory> itemInvList;
    private Context mContext;

    public InventoryAdapter(Context context, ArrayList<ItemInInventory> itemsInv) {
        itemInvList = itemsInv;
        mContext = context;
    }

    @Override
    public int getCount() {
        return itemInvList.size();
    }

    @Override
    public ItemInInventory getItem(int position) {
        return itemInvList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemInInventory itemInv = getItem(position);
        Log.d(LOG_TAG, itemInv.toString());
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_inventory, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        Glide.with(mContext)
                .load(QuestioHelper.getImgLink(itemInv.getItemPicPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.itemImage);

        if(itemInv.getIsEquipped() == 1){
            viewHolder.itemImage.setImageAlpha(100);
        }

        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        @BindView(R.id.item_inventory)
        ImageView itemImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}