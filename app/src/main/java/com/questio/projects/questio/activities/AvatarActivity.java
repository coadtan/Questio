package com.questio.projects.questio.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.InventoryAdapter;
import com.questio.projects.questio.models.Avatar;
import com.questio.projects.questio.models.Item;
import com.questio.projects.questio.models.ItemInInventory;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.questio.projects.questio.utilities.QuestioConstants.*;


public class AvatarActivity extends AppCompatActivity {
    private static final String LOG_TAG = AvatarActivity.class.getSimpleName();
    QuestioAPIService api;
    RestAdapter adapter;
    long adventurerId;
    Context mContext;
    InventoryAdapter inventoryAdapter = null;
    ArrayList<ItemInInventory> itemsEquip;
    GridView equipLayout;
    Avatar avatar;
    long oldItemId;

    @Bind(R.id.avatar_toolbar)
    Toolbar toolbar;

    @Bind(R.id.avatar_head)
    ImageView avatarHead;

    @Bind(R.id.avatar_background)
    ImageView avatarBackground;

    @Bind(R.id.avatar_neck)
    ImageView avatarNeck;

    @Bind(R.id.avatar_top)
    ImageView avatarBody;

    @Bind(R.id.avatar_handleft)
    ImageView avatarHandLeft;

    @Bind(R.id.avatar_handright)
    ImageView avatarHandRight;

    @Bind(R.id.avatar_arms)
    ImageView avatarArms;

    @Bind(R.id.avatar_bottom)
    ImageView avatarLegs;

    @Bind(R.id.avatar_feet)
    ImageView avatarFoot;

    @Bind(R.id.avatar_aura)
    ImageView avatarSpecial;

    @Bind(R.id.button_head)
    ImageButton buttonHead;

    @Bind(R.id.button_background)
    ImageButton buttonBackground;

    @Bind(R.id.button_neck)
    ImageButton buttonNeck;

    @Bind(R.id.button_top)
    ImageButton buttonBody;

    @Bind(R.id.button_handleft)
    ImageButton buttonHandLeft;

    @Bind(R.id.button_handright)
    ImageButton buttonHandRight;

    @Bind(R.id.button_arms)
    ImageButton buttonArms;

    @Bind(R.id.button_bottom)
    ImageButton buttonBottom;

    @Bind(R.id.button_feet)
    ImageButton buttonFoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RestAdapter.Builder().setEndpoint(ENDPOINT).build();
        api = adapter.create(QuestioAPIService.class);
        setContentView(R.layout.activity_avatar);
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
        handleInstanceState(savedInstanceState);

