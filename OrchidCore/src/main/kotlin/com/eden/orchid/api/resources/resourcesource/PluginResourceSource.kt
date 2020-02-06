package com.eden.orchid.api.resources.resourcesource

object PluginResourceSource : OrchidResourceSource.Scope{
    override val scopePriority: Int = Int.MAX_VALUE - 2
}
