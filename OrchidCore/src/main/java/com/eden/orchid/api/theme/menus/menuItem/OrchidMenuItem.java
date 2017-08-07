package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class OrchidMenuItem {

    protected String alias;

    protected OrchidContext context;

    protected List<OrchidMenuItem> children;
    protected OrchidPage page;

    protected String title;
    protected boolean hasChildren;

    protected boolean isSeparator;

    public OrchidMenuItem(OrchidContext context, String title, OrchidIndex index) {
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
                        this.children.add(new OrchidMenuItem(context, childIndex.getValue().getOwnPages().get(0)));
                    }
                    else {
                        this.children.add(new OrchidMenuItem(context, childIndex.getKey(), childIndex.getValue()));
                    }
                }
                else {
                    this.children.add(new OrchidMenuItem(context, childIndex.getKey(), childIndex.getValue()));
                }
            }
        }

        if (!EdenUtils.isEmpty(index.getOwnPages())) {
            for (OrchidPage page : index.getOwnPages()) {
                this.children.add(new OrchidMenuItem(context, page));
            }
        }
    }

    public OrchidMenuItem(OrchidContext context, @NonNull String title, @NonNull List<OrchidPage> pages) {
        this.context = context;
        this.hasChildren = true;

        this.title = title;

        this.children = new ArrayList<>();
        this.page = null;

        for (OrchidPage page : pages) {
            this.children.add(new OrchidMenuItem(context, page));
        }
    }

    public OrchidMenuItem(OrchidContext context, String title, @NonNull OrchidPage page) {
        this.context = context;
        this.hasChildren = false;
        this.title = title;
        this.children = null;
        this.page = page;
    }

    public OrchidMenuItem(OrchidContext context, @NonNull OrchidPage page) {
        this(context, page.getTitle(), page);
    }

    public OrchidMenuItem(OrchidContext context, String dividerTitle) {
        this.context = context;
        this.hasChildren = false;

        this.title = dividerTitle;

        this.children = null;
        this.page = null;

        this.isSeparator = true;
    }

    public OrchidMenuItem(OrchidContext context) {
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

    public List<OrchidMenuItem> getChildren() {
        return children;
    }

    public OrchidPage getPage() {
        return page;
    }

    public boolean isSeparator() {
        return isSeparator;
    }
}
