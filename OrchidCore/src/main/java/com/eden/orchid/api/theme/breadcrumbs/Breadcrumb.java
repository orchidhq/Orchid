package com.eden.orchid.api.theme.breadcrumbs;


import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Breadcrumb {

    private OrchidPage page;
    private String title;

    public Breadcrumb(OrchidPage page) {
        this.page = page;
        this.title = page.getTitle();
    }

    public Breadcrumb(OrchidPage page, String title) {
        this.page = page;
        this.title = title;
    }

    // have both of these methods for parity with OrchidMenuItemImpl
    public boolean isActivePage(OrchidPage page) {
        return this.page == page;
    }

    public boolean isActive(OrchidPage page) {
        return isActivePage(page);
    }

    public String getLink() {
        return (page != null)
                ? page.getLink()
                : null;
    }

    public String activeClass(OrchidPage page) {
        return activeClass(page, "active");
    }

    public String activeClass(OrchidPage page, String className) {
        return (isActive(page)) ? className : "";
    }

}
