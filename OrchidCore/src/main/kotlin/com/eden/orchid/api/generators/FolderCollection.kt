package com.eden.orchid.api.generators

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(
    "A Folder Collection represents a collection of OrchidPages that are located as a batch from a " +
            "specified folder in your resources. A page is matched from a Folder Collection with an \'itemId\' " +
            "matching the page\'s title."
)
open class FolderCollection(
    generator: OrchidGenerator,
    collectionId: String,
    items: List<OrchidPage>,
    val pageClass: Class<out OrchidPage>,
    val resourceRoot: String
) : OrchidCollection<OrchidPage>(generator, collectionId, { items.stream() }) {

    var isCanCreate = true
    var isCanDelete = true
    var slugFormat = "{slug}"
}
