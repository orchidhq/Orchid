package com.eden.orchid.api.server.admin

import com.eden.orchid.api.options.Descriptive

/**
 * Admin Lists add collections of items to be displayed within the admin area for the purposes of self-documentation and
 * discovery.
 */
interface AdminList : Descriptive {
    val key: String
    val listClass: Class<*>
    val items: Collection<Class<*>?>
    val isImportantType: Boolean

    @JvmDefault
    override fun getDescriptiveName(): String {
        return Descriptive.getDescriptiveName(listClass)
    }

    @JvmDefault
    override fun getDescription(): String {
        return Descriptive.getDescription(listClass)
    }
}
