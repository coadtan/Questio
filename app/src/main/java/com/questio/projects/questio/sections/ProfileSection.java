package com.questio.projects.questio.sections;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.AvatarActivity;
import com.questio.projects.questio.activities.HOFActivity;
import com.questio.projects.questio.activities.HOFPlaceActivity;
import com.questio.projects.questio.activities.InventoryActivity;
import com.questio.projects.questio.activities.LoginActivity;
import com.questio.projects.questio.adepters.RewardsAdapter;
import com.questio.projects.questio.models.RewardHOF;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileSection extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = ProfileSection.class.getSimpleName();
    Context mContext;
    @Bind(R.id.profile_picture)
    ImageView profilePicture;

    @Bind(R.id.profile_inventory)
    ImageButton profileInventory;

    @Bind(R.id.profile_place_rewards_button)
    ImageButton placeRewardsButton;
    @Bind(R.id.profile_rewards_button)
    ImageButton rewardsButton;
//    @Bind(R.id.halloffame)
//    GridView hallOfFame;

    View view;
    Person currentPerson;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    RestAdapter adapter;
    long adventurerId;
    QuestioAPIService api;
    ArrayList<RewardHOF> rewards;
    RewardsAdapter rewardsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        prefs = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        editor = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        currentPerson = Plus.PeopleApi
                .getCurrentPerson(QuestioApplication.mGoogleApiClient);
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
    }

    public void init() {
        requestRewardsHOFData(adventurerId);
//        hallOfFame.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_profile, container, false);
        ButterKnife.bind(this, view);
        init();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAvatar = new Intent(getActivity(), AvatarActivity.class);
                intentToAvatar.putExtra(QuestioConstants.ADVENTURER_ID, adventurerId);
                getActivity().startActivity(intentToAvatar);
            }
        });
        placeRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToPlaceHOF = new Intent(getActivity(), HOFPlaceActivity.class);
                intentToPlaceHOF.putExtra(QuestioConstants.ADVENTURER_ID, adventurerId);
                getActivity().startActivity(intentToPlaceHOF);
            }
        });
        rewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHOF = new Intent(getActivity(), HOFActivity.class);
                intentToHOF.putExtra(QuestioConstants.ADVENTURER_ID, adventurerId);
                getActivity().startActivity(intentToHOF);
            }
        });
//        Glide.with(this)
//                .load(currentPerson.getImage().getUrl())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(profilePicture);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOutFromGplus();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_adventurer_section, menu);
    }

    private void signOutFromGplus() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        if (QuestioApplication.mGoogleApiClient.isConnected()) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirm_signout_dialog);
            Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialog.getWindow().setBackgroundDrawable(transparentDrawable);
            dialog.setCancelable(true);
            TextView TVConfirmName = ButterKnife.findById(dialog, R.id.dialog_confirm_name);
            ImageView imageViewConfirmPicture = ButterKnife.findById(dialog, R.id.dialog_confirm_picture);


            String personName = currentPerson.getDisplayName();
            Glide.with(this)
                    .load(currentPerson.getImage().getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewConfirmPicture);
            TVConfirmName.setText(personName);
            ImageButton btnNo = ButterKnife.findById(dialog, R.id.confirm_no);
            btnNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            ImageButton btnYes = ButterKnife.findById(dialog, R.id.confirm_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Plus.AccountApi.clearDefaultAccount(QuestioApplication.mGoogleApiClient);
                    QuestioApplication.mGoogleApiClient.disconnect();
                    QuestioApplication.setLogin(false);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    dialog.cancel();
                }
            });

            dialog.show();
        }
    }

    private void requestRewardsHOFData(long id) {
        api.getAllRewardsInHalloffameByAdventurerId(id, QuestioConstants.QUESTIO_KEY, new Callback<ArrayList<RewardHOF>>() {
            @Override
            public void success(ArrayList<RewardHOF> rewardHOFs, Response response) {
                if (rewardHOFs != null) {
                    rewards = rewardHOFs;
                    rewardsAdapter = new RewardsAdapter(mContext, rewardHOFs);
//                    hallOfFame.setAdapter(rewardsAdapter);
                    rewardsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RewardHOF reward = rewards.get(position);
        final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(mContext);
        dialog
                .withTitle("Reward Description")
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
                .setCustomView(R.layout.reward_description_dialog, mContext);
        TextView tvRewardName = ButterKnife.findById(dialog, R.id.dialog_reward_name);
        TextView tvRewardDesc = ButterKnife.findById(dialog, R.id.dialog_reward_desc);
        TextView tvRewardDate = ButterKnife.findById(dialog, R.id.dialog_reward_datereceived);
        ImageView rewardImage = ButterKnife.findById(dialog, R.id.dialog_reward_picture);
        //Button closeBtn = ButterKnife.findById(dialog, R.id.button_reward_close);

        String rewardName = reward.getRewardName();
        String rewardDesc = reward.getDescription();
        String rewardDate = reward.getDateReceived();

        Glide.with(mContext)
                .load(QuestioConstants.BASE_QUESTIO_MANAGEMENT + reward.getRewardPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(rewardImage);
        tvRewardName.setText(rewardName);
        tvRewardDesc.setText(rewardDesc);
        tvRewardDate.setText(rewardDate);

//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.cancel();
//            }
//        });
        dialog.show();
    }

    @OnClick(R.id.profile_inventory)
    void onClick() {
        Intent intent = new Intent(getActivity(), InventoryActivity.class);
        startActivity(intent);
    }
}
