package com.eden.orchid.posts

import com.eden.orchid.posts.utils.PostsUtils
import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@DisplayName("Tests the formatting accepted by the LatestPostsCollection")
class PostsCollectionTest : OrchidUnitTest() {

    @ParameterizedTest
    @CsvSource(
        "':latestBlogPost                ',            , 1",
        "':latestBlogPost()              ',            , 1",
        "':latestBlogPost(programming)   ', programming, 1",
        "':latestBlogPost(3)             ',            , 3",
        "':latestBlogPost(programming, 3)', programming, 3",
        "':latestBlogPost(3, programming)', programming, 3",

        "':latestBlogPosts                ',            , 1",
        "':latestBlogPosts()              ',            , 1",
        "':latestBlogPosts(programming)   ', programming, 1",
        "':latestBlogPosts(3)             ',            , 3",
        "':latestBlogPosts(programming, 3)', programming, 3",
        "':latestBlogPosts(3, programming)', programming, 3"
    )
    fun testValidPostCollectionPatterns(input: String, expectedCategory: String?, expectedCount: Int) {
        expectThat(PostsUtils.parseLatestPostCollectionId(input.trim()))
            .isNotNull()
            .and {
                get { this.first }.isEqualTo(expectedCategory?.trim())
            }
            .and {
                get { this.second }.isEqualTo(expectedCount)
            }
    }

    @ParameterizedTest
    @CsvSource(
        "'latestBlogPost'",
        "'latestBlogPosts'",
        "':latestBlogPosts(a,1,f)'",
        "':latestBlogPosts(,a)'",
        "':latestBlogPosts(a,)'",
        "':latestBlogPosts(,1)'",
        "':latestBlogPosts(1,)'",
        "':latestBlogPosts('",
        "':latestBlogPosts)'"
    )
    fun testInvalidPostCollectionPatterns(input: String) {
        expectThat(PostsUtils.parseLatestPostCollectionId(input.trim()))
            .isNull()
    }

}
