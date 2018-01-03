package com.eden.orchid.posts.pages

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage


abstract class PermalinkPage : OrchidPage {

    constructor(resource: OrchidResource, key: String) : super(resource, key) {}

    constructor(resource: OrchidResource, key: String, title: String) : super(resource, key, title) {}

    constructor(resource: OrchidResource, key: String, title: String, path: String) : super(resource, key, title, path) {}

    abstract fun getPermalink(): String

}

