package com.android.dev.yashchuk.sickleaves


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesActivity
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_TITLE = "Test sick leave"
private const val TEST_DESCRIPTION = "Test description"

@RunWith(AndroidJUnit4::class)
class SickLeavesTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<SickLeavesActivity>(SickLeavesActivity::class.java)

    @Test
    fun clickAddSickLeaveButton_shouldOpenDetailScreen() {
        onView(withId(R.id.create_btn)).perform(click())

        onView(withId(R.id.title)).check(matches(isDisplayed()))
    }

    @Test
    fun createSickLeave_shouldShowInList() {
        createSickLeave(TEST_TITLE, TEST_DESCRIPTION)

        onView(withItemText(TEST_TITLE)).check(matches(isDisplayed()))
    }

    @Test
    fun showAllSickLeaves_shouldShowAllSickLeavesToolbarTitle() {
        showAllSickLeaves()

        val title =
                InstrumentationRegistry.getTargetContext().getString(R.string.sick_list_toolbar_title_all)

        matchToolbarTitle(title)
    }

    @Test
    fun showOpenedSickLeaves_shouldShowOpenedSickLeaveToolbarTitle() {
        showOpenedSickLeaves()

        val title =
                InstrumentationRegistry.getTargetContext().getString(R.string.sick_list_toolbar_title_opened)

        matchToolbarTitle(title)
    }

    @Test
    fun showClosedSickLeaves_shouldShowClosedSickLeavesToolbarTitle() {
        showClosedSickLeaves()

        val title =
                InstrumentationRegistry.getTargetContext().getString(R.string.sick_list_toolbar_title_closed)

        matchToolbarTitle(title)
    }

    private fun showAllSickLeaves() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.sick_list_sort_menu_all)).perform(click())
    }

    private fun showOpenedSickLeaves() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.sick_list_sort_menu_opened)).perform(click())
    }

    private fun showClosedSickLeaves() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.sick_list_sort_menu_closed)).perform(click())
    }

    private fun createSickLeave(title: String, description: String) {
        onView(withId(R.id.create_btn)).perform(click())

        onView(withId(R.id.title)).perform(typeText(title), closeSoftKeyboard())
        onView(withId(R.id.description)).perform(typeText(description), closeSoftKeyboard())
        onView(withId(R.id.create_save_btn)).perform(click())
    }

    private fun withItemText(itemText: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                return Matchers.allOf(
                        ViewMatchers.isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                        ViewMatchers.withText(itemText)
                ).matches(item)
            }

            override fun describeTo(description: Description?) {
                description?.appendText("is Descendant of a RecyclerView with text $itemText")
            }
        }
    }

    private fun matchToolbarTitle(title: CharSequence): ViewInteraction {
        return onView(
                allOf(
                        isAssignableFrom(TextView::class.java),
                        withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(title.toString())))
    }
}
