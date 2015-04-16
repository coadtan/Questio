package com.questio.projects.questio.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.questio.projects.questio.R;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;
import com.questio.projects.questio.models.Riddle;
import com.questio.projects.questio.utilities.QuestioAPIService;
import com.questio.projects.questio.utilities.QuestioConstants;
import com.questio.projects.questio.utilities.QuestioHelper;

import net.sourceforge.zbar.Symbol;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ning jittima on 12/4/2558.
 */
public class RiddleAction extends ActionBarActivity implements View.OnClickListener {
    private static final String LOG_TAG = RiddleAction.class.getSimpleName();
    Toolbar toolbar;
    TextView riddle;
    Button hint1Btn;
    Button hint2Btn;
    Button hint3Btn;
    ImageButton scanHere;
    TextView hintReveal1;
    TextView hintReveal2;
    TextView hintReveal3;


    TextView scanTV;
    int scanLimit;

    Riddle r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_action);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        scanTV = (TextView) toolbar.findViewById(R.id.toolbar_limit);
        scanTV.setText(Integer.toString(scanLimit));

        riddle = (TextView)findViewById(R.id.riddle_riddle);
        hint1Btn = (Button)findViewById(R.id.riddle_hint1Btn);
        hint2Btn = (Button)findViewById(R.id.riddle_hint2Btn);
        hint3Btn = (Button)findViewById(R.id.riddle_hint3Btn);
        scanHere = (ImageButton)findViewById(R.id.riddle_scanHere);
        hintReveal1 = (TextView)findViewById(R.id.riddle_hintReveal1);
        hintReveal2 = (TextView)findViewById(R.id.riddle_hintReveal2);
        hintReveal3 = (TextView)findViewById(R.id.riddle_hintReveal3);






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String questId;
        String questName;


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                questId = null;
                questName = null;
            } else {
                questId = extras.getString(QuestioConstants.QUEST_ID);
                questName = extras.getString(QuestioConstants.QUEST_NAME);
            }
        } else {
            questId = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_ID);
            questName = (String) savedInstanceState.getSerializable(QuestioConstants.QUEST_NAME);
        }
        Log.d(LOG_TAG, "questid: " + questId + " questName: " + questName);

        getSupportActionBar().setTitle(questName);

        //r = Riddle.getAllRiddleByRiddleId((Integer.parseInt(questId)));
        requestRiddleData(Integer.parseInt(questId));


    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.riddle_scanHere:
                Intent intent = new Intent(this, ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);
                break;
            case R.id.riddle_hint1Btn:
                hintReveal1.setText(r.getHint1());
                break;
            case R.id.riddle_hint2Btn:
                hintReveal2.setText(r.getHint2());
                break;
            case R.id.riddle_hint3Btn:
                hintReveal3.setText(r.getHint3());
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode: " + requestCode);
        Log.d(LOG_TAG, "Activity.RESULT_OK: " + Activity.RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {
            // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
            // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
            //
            // Toast.makeText(getActivity(), "Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0), Toast.LENGTH_SHORT).show();
            // The value of type indicates one of the symbols listed in Advanced Options below.
            String[] qr = QuestioHelper.getDeQRCode(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            if (qr[0].equalsIgnoreCase(QuestioConstants.QRTYPE_RIDDLE_ANSWER)) {
                onAnswer(qr[1]);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    void onAnswer(String answer){

        if(scanLimit != 0){
            if(answer.equalsIgnoreCase(Long.toString(r.getQrCode()))){
                riddle.setBackgroundColor(getResources().getColor(R.color.green_quiz_correct));
            }else{
                scanLimit--;
                scanTV.setText(Integer.toString(scanLimit));
            }
        }

    }

    private void requestRiddleData(int id) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(QuestioConstants.ENDPOINT)
                .build();
        QuestioAPIService api = adapter.create(QuestioAPIService.class);
        api.getRiddleByQuestId(id, new Callback<Riddle[]>() {
            @Override
            public void success(Riddle[] riddleTemp, Response response) {
                r = riddleTemp[0];
                Log.d(LOG_TAG, r.toString());
                if(r.getHint1().equalsIgnoreCase("")){
                    hint1Btn.setEnabled(false);
                    hint1Btn.setClickable(false);
                    hint1Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                    hintReveal1.setVisibility(View.INVISIBLE);
                }

                if(r.getHint2().equalsIgnoreCase("")){
                    hint2Btn.setEnabled(false);
                    hint2Btn.setClickable(false);
                    hint2Btn.setBackgroundColor(getResources().getColor(R.color.grey_500));
                    hintReveal2.setVisibility(View.INVISIBLE);
                }
                if(r.getHint3().equalsIgnoreCase("")){
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
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "Fail: " + retrofitError.toString());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getUrl());
                Log.d(LOG_TAG, "Fail: " + retrofitError.getStackTrace());
            }
        });
    }
}
