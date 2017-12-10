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
import java.util.List;

/**
 * This is the description of the class.
 */
@Getter @Setter
public class Social implements OptionsHolder {

    @Option public String twitter;
    @Option public String facebook;
    @Option public String instagram;
    @Option public String googlePlus;
    @Option public String email;
    @Option public String github;

    /**
     * No-arg Constructor to initialize empty object
     *
     * @param <T> the type parameter
     */
    public <T> Social() {

    }

    /**
     * Initialize with all parameters
     *
     * @param twitter twitter handle
     * @param facebook facebook handle
     * @param instagram instagram handle
     * @param googlePlus google+ username
     * @param email email address
     * @param github github user/repo
     * @param other a list of Items where you can customize the text, link, and icon, useful for adding social links to
     *             services not supported by default
     */
    public Social(String twitter, String facebook, String instagram, String googlePlus, String email, String github, List<Item> other) {
        this.twitter = twitter;
        this.facebook = facebook;
        this.instagram = instagram;
        this.googlePlus = googlePlus;
        this.email = email;
        this.github = github;
        this.other = other;
    }

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


    /**
     * This method is null and void
     */
    public void voidMethod() {

    }

    /**
     * This method takes in 3 strings and returns nothing
     *
     * @param s1 param 1
     * @param s2 param 2
     * @param s3 param 3
     */
    public void methodWithStrings(String s1, String s2, String s3) {

    }

    /**
     * This method takes a String, a template type, and another string.
     *
     * @param s1 param 1
     * @param s2 param 2 of generic type
     * @param s3 param 3
     * @param <T> some generic type
     */
    public <T> void methodWithStrings(String s1, T s2, String s3) {

    }

    /**
     * This method returns a String
     *
     * @return a String
     */
    public String methodReturnsString() {
        return null;
    }

}