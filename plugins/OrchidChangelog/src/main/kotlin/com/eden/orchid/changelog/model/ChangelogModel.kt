package com.eden.orchid.changelog.model

import javax.inject.Singleton

@Singleton
class ChangelogModel {

    lateinit var versions: List<ChangelogVersion>

    fun initialize(versions: List<ChangelogVersion>) {
        this.versions = versions
    }

    fun getVersion(versionName: String): ChangelogVersion? {
        return versions.find { it.version == versionName }
    }

}

