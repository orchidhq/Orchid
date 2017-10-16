package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class OrchidMenuItemImpl {

    protected final OrchidContext context;

    protected String alias;

    protected List<OrchidMenuItemImpl> children;
    protected OrchidPage page;

    protected String title;
    protected String subtitle;
    protected boolean hasChildren;

    protected boolean isSeparator;

    public OrchidMenuItemImpl(OrchidContext context, String title, OrchidIndex index) {
        this.context = context;
        this.hasChildren = true;
        this.title = title;
        this.page = null;
        this.isSeparator = false;

        this.children = new ArrayList<>();

        if (index.getChildren() != null && index.getChildren().size() > 0) {
            for (Map.Entry<String, OrchidIndex> childIndex : index.getChildren().entrySet()) {
                if (!EdenUtils.isEmpty(childIndex.getValue().getOwnPages())) {
                    if (childIndex.getValue().getOwnPages().size() == 1) {
                        this.children.add(new OrchidMenuItemImpl(context, childIndex.getValue().getOwnPages().get(0)));
                    }
                    else {
                        this.children.add(new OrchidMenuItemImpl(context, childIndex.getKey(), childIndex.getValue()));
                    }
                }
                else {
                    this.children.add(new OrchidMenuItemImpl(context, childIndex.getKey(), childIndex.getValue()));
                }
            }
        }

        if (!EdenUtils.isEmpty(index.getOwnPages())) {
            for (OrchidPage page : index.getOwnPages()) {
                this.children.add(new OrchidMenuItemImpl(context, page));
            }
        }
    }

    public OrchidMenuItemImpl(OrchidContext context, @NonNull String title, @NonNull List<OrchidPage> pages) {
        this.context = context;
        this.hasChildren = true;

        this.title = title;

        this.children = new ArrayList<>();
        this.page = null;

        for (OrchidPage page : pages) {
            this.children.add(new OrchidMenuItemImpl(context, page));
        }
    }

    public OrchidMenuItemImpl(OrchidContext context, @NonNull List<OrchidMenuItemImpl> menuItems, @NonNull String title) {
        this.context = context;
        this.hasChildren = true;

        this.title = title;

        this.children = new ArrayList<>(menuItems);
        this.page = null;
    }

    public OrchidMenuItemImpl(OrchidContext context, String title, @NonNull OrchidPage page) {
        this.context = context;
        this.hasChildren = false;
        this.title = title;
        this.children = null;
        this.page = page;
    }

    public OrchidMenuItemImpl(OrchidContext context, @NonNull OrchidPage page) {
        this(context, page.getTitle(), page);
    }

    public OrchidMenuItemImpl(OrchidContext context, String dividerTitle) {
        this.context = context;
        this.hasChildren = false;

        this.title = dividerTitle;

        this.children = null;
        this.page = null;

        this.isSeparator = true;
    }

    public OrchidMenuItemImpl(OrchidContext context) {
        this.context = context;
        this.hasChildren = false;

        this.title = null;

        this.children = null;
        this.page = null;

        this.isSeparator = true;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return (page != null) ? page.getLink() : null;
    }

    public List<OrchidMenuItemImpl> getChildren() {
        return children;
    }

    public OrchidPage getPage() {
        return page;
    }

    public boolean isSeparator() {
        return isSeparator;
    }
}
