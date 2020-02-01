package com.eden.orchid.api.resources

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.ResourceWrapper
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

    override fun getContent(): String {
        return transform(super.getContent(), contentPreTransformations)
    }

    override fun getContentStream(): InputStream {
        return transform(super.getContentStream(), contentStreamTransformations)
    }

    override fun compileContent(data: Any?): String? {
        return transform(super.compileContent(data), contentPostTransformations)
    }

    fun <T> transform(input: T, transformations: List<(T) -> T>): T {
        return if(transformations.isEmpty()) input else transformations.reduce { a, b -> { o: T -> b.invoke(a.invoke(o)) } } .invoke(input)
    }

}
