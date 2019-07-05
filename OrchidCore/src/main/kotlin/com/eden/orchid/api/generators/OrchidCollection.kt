package com.eden.orchid.api.generators

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.with
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
abstract class OrchidCollection<T : Collectible<*>>(
    generator: OrchidGenerator,
    val collectionId: String?,
    private val items: () -> Stream<T>
) : Descriptive {

    val collectionType: String = generator.key

    val title: String
        get() {
            val collectionTypeTitle = collectionType from { camelCase() } with { capitalize() } to { titleCase() }
            val collectionIdTitle: String? = collectionId?.from { camelCase() }?.with { capitalize() }?.to { titleCase() }

            return if (!EdenUtils.isEmpty(collectionIdTitle) && collectionTypeTitle != collectionIdTitle) {
                "$collectionTypeTitle > $collectionIdTitle"
            } else {
                collectionTypeTitle
            }
        }

    fun stream(): Stream<T> {
        return this.items()
    }

    fun getItems(): List<T> {
        return this.items().collect(Collectors.toList<T>())
    }
}
