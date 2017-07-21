package com.eden.orchid.posts;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class PostsGenerator extends OrchidGenerator implements OptionsHolder {

    public static final Pattern pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)");
    private PostsPermalinkStrategy permalinkStrategy;

    public static Map<String, EdenPair<List<PostPage>, List<PostArchivePage>>> categories;

    @Option
    public String permalink;

    @Option("categories")
    public String[] categoryNames;

    @Option
    public PostsPaginator pagination;

    @Inject
    public PostsGenerator(OrchidContext context, OrchidResources resources, OrchidRenderer renderer, PostsPermalinkStrategy permalinkStrategy) {
        super(700, "posts", context, resources, renderer);
        this.permalinkStrategy = permalinkStrategy;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        // Map category names to pairs of PostPages and PostArchivePages
        categories = new HashMap<>();

        if(EdenUtils.isEmpty(categoryNames)) {
            List<PostPage> posts = getPostsList(null);
            List<PostArchivePage> archive = buildArchive(null, posts);
            categories.put(null, new EdenPair<>(posts, archive));
        }
        else {
            for (String category : categoryNames) {
                List<PostPage> posts = getPostsList(category);
                List<PostArchivePage> archive = buildArchive(category, posts);
                categories.put(category, new EdenPair<>(posts, archive));
            }
        }

        List<OrchidPage> allPages = new ArrayList<>();
        for(String key : categories.keySet()) {
            allPages.addAll(categories.get(key).first);
            allPages.addAll(categories.get(key).second);
        }

        return allPages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> posts) {
        posts.forEach(renderer::renderTemplate);
    }

    private OrchidPage previous(List<? extends OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i != 0) {
                return posts.get(i - 1);
            }
        }

        return null;
    }

    private OrchidPage next(List<? extends OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i < posts.size() - 1) {
                return posts.get(i + 1);
            }
        }

        return null;
    }

    private List<PostPage> getPostsList(String category) {
        List<OrchidResource> resourcesList;
        if(EdenUtils.isEmpty(category)) {
            resourcesList = resources.getLocalResourceEntries("posts", null, false);
        }
        else {
            resourcesList = resources.getLocalResourceEntries("posts/" + category, null, false);
        }

        List<PostPage> posts = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            entry.getReference().setUsePrettyUrl(false);
            Matcher matcher = pageTitleRegex.matcher(entry.getReference().getFileName());

            if (matcher.matches()) {
                PostPage post = new PostPage(entry);

                JSONObject pageData =
                        (OrchidUtils.elementIsObject(entry.getEmbeddedData()))
                                ? (JSONObject) entry.getEmbeddedData().getElement()
                                : new JSONObject();

                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int day = Integer.parseInt(matcher.group(3));

                String title = matcher.group(4);

                title = Arrays.stream(title.split("-"))
                              .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                              .collect(Collectors.joining(" "));

                post.setYear(year);
                post.setMonth(month);
                post.setDay(day);

                post.setCategory(category);

                post.getReference().setTitle(title);
                post.setData(pageData);

                permalinkStrategy.applyPermalink(post);

                posts.add(post);
            }
        }

        posts.sort((o1, o2) -> {
            String[] criteria = new String[]{"year", "month", "day"};

            for (String item : criteria) {
                int result = 0;

                switch (item) {
                    case "year":
                        result = o2.getYear() - o1.getYear();
                        break;
                    case "month":
                        result = o2.getMonth() - o1.getMonth();
                        break;
                    case "day":
                        result = o2.getDay() - o1.getDay();
                        break;
                }

                if (result != 0) {
                    return result;
                }
            }

            return 0;
        });

        int i = 0;
        for (PostPage post : posts) {
            if (next(posts, i) != null) { post.setNext(next(posts, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(posts, i)); }
            i++;
        }

        return posts;
    }

    private List<PostArchivePage> buildArchive(String category, List<PostPage> posts) {
        List<PostArchivePage> archivePages = new ArrayList<>();

        int pages = (int) Math.ceil(posts.size() / pagination.pageSize);

        for (int i = 0; i <= pages; i++) {
            String pageName = (!EdenUtils.isEmpty(category))
                ? "archive/" + category + "/" + (i + 1) + ".html"
                : "archive/" + (i + 1) + ".html";

            OrchidReference pageRef = new OrchidReference(context, pageName);

            if(i == 0) {
                if(!EdenUtils.isEmpty(category)) {
                    pageRef.setTitle(StringUtils.capitalize(category));
                }
                else {
                    pageRef.setTitle("Blog Archive");
                }
            }
            else {
                if(!EdenUtils.isEmpty(category)) {
                    pageRef.setTitle(StringUtils.capitalize(category) + " Archive (Page " + (i + 1) + ")");
                }
                else {
                    pageRef.setTitle("Blog Archive (Page " + (i + 1) + ")");
                }
            }

            PostArchivePage page = new PostArchivePage(new StringResource("", pageRef));

            page.setPostList(posts.subList((i * pagination.pageSize), Math.min(((i+1) * pagination.pageSize), posts.size())));

            if(!EdenUtils.isEmpty(category)) {
                page.setCategory(category);
            }

            archivePages.add(page);
        }

        int i = 0;
        for (PostArchivePage post : archivePages) {
            if (next(posts, i) != null) { post.setNext(next(archivePages, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(archivePages, i)); }
            i++;
        }

        return archivePages;
    }
}
