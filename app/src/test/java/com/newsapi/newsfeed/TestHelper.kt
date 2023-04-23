package com.newsapi.newsfeed

import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.Source

object TestHelper {
    @Suppress("LongMethod")
    fun mockNewArticle(): Article {
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

    @Suppress("LongMethod")
    fun mockNewSource(): Source {
        return Source(
            "australian-financial-review",
            "Australian Financial Review",
            "The Australian Financial Review reports the latest news from business, " +
                    "finance, investment and politics, " +
                    " in real time. It has a reputation for independent, award-winning journalism " +
                    "and is essential reading for " +
                    "the business and investor community.",
            "http://www.afr.com",
            "business",
            "en",
            "au"
        )
    }

}