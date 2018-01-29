package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option


class TaxonomyPaginator : OptionsHolder {

    @Option
    @IntDefault(10)
    var pageSize: Int = 0

    @Option
    lateinit var permalink: String

}