        api.getAvatarCountByAvatarId(adventurerId, QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String avatarCountStr = QuestioHelper.getJSONStringValueByTag("avatarcount", response);
                int avatarCount = Integer.parseInt(avatarCountStr);
                boolean hasAvatar = (avatarCount == 1);
                if (hasAvatar) {
                    populateAvatar();
                } else {
                    insertNewAvatar();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        setButtonClick();

    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                adventurerId = 0;
            } else {
                adventurerId = extras.getLong(ADVENTURER_ID);
            }
        } else {
            adventurerId = (long) savedInstanceState.getSerializable(ADVENTURER_ID);
        }
        Log.d(LOG_TAG, ADVENTURER_ID + ": " + adventurerId);
        SharedPreferences prefs = getSharedPreferences(ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(ADVENTURER_ID, 0);
        mContext = this;
        avatar = new Avatar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Avatar");
        }
    }

    private void insertNewAvatar() {
        api.insertNewAvatar(adventurerId, QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "insert new avatar successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "insert new avatar failure");
            }
        });
    }

    private void populateAvatar() {
        api.getAvatarByAvatarId(adventurerId, QUESTIO_KEY, new Callback<Avatar[]>() {
            @Override
            public void success(Avatar[] avatars, Response response) {
                Log.d(LOG_TAG, "get avatar successfully");
                Log.d(LOG_TAG, avatars[0].toString());
                avatar = avatars[0];
                setImageSpike(
                        avatar.getHeadId(),
                        avatar.getBackgroundId(),
                        avatar.getNeckId(),
                        avatar.getBodyId(),
                        avatar.getHandleftId(),
                        avatar.getHandrightId(),
                        avatar.getArmId(),
                        avatar.getLegId(),
                        avatar.getFootId(),
                        avatar.getSpecialId()
                );
                Log.d(LOG_TAG, "end of populateAvatar");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "get avatar failure");
            }
        });
    }

    private void setImageSpike(
            long headId,
            long backgroundId,
            long neckId,
            long bodyId,
            long handleftId,
            long handrightId,
            long armId,
            long legId,
            long footId,
            long specialId
    ) {
        Log.d(LOG_TAG, "setImageSpike called");
        api.getItemsBySetOfItemId(
                headId,
                backgroundId,
                neckId,
                bodyId,
                handleftId,
                handrightId,
                armId,
                legId,
                footId,
                specialId,
                QUESTIO_KEY,
                new Callback<Item[]>() {
                    @Override
                    public void success(Item[] items, Response response) {
                        Log.d(LOG_TAG, "setImageSpike load successfully");
                        if (items != null) {
                            for (Item item : items) {
                                Log.d(LOG_TAG, item.toString());
                                switch (item.getPositionId()) {
                                    case POSITION_HEAD:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarHead);
                                        break;
                                    case POSITION_BACKGROUND:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarBackground);
                                        break;
                                    case POSITION_NECK:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarNeck);
                                        break;
                                    case POSITION_BODY:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarBody);
                                        break;
                                    case POSITION_HANDLEFT:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarHandLeft);
                                        break;
                                    case POSITION_HANDRIGHT:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarHandRight);
                                        break;
                                    case POSITION_ARMS:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarArms);
                                        break;
                                    case POSITION_LEGS:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarLegs);
                                        break;
                                    case POSITION_FOOT:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarFoot);
                                        break;
                                    case POSITION_AURA:
                                        Glide.with(AvatarActivity.this)
                                                .load(BASE_QUESTIO_MANAGEMENT + item.getEquipSpritePath())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(avatarSpecial);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(LOG_TAG, "setImageSpike load failed");
                        Log.d(LOG_TAG, error.getUrl());
                    }
                }

        );
    }

    public void showEquipDialog(final int position){
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(this);
        dialog
                .withTitle("Equip")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("Close")
                .isCancelableOnTouchOutside(true)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .setCustomView(R.layout.inventory_equip_layout, this);
        equipLayout = ButterKnife.findById(dialog, R.id.item_inventory_equip);
        api.getAllItemInInventoryByAdventurerIdAndPositionId(adventurerId, position, QuestioConstants.QUESTIO_KEY, new Callback<ArrayList<ItemInInventory>>() {

            @Override
            public void success(ArrayList<ItemInInventory> itemInInventories, Response response) {
                if (itemInInventories != null) {
                    if (!itemInInventories.isEmpty()) {
                        itemsEquip = itemInInventories;
                        inventoryAdapter = new InventoryAdapter(mContext, itemsEquip);
                        equipLayout.setAdapter(inventoryAdapter);
                        equipLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d(LOG_TAG, view.toString());
                                ItemInInventory newItem = itemsEquip.get(i);
                                equipNewItem(position, newItem.getItemId());
                                dialog.dismiss();
                                return false;
                            }
                        });
                        inventoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

            dialog.show();
    }

    public void setButtonClick(){
        buttonHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_HEAD);
            }
        });
        buttonBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_BACKGROUND);
            }
        });
        buttonNeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_NECK);
            }
        });
        buttonBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_BODY);
            }
        });
        buttonHandLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_HANDLEFT);
            }
        });
        buttonHandRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_HANDRIGHT);
            }
        });
        buttonArms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_ARMS);
            }
        });
        buttonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_LEGS);
            }
        });
        buttonFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipDialog(POSITION_FOOT);
            }
        });
    }

    public void equipNewItem(final int partId, final long newItemId){
        if(avatar.getAvatarId() != 0){
            switch (partId){
                case POSITION_HEAD:
                    oldItemId = avatar.getHeadId();
                    avatar.setHeadId(newItemId);
                    break;
                case POSITION_BACKGROUND:
                    oldItemId = avatar.getBackgroundId();
                    avatar.setBackgroundId(newItemId);
                    break;
                case POSITION_NECK:
                    oldItemId = avatar.getNeckId();
                    avatar.setNeckId(newItemId);
                    break;
                case POSITION_BODY:
                    oldItemId = avatar.getBodyId();
                    avatar.setBodyId(newItemId);
                    break;
                case POSITION_HANDLEFT:
                    oldItemId = avatar.getHandleftId();
                    avatar.setHandleftId(newItemId);
                    break;
                case POSITION_HANDRIGHT:
                    oldItemId = avatar.getHandrightId();
                    avatar.setHandrightId(newItemId);
                    break;
                case POSITION_ARMS:
                    oldItemId = avatar.getArmId();
                    avatar.setArmId(newItemId);
                    break;
                case POSITION_LEGS:
                    oldItemId = avatar.getLegId();
                    avatar.setLegId(newItemId);
                    break;
                case POSITION_FOOT:
                    oldItemId = avatar.getFootId();
                    avatar.setFootId(newItemId);
                    break;
                case POSITION_AURA:
                    oldItemId = avatar.getSpecialId();
                    avatar.setSpecialId(newItemId);
                    break;
            }
            Log.d(LOG_TAG, "Old Item ID - " + oldItemId);
        }
        api.equipNewItem(partId, newItemId, oldItemId, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (oldItemId != 0) {
                    if (newItemId != oldItemId) {
                        String status = QuestioHelper.getJSONStringValueByTag("status", response);
                        if (status.equalsIgnoreCase("1")) {
                            changeSpritePathByNewItemId(partId, newItemId);
                        } else {
                            Log.d(LOG_TAG, "EquipNewItem Failed");
                        }
                    }else{
                        String status = QuestioHelper.getJSONStringValueByTag("status", response);
                        if (status.equalsIgnoreCase("1")) {
                            unequipItem(partId, oldItemId);
                        } else {
                            Log.d(LOG_TAG, "EquipNewItem Failed");
                        }
                    }
                } else {
                    String status = QuestioHelper.getJSONStringValueByTag("status", response);
                    if (status.equalsIgnoreCase("1")) {
                        changeSpritePathByNewItemId(partId, newItemId);
                    } else {
                        Log.d(LOG_TAG, "EquipNewItem Failed");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void unequipItem(final int partId, long itemId){
        api.unequipItem(partId, itemId, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String status = QuestioHelper.getJSONStringValueByTag("status", response);
                if (status.equalsIgnoreCase("1")) {
                    switch (partId) {
                        case POSITION_HEAD:
                            avatarHead.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_BACKGROUND:
                            avatarBackground.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_NECK:
                            avatarNeck.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_BODY:
                            avatarBody.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_HANDLEFT:
                            avatarHandLeft.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_HANDRIGHT:
                            avatarHandRight.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_ARMS:
                            avatarArms.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_LEGS:
                            avatarLegs.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_FOOT:
                            avatarFoot.setVisibility(View.INVISIBLE);
                            break;
                        case POSITION_AURA:
                            avatarSpecial.setVisibility(View.INVISIBLE);
                            break;
                    }
                } else {
                    Log.d(LOG_TAG, "EquipNewItem Failed");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void changeSpritePathByNewItemId(final int partId, long newItemId){
        api.getEquipSpritePathByItemId(newItemId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String url = QuestioHelper.getJSONStringValueByTag("equipspritepath", response);
                switch (partId) {
                    case POSITION_HEAD:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHead);
                        break;
                    case POSITION_BACKGROUND:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarBackground);
                        break;
                    case POSITION_NECK:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarNeck);
                        break;
                    case POSITION_BODY:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarBody);
                        break;
                    case POSITION_HANDLEFT:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHandLeft);
                        break;
                    case POSITION_HANDRIGHT:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHandRight);
                        break;
                    case POSITION_ARMS:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarArms);
                        break;
                    case POSITION_LEGS:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarLegs);
                        break;
                    case POSITION_FOOT:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarFoot);
                        break;
                    case POSITION_AURA:
                        Glide.with(AvatarActivity.this)
                                .load(BASE_URL + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarSpecial);
                        break;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}