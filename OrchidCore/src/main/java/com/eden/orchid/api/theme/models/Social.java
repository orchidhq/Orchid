package com.eden.orchid.api.theme.models;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.annotations.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Social implements OptionsHolder {

    @Option private String twitter;
    @Option private String facebook;
    @Option private String instagram;
    @Option private String googlePlus;
    @Option private String email;
    @Option private String github;

    @Option @ListClass(Item.class) private List<Item> other;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item implements OptionsHolder {

        @Option public String label;
        @Option public String link;
        @Option public String icon;

    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        if (!EdenUtils.isEmpty(twitter)) {
            items.add(new Item(
                    "twitter",
                    "https://twitter.com/" + twitter,
                    "fa-twitter"
            ));
        }
        if (!EdenUtils.isEmpty(facebook)) {
            items.add(new Item(
                    "facebook",
                    "https://www.facebook.com/" + facebook,
                    "fa-facebook"
            ));
        }
        if (!EdenUtils.isEmpty(instagram)) {
            items.add(new Item(
                    "instagram",
                    "https://www.instagram.com/" + instagram,
                    "fa-instagram"
            ));
        }
        if (!EdenUtils.isEmpty(googlePlus)) {
            items.add(new Item(
                    "googlePlus",
                    "" + googlePlus,
                    "fa-google-Plus"
            ));
        }
        if (!EdenUtils.isEmpty(email)) {
            items.add(new Item(
                    "email",
                    "mailto:" + email,
                    "fa-email"
            ));
        }
        if (!EdenUtils.isEmpty(github)) {
            items.add(new Item(
                    "github",
                    "https://github.com/" + github,
                    "fa-github"
            ));
        }

        if (!EdenUtils.isEmpty(other)) {
            items.addAll(other);
        }

        return items;
    }

}