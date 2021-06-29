package com.eden.orchid.api.site

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.FlexibleIterableConverter
import com.eden.orchid.api.converters.FlexibleMapConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.theme.components.ModularList
import com.eden.orchid.api.theme.components.ModularListItem
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import java.lang.reflect.Field
import javax.inject.Inject
import javax.inject.Provider

@Archetype(value = ConfigArchetype::class, key = "site.baseUrl")
@Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
abstract class BaseUrlFactory(
    private val _type: String,
    priority: Int = OrchidUtils.DEFAULT_PRIORITY
) : Prioritized(priority), ModularListItem<OrchidSiteBaseUrls, BaseUrlFactory> {

    @Option("order")
    @IntDefault(0)
    @Description("By default, menu items are rendered in the order in which they are declared, but the ordering can be changed by setting the order on any individual menu item. A higher value for order will render that menu item earlier in the list.")
    var _order: Int = 0

    override fun getOrder(): Int {
        return _order
    }

    override fun setOrder(order: Int) {
        _order = order
    }

    override fun getType(): String = _type
    abstract fun isEnabled(context: OrchidContext): Boolean
    abstract fun getBaseUrl(context: OrchidContext): String
}

class OrchidSiteBaseUrls : ModularList<OrchidSiteBaseUrls, BaseUrlFactory>() {

    init {
        defaultType = "default"
    }

    override fun getItemClass(): Class<BaseUrlFactory> = BaseUrlFactory::class.java
}

@Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
class OrchidSiteBaseUrlsOptionExtractor
@Inject
constructor(
    val contextProvider: Provider<OrchidContext>,
    val iterableConverter: FlexibleIterableConverter,
    val mapConverter: FlexibleMapConverter
) : OptionExtractor<OrchidSiteBaseUrls>(10000) {

    override fun acceptsClass(clazz: Class<*>?): Boolean = clazz == OrchidSiteBaseUrls::class.java

    override fun getOption(field: Field, sourceObject: Any?, key: String): OrchidSiteBaseUrls {
        val baseUrl = OrchidSiteBaseUrls()
        baseUrl.initialize(contextProvider.get(), mutableListOf())
        val validTypes = baseUrl.validTypes

        if (sourceObject is String) {
            if (sourceObject in validTypes) {
                // if matches one of the declared types factory types, use the default of that type
                baseUrl.add(mapOf("type" to sourceObject))
            } else {
                // otherwise, pass it to [default] factory
                baseUrl.add(
                    mapOf(
                        "type" to "default",
                        "value" to sourceObject,
                        "order" to Int.MAX_VALUE
                    )
                )
            }
        } else {
            val iterable = iterableConverter.convert(field.type, sourceObject, "type", "value").second.toList()
            for (o in iterable) {
                val map = mapConverter.convert(field.type, o, "type").second as Map<String, Any?>
                baseUrl.add(map)
            }
        }

        // always use the dev server URL if running a serve task
        baseUrl.add(
            mapOf(
                "type" to "devServer",
                "order" to Int.MIN_VALUE
            )
        )

        return baseUrl
    }

    override fun getDefaultValue(field: Field?): OrchidSiteBaseUrls {
        val baseUrl = OrchidSiteBaseUrls()
        baseUrl.initialize(contextProvider.get(), mutableListOf())

        baseUrl.add(
            mapOf(
                "type" to "default",
                "order" to Int.MAX_VALUE
            )
        )

        // always use the dev server URL if running a serve task
        baseUrl.add(
            mapOf(
                "type" to "devServer",
                "order" to Int.MIN_VALUE
            )
        )

        return baseUrl
    }
}
