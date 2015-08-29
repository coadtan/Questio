package com.questio.projects.questio.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.questio.projects.questio.R;
import com.questio.projects.questio.models.PicturePuzzle;
import com.questio.projects.questio.models.Reward;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PicturePuzzleAction extends ActionBarActivity implements View.OnClickListener, TextWatcher, Callback<Response> {
    private static final String LOG_TAG = PicturePuzzleAction.class.getSimpleName();

    @Bind(R.id.picture_puzzle_question)
    ImageView picturePuzzleQuestion;

    @Bind(R.id.topLeft)
    ImageView topLeft;

    @Bind(R.id.topMiddle)
    ImageView topMiddle;

    @Bind(R.id.topRight)
    ImageView topRight;

    @Bind(R.id.middleLeft)
    ImageView middleLeft;

    @Bind(R.id.middleMiddle)
    ImageView middleMiddle;

    @Bind(R.id.middleRight)
    ImageView middleRight;

    @Bind(R.id.bottomLeft)
    ImageView bottomLeft;

    @Bind(R.id.bottomMiddle)
    ImageView bottomMiddle;

    @Bind(R.id.bottomRight)
    ImageView bottomRight;

    @Bind(R.id.picture_puzzle_answer)
    EditText picturePuzzleAnswer;

    @Bind(R.id.picture_puzzle_showhint_btn)
    ImageButton picturePuzzleShowHintBtn;

    @Bind(R.id.app_bar)
    Toolbar toolbar;

    TextView pointTV;
    String currentAnswer;
    PicturePuzzle pp;
    int points;
    int ref;
    int qid;
    int zid;
    long adventurerId;
    Reward reward;

    int questStatus = QuestioConstants.QUEST_NOT_STARTED;
    QuestioAPIService api;
    RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_action);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pointTV = ButterKnife.findById(toolbar, R.id.toolbar_points);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String questId;
        String questName;
        String zoneId;
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
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

        getSupportActionBar().setTitle(questName);
        assert questId != null;
        requestPicturePuzzleData(Integer.parseInt(questId));
        qid = Integer.parseInt(questId);
        assert zoneId != null;
        zid = Integer.parseInt(zoneId);
        SharedPreferences prefs = getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, MODE_PRIVATE);
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        ref = Integer.parseInt(Integer.toString(qid) + (int) adventurerId);
        getCurrentPoints();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topLeft:
                topLeft.setVisibility(View.INVISIBLE);
                onUnMask(1);
                break;
            case R.id.topMiddle:
                topMiddle.setVisibility(View.INVISIBLE);
                onUnMask(2);
                break;
            case R.id.topRight:
                topRight.setVisibility(View.INVISIBLE);
                onUnMask(3);
                break;
            case R.id.middleLeft:
                middleLeft.setVisibility(View.INVISIBLE);
                onUnMask(4);
                break;
            case R.id.middleMiddle:
                middleMiddle.setVisibility(View.INVISIBLE);
                onUnMask(5);
                break;
            case R.id.middleRight:
                middleRight.setVisibility(View.INVISIBLE);
                onUnMask(6);
                break;
            case R.id.bottomLeft:
                bottomLeft.setVisibility(View.INVISIBLE);
                onUnMask(7);
                break;
            case R.id.bottomMiddle:
                bottomMiddle.setVisibility(View.INVISIBLE);
                onUnMask(8);
                break;
            case R.id.bottomRight:
                bottomRight.setVisibility(View.INVISIBLE);
                onUnMask(9);
                break;
            case R.id.picture_puzzle_showhint_btn:
                Toast.makeText(this, pp.getHelperAnswer(), Toast.LENGTH_LONG).show();
                break;
        }

    }

    void onUnMask(int position) {
        points--;
        pointTV.setText(Integer.toString(points));
        switch (position) {
            case 1:
                api.updatePuzzleProgressTopLeftPieceByRef(adventurerId, qid, this);
                break;
            case 2:
                api.updatePuzzleProgressTopMidPieceByRef(adventurerId, qid, this);
                break;
            case 3:
                api.updatePuzzleProgressTopRightPieceByRef(adventurerId, qid, this);
                break;
            case 4:
                api.updatePuzzleProgressMidLeftPieceByRef(adventurerId, qid, this);
                break;
            case 5:
                api.updatePuzzleProgressMidMidPieceByRef(adventurerId, qid, this);
                break;
            case 6:
                api.updatePuzzleProgressMidRightPieceByRef(adventurerId, qid, this);
                break;
            case 7:
                api.updatePuzzleProgressBottomLeftPieceByRef(adventurerId, qid, this);
                break;
            case 8:
                api.updatePuzzleProgressBottomMidPieceByRef(adventurerId, qid, this);
                break;
            case 9:
                api.updatePuzzleProgressBottomRightPieceByRef(adventurerId, qid, this);
                break;

        }
    }

    private void requestPicturePuzzleData(int id) {
        api.getPicturePuzzleByPuzzleId(id, new Callback<PicturePuzzle[]>() {
            @Override
            public void success(PicturePuzzle[] picturePuzzleTemp, Response response) {
                if (picturePuzzleTemp[0] != null) {
                    pp = picturePuzzleTemp[0];
                    Log.d(LOG_TAG, QuestioHelper.getImgLink(pp.getImageUrl()));
                    Glide.with(PicturePuzzleAction.this)
                            .load(QuestioHelper.getImgLink(pp.getImageUrl()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(picturePuzzleQuestion);
                    requestQuestProgress();
                    picturePuzzleShowHintBtn.setOnClickListener(PicturePuzzleAction.this);
                    picturePuzzleAnswer.addTextChangedListener(PicturePuzzleAction.this);
                    topLeft.setOnClickListener(PicturePuzzleAction.this);
                    topMiddle.setOnClickListener(PicturePuzzleAction.this);
                    topRight.setOnClickListener(PicturePuzzleAction.this);
                    middleLeft.setOnClickListener(PicturePuzzleAction.this);
                    middleMiddle.setOnClickListener(PicturePuzzleAction.this);
                    middleRight.setOnClickListener(PicturePuzzleAction.this);
                    bottomLeft.setOnClickListener(PicturePuzzleAction.this);
                    bottomMiddle.setOnClickListener(PicturePuzzleAction.this);
                    bottomRight.setOnClickListener(PicturePuzzleAction.this);
                } else {
                    Log.d(LOG_TAG, "Picture Puzzle is null");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
            }
        });
    }

    private void requestQuestProgress() {
        api.getQuestProgressByQuestIdAndAdventurerId(qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (QuestioHelper.responseToString(response).equalsIgnoreCase("null")) {
                    insertProgressData();
                } else {
                    String statusStr = QuestioHelper.getJSONStringValueByTag("statusid", response);
                    int status = Integer.parseInt(statusStr);
                    if (status == QuestioConstants.QUEST_FINISHED) {
                        questStatus = QuestioConstants.QUEST_FINISHED;
                        topLeft.setVisibility(View.INVISIBLE);
                        topRight.setVisibility(View.INVISIBLE);
                        topMiddle.setVisibility(View.INVISIBLE);
                        middleLeft.setVisibility(View.INVISIBLE);
                        middleMiddle.setVisibility(View.INVISIBLE);
                        middleRight.setVisibility(View.INVISIBLE);
                        bottomLeft.setVisibility(View.INVISIBLE);
                        bottomMiddle.setVisibility(View.INVISIBLE);
                        bottomRight.setVisibility(View.INVISIBLE);
                        disableAll();
                        picturePuzzleAnswer.setText(pp.getCorrectAnswer());
                        picturePuzzleAnswer.setEnabled(false);
                        picturePuzzleAnswer.setClickable(false);
                    } else {
                        api.getPuzzleProgressByRef(adventurerId, qid, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("topleftopened", response)) == 1) {
                                    topLeft.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("topmidopened", response)) == 1) {
                                    topMiddle.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("toprightopened", response)) == 1) {
                                    topRight.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("midleftopened", response)) == 1) {
                                    middleLeft.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("midmidopened", response)) == 1) {
                                    middleMiddle.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("midrightopened", response)) == 1) {
                                    middleRight.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("bottomleftopened", response)) == 1) {
                                    bottomLeft.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("bottommidopened", response)) == 1) {
                                    bottomMiddle.setVisibility(View.INVISIBLE);
                                }
                                if (Integer.parseInt(QuestioHelper.getJSONStringValueByTag("bottomrightopened", response)) == 1) {
                                    bottomRight.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(LOG_TAG, error.getMessage());
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
        Log.d(LOG_TAG, "No Progress in Quest");
        api.addQuestProgress(qid, adventurerId, zid, 3, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);
                insertPuzzleProgress();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void insertPuzzleProgress() {
        Log.d(LOG_TAG, "No Progress in Puzzle");
        api.addPuzzleProgress(adventurerId, qid, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String questioStatus = QuestioHelper.responseToString(response);
                Log.d(LOG_TAG, "Add Quest Progress: " + qid + " " + questioStatus);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateQuestStatus(int status) {
        Log.d(LOG_TAG, "updateQuestStatus: called");
        api.updateStatusQuestProgressByQuestIdAndAdventurerId(status, qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateScore() {
        api.updateScoreQuestProgressByQuestIdAndAdventurerId(points, qid, adventurerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(LOG_TAG, "updateScore: success with points = " + points);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "updateScore: failed");
            }
        });
    }

    private void disableAll() {
        topRight.setClickable(false);
        topMiddle.setClickable(false);
        topLeft.setClickable(false);
        middleRight.setClickable(false);
        middleMiddle.setClickable(false);
        middleLeft.setClickable(false);
        bottomRight.setClickable(false);
        bottomMiddle.setClickable(false);
        bottomLeft.setClickable(false);
        topRight.setEnabled(false);
        topMiddle.setEnabled(false);
        topLeft.setEnabled(false);
        bottomRight.setEnabled(false);
        bottomMiddle.setEnabled(false);
        bottomLeft.setEnabled(false);
        middleRight.setEnabled(false);
        middleMiddle.setEnabled(false);
        middleLeft.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (questStatus != QuestioConstants.QUEST_FINISHED && questStatus != QuestioConstants.QUEST_FAILED) {
            currentAnswer = picturePuzzleAnswer.getText().toString();
            if (currentAnswer.equalsIgnoreCase(pp.getCorrectAnswer())) {
                onCorrect();
            }
        }
    }

    private void onCorrect() {
        picturePuzzleAnswer.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
        updateQuestStatus(QuestioConstants.QUEST_FINISHED);
        updateScore();
        topLeft.setVisibility(View.INVISIBLE);
        topRight.setVisibility(View.INVISIBLE);
        topMiddle.setVisibility(View.INVISIBLE);
        middleLeft.setVisibility(View.INVISIBLE);
        middleMiddle.setVisibility(View.INVISIBLE);
        middleRight.setVisibility(View.INVISIBLE);
        bottomLeft.setVisibility(View.INVISIBLE);
        bottomMiddle.setVisibility(View.INVISIBLE);
        bottomRight.setVisibility(View.INVISIBLE);
        disableAll();
        picturePuzzleAnswer.setEnabled(false);
        picturePuzzleAnswer.setClickable(false);
        api.getRewardByQuestId(qid, new Callback<Reward[]>() {
            @Override
            public void success(Reward[] rewards, Response response) {
                if (rewards != null) {
                    reward = rewards[0];
                    api.getCountHOFByAdventurerIdAndRewardId(adventurerId, reward.getRewardId(), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int rewardCount = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("hofcount", response));
                            Log.d(LOG_TAG, "Reward count: " + rewardCount);
                            if (rewardCount == 0) {
                                addRewardHOF(reward.getRewardId(), QuestioConstants.REWARD_RANK_NORMAL);
                                showObtainRewardDialog(QuestioConstants.REWARD_RANK_NORMAL);
                            }
                            showCompleteDialog(points);
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

    private void getCurrentPoints() {
        api.getCurrentPointsByRef(adventurerId, qid, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                points = Integer.parseInt(QuestioHelper.getJSONStringValueByTag("points", QuestioHelper.responseToString(response)));
                pointTV.setText(Integer.toString(points));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void success(Response response, Response response2) {

    }

    @Override
    public void failure(RetrofitError error) {

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
        String puzzleScore = "คุณได้รับ " + Integer.toString(score) + " แต้ม";
        dialog.withMessage(puzzleScore);
//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }

    public void addRewardHOF(int rewardId, int rank) {
        api.addRewards(adventurerId, rewardId, rank, new Callback<Response>() {
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
//        Button closeBtn = ButterKnife.findById(dialog, R.id.button_obtain_reward_close);

        String rewardName = reward.getRewardName();
        tvRewardName.setText(rewardName);
        String rewardRank = "";
        if (rank == QuestioConstants.REWARD_RANK_NORMAL) {
            rewardRank = "ระดับปกติ";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_BRONZE) {
            rewardRank = "ระดับทองแดง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new SepiaFilterTransformation(this, Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_SILVER) {
            rewardRank = "ระดับเงิน";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
                    .bitmapTransform(new GrayscaleTransformation(Glide.get(this).getBitmapPool()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rewardPicture);
        } else if (rank == QuestioConstants.REWARD_RANK_GOLD) {
            rewardRank = "ระดับทอง";
            Glide.with(this)
                    .load(QuestioConstants.BASE_URL + reward.getRewardPic())
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
}
