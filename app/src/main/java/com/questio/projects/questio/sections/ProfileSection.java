package com.questio.projects.questio.sections;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.questio.projects.questio.activities.LoginActivity;
import com.questio.projects.questio.adepters.RewardsAdapter;
import com.questio.projects.questio.models.RewardHOF;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.questio.projects.questio.utilities.QuestioConstants.ADVENTURER_DISPLAYNAME;

public class ProfileSection extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = ProfileSection.class.getSimpleName();
    Context mContext;
    @BindView(R.id.profile_picture)
    ImageView profilePicture;
    @BindView(R.id.profile_place_rewards_button)
    ImageButton placeRewardsButton;
    @BindView(R.id.profile_rewards_button)
    ImageButton rewardsButton;

    @BindView(R.id.profile_nickname)
    TextView profileName;

    View view;
    Person currentPerson;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    RestAdapter adapter;
    long adventurerId;
    String adventurerName;
    QuestioAPIService api;
    ArrayList<RewardHOF> rewards;
    RewardsAdapter rewardsAdapter;
    private Unbinder unbinder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        prefs = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE);
        editor = this.getActivity().getSharedPreferences(QuestioConstants.ADVENTURER_PROFILE, Context.MODE_PRIVATE).edit();
        adventurerId = prefs.getLong(QuestioConstants.ADVENTURER_ID, 0);
        adventurerName = prefs.getString(ADVENTURER_DISPLAYNAME, "Unknown");
        currentPerson = Plus.PeopleApi
                .getCurrentPerson(QuestioApplication.mGoogleApiClient);
        adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        api = adapter.create(QuestioAPIService.class);
    }

    public void init() {
        requestRewardsHOFData(adventurerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        String nameToShow = adventurerName;
        String[] parts = nameToShow.split(" ");

        nameToShow = parts[0] + " " + (parts[1].charAt(0) + "").toUpperCase() + ".";
        profileName.setText(nameToShow);

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

        File imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "questio_avatar.png");
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            profilePicture.setImageBitmap(bitmap);
        } else {
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.avatar_default));
        }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume: called");
        File imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "questio_avatar.png");
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            profilePicture.setImageBitmap(bitmap);
        } else {
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.avatar_default));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

        dialog.show();
    }
}
