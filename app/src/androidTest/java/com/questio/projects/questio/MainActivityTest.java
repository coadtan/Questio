package com.questio.projects.questio;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.questio.projects.questio.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    public static final String LOG_TAG = MainActivityTest.class.getSimpleName();
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        Log.d(LOG_TAG, "Test was set up!");
    }

    @After
    public void tearDown() {
        Log.d(LOG_TAG, "Test was torn down!");
    }

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withId(R.id.ranking_section))
                .check(matches(isDisplayed()));
    }

    // Test Navigate Tab
    @Test
    public void tabNavigateShouldWorkCorrectly() {
        onView(withContentDescription("go to profile section tab"))
                .perform(click());
        onView(withId(R.id.profile_section))
                .check(matches(isDisplayed()));
        onView(withContentDescription("go to hall of frame section tab"))
                .perform(click());
        onView(withId(R.id.hof_section))
                .check(matches(isDisplayed()));
        onView(withContentDescription("go to quest section tab"))
                .perform(click());
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));
        onView(withContentDescription("go to search section tab"))
                .perform(click());
        onView(withId(R.id.search_section))
                .check(matches(isDisplayed()));
        onView(withContentDescription("go to ranking section tab"))
                .perform(click());
        onView(withId(R.id.ranking_section))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldGoToQuestSectionWhenClickQuestIcon() {
        onView(withContentDescription("go to quest section tab"))
                .perform(click());
        onView(withContentDescription("go to hall of frame section tab"))
                .perform(click());
        onView(withContentDescription("go to quest section tab"))
                .perform(click());
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldGoToRankingSectionWhenClickRankingIcon() {

        onView(withContentDescription("go to ranking section tab"))
                .perform(click());
        onView(withId(R.id.ranking_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToSearchSectionWhenClickSeachIcon() {
        onView(withContentDescription("go to search section tab"))
                .perform(click());

        onView(withId(R.id.search_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToHOFSectionWhenClickHOFIcon() {
        onView(withContentDescription("go to hall of frame section tab"))
                .perform(click());


        onView(withId(R.id.hof_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToProfileSectionWhenClickProfileIcon() {

        onView(withContentDescription("go to profile section tab"))
                .perform(click());


        onView(withId(R.id.profile_section))
                .check(matches(isDisplayed()));

    }
    // End of navigate tab

    @Test
    public void shouldShowCameraLayoutWhenClickQRCodeScanIconQuest() {
        onView(withContentDescription("go to quest section tab"))
                .perform(click());
        onView(withId(R.id.action_qrcode_scan))
                .perform(click());
        onView(withId(R.id.cameraPreview))
                .check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_qrcode_scan))
                .check(matches(isDisplayed()));
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowCameraLayoutWhenClickQRCodeScanIconRanking() {
        onView(withContentDescription("go to ranking section tab"))
                .perform(click());
        onView(withId(R.id.action_qrcode_scan))
                .perform(click());
        onView(withId(R.id.cameraPreview))
                .check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_qrcode_scan))
                .check(matches(isDisplayed()));
        onView(withId(R.id.ranking_section))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onProfileSectionShouldHaveLogoutIcon() {
        onView(withContentDescription("go to profile section tab"))
                .perform(click());
        onView(withContentDescription("go to hall of frame section tab"))
                .perform(click());
        onView(withContentDescription("go to profile section tab"))
                .perform(click());
        onView(withId(R.id.profile_section))
                .check(matches(isDisplayed()));
        onView(withId(R.id.action_logout))
                .check(matches(isDisplayed()));
    }


}