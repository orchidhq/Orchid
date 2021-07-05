package com.eden.orchid.api.generators

/**
 * An interface which marks an object as able to be held within an [OrchidCollection]. It should either wrap another
 * item, returning that item and it's item IDS, or else be applied to an item, which returns itself and its own item
 * IDs.
 */
interface Collectible<T> {
    val item: T
    val itemIds: List<String>
}
