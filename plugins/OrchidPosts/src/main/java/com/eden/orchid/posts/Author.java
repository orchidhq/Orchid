package com.eden.orchid.posts;

import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ApplyBaseUrl;

public class Author implements OptionsHolder {

    @Option public String name;

    @Option
    @ApplyBaseUrl
    public String avatar;

}
