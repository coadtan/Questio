package com.questio.projects.questio.sections;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.InventoryAdapter;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class InventorySection extends Fragment implements AdapterView.OnItemClickListener {
    private final String LOG_TAG = InventorySection.class.getSimpleName();
    Context mContext;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    View rootView;
    GridView inventory;
    InventoryAdapter inventoryAdapter = null;
    ArrayList<ItemInInventory> itemsInv;
    long adventurerId;
    RestAdapter adapter;
    QuestioAPIService api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        prefs = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        editor = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.section_inventory, container, false);
        inventory = (GridView) rootView.findViewById(R.id.inventory);
        requestItemInventoryData(adventurerId);

        inventory.setOnItemClickListener(this);


        return rootView;
    }

    private void requestItemInventoryData(long id){
        api.getAllItemInInventoryByAdventurerId(id, new Callback<ArrayList<ItemInInventory>>() {
            @Override
            public void success(ArrayList<ItemInInventory> itemInInventories, Response response) {
                if (itemInInventories != null) {
                    itemsInv = itemInInventories;
                    inventoryAdapter = new InventoryAdapter(mContext, itemInInventories);
                    inventory.setAdapter(inventoryAdapter);
                    inventoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemInInventory item = itemsInv.get(position);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_description_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        TextView tvItemName = (TextView) dialog.findViewById(R.id.dialog_item_name);
        TextView tvItemCollection = (TextView) dialog.findViewById(R.id.dialog_item_collection);
        ImageView itemImage = (ImageView) dialog.findViewById(R.id.dialog_item_picture);
        Button closeBtn = (Button) dialog.findViewById(R.id.button_item_close);

        String itemName = item.getItemName();
        String itemCollection = item.getItemCollection();

        Glide.with(mContext)
                .load(QuestioConstants.BASE_URL + item.getItemPicPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemImage);
        tvItemName.setText(itemName);
        tvItemCollection.setText(itemCollection);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
