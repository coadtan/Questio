package com.questio.projects.questio;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.questio.projects.questio.activities.MainActivity;
import com.questio.projects.questio.libraries.zbarscanner.ZBarScannerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public final IntentsTestRule<ZBarScannerActivity> main2 = new IntentsTestRule<>(ZBarScannerActivity.class);

    @Test
    public void setUp() {
        if (!QuestioApplication.isLogin()) {
            onView(withId(R.id.sign_in_button))
                    .perform(click());
        }
    }

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        if (!QuestioApplication.isLogin()) {
            onView(withId(R.id.sign_in_button))
                    .check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.ranking_section))
                    .check(matches(isDisplayed()));
        }
    }

    // Test Navigate Tab
    @Test
    public void shouldGoToSearchSectionWhenClickSearchIcon() {
        onView(withContentDescription("searchTab"))
                .perform(click());
        onView(withId(R.id.search_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToQuestSectionWhenClickQuestIcon() {
        onView(withContentDescription("questTab"))
                .perform(click());
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToHOFSectionWhenClickHOFIcon() {
        onView(withContentDescription("hofTab"))
                .perform(click());
        onView(withId(R.id.inventory_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToProfileSectionWhenClickProfileIcon() {

        onView(withContentDescription("profileTab"))
                .perform(click());
        onView(withId(R.id.profile_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldGoToRankingSectionWhenClickRankingIcon() {

        onView(withContentDescription("rankingTab"))
                .perform(click());
        onView(withId(R.id.ranking_section))
                .check(matches(isDisplayed()));

    }

    // End of navigate tab

    @Test
    public void shouldShowCameraLayoutWhenClickQRCodeScanIconQuest() {
        onView(withContentDescription("questTab"))
                .perform(click());
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));
        onView(withId(R.id.action_qrcode_scan))
                .perform(click());
        onView(withId(R.id.cameraPreview))
                .check(matches(isDisplayed()));
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.quest_section))
                .check(matches(isDisplayed()));

    }

    @Test
    public void shouldShowMarkerWhenPressPlaces() throws UiObjectNotFoundException {
        onView(withContentDescription("questTab"))
                .perform(click());
        onView(withContentDescription("KMUTT"))
                .perform(click());
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Destination"));
        assertTrue(marker.click());

    }

    @Test
    public void shouldLogoutWhenClickLogoutButton(){
        onView(withContentDescription("profileTab"))
                .perform(click());
        onView(withId(R.id.action_logout))
                .perform(click());
        onView(withId(R.id.confirm_yes))
                .perform(click());
        onView(withId(R.id.sign_in_button))
                .check(matches(isDisplayed()));
    }


}