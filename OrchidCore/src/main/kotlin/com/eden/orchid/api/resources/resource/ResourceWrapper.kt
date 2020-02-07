package com.eden.orchid.api.resources.resource

import com.eden.common.json.JSONElement
import com.eden.orchid.api.theme.pages.OrchidReference
import java.io.IOException
import java.io.InputStream

/**
 * A Resource type that wraps another resource, optionally applying a transformation along the way.
 */
abstract class ResourceWrapper(private val resource: OrchidResource) : OrchidResource(resource.reference) {

    override fun getContentStream(): InputStream {
        return resource.getContentStream()
    }

    override fun shouldPrecompile(): Boolean {
        return resource.shouldPrecompile()
    }

    override fun shouldRender(): Boolean {
        return resource.shouldRender()
    }

    override fun compileContent(data: Any?): String {
        return resource.compileContent(data)
    }

    override val precompilerExtension: String
        get() = resource.precompilerExtension

    override fun canUpdate(): Boolean {
        return resource.canUpdate()
    }

    override fun canDelete(): Boolean {
        return resource.canDelete()
    }

    override fun update(newContent: InputStream) {
        resource.update(newContent)
    }

    override fun delete() {
        resource.delete()
    }

    override fun toString(): String {
        return resource.toString()
    }

    override val content: String
        get() = resource.content

    override val embeddedData: JSONElement
        get() = resource.embeddedData

    override fun free() {
        resource.free()
    }

    override fun equals(other: Any?): Boolean {
        return resource.equals(other)
    }

    override fun hashCode(): Int {
        return resource.hashCode()
    }
}
