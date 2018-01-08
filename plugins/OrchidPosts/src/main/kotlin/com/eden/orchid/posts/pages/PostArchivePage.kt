package com.eden.orchid.posts.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import java.time.Month
import java.util.*
import java.util.stream.Collectors

@Archetype(value = ConfigArchetype::class, key = "postArchivePages")
open class PostArchivePage : PermalinkPage {

    var postList: List<PostPage> = emptyList()
    var category: String? = null
    var index: Int = 0

    val years: List<Int>
        get() {
            val years = ArrayList<Int>()

            for (post in postList) {
                if (!years.contains(post.year)) {
                    years.add(post.year)
                }
            }

            years.reverse()
            return years
        }

    constructor(resource: OrchidResource, index: Int, permalink: String) : super(resource, "postArchive") {
        this.permalink = permalink
        this.index = index
    }

    protected constructor(resource: OrchidResource, key: String, index: Int, permalink: String) : super(resource, key) {
        this.permalink = permalink
        this.index = index
    }

    fun getMonths(year: Int): List<Int> {
        val months = ArrayList<Int>()

        for (post in postsInYear(year)) {
            if (!months.contains(post.month)) {
                months.add(post.month)
            }
        }

        months.reverse()

        return months
    }

    fun postsInYear(year: Int): List<PostPage> {
        return postList.stream()
                .filter { postPage -> postPage.year == year }
                .collect(Collectors.toList())
    }

    fun postsInMonth(year: Int, month: Int): List<PostPage> {
        return postList.stream()
                .filter { postPage -> postPage.year == year && postPage.month == month }
                .collect(Collectors.toList())
    }

    fun getMonthName(month: Int): String {
        return Month.of(month).toString()
    }

    override fun getTemplates(): List<String> {
        val templates = super.getTemplates()
        if (!EdenUtils.isEmpty(category)) {
            templates.add(0, category)
            templates.add(0, "postArchive-" + category!!)
        }

        return templates
    }

}

