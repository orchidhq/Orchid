package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.OrchidContext
import java.io.InputStream

/**
 * A Resource type that wraps another resource, optionally applying a transformation along the way.
 */
open class ResourceTransformation
@JvmOverloads
constructor(
    resource: OrchidResource,
    protected var contentPreTransformations: MutableList<(String) -> String> = mutableListOf(),
    protected var contentPostTransformations: MutableList<(String) -> String> = mutableListOf(),
    protected var contentStreamTransformations: MutableList<(InputStream) -> InputStream> = mutableListOf()
) : ResourceWrapper(resource) {

    override fun getContentStream(): InputStream {
        return transform(super.getContentStream(), contentStreamTransformations)
    }

    override fun compileContent(context: OrchidContext, data: Any?): String {
        return transform(super.compileContent(context, data), contentPostTransformations)
    }

    override val content: String
        get() = transform(super.content, contentPreTransformations)

    private fun <T> transform(input: T, transformations: List<(T) -> T>): T {
        return if (transformations.isEmpty()) {
            input
        } else {
            transformations
                .reduce { a, b -> { o: T -> b.invoke(a.invoke(o)) } }
                .invoke(input)
        }
    }
}
