package com.questio.projects.questio.sections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.questio.projects.questio.R;
import com.questio.projects.questio.activities.QuestAction;
import com.questio.projects.questio.libraries.zbarscanner.ZBarConstants;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

/**
 * Created by coad4u4ever on 01-Apr-15.
 */
public class RankingSection extends StatedFragment {
    private static final String LOG_TAG = RankingSection.class.getSimpleName();
    private int i;

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt("i", i);
        Log.d(LOG_TAG, "onSaveState i: " + i);
        super.onSaveState(outState);

    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        i = savedInstanceState.getInt("i");
        Log.d(LOG_TAG, "onRestoreState i: " + i);
        super.onRestoreState(savedInstanceState);
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            i = (int) (Math.random()*100);
            Log.d(LOG_TAG, "re-random i: "+ i);
        }else{
            Log.d(LOG_TAG, "no re-random i: " + i);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_ranking, container, false);
        Bundle args = getArguments();

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_place_section, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_qrcode_scan:
                Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
                intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_enter_zone68001:
                String[] qr = {"zone", "68001"};
                if (qr[0].equalsIgnoreCase("zone")) {
                    Intent intentToQuestAction = new Intent(getActivity(), QuestAction.class);
                    intentToQuestAction.putExtra("qrcode", qr[1]);
                    startActivity(intentToQuestAction);
                }
                return true;
            default:
                break;
        }
        return false;
    }
}
