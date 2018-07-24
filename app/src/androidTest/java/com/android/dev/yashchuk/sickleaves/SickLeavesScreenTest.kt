package com.android.dev.yashchuk.sickleaves


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SickLeavesScreenTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<SickLeavesActivity>(SickLeavesActivity::class.java)

    @Test
    fun clickAddSickLeaveButton_shouldOpenDetailScreen() {
        onView(withId(R.id.create_btn)).perform(click())

        onView(withId(R.id.title)).check(matches(isDisplayed()))
    }
}
