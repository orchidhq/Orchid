package com.eden.orchid.changelog.model

import javax.inject.Singleton

@Singleton
class ChangelogModel {

    var versions: List<ChangelogVersion> = emptyList()

    fun initialize(versions: List<ChangelogVersion>) {
        this.versions = versions
    }

    fun getVersion(versionName: String): ChangelogVersion? {
        return versions.find { it.version == versionName }
    }

}

