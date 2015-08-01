package com.questio.projects.questio.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.questio.projects.questio.R;
import com.questio.projects.questio.adepters.QuestInActionAdapter;
import com.questio.projects.questio.models.Item;
import com.questio.projects.questio.models.Quest;
import com.questio.projects.questio.models.QuestStatusAndScore;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.models.Zone;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ZoneActivity extends ActionBarActivity {
    private static final String LOG_TAG = ZoneActivity.class.getSimpleName();
    Toolbar toolbar;
    ArrayList<Quest> questsList;
    ArrayList<QuestStatusAndScore> statusList;
    private ListView questListview;
    int zoneId;
    Zone zone;
    ImageView questActionImg;
    ImageView questActionMiniImg;
    ImageView itemPic;
    ImageView rewardPic;
    ImageView zonetype;
    ImageView iconQuestProgress;
    ImageView iconScoreProgress;
    String qrcode;
    RestAdapter adapter;
    QuestioAPIService api;
    long adventurerId;
    QuestInActionAdapter adapterQuestList = null;
    ProgressBar questActionQuizfinishProgressbar;
    ProgressBar questActionScoreGainProgressbar;
    Item item;
    Reward reward;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("qrcode", qrcode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_action);
        handleView();
        handleToolbar();
        handleInstanceState(savedInstanceState);
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        int zoneIdFromQRCode = Zone.findZoneIdByQRCode(Integer.parseInt(qrcode));
        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);

        zoneId = zoneIdFromQRCode;
        Log.d(LOG_TAG, "zoneIdFromQRCode is " + zoneIdFromQRCode);
        requestZoneData(zoneIdFromQRCode);
        requestQuestData(zoneIdFromQRCode);

        questListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView questId = (TextView) view.findViewById(R.id.questid);
                TextView questName = (TextView) view.findViewById(R.id.questname);
                TextView questTypeInvisible = (TextView) view.findViewById(R.id.questTypeInvisible);
                TextView zoneId = (TextView) view.findViewById(R.id.quest_zoneId);
                String questIdForIntent = questId.getText().toString();
                String questNameForIntent = questName.getText().toString();
                String zoneIdForIntent = zoneId.getText().toString();
                switch (Integer.parseInt(questTypeInvisible.getText().toString())) {
                    case 1:
                        Intent intentToQuiz = new Intent(ZoneActivity.this, QuizActivity.class);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToQuiz.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);
                        startActivity(intentToQuiz);
                        break;
                    case 2:
                        Intent intentToRiddle = new Intent(ZoneActivity.this, RiddleAction.class);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToRiddle.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);
                        startActivity(intentToRiddle);
                        break;
                    case 3:
                        Intent intentToPuzzle = new Intent(ZoneActivity.this, PicturePuzzleAction.class);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_ID, questIdForIntent);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_NAME, questNameForIntent);
                        intentToPuzzle.putExtra(QuestioConstants.QUEST_ZONE_ID, zoneIdForIntent);
                        startActivity(intentToPuzzle);
                        break;
                }


            }
        });

        iconQuestProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ZoneActivity.this, "This is your zone progress", Toast.LENGTH_SHORT).show();
            }
        });

        iconScoreProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ZoneActivity.this, "Your current point in this zone", Toast.LENGTH_SHORT).show();
            }
        });

        zonetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int zoneTypeId = zone.getZoneTypeId();
                String zoneTypeDetail = "";
                switch (zoneTypeId) {
                    case 1:
                        zoneTypeDetail = "Thai";
                        break;
                    case 2:
                        zoneTypeDetail = "Math";
                        break;
                    case 3:
                        zoneTypeDetail = "Science";
                        break;
                    case 4:
                        zoneTypeDetail = "Social Studies";
                        break;
                    case 5:
                        zoneTypeDetail = "Health";
                        break;
                    case 6:
                        zoneTypeDetail = "English";
                        break;
                    case 7:
                        zoneTypeDetail = "Art";
                        break;
                    case 8:
                        zoneTypeDetail = "Career and Technology";
                        break;
                    case 9:
                        zoneTypeDetail = "Misc.";
                        break;
                }
                Toast.makeText(ZoneActivity.this, "This zone is about " + zoneTypeDetail, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestRewardData(int id) {
        api.getRewardByZoneId(id, new Callback<Reward[]>() {
            @Override
            public void success(Reward[] rewards, Response response) {
                if (rewards != null) {
                    reward = rewards[0];
                    Log.d(LOG_TAG, rewards[0].toString());
                    Glide.with(ZoneActivity.this)
                            .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(rewardPic);
                } else {
                    Log.d(LOG_TAG, "Item: null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestItemData: failure");

            }
        });
    }

    private void requestItemData(int id) {
        api.getItemByZoneId(id, new Callback<Item[]>() {
            @Override
            public void success(Item[] items, Response response) {
                if (items != null) {
                    item = items[0];
                    Log.d(LOG_TAG, item.toString());
                    Glide.with(ZoneActivity.this)
                            .load(QuestioConstants.BASE_URL + item.getItemPicPath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(itemPic);
                } else {
                    Log.d(LOG_TAG, "Item: null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestItemData: failure");
            }
        });
    }

    private void handleView() {
        questActionImg = (ImageView) findViewById(R.id.quest_action_picture);
        questActionMiniImg = (ImageView) findViewById(R.id.quest_action_minimap);
        iconQuestProgress = (ImageView) findViewById(R.id.icon_quest_finish);
        iconScoreProgress = (ImageView) findViewById(R.id.icon_score_progress);
        itemPic = (ImageView) findViewById(R.id.quest_action_item_picture);
        rewardPic = (ImageView) findViewById(R.id.quest_action_reward_picture);
        zonetype = (ImageView) findViewById(R.id.quest_action_zonetype_picture);
        questListview = (ListView) findViewById(R.id.quest_action_listview);
        questActionQuizfinishProgressbar = (ProgressBar) findViewById(R.id.quest_action_quizfinish_progressbar);
        questActionScoreGainProgressbar = (ProgressBar) findViewById(R.id.quest_action_scoregain_progressbar);
    }

    private void handleToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("กำลังโหลดข้อมูล");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Log.d(LOG_TAG, "savedInstanceState: null");
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                qrcode = null;
            } else {
                qrcode = extras.getString("qrcode");
            }
        } else {
            Log.d(LOG_TAG, "savedInstanceState: !null");
            //qrcode = (String) savedInstanceState.getSerializable("qrcode");
            qrcode = savedInstanceState.getString("qrcode");
        }
        Log.d(LOG_TAG, "qrcode: " + qrcode);
        Log.d(LOG_TAG, "savedInstanceState: " + savedInstanceState);
    }

    private void requestQuestData(final int id) {
        Log.d(LOG_TAG, "requestQuestData called");
        api.getAllQuestByZoneId(id, new Callback<ArrayList<Quest>>() {
            @Override
            public void success(final ArrayList<Quest> quests, Response response) {
                Log.d(LOG_TAG, "requestQuestData success");
                if (quests != null) {
                    questsList = quests;
                    api.getQuestStatusAndScoreByZoneAdventurerid(id, adventurerId, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            statusList = QuestStatusAndScore.createStatusList(response);
                            if (statusList != null) {
                                int questFinished = 0;
                                int scoreGain = 0;
                                for (QuestStatusAndScore q : statusList) {
                                    if (q.getStatus() == QuestioConstants.QUEST_FINISHED || q.getStatus() == QuestioConstants.QUEST_FAILED) {
                                        questFinished++;
                                        scoreGain += q.getScore();
                                    }
                                }
                                double totalQuestInZone = questsList.size();
                                questActionQuizfinishProgressbar.setProgress(QuestioHelper.getPercentFrom2ValueAsInt(questFinished, totalQuestInZone));
                                questActionScoreGainProgressbar.setProgress(QuestioHelper.getPercentFrom2ValueAsInt(scoreGain, totalQuestInZone * 10));
                            }

                            adapterQuestList = new QuestInActionAdapter(ZoneActivity.this, questsList, statusList);
                            questListview.setAdapter(adapterQuestList);
                            adapterQuestList.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(LOG_TAG, "requestQuestData failure");
                        }
                    });
                } else {
                    Log.d(LOG_TAG, "quests: is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "requestQuestData: failure");
            }
        });
    }

    private void requestZoneData(final int id) {
        api.getZoneByZoneId(id, new Callback<Zone[]>() {
            @Override
            public void success(Zone[] zones, Response response) {
                if (zones != null) {
                    zone = zones[0];
                    getSupportActionBar().setTitle(zone.getZoneName());
                    //item.setText(zone.getItemSet());
                    //reward.setText(Integer.toString(zone.getRewardId()));

                    if (!(zone.getImageUrl() == null)) {
                        Glide.with(ZoneActivity.this)
                                .load(QuestioHelper.getImgLink(zone.getImageUrl()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(questActionImg);
                    }
                    if (!(zone.getMiniMapUrl() == null)) {
                        Glide.with(ZoneActivity.this)
                                .load(QuestioHelper.getImgLink(zone.getMiniMapUrl()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(questActionMiniImg);
                    }
                    if (!(zone.getZoneTypeImage() == null)) {
                        Glide.with(ZoneActivity.this)
                                .load(QuestioHelper.getImgLink(zone.getZoneTypeImage()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(zonetype);
                    }
                    questActionMiniImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog nagDialog = new Dialog(ZoneActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.setCancelable(true);
                            nagDialog.setContentView(R.layout.mini_map_full_screen);
                            ImageView imgPreview = (ImageView) nagDialog.findViewById(R.id.mini_map_full_view);
                            imgPreview.setBackgroundDrawable(questActionMiniImg.getDrawable());
                            imgPreview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nagDialog.dismiss();
                                }
                            });
                            nagDialog.show();
                        }
                    });
                    requestItemData(id);
                    requestRewardData(id);

                } else {
                    Log.d(LOG_TAG, "Zone data is null");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume: called");
        if (adapterQuestList != null) {
            api.getQuestStatusAndScoreByZoneAdventurerid(zoneId, adventurerId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    statusList = QuestStatusAndScore.createStatusList(response);
                    if (statusList != null) {
                        int questFinished = 0;
                        int scoreGain = 0;
                        for (QuestStatusAndScore q : statusList) {
                            if (q.getStatus() == QuestioConstants.QUEST_FINISHED || q.getStatus() == QuestioConstants.QUEST_FAILED) {
                                questFinished++;
                                scoreGain += q.getScore();
                            }
                        }
                        double totalQuestInZone = questsList.size();
                        questActionQuizfinishProgressbar.setProgress(QuestioHelper.getPercentFrom2ValueAsInt(questFinished, totalQuestInZone));
                        questActionScoreGainProgressbar.setProgress(QuestioHelper.getPercentFrom2ValueAsInt(scoreGain, totalQuestInZone * 10));
                        if (questFinished == totalQuestInZone) {
                            api.getCountInventoryByAdventurerIdAndItemId(adventurerId, item.getItemId(), new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    int itemCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("inventorycount", response));
                                    Log.d(LOG_TAG, "Item count: " + itemCount);
                                    if (itemCount == 0) {
                                        showObtainItemDialog();
                                        api.addInventory(adventurerId, item.getItemId(), new Callback<Response>() {
                                            @Override
                                            public void success(Response response, Response response2) {

                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d(LOG_TAG, "checkItemData: failure");
                                }
                            });

                            api.getCountHOFByAdventurerIdAndRewardId(adventurerId, reward.getRewardId(), new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    int rewardCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hofcount", response));
                                    Log.d(LOG_TAG, "Reward count: " + rewardCount);
                                    if (rewardCount == 0) {
                                        int rank = calculateZoneReward();
                                        showObtainRewardDialog(rank);
                                        addRewardHOF(reward.getRewardId(), rank);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d(LOG_TAG, "checkRewardData: failure");
                                }
                            });



                        }

                    }
                    adapterQuestList = new QuestInActionAdapter(ZoneActivity.this, questsList, statusList);
                    questListview.setAdapter(adapterQuestList);
                    adapterQuestList.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            Log.d(LOG_TAG, "onResume: is null");
        }

        super.onResume();
    }

    void showObtainItemDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_obtain_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        ImageView tvItemPicture = (ImageView) dialog.findViewById(R.id.dialog_obtain_item_picture);
        TextView tvItemName = (TextView) dialog.findViewById(R.id.dialog_obtain_item_name);
        Button closeBtn = (Button) dialog.findViewById(R.id.button_obtain_item_close);

        String obtainedName = item.getItemName();
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + item.getItemPicPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tvItemPicture);

        tvItemName.setText(obtainedName);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    void showObtainRewardDialog(int rank){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reward_obtain_dialog);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
        dialog.setCancelable(true);
        ImageView rewardPicture = (ImageView) dialog.findViewById(R.id.dialog_obtain_reward_picture);
        TextView tvRewardName = (TextView) dialog.findViewById(R.id.dialog_obtain_reward_name);
        TextView tvRewardRank = (TextView) dialog.findViewById(R.id.dialog_obtain_reward_rank);
        Button closeBtn = (Button) dialog.findViewById(R.id.button_obtain_reward_close);

        String rewardName = reward.getRewardName();


        tvRewardName.setText(rewardName);
        String rewardRank = "";
        if(rank == QuestioConstants.REWARD_RANK_NORMAL){
            rewardRank = "ระดับปกติ";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }else if(rank == QuestioConstants.REWARD_RANK_BRONZE){
            rewardRank = "ระดับทองแดง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new SepiaFilterTransformation(this, Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }else if(rank == QuestioConstants.REWARD_RANK_SILVER){
            rewardRank = "ระดับเงิน";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }else if(rank == QuestioConstants.REWARD_RANK_GOLD){
            rewardRank = "ระดับทอง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new BrightnessFilterTransformation(this, Glide.get(this).getBitmapPool(), 0.5f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }

        tvRewardRank.setText(rewardRank);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public int calculateZoneReward(){
        int scoreGain = questActionScoreGainProgressbar.getProgress();
        if(scoreGain <= 30){
            return QuestioConstants.REWARD_RANK_NORMAL;
        }else if (scoreGain > 30 && scoreGain <= 60){
            return QuestioConstants.REWARD_RANK_BRONZE;
        }else if (scoreGain > 60 && scoreGain <= 90){
            return QuestioConstants.REWARD_RANK_SILVER;
        }else{
            return QuestioConstants.REWARD_RANK_GOLD;
        }

    }

    public void addRewardHOF(int rewardId, int rank){
        api.addRewards(adventurerId, rewardId, rank, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
