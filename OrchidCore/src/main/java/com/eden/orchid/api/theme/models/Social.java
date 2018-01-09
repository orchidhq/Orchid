package com.eden.orchid.api.theme.models;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.annotations.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This is the description of the class.
 */
@Getter @Setter
public class Social implements OptionsHolder {

    @Option public String email;
    @Option public String facebook;
    @Option public String github;
    @Option public String googlePlus;
    @Option public String instagram;
    @Option public String linkedin;
    @Option public String twitter;

    @Option @ListClass(Item.class) private List<Item> other;

    public Social() {
        
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item implements OptionsHolder {

        @Option public String label;
        @Option public String link;
        @Option public String icon;
        @Option public int order;

    }

    public List<Item> getItems() {
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
                    "https://www.linkedin.com/in/" + instagram,
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

        return items;
    }

}