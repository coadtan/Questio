package com.questio.projects.questio.libraries.slidingtabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questio.projects.questio.R;
import com.questio.projects.questio.sections.ProfileSection;
import com.questio.projects.questio.sections.HOFSection;
import com.questio.projects.questio.sections.PlaceSection;
import com.questio.projects.questio.sections.RankingSection;
import com.questio.projects.questio.sections.SearchSection;


public class SlidingTabsBasicFragment extends Fragment {

    private static final String LOG_TAG = SlidingTabsBasicFragment.class.getSimpleName();
    AppSectionsPagerAdapter mPagerAdapter;


    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new AppSectionsPagerAdapter(getFragmentManager());
    }

    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_holder, container, false);

    }

    // BEGIN_INCLUDE (fragment_onviewcreated)


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

    }


    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "text" + position;
        }


        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            switch (i) {
                case 0:
                    fragment = new RankingSection();
                    break;
                case 1:
                    fragment = new SearchSection();
                    break;
                case 2:
                    fragment = new PlaceSection();
                    break;
                case 3:
                    fragment = new HOFSection();
                    break;
                default:
                    fragment = new ProfileSection();
                    break;
            }


            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;


        }

        @Override
        public int getCount() {
            return 5;
        }


    }
}
