package com.eden.orchid.posts;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PostsGenerator extends OrchidGenerator {

    private OrchidResources resources;

    @Inject
    public PostsGenerator(OrchidResources resources) {
        this.resources = resources;
        this.priority = 700;
    }

    @Override
    public String getName() {
        return "posts";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = resources.getLocalResourceEntries("posts", null, true);
        List<OrchidPage> posts = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            if(!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                entry.getReference().setTitle(entry.queryEmbeddedData("title").toString());
            }
            else {
                entry.getReference().setTitle(entry.getReference().getFileName());
            }

            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripBasePath("posts/");
                }
            }

            entry.getReference().setUsePrettyUrl(true);

            OrchidPage post = new OrchidPage(entry);

            posts.add(post);
        }

        return posts;
    }

    @Override
    public void startGeneration(List<OrchidPage> posts) {
        int i = 0;
        for (OrchidPage post : posts) {
            if (next(posts, i) != null) { post.setNext(next(posts, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(posts, i)); }
            post.renderTemplate("templates/pages/page.twig");
            i++;
        }
    }

    public OrchidPage previous(List<OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i == 0) {
                return posts.get(posts.size() - 1);
            }
            else {
                return posts.get(i - 1);
            }
        }

        return null;
    }

    public OrchidPage next(List<OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i == posts.size() - 1) {
                return posts.get(0);
            }
            else {
                return posts.get(i + 1);
            }
        }

        return null;
    }
}
