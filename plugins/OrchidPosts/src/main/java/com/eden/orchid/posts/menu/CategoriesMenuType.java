package com.eden.orchid.posts.menu;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.PostsModel;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesMenuType extends OrchidMenuItem {

    private PostsModel postsModel;

    @Option
    public String category;

    @Option
    public String title;

    @Inject
    public CategoriesMenuType(OrchidContext context, PostsModel postsModel) {
        super(context, "postCategories", 100);

        this.postsModel = postsModel;
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> items = new ArrayList<>();

        if(!EdenUtils.isEmpty(category) && postsModel.getCategories().containsKey(category)) {
            EdenPair<List<PostPage>, List<PostArchivePage>> categoryDef = postsModel.getCategories().get(category);
            if(!EdenUtils.isEmpty(categoryDef.second)) {
                OrchidMenuItemImpl menuItem = new OrchidMenuItemImpl(context, categoryDef.second.get(0));
                if (!EdenUtils.isEmpty(title)) {
                    menuItem.setTitle(title);
                }
                items.add(menuItem);
            }
        }
        else if(postsModel.getCategoryNames().size() > 1) {
            List<OrchidPage> pages = new ArrayList<>();
            for(Map.Entry<String, EdenPair<List<PostPage>, List<PostArchivePage>>> tag : postsModel.getCategories().entrySet()) {
                if(!EdenUtils.isEmpty(tag.getValue().second)) {
                    pages.add(tag.getValue().second.get(0));
                }
            }

            OrchidMenuItemImpl menuItem = new OrchidMenuItemImpl(context, "Categories", pages);
            if(!EdenUtils.isEmpty(title)) {
                menuItem.setTitle(title);
            }
            items.add(menuItem);
        }
        else if(postsModel.getCategories().containsKey(null)) {
            EdenPair<List<PostPage>, List<PostArchivePage>> categoryDef = postsModel.getCategories().get(null);
            if(!EdenUtils.isEmpty(categoryDef.second)) {
                OrchidMenuItemImpl menuItem = new OrchidMenuItemImpl(context, categoryDef.second.get(0));
                if (!EdenUtils.isEmpty(title)) {
                    menuItem.setTitle(title);
                }
                items.add(menuItem);
            }
        }

        return items;
    }
}
