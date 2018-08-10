package com.eden.orchid.api.theme.models;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This is the description of the class.
 */
@Getter @Setter
public final class Social implements OptionsHolder {

    @AllOptions
    public Map<String, Object> allOptions;

    @Option
    @Description("Your email address.")
    public String email;

    @Option
    @Description("Your Facebook profile username.")
    public String facebook;

    @Option
    @Description("Your GitHub username.")
    public String github;

    @Option
    @Description("Your Google+ username.")
    public String googlePlus;

    @Option
    @Description("Your Instagram handle, without the @.")
    public String instagram;

    @Option
    @Description("Your Linked username.")
    public String linkedin;

    @Option
    @Description("Your twitter handle, without the @.")
    public String twitter;

    @Option
    @Description("For social platforms not included by default, you can fully specify the info yourself. You may " +
            "also choose to create an `other` item instead of using the default to fully customize the title, " +
            "link, order, or icon."
    )
    private List<Item> other;

    private List<Item> allItems;

    public Social() {
        
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item implements OptionsHolder {

        @Option
        @Description("The text to display with this social item.")
        public String label;

        @Option
        @Description("The full URL to link to.")
        public String link;

        @Option
        @Description("The icon to use for this item.")
        public String icon;

        @Option
        @Description("The order in which this item is rendered.")
        public int order;

    }

    public List<Item> getItems() {
        if(allItems == null) {

            List<Item> items = new ArrayList<>();

            if (!EdenUtils.isEmpty(email)) {
                items.add(new Item(
                        "email",
                        "mailto:" + email,
                        "fa-email",
                        10
                ));
            }
            if (!EdenUtils.isEmpty(facebook)) {
                items.add(new Item(
                        "facebook",
                        "https://www.facebook.com/" + facebook,
                        "fa-facebook",
                        20
                ));
            }
            if (!EdenUtils.isEmpty(github)) {
                items.add(new Item(
                        "github",
                        "https://github.com/" + github,
                        "fa-github",
                        30
                ));
            }
            if (!EdenUtils.isEmpty(googlePlus)) {
                items.add(new Item(
                        "googlePlus",
                        "" + googlePlus,
                        "fa-google-Plus",
                        40
                ));
            }
            if (!EdenUtils.isEmpty(instagram)) {
                items.add(new Item(
                        "instagram",
                        "https://www.instagram.com/" + instagram,
                        "fa-instagram",
                        50
                ));
            }
            if (!EdenUtils.isEmpty(linkedin)) {
                items.add(new Item(
                        "linkedin",
                        "https://www.linkedin.com/in/" + linkedin,
                        "fa-linkedin-square",
                        60
                ));
            }
            if (!EdenUtils.isEmpty(twitter)) {
                items.add(new Item(
                        "twitter",
                        "https://twitter.com/" + twitter,
                        "fa-twitter",
                        70
                ));
            }

            if (!EdenUtils.isEmpty(other)) {
                items.addAll(other);
            }

            items.sort(Comparator.comparingInt(value -> value.order));

            allItems = items;
        }

        return allItems;
    }

}