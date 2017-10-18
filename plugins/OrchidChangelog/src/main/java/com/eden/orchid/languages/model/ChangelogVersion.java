package com.eden.orchid.languages.model;

import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import lombok.Getter;
import lombok.Setter;

public class ChangelogVersion implements OptionsHolder {

    @Getter @Setter @Option String name;
    @Getter @Setter @Option String version;
    @Getter @Setter @Option String url;

}
