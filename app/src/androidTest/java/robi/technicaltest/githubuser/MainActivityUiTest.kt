package robi.technicaltest.githubuser

import android.view.KeyEvent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityUiTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSearchBarVisibilityOnClick() {
        onView(withId(R.id.menu_search_bar)).perform(click())

        // Check if the search bar is visible
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchFunctionality() {
        // Click on the search menu item
        onView(withId(R.id.menu_search_bar)).perform(click())

        // Enter text in search input and press enter
        onView(withId(R.id.inputSearchBar)).perform(typeText("User"), pressKey(KeyEvent.KEYCODE_ENTER))

        // Verify that the search fragment is displayed
        onView(withId(R.id.nav_host_fragment_content_main)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToListUserFragment() {
        // Verify the default fragment is ListUserFragment
        onView(withId(R.id.listUserFragment)).check(matches(isDisplayed()))
    }
}