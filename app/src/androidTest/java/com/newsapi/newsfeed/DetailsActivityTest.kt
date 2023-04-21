package com.newsapi.newsfeed

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.newsapi.newsfeed.helpers.Helper
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.Source
import com.newsapi.newsfeed.view.DetailsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DetailsActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java).apply {
        val extras = Bundle()
        extras.putParcelable(Helper.ARTICLE_PARAM, mockNewArticle())
        putExtras(extras)
    }

    @Rule
    @JvmField
    var activityScenarioRule: ActivityScenarioRule<DetailsActivity> = ActivityScenarioRule<DetailsActivity>(intent)


    @Test
    fun whenStartActivity_thenToolbarIsDisplayed() {
        makeActivityWait()

        onView(withId(R.id.detailsToolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun makeActivityWait() = Thread.sleep(2000)

    private fun mockNewArticle(): Article {
        return Article(
            "Argaam",
            null,
            "Malicious encryptors for Apple computers could herald new risks for macOS users.",
            "2023-04-18T13:21:16+00:00",
            Source("ars-technica", "Ars Technica"),
            "Apple’s Macs have long escaped ransomware, but that may be changing",
            "https://www.wired.com/story/apple-mac-lockbit-ransomware-samples/",
            "\"https://cdn.arstechnica.net/wp-content/uploads/2023/04/macbook-pink-760x380.jpg"
        )
    }

}