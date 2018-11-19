package com.eden.orchid.api.resources

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.ResourceWrapper
import java.io.InputStream

/**
 * A Resource type that wraps another resource, optionally applying a transformation along the way.
 */
open class ResourceTransformation(
        resource: OrchidResource,
        protected var contentTransformations: List<(String) -> String> = emptyList(),
        protected var contentStreamTransformations: List<(InputStream) -> InputStream> = emptyList()
) : ResourceWrapper(resource) {

    override fun getContent(): String {
        return transform(super.getContent(), contentTransformations)
    }

    override fun getContentStream(): InputStream {
        return transform(super.getContentStream(), contentStreamTransformations)
    }

    fun <T> transform(input: T, transformations: List<(T) -> T>): T {
        return transformations.reduce { a, b -> { o: T -> b.invoke(a.invoke(o)) } } .invoke(input)
    }

}