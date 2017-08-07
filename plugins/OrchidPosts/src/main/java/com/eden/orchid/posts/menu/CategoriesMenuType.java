package com.eden.orchid.posts.menu;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.PostsModel;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesMenuType implements OrchidMenuItemFactory {

    private OrchidContext context;
    private PostsModel postsModel;

    @Inject
    public CategoriesMenuType(OrchidContext context, PostsModel postsModel) {
        this.context = context;
        this.postsModel = postsModel;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> items = new ArrayList<>();

        if(postsModel.getCategoryNames().size() > 1) {
            List<OrchidPage> pages = new ArrayList<>();
            for(Map.Entry<String, EdenPair<List<PostPage>, List<PostArchivePage>>> tag : postsModel.getCategories().entrySet()) {
                pages.add(tag.getValue().second.get(0));
            }
            items.add(new OrchidMenuItem(context, "Categories", pages));
        }
        else {
            items.add(new OrchidMenuItem(context, postsModel.getCategories().get(null).second.get(0)));
        }

        return items;
    }
}
