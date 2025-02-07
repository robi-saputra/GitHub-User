package robi.technicaltest.githubuser

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import org.junit.Rule
import org.junit.Test

@MediumTest
class DetailUserFragmentUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testUserDetailsDisplayedCorrectly() {
        // Assuming DetailUserFragment is already launched with a user

        // Check if avatar, name, and username are displayed
        onView(withId(R.id.ivAvatar)).check(matches(isDisplayed()))
        onView(withId(R.id.tvAccountName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()))
    }

    @Test
    fun testRepositoryListDisplayed() {
        // Check if the repositories list is visible
        onView(withId(R.id.rvRepository)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoadingState() {
        // Simulate loading state
        onView(withId(R.id.shimmer_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.rvRepository)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testErrorState() {
        // Simulate error state (assuming exception layout is used)
        onView(withId(R.id.exceptionLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTittle)).check(matches(withText("Upps!!")))
    }

    @Test
    fun testMoreRepositoriesButtonClick() {
        // Click the "More Repositories" button
        onView(withId(R.id.btnMoreRepositories)).perform(click())

        // Check if it navigates to ListRepositoryFragment
        onView(withId(R.id.listRepositoryFragment)).check(matches(isDisplayed()))
    }
}