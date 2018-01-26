package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.theme.pages.OrchidPage

class Term(val key: String) {

    public val pages = HashSet<OrchidPage>()

}