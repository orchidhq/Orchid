package com.eden.orchid.posts.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.ApplyBaseUrl
import com.eden.orchid.api.options.annotations.Option

class Author : OptionsHolder {

    @Option
    lateinit var name: String

    @Option
    @ApplyBaseUrl
    lateinit var avatar: String

}

