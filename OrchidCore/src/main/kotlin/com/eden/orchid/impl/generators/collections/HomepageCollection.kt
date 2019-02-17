package com.eden.orchid.impl.generators.collections

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.generators.HomepageGenerator
import java.util.stream.Stream

class HomepageCollection(homepageGenerator: HomepageGenerator) :
    OrchidCollection<OrchidPage>(homepageGenerator, HomepageGenerator.GENERATOR_KEY, homepageGenerator.pages) {

    override fun find(id: String): Stream<OrchidPage>? {
        return if (id.equals("home", ignoreCase = true) && !EdenUtils.isEmpty(this.items)) {
            Stream.of(this.items[0])
        } else null
    }
}