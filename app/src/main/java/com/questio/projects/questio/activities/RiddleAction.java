package com.questio.projects.questio.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import net.sourceforge.zbar.Symbol;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RiddleAction extends AppCompatActivity implements View.OnClickListener, Callback<Response> {
    private static final String LOG_TAG = RiddleAction.class.getSimpleName();

    @Bind(R.id.app_bar)
    Toolbar toolbar;

    @Bind(R.id.riddle_riddle)
    FlatTextView riddle;

    @Bind(R.id.riddle_hint1Btn)
    FlatButton hint1Btn;

    @Bind(R.id.riddle_hint2Btn)
    FlatButton hint2Btn;

    @Bind(R.id.riddle_hint3Btn)
    FlatButton hint3Btn;

    @Bind(R.id.riddle_scanHere)
    ImageButton scanHere;

    @Bind(R.id.riddle_hintReveal1)
    FlatTextView hintReveal1;

    @Bind(R.id.riddle_hintReveal2)
    FlatTextView hintReveal2;

    @Bind(R.id.riddle_hintReveal3)
    FlatTextView hintReveal3;

    @Bind(R.id.toolbar_limit)
    TextView scanTV;

    int points;
    int scanLimit;
    int qid;
    int zid;
    long adventurerId;
    RestAdapter adapter;
    QuestioAPIService api;
    int ref;
    Reward reward;

    Riddle r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.ORANGE);
        setContentView(R.layout.riddle_action);
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
        String questId;
        String questName;
        String zoneId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                questId = null;
                questName = null;
                zoneId = null;
            } else {
                questId = extras.getString(QuestioConstants.QUEST_ID);
                questName = extras.getString(QuestioConstants.QUEST_NAME);
                zoneId = extras.getString(QuestioConstants.QUEST_ZONE_ID);
            }
        } else {
            questId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ID);
            questName = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_NAME);
            zoneId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ZONE_ID);
        }
        Log.d(LOG_TAG, "questid: " + questId + " questName: " + questName);

        getSupportActionBar().setTitle(questName);

        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        qid = Integer.parseInt(questId);
        zid = Integer.parseInt(zoneId);
        ref = Integer.parseInt(Integer.toString(qid) + (int) adventurerId);

        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
        //r = Riddle.getAllRiddleByRiddleId((Integer.parseInt(questId)));
        requestRiddleData(Integer.parseInt(questId));


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.riddle_scanHere:
                Intent intent = new Intent(this, ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);
                break;
            case R.id.riddle_hint1Btn:
                hintReveal1.setText(r.getHint1());
                hint1Btn.setEnabled(false);
                hint1Btn.setClickable(false);
                api.updateRiddleProgressHint1ByRef(adventurerId, qid, QuestioConstants.QUESTIO_KEY, this);
                break;
            case R.id.riddle_hint2Btn:
                hintReveal2.setText(r.getHint2());
                hint2Btn.setEnabled(false);
                hint2Btn.setClickable(false);
                api.updateRiddleProgressHint2ByRef(adventurerId, qid, QuestioConstants.QUESTIO_KEY, this);
                break;
            case R.id.riddle_hint3Btn:
                hintReveal3.setText(r.getHint3());
                hint3Btn.setEnabled(false);
                hint3Btn.setClickable(false);
                api.updateRiddleProgressHint3ByRef(adventurerId, qid, QuestioConstants.QUESTIO_KEY, this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode: " + requestCode);
        Log.d(LOG_TAG, "Activity.RESULT_OK: " + Activity.RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {

            String[] qr = QuestioHelper.getDeQRCode(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            Log.d(LOG_TAG, "qr[0] = " + qr[0] + "qr[1] = " + qr[1]);
            if (qr[0].equalsIgnoreCase(QuestioConstants.QRTYPE_RIDDLE_ANSWER)) {
                onAnswer(qr[1]);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void onAnswer(String answer) {
        if (scanLimit != 0) {
            if (answer.equalsIgnoreCase(Long.toString(r.getQrCode()))) {
                riddle.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
                updateQuestStatus(QuestioConstants.QUEST_FINISHED);
                updateScoreToQuestProgress();
                onQuestFinish();
            } else {
                scanLimit--;
                scanTV.setText(Integer.toString(scanLimit));
                api.updateRiddleProgressScanLimitByRef(scanLimit, adventurerId, qid, QuestioConstants.QUESTIO_KEY, this);
                if (scanLimit == 0) {
                    updateQuestStatus(QuestioConstants.QUEST_FAILED);
                    showCompleteDialog(points);
                    onQuestFinish();
                }
            }
        }
    }

    private void updateScoreToQuestProgress() {
        api.getCurrentRiddlePointByRef(adventurerId, qid, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updateScoreToQuestProgressTest: response = " + QuestioHelper.responseToString(response));
                Log.d(LOG_TAG, "updateScoreToQuestProgressTest: response/points = " + QuestioHelper.getJSONStringValueByTag("points", response));
                Log.d(LOG_TAG, "updateScoreToQuestProgressTest: ref = " + ref);
                Log.d(LOG_TAG, "updateScoreToQuestProgressTest: response2 = " + response2.getUrl());


                points = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("points", response));
                showCompleteDialog(points);
                api.updateScoreQuestProgressByQuestIdAndAdventurerId(points, qid, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void requestRiddleData(int id) {
        api.getRiddleByQuestId(id, QuestioConstants.QUESTIO_KEY, new Callback<Riddle[]>() {
            @Override
            public void success(Riddle[] riddleTemp, Response response) {
                if (riddleTemp != null) {
                    r = riddleTemp[0];
                    Log.d(LOG_TAG, r.toString());
                    if (r.getHint1().equalsIgnoreCase("")) {
                        hint1Btn.setEnabled(false);
                        hint1Btn.setClickable(false);
                        hint1Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal1.setVisibility(View.INVISIBLE);
                    }

                    if (r.getHint2().equalsIgnoreCase("")) {
                        hint2Btn.setEnabled(false);
                        hint2Btn.setClickable(false);
                        hint2Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal2.setVisibility(View.INVISIBLE);
                    }
                    if (r.getHint3().equalsIgnoreCase("")) {
                        hint3Btn.setEnabled(false);
                        hint3Btn.setClickable(false);
                        hint3Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                        hintReveal3.setVisibility(View.INVISIBLE);
                    }

                    riddle.setText(r.getRidDetails());

                    scanLimit = r.getScanLimit();

                    hint1Btn.setOnClickListener(RiddleAction.this);
                    hint2Btn.setOnClickListener(RiddleAction.this);
                    hint3Btn.setOnClickListener(RiddleAction.this);
                    scanHere.setOnClickListener(RiddleAction.this);

                    scanTV.setText(Integer.toString(scanLimit));

                    requestProgressData();

                } else {
                    Log.d(LOG_TAG, "Riddle is null");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
            }
        });
    }

    private void requestProgressData() {

        api.getQuestProgressByQuestIdAndAdventurerId(qid, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (QuestioHelper.responseToString(response).equalsIgnoreCase("null")) {
                    insertProgressData();

                } else {
                    String statusStr = QuestioHelper.getJSONStringValueByTag("statusid", response);
                    int status = Integer.parseInt(statusStr);
                    if (status == QuestioConstants.QUEST_FINISHED || status == QuestioConstants.QUEST_FAILED) {
                        onQuestFinish();
                    } else {
                        api.getRiddleProgressByRef(adventurerId, qid, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                String scanLimitStr = QuestioHelper.getJSONStringValueByTag("scanlimit", response);
                                scanLimit = Integer.parseInt(scanLimitStr);
                                scanTV.setText(Integer.toString(scanLimit));
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hint1opened", response)) == 1) {
                                    hint1Btn.performClick();
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hint2opened", response)) == 1) {
                                    hint2Btn.performClick();
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hint3opened", response)) == 1) {
                                    hint3Btn.performClick();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void insertProgressData() {
        api.addQuestProgress(qid, adventurerId, zid, 2, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);
                insertRiddleProgress();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void insertRiddleProgress() {
        api.addRiddleProgress(adventurerId, qid, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateQuestStatus(int status) {
        api.updateStatusQuestProgressByQuestIdAndAdventurerId(status, qid, adventurerId, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    // Nothing to do with this
    @Override
    public void success(Response response, Response response2) {

    }

    @Override
    public void failure(RetrofitError error) {

    }

    private void onQuestFinish() {
        scanHere.setEnabled(false);
        scanHere.setClickable(false);
        hintReveal1.setText(r.getHint1());
        hintReveal2.setText(r.getHint2());
        hintReveal3.setText(r.getHint3());
        hint1Btn.setEnabled(false);
        hint1Btn.setClickable(false);
        hint2Btn.setEnabled(false);
        hint2Btn.setClickable(false);
        hint3Btn.setEnabled(false);
        hint3Btn.setClickable(false);

    }

    void showCompleteDialog(int score) {
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(this);
        dialog
                .withTitle("Puzzle Complete")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("Close")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .isCancelableOnTouchOutside(false);
//        final Dialog dialog = new Dialog(PicturePuzzleAction.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.quest_finished_puzzle_dialog);
//        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
//        dialog.setCancelable(true);
//        TextView puzzleScoreTV = (TextView) dialog.findViewById(R.id.dialog_puzzle_score);
        //Button goBack = (Button) dialog.findViewById(R.id.button_puzzle_goback);
        String riddleScore = "คุณได้รับ " + Integer.toString(score) + " แต้ม";
        dialog.withMessage(riddleScore);
//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }

    public void addRewardHOF(int rewardId, int rank) {
        api.addRewards(adventurerId, rewardId, rank, QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    void showObtainRewardDialog(int rank) {
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(this);
        dialog
                .withTitle("Obtain Reward")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("You got reward:")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withDuration(300)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("Close")
                .isCancelableOnTouchOutside(false)
                .setCustomView(R.layout.reward_obtain_dialog, this);
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.reward_obtain_dialog);
//        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//        dialog.getWindow().setBackgroundDrawable(transparentDrawable);
//        dialog.setCancelable(true);
        ImageView rewardPicture = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_picture);
        TextView tvRewardName = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_name);
        TextView tvRewardRank = ButterKnife.findById(dialog, R.id.dialog_obtain_reward_rank);
        //Button closeBtn = ButterKnife.findById(dialog, R.id.button_obtain_reward_close);

        String rewardName = reward.getRewardName();
        tvRewardName.setText(rewardName);
        String rewardRank = "";
        if (rank == QuestioConstants.REWARD_RANK_NORMAL) {
            rewardRank = "ระดับปกติ";
            Glide.with(this)
                    .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + reward.getRewardPic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_BRONZE) {
            rewardRank = "ระดับทองแดง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + reward.getRewardPic())
                    .bitmapTransform(new SepiaFilterTransformation(this, Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_SILVER) {
            rewardRank = "ระดับเงิน";
            Glide.with(this)
                    .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + reward.getRewardPic())
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_GOLD) {
            rewardRank = "ระดับทอง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + reward.getRewardPic())
                    .bitmapTransform(new BrightnessFilterTransformation(this, Glide.get(this).getBitmapPool(), 0.5f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        }

        tvRewardRank.setText(rewardRank);

        dialog.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();
    }


    void checkRewards() {
        api.getRewardByQuestId(qid, QuestioConstants.QUESTIO_KEY, new Callback<Reward[]>() {
            @Override
            public void success(Reward[] rewards, Response response) {
                if (rewards != null) {
                    reward = rewards[0];
                    api.getCountHOFByAdventurerIdAndRewardId(adventurerId, reward.getRewardId(), QuestioConstants.QUESTIO_KEY, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int rewardCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hofcount", response));
                            Log.d(LOG_TAG, "Reward count: " + rewardCount);
                            if (rewardCount == 0) {
                                addRewardHOF(reward.getRewardId(), QuestioConstants.REWARD_RANK_NORMAL);
                                showObtainRewardDialog(QuestioConstants.REWARD_RANK_NORMAL);
                            } else {
                                onBackPressed();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(LOG_TAG, "checkRewardData: failure");
                        }
                    });

                } else {
                    onBackPressed();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
