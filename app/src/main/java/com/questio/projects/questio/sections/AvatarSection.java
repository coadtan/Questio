package com.questio.projects.questio.sections;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.questio.projects.questio.QuestioApplication;
import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.LoginActivity;
import com.questio.projects.questio.utilities.QuestioConstants;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class AvatarSection extends Fragment {
    private static final String LOG_TAG = AvatarSection.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_avatar, container, false);
        Bundle args = getArguments();

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(LOG_TAG, "action_logout clicked");
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
            dialog.setCancelable(true);
            TextView TVConfirmName = (TextView) dialog.findViewById(R.id.dialog_confirm_name);
            ImageView imageViewConfirmPicture = (ImageView) dialog.findViewById(R.id.dialog_confirm_picture);


            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(QuestioApplication.mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            Glide.with(this)
                    .load(currentPerson.getImage().getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewConfirmPicture);
            TVConfirmName.setText(personName);
            ImageButton btnNo = (ImageButton) dialog.findViewById(R.id.confirm_no);
            btnNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            ImageButton btnYes = (ImageButton) dialog.findViewById(R.id.confirm_yes);
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


        } else {
            Log.d(LOG_TAG, "not connected");
        }
    }
}
