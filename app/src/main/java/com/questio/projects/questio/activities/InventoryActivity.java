package com.questio.projects.questio.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.InventoryAdapter;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class InventoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String LOG_TAG = InventoryActivity.class.getSimpleName();
    Context mContext;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    View view;

    @Bind(R.id.inventory_toolbar)
    Toolbar toolbar;

    @Bind(R.id.inventory_filter_name2)
    EditText filterName;

    @Bind(R.id.inventory_filter_button2)
    ImageButton filterBtn;

    @Bind(R.id.item_inventory)
    GridView inventory;

    ArrayList<ItemInInventory> itemsInv;
    ArrayList<ItemInInventory> itemsInvFilterType;
    ArrayList<ItemInInventory> itemsInvForShow;
    LinkedHashMap<String, Boolean> mapOfCheck = new LinkedHashMap<>();
    InventoryAdapter inventoryAdapter = null;
    Boolean positionChecker[] = null;
    long adventurerId;
    RestAdapter adapter;
    QuestioAPIService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mContext = this;
        prefs = this.getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        editor = this.getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        mapOfCheck.put("checkHead", true);
        mapOfCheck.put("checkBackground", true);
        mapOfCheck.put("checkNeck", true);
        mapOfCheck.put("checkBody", true);
        mapOfCheck.put("checkLeftHand", true);
        mapOfCheck.put("checkRightHand", true);
        mapOfCheck.put("checkArm", true);
        mapOfCheck.put("checkLeg", true);
        mapOfCheck.put("checkFoot", true);
        mapOfCheck.put("checkSpecial", true);

        requestItemInventoryData(adventurerId);
        inventory.setOnItemClickListener(this);
    }

    @OnClick(R.id.inventory_filter_button2)
    public void showFilterOption(View view) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("Filter")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("\"Please clarify your filter option\"")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withIcon(getResources().getDrawable(R.drawable.ic_icon_search))
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("OK")
                .withButton2Text("Cancel")
                .isCancelableOnTouchOutside(false)
                .setCustomView(R.layout.dialog_filter_inventory, view.getContext());
        final CheckBox checkHead = ButterKnife.findById(dialogBuilder, R.id.filter_type_head);
        final CheckBox checkBackground = ButterKnife.findById(dialogBuilder, R.id.filter_type_background);
        final CheckBox checkNeck = ButterKnife.findById(dialogBuilder, R.id.filter_type_neck);
        final CheckBox checkBody = ButterKnife.findById(dialogBuilder, R.id.filter_type_body);
        final CheckBox checkLeftHand = ButterKnife.findById(dialogBuilder, R.id.filter_type_handleft);
        final CheckBox checkRightHand = ButterKnife.findById(dialogBuilder, R.id.filter_type_handright);
        final CheckBox checkArm = ButterKnife.findById(dialogBuilder, R.id.filter_type_arm);
        final CheckBox checkLeg = ButterKnife.findById(dialogBuilder, R.id.filter_type_leg);
        final CheckBox checkFoot = ButterKnife.findById(dialogBuilder, R.id.filter_type_foot);
        final CheckBox checkSpecial = ButterKnife.findById(dialogBuilder, R.id.filter_type_special);

        if (positionChecker != null) {
            checkHead.setChecked(positionChecker[0]);
            checkBackground.setChecked(positionChecker[1]);
            checkNeck.setChecked(positionChecker[2]);
            checkBody.setChecked(positionChecker[3]);
            checkLeftHand.setChecked(positionChecker[4]);
            checkRightHand.setChecked(positionChecker[5]);
            checkArm.setChecked(positionChecker[6]);
            checkLeg.setChecked(positionChecker[7]);
            checkFoot.setChecked(positionChecker[8]);
            checkSpecial.setChecked(positionChecker[9]);
        }
        dialogBuilder.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapOfCheck.put("checkHead", checkHead.isChecked());
                mapOfCheck.put("checkBackground", checkBackground.isChecked());
                mapOfCheck.put("checkNeck", checkNeck.isChecked());
                mapOfCheck.put("checkBody", checkBody.isChecked());
                mapOfCheck.put("checkLeftHand", checkLeftHand.isChecked());
                mapOfCheck.put("checkRightHand", checkRightHand.isChecked());
                mapOfCheck.put("checkArm", checkArm.isChecked());
                mapOfCheck.put("checkLeg", checkLeg.isChecked());
                mapOfCheck.put("checkFoot", checkFoot.isChecked());
                mapOfCheck.put("checkSpecial", checkSpecial.isChecked());
                positionChecker = new Boolean[10];
                for (Map.Entry<String, Boolean> each : mapOfCheck.entrySet()) {
                    switch (each.getKey()) {
                        case "checkHead":
                            positionChecker[0] = each.getValue();
                            break;
                        case "checkBackground":
                            positionChecker[1] = each.getValue();
                            break;
                        case "checkNeck":
                            positionChecker[2] = each.getValue();
                            break;
                        case "checkBody":
                            positionChecker[3] = each.getValue();
                            break;
                        case "checkLeftHand":
                            positionChecker[4] = each.getValue();
                            break;
                        case "checkRightHand":
                            positionChecker[5] = each.getValue();
                            break;
                        case "checkArm":
                            positionChecker[6] = each.getValue();
                            break;
                        case "checkLeg":
                            positionChecker[7] = each.getValue();
                            break;
                        case "checkFoot":
                            positionChecker[8] = each.getValue();
                            break;
                        case "checkSpecial":
                            positionChecker[9] = each.getValue();
                            break;
                    }
                }
                if (itemsInv != null) {
                    itemsInvFilterType.clear();
                    itemsInvForShow.clear();
                    for (ItemInInventory each : itemsInv) {
                        if (positionChecker[each.getPositionId() - 1]) {
                            Log.d(LOG_TAG, each.getPositionId() + " was added to itemsInvFilterType");
                            itemsInvFilterType.add(each);
                        }
                    }
                    itemsInvForShow.addAll(itemsInvFilterType);
                    onTextChanged(filterName.getText());

                }

                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();
    }

    private void requestItemInventoryData(long id) {
        api.getAllItemInInventoryByAdventurerId(id, QuestioConstants.QUESTIO_KEY, new Callback<ArrayList<ItemInInventory>>() {
            @Override
            public void success(ArrayList<ItemInInventory> itemInInventories, Response response) {

                if (itemInInventories != null) {
                    itemsInv = itemInInventories;
                    itemsInvFilterType = new ArrayList<>(itemInInventories);
                    itemsInvForShow = new ArrayList<>(itemInInventories);
                    inventoryAdapter = new InventoryAdapter(mContext, itemsInvForShow);
                    inventory.setAdapter(inventoryAdapter);
                    inventoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnTextChanged(R.id.inventory_filter_name2)
    void onTextChanged(CharSequence text) {
        if (itemsInvFilterType != null) {
            itemsInvForShow.clear();
            inventoryAdapter.notifyDataSetChanged();
            if (text.toString().equalsIgnoreCase("")) {
                itemsInvForShow.addAll(itemsInvFilterType);
                inventoryAdapter.notifyDataSetChanged();
            } else {
                for (ItemInInventory each : itemsInvFilterType) {
                    if (each.getItemName().contains(text)) {
                        itemsInvForShow.add(each);
                        inventoryAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemInInventory item = itemsInvForShow.get(position);
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(mContext);
        dialog
                .withTitle("Item Description")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("Close")
                .isCancelableOnTouchOutside(false)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .setCustomView(R.layout.item_description_dialog, mContext);
        TextView tvItemName = ButterKnife.findById(dialog, R.id.dialog_item_name);
        TextView tvItemCollection = ButterKnife.findById(dialog, R.id.dialog_item_collection);
        ImageView itemImage = ButterKnife.findById(dialog, R.id.dialog_item_picture);

        String itemName = item.getItemName();
        String itemCollection = item.getItemCollection();

        Glide.with(mContext)
                .load(QuestioConstants.BASE_URL + item.getItemPicPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemImage);
        tvItemName.setText(itemName);
        tvItemCollection.setText(itemCollection);
        dialog.show();
    }
}
