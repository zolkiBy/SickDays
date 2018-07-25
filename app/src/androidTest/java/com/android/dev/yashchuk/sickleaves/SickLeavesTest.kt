package com.android.dev.yashchuk.sickleaves


import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
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
    fun createSickLeave_close_delete_shouldNotShowInList() {
        createSickLeave(TEST_TITLE, TEST_DESCRIPTION)

        onView(withItemText(TEST_TITLE)).check(matches(isDisplayed()))

        closeSickLeave()

        onView(isRoot()).perform(waitFor(3000))

        val status = InstrumentationRegistry.getTargetContext().getString(R.string.sick_list_item_status_closed)

        onView(withItemId(R.id.status)).check(matches(withText(status)))

        onView(isRoot()).perform(waitFor(1000))

        deleteSickLeave()

        onView(isRoot()).perform(waitFor(3000))

        onView(withItemText(TEST_TITLE)).check(matches(not(isDisplayed())))
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

    private fun closeSickLeave() {
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickChildViewWithId(R.id.close_btn)
                ))
    }

    private fun deleteSickLeave() {
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickChildViewWithId(R.id.delete_btn)
                ))
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

    private fun withItemId(@IdRes id: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                return Matchers.allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                        withId(id)
                ).matches(item)
            }

            override fun describeTo(description: Description?) {
                description?.appendText("is Descendant of a RecyclerView with id $id")
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

    private fun clickChildViewWithId(@IdRes id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                return "Click on view with id: $id"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.findViewById<View>(id)?.performClick()
            }
        }
    }

    private fun waitFor(millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds"
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(millis)
            }
        }
    }
}
