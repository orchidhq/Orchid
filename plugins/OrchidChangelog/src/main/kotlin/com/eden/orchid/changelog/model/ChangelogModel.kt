package com.eden.orchid.changelog.model

import javax.inject.Singleton

@Singleton
class ChangelogModel {

    lateinit var versions: List<ChangelogVersion>

    fun initialize(versions: List<ChangelogVersion>) {
        this.versions = versions
    }

}

