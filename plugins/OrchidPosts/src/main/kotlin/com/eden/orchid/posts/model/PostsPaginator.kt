package com.eden.orchid.posts.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option


class PostsPaginator : OptionsHolder {

    @Option
    @IntDefault(100)
    var pageSize: Int = 0

    @Option
    lateinit var permalink: String

}

