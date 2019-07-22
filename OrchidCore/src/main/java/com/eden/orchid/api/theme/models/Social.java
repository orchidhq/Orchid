package com.eden.orchid.api.theme.models;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This is the description of the class.
 */
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
    @Description("Your Instagram handle, without the @.")
    public String instagram;
    @Option
    @Description("Your Linked username.")
    public String linkedin;
    @Option
    @Description("Your twitter handle, without the @.")
    public String twitter;
    @Option
    @Description("For social platforms not included by default, you can fully specify the info yourself. You may also choose to create an `other` item instead of using the default to fully customize the title, link, order, or icon.")
    private List<Item> other;
    private List<Item> allItems;

    public Social() {
    }


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
        @AllOptions
        private Map<String, Object> allData;

        public Item(String label, String link, String icon, int order) {
            this.label = label;
            this.link = link;
            this.icon = icon;
            this.order = order;
        }

        public Object get(String key) {
            return allData.get(key);
        }

        public Item() {
        }

        public Map<String, Object> getAllData() {
            return this.allData;
        }

        public void setAllData(final Map<String, Object> allData) {
            this.allData = allData;
        }
    }

    public List<Item> getItems() {
        if (allItems == null) {
            List<Item> items = new ArrayList<>();
            if (!EdenUtils.isEmpty(email)) {
                items.add(new Item("email", "mailto:" + email, "fa-envelope", 10));
            }
            if (!EdenUtils.isEmpty(facebook)) {
                items.add(new Item("facebook", "https://www.facebook.com/" + facebook, "fa-facebook", 20));
            }
            if (!EdenUtils.isEmpty(github)) {
                items.add(new Item("github", "https://github.com/" + github, "fa-github", 30));
            }
            if (!EdenUtils.isEmpty(instagram)) {
                items.add(new Item("instagram", "https://www.instagram.com/" + instagram, "fa-instagram", 50));
            }
            if (!EdenUtils.isEmpty(linkedin)) {
                items.add(new Item("linkedin", "https://www.linkedin.com/in/" + linkedin, "fa-linkedin-square", 60));
            }
            if (!EdenUtils.isEmpty(twitter)) {
                items.add(new Item("twitter", "https://twitter.com/" + twitter, "fa-twitter", 70));
            }
            if (!EdenUtils.isEmpty(other)) {
                items.addAll(other);
            }
            items.sort(Comparator.comparingInt(value -> value.order));
            allItems = items;
        }
        return allItems;
    }

    public Map<String, Object> getAllOptions() {
        return this.allOptions;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public String getGithub() {
        return this.github;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public List<Item> getOther() {
        return this.other;
    }

    public List<Item> getAllItems() {
        return this.allItems;
    }

    public void setAllOptions(final Map<String, Object> allOptions) {
        this.allOptions = allOptions;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFacebook(final String facebook) {
        this.facebook = facebook;
    }

    public void setGithub(final String github) {
        this.github = github;
    }

    public void setInstagram(final String instagram) {
        this.instagram = instagram;
    }

    public void setLinkedin(final String linkedin) {
        this.linkedin = linkedin;
    }

    public void setTwitter(final String twitter) {
        this.twitter = twitter;
    }

    public void setOther(final List<Item> other) {
        this.other = other;
    }

    public void setAllItems(final List<Item> allItems) {
        this.allItems = allItems;
    }
}
