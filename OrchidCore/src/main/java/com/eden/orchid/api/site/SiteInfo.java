package com.eden.orchid.api.site;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.RelationConfig;
import com.eden.orchid.impl.relations.AssetRelation;

@Description(value = "The global information for your Orchid site.", name = "Site Info")
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

    public AssetRelation getAvatar() {
        return this.avatar;
    }

    public AssetRelation getFavicon() {
        return this.favicon;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getSiteDescription() {
        return this.siteDescription;
    }

    public void setAvatar(AssetRelation avatar) {
        this.avatar = avatar;
    }

    public void setFavicon(AssetRelation favicon) {
        this.favicon = favicon;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }
}