package com.eden.orchid.posts;

import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;

public class Author implements OptionsHolder {

    @Option public String name;
    @Option public String avatar;

}
