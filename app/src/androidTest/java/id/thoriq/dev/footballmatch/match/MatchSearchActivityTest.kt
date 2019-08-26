package id.thoriq.dev.footballmatch.match


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.view.KeyEvent
import android.widget.AutoCompleteTextView
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.R.id.rvEventsSearch
import id.thoriq.dev.footballmatch.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchSearchActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MatchSearchActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun testSearchMatch() {
        onView(ViewMatchers.withId(R.id.searchMenu))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.searchMenu)).perform(click())
        onView(ViewMatchers.isAssignableFrom(AutoCompleteTextView::class.java)).perform(
            typeText("Chelsea"),
            pressImeActionButton()
        )
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))

        onView(ViewMatchers.isAssignableFrom(AutoCompleteTextView::class.java)).perform(
            typeText("Barcelona"),
            pressImeActionButton()
        )
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(ViewMatchers.isAssignableFrom(AutoCompleteTextView::class.java)).perform(
            typeText("Madrid"),
            pressImeActionButton()
        )
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
            .perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(ViewMatchers.withId(rvEventsSearch))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}