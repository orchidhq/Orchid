package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public final class OrchidMenuItemImpl {

    @NonNull private final OrchidContext context;
    @NonNull private final String title;
    @Nullable private final OrchidPage page;
    @NonNull private final List<OrchidMenuItemImpl> children;
    @NonNull private final String anchor;
    private final boolean separator;
    @NonNull private Map<String, Object> allData;

    private Comparator<OrchidMenuItemImpl> indexComparator;

    private OrchidMenuItemImpl(
            @NonNull OrchidContext context,
            @NonNull String title,
            @Nullable OrchidPage page,
            @NonNull List<OrchidMenuItemImpl> children,
            @NonNull String anchor,
            boolean separator,
            @NonNull Map<String, Object> allData,
            @Nullable Comparator<OrchidMenuItemImpl> indexComparator
    ) {
        this.context = context;
        this.title = title;
        this.page = page;
        this.children = children;
        this.anchor = anchor;
        this.separator = separator;
        this.allData = allData;
        this.indexComparator = indexComparator;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return (page != null)
                ? page.getLink()
                : (!EdenUtils.isEmpty(anchor))
                ? "#" + anchor
                : null;
    }

    public List<OrchidMenuItemImpl> getChildren() {
        if (indexComparator != null) {
            for (OrchidMenuItemImpl child : children) {
                child.setIndexComparator(indexComparator);
            }

            children.sort(indexComparator);
        }
        return children;
    }

    @Nullable
    public OrchidPage getPage() {
        return page;
    }

    public boolean isSeparator() {
        return separator;
    }

    public boolean isActive(OrchidPage page) {
        return isActivePage(page) || hasActivePage(page);
    }

    public boolean isActivePage(OrchidPage page) {
        return !isHasChildren() && this.page == page;
    }

    public boolean hasActivePage(OrchidPage page) {
        if (isHasChildren()) {
            for (OrchidMenuItemImpl child : children) {
                if (child.isActivePage(page) || child.hasActivePage(page)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String activeClass(OrchidPage page) {
        return activeClass(page, "active");
    }

    public String activeClass(OrchidPage page, String className) {
        return (isActive(page)) ? className : "";
    }

    public boolean isHasChildren() {
        return hasChildren();
    }

    public boolean hasChildren() {
        return !EdenUtils.isEmpty(children);
    }

    public Object get(String key) {
        return allData.get(key);
    }

    public OrchidMenuItemImpl.Builder toBuilder() {
        return new OrchidMenuItemImpl.Builder(context)
                .title(title)
                .page(page)
                .children(children)
                .anchor(anchor)
                .separator(separator)
                .data(allData)
                .indexComparator(indexComparator);
    }

// Builder
//----------------------------------------------------------------------------------------------------------------------

    public static final class Builder {

        @NonNull protected final OrchidContext context;
        @Nullable private List<OrchidMenuItemImpl.Builder> children;
        @Nullable private OrchidPage page;
        @Nullable private String anchor;
        @Nullable private String title;
        @Nullable private Comparator<OrchidMenuItemImpl> indexComparator;
        @Nullable private Map<String, Object> data;

        private boolean separator;

        public Builder(@NonNull OrchidContext context) {
            this.context = context;
        }

        public Builder fromIndex(@NonNull OrchidIndex index) {
            if (children == null) {
                children = new ArrayList<>();
            }

            if (index.getChildren().size() > 0) {
                for (Map.Entry<String, OrchidIndex> childIndex : index.getChildren().entrySet()) {
                    if (!EdenUtils.isEmpty(childIndex.getValue().getOwnPages())) {
                        if (childIndex.getValue().getOwnPages().size() == 1) {
                            this.children.add(new Builder(context)
                                    .page(
                                            childIndex
                                                    .getValue()
                                                    .getOwnPages()
                                                    .get(0)
                                    )
                            );
                        }
                        else {
                            this.children.add(new Builder(context).title(childIndex.getKey()).fromIndex(childIndex.getValue()));
                        }
                    }
                    else {
                        this.children.add(new Builder(context).title(childIndex.getKey()).fromIndex(childIndex.getValue()));
                    }
                }
            }

            if (!EdenUtils.isEmpty(index.getOwnPages())) {
                for (OrchidPage page : index.getOwnPages()) {
                    this.children.add(new Builder(context).page(page));
                }
            }

            return this;
        }

        public OrchidContext getContext() {
            return context;
        }

        @Nullable
        public List<OrchidMenuItemImpl.Builder> children() {
            return children;
        }

        public Builder childrenBuilders(@NonNull List<OrchidMenuItemImpl.Builder> children) {
            this.children = children;
            return this;
        }

        public Builder children(@NonNull List<OrchidMenuItemImpl> children) {
            this.children = children.stream().map(OrchidMenuItemImpl::toBuilder).collect(Collectors.toList());
            return this;
        }

        public Builder child(@NonNull OrchidMenuItemImpl.Builder child) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
            return this;
        }

        public Builder pages(@NonNull List<OrchidPage> pages) {
            this.children = pages
                    .stream()
                    .map(it -> new Builder(context).page(it))
                    .collect(Collectors.toList())
            ;
            return this;
        }

        @Nullable
        public OrchidPage page() {
            return page;
        }

        public Builder page(@Nullable OrchidPage page) {
            this.page = page;
            return this;
        }

        @Nullable
        public String anchor() {
            return anchor;
        }

        public Builder anchor(@NonNull String anchor) {
            this.anchor = anchor;
            return this;
        }

        @Nullable
        public String title() {
            return title;
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public boolean separator() {
            return separator;
        }

        public Builder separator(boolean separator) {
            this.separator = separator;
            return this;
        }

        @Nullable
        public Comparator<OrchidMenuItemImpl> indexComparator() {
            return indexComparator;
        }

        public Builder indexComparator(@Nullable Comparator<OrchidMenuItemImpl> indexComparator) {
            this.indexComparator = indexComparator;
            return this;
        }

        @Nullable
        public Map<String, Object> data() {
            return data;
        }

        public Builder data(@NonNull Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public OrchidMenuItemImpl build() {
            if (EdenUtils.isEmpty(title) && page != null) {
                title = page.getTitle();
            }

            if (EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(anchor)) {
                title = anchor;
            }

            if (EdenUtils.isEmpty(title)) {
                title = "";
            }

            return new OrchidMenuItemImpl(
                    context,
                    title,
                    page,
                    (children != null) ? children.stream().map(OrchidMenuItemImpl.Builder::build).collect(Collectors.toList()) : new ArrayList<>(),
                    (anchor != null) ? anchor : "",
                    separator,
                    (data != null) ? data : new HashMap<>(),
                    indexComparator
            );
        }
    }

}
