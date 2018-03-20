package com.eden.orchid.api.site;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.RelationConfig;
import com.eden.orchid.impl.relations.AssetRelation;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class SiteInfo implements OptionsHolder {

    @Option
    @Description("A fully-specified URL to an avatar image, or a relative path to an Orchid image.")
    private AssetRelation avatar;

    @Option @RelationConfig(itemId = "favicon.ico")
    @Description("A fully-specified URL to a favicon image, or a relative path to an Orchid image.")
    private AssetRelation favicon;

    @Option
    @Description("The proper name of the site. May be used for display, or as a fallback to a page's title tag if " +
            "the page doesn't specify a title."
    )
    private String siteName;

    @Option
    @Description("A short subtitle for the site, usually just for display.")
    private String subtitle;

    @Option
    @Description("A short description of the site. May be used for display, or as a fallback to a page's meta " +
            "description tag if the page doesn't specify a description."
    )
    private String siteDescription;

}