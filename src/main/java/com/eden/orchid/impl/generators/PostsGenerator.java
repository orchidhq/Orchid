package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PostsGenerator implements OrchidGenerator {

    private List<OrchidPage> posts;
    private OrchidResources resources;

    @Inject
    public PostsGenerator(OrchidResources resources) {
        this.resources = resources;
    }

    @Override
    public int priority() {
        return 700;
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
    public JSONElement startIndexing() {
        JSONObject sitePosts = new JSONObject();

        List<OrchidResource> resourcesList = resources.getResourceDirEntries("posts", null, true);
        posts = new ArrayList<>();

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

            JSONObject index = new JSONObject();
            index.put("name", post.getReference().getTitle());
            index.put("title", post.getReference().getTitle());
            index.put("url", post.getReference().toString());

            OrchidUtils.buildTaxonomy(entry, sitePosts, index);
        }

        return new JSONElement(sitePosts);
    }

    @Override
    public void startGeneration() {
        int i = 0;
        for (OrchidPage post : posts) {
            if (next(i) != null) { post.setNext(next(i)); }
            if (previous(i) != null) { post.setPrevious(previous(i)); }
            post.renderTemplate("templates/pages/page.twig");
            i++;
        }
    }

    public OrchidPage previous(int i) {
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

    public OrchidPage next(int i) {
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
