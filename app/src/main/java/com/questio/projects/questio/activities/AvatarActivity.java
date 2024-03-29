package com.questio.projects.questio.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.questio.projects.questio.utilities.QuestioConstants.ADVENTURER_ID;
import static com.questio.projects.questio.utilities.QuestioConstants.ADVENTURER_PROFILE;
import static com.questio.projects.questio.utilities.QuestioConstants.BASE_QUESTIO_MANAGEMENT;
import static com.questio.projects.questio.utilities.QuestioConstants.ENDPOINT;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_ARMS;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_AURA;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_BACKGROUND;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_BODY;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_FOOT;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_HANDLEFT;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_HANDRIGHT;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_HEAD;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_LEGS;
import static com.questio.projects.questio.utilities.QuestioConstants.POSITION_NECK;
import static com.questio.projects.questio.utilities.QuestioConstants.QUESTIO_KEY;


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

    @BindView(R.id.avatar_toolbar)
    Toolbar toolbar;

    @BindView(R.id.avatar_head)
    ImageView avatarHead;

    @BindView(R.id.avatar_background)
    ImageView avatarBackground;

    @BindView(R.id.avatar_neck)
    ImageView avatarNeck;

    @BindView(R.id.avatar_top)
    ImageView avatarBody;

    @BindView(R.id.avatar_handleft)
    ImageView avatarHandLeft;

    @BindView(R.id.avatar_handright)
    ImageView avatarHandRight;

    @BindView(R.id.avatar_arms)
    ImageView avatarArms;

    @BindView(R.id.avatar_bottom)
    ImageView avatarLegs;

    @BindView(R.id.avatar_feet)
    ImageView avatarFoot;

    @BindView(R.id.avatar_aura)
    ImageView avatarSpecial;

    @BindView(R.id.button_head)
    ImageButton buttonHead;

    @BindView(R.id.button_background)
    ImageButton buttonBackground;

    @BindView(R.id.button_neck)
    ImageButton buttonNeck;

    @BindView(R.id.button_top)
    ImageButton buttonBody;

    @BindView(R.id.button_handleft)
    ImageButton buttonHandLeft;

    @BindView(R.id.button_handright)
    ImageButton buttonHandRight;

    @BindView(R.id.button_arms)
    ImageButton buttonArms;

    @BindView(R.id.button_bottom)
    ImageButton buttonBottom;

    @BindView(R.id.button_feet)
    ImageButton buttonFoot;

    @BindView(R.id.button_save)
    Button buttonSave;

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
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void populateAvatar() {
        api.getAvatarByAvatarId(adventurerId, QUESTIO_KEY, new Callback<Avatar[]>() {
            @Override
            public void success(Avatar[] avatars, Response response) {
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
            }

            @Override
            public void failure(RetrofitError error) {
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
                        if (items != null) {
                            for (Item item : items) {
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
                    }
                }

        );
    }

    public void showEquipDialog(final int position) {
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

    public void setButtonClick() {
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

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarBackground.setVisibility(View.INVISIBLE);
                View content = findViewById(R.id.avatar_profile_layout);
                try {
                    assert content != null;
                    content.setDrawingCacheEnabled(true);
                    Bitmap bitmap = content.getDrawingCache();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "questio_avatar.png");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                    fos.flush();
                    fos.close();
                    bitmap.recycle();
                    content.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    assert content != null;
                    content.setDrawingCacheEnabled(false);
                }
                avatarBackground.setVisibility(View.VISIBLE);
                Toast.makeText(AvatarActivity.this, "Profile saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void equipNewItem(final int partId, final long newItemId) {
        if (avatar.getAvatarId() != 0) {
            switch (partId) {
                case POSITION_HEAD:
                    oldItemId = avatar.getHeadId();
                    avatarHead.setVisibility(View.VISIBLE);
                    avatar.setHeadId(newItemId);
                    break;
                case POSITION_BACKGROUND:
                    oldItemId = avatar.getBackgroundId();
                    avatarBackground.setVisibility(View.VISIBLE);
                    avatar.setBackgroundId(newItemId);
                    break;
                case POSITION_NECK:
                    oldItemId = avatar.getNeckId();
                    avatarNeck.setVisibility(View.VISIBLE);
                    avatar.setNeckId(newItemId);
                    break;
                case POSITION_BODY:
                    oldItemId = avatar.getBodyId();
                    avatarBody.setVisibility(View.VISIBLE);
                    avatar.setBodyId(newItemId);
                    break;
                case POSITION_HANDLEFT:
                    oldItemId = avatar.getHandleftId();
                    avatarHandLeft.setVisibility(View.VISIBLE);
                    avatar.setHandleftId(newItemId);
                    break;
                case POSITION_HANDRIGHT:
                    oldItemId = avatar.getHandrightId();
                    avatarHandRight.setVisibility(View.VISIBLE);
                    avatar.setHandrightId(newItemId);
                    break;
                case POSITION_ARMS:
                    oldItemId = avatar.getArmId();
                    avatarArms.setVisibility(View.VISIBLE);
                    avatar.setArmId(newItemId);
                    break;
                case POSITION_LEGS:
                    oldItemId = avatar.getLegId();
                    avatarLegs.setVisibility(View.VISIBLE);
                    avatar.setLegId(newItemId);
                    break;
                case POSITION_FOOT:
                    oldItemId = avatar.getFootId();
                    avatarFoot.setVisibility(View.VISIBLE);
                    avatar.setFootId(newItemId);
                    break;
                case POSITION_AURA:
                    oldItemId = avatar.getSpecialId();
                    avatarSpecial.setVisibility(View.VISIBLE);
                    avatar.setSpecialId(newItemId);
                    break;
            }
        }
        api.equipNewItem(partId, newItemId, oldItemId, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (oldItemId != 0) {
                    if (newItemId != oldItemId) {
                        String status = QuestioHelper.getJSONStringValueByTag("status", response);
                        if (status.equalsIgnoreCase("1")) {
                            changeSpritePathByNewItemId(partId, newItemId);
                        }
                    } else {
                        String status = QuestioHelper.getJSONStringValueByTag("status", response);
                        if (status.equalsIgnoreCase("1")) {
                            unequipItem(partId, oldItemId);
                        }
                    }
                } else {
                    String status = QuestioHelper.getJSONStringValueByTag("status", response);
                    if (status.equalsIgnoreCase("1")) {
                        changeSpritePathByNewItemId(partId, newItemId);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void unequipItem(final int partId, long itemId) {
        api.unequipItem(partId, itemId, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String status = QuestioHelper.getJSONStringValueByTag("status", response);
                if (status.equalsIgnoreCase("1")) {
                    switch (partId) {
                        case POSITION_HEAD:
                            avatarHead.setVisibility(View.INVISIBLE);
                            avatar.setHeadId(0);
                            break;
                        case POSITION_BACKGROUND:
                            avatarBackground.setVisibility(View.INVISIBLE);
                            avatar.setBackgroundId(0);
                            break;
                        case POSITION_NECK:
                            avatarNeck.setVisibility(View.INVISIBLE);
                            avatar.setNeckId(0);
                            break;
                        case POSITION_BODY:
                            avatarBody.setVisibility(View.INVISIBLE);
                            avatar.setBodyId(0);
                            break;
                        case POSITION_HANDLEFT:
                            avatarHandLeft.setVisibility(View.INVISIBLE);
                            avatar.setHandleftId(0);
                            break;
                        case POSITION_HANDRIGHT:
                            avatarHandRight.setVisibility(View.INVISIBLE);
                            avatar.setHandrightId(0);
                            break;
                        case POSITION_ARMS:
                            avatarArms.setVisibility(View.INVISIBLE);
                            avatar.setArmId(0);
                            break;
                        case POSITION_LEGS:
                            avatarLegs.setVisibility(View.INVISIBLE);
                            avatar.setLegId(0);
                            break;
                        case POSITION_FOOT:
                            avatarFoot.setVisibility(View.INVISIBLE);
                            avatar.setFootId(0);
                            break;
                        case POSITION_AURA:
                            avatarSpecial.setVisibility(View.INVISIBLE);
                            avatar.setSpecialId(0);
                            break;
                    }
                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void changeSpritePathByNewItemId(final int partId, long newItemId) {
        api.getEquipSpritePathByItemId(newItemId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String url = QuestioHelper.getJSONStringValueByTag("equipspritepath", response);
                switch (partId) {
                    case POSITION_HEAD:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHead);
                        break;
                    case POSITION_BACKGROUND:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarBackground);
                        break;
                    case POSITION_NECK:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarNeck);
                        break;
                    case POSITION_BODY:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarBody);
                        break;
                    case POSITION_HANDLEFT:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHandLeft);
                        break;
                    case POSITION_HANDRIGHT:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarHandRight);
                        break;
                    case POSITION_ARMS:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarArms);
                        break;
                    case POSITION_LEGS:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarLegs);
                        break;
                    case POSITION_FOOT:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarFoot);
                        break;
                    case POSITION_AURA:
                        Glide.with(AvatarActivity.this)
                                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + url)
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
