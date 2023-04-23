package com.newsapi.newsfeed

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.newsapi.newsfeed.view.MainActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun whenStartActivity_thenToolbarIsDisplayed() {
        makeActivityWait()

        onView(withId(R.id.mainToolbar))
             .check(matches(isDisplayed()))
    }

    @Test
    fun whenStartActivity_thenRecycleViewIsDisplayed() {
        makeActivityWait()

        onView(withId(R.id.rvNewsList))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenReachRecyclerViewBottom_thenProgressBarIsDisplayed() {
        makeActivityWait()

        onView(withId(R.id.rvNewsList)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                activityRule.activity.
                rvNewsList.adapter?.itemCount!! - 1
            )
        )

        onView(withId(R.id.pbHeadlineLoad))
            .check(matches(isDisplayed()))


    }

    private fun makeActivityWait() = Thread.sleep(2000)
}