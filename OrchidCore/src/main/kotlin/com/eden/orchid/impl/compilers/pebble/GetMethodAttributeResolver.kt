package com.eden.orchid.impl.compilers.pebble

import com.eden.common.util.EdenPair
import com.eden.orchid.Orchid
import com.eden.orchid.api.events.On
import com.eden.orchid.api.events.OrchidEventListener
import com.mitchellbosecke.pebble.attributes.AttributeResolver
import com.mitchellbosecke.pebble.attributes.ResolvedAttribute
import com.mitchellbosecke.pebble.node.ArgumentsNode
import com.mitchellbosecke.pebble.template.EvaluationContextImpl

import javax.inject.Inject
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

class GetMethodAttributeResolver @Inject
constructor() : AttributeResolver, OrchidEventListener {

    val cache: ConcurrentHashMap<Class<*>, ConcurrentHashMap<String, EdenPair<Boolean, Method>>> = ConcurrentHashMap()

    override fun resolve(
        instance: Any?,
        attributeNameValue: Any,
        argumentValues: Array<Any>,
        args: ArgumentsNode,
        context: EvaluationContextImpl,
        filename: String,
        lineNumber: Int
    ): ResolvedAttribute? {
        if (instance == null) return null

        try {
            val attributeName = attributeNameValue.toString()
            val resolved = cacheOrShouldInvoke(instance, attributeName)
            if (resolved.first) {
                val result = resolved.second.invoke(instance, attributeName)
                return ResolvedAttribute(result)
            }
        } catch (e: Exception) {
        }

        return null
    }

    private fun cacheOrShouldInvoke(instance: Any, attributeName: String): EdenPair<Boolean, Method> {
        // check for the item in cache and return it if found
        var classCache: ConcurrentHashMap<String, EdenPair<Boolean, Method>>? = cache[instance.javaClass]
        if (classCache != null) {
            val cachedValue = classCache[attributeName]
            if (cachedValue != null) {
                return cachedValue
            }
        }

        // method wasn't found, lets figure it out for ourselves...
        val unCachedValue = shouldInvoke(instance, attributeName)

        // ...then cache what we found...
        if (classCache == null) {
            classCache = ConcurrentHashMap()
            cache[instance.javaClass] = classCache
        }
        classCache[attributeName] = unCachedValue

        // ...and return the value
        return unCachedValue
    }

    private fun shouldInvoke(instance: Any, attributeName: String): EdenPair<Boolean, Method> {
        try {
            val dynamicGetMethod = instance.javaClass.getMethod("get", String::class.java)
            return EdenPair(true, dynamicGetMethod)
        } catch (e: Exception) {
        }

        return EdenPair<Boolean, Method>(false, null)
    }

    @On(Orchid.Lifecycle.ClearCache::class)
    fun onClearCache(event: Orchid.Lifecycle.ClearCache) {
        cache.clear()
    }
}
