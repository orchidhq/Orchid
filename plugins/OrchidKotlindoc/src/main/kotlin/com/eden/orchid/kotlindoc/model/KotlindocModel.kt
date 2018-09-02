package com.eden.orchid.kotlindoc.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import com.eden.orchid.kotlindoc.page.KotlindocPackagePage
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KotlindocModel
@Inject
constructor(val context: OrchidContext) {

    var allClasses: MutableList<KotlindocClassPage> = ArrayList()
    var allPackages: MutableList<KotlindocPackagePage> = ArrayList()

    private var classPageCache: MutableMap<String, OrchidPage?> = HashMap()
    private var packagePageCache: MutableMap<String, OrchidPage?> = HashMap()

    val allPages: List<OrchidPage>
        get() {
            val pages = ArrayList<OrchidPage>()
            pages.addAll(allClasses)
            pages.addAll(allPackages)

            return pages
        }

    fun initialize(allClasses: MutableList<KotlindocClassPage>, allPackages: MutableList<KotlindocPackagePage>) {
        this.allClasses = allClasses
        this.allPackages = allPackages
        this.classPageCache = HashMap()
        this.packagePageCache = HashMap()
    }

}
