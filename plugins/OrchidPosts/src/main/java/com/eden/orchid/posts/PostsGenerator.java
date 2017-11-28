package com.eden.orchid.posts;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.pages.PostTagArchivePage;
import com.eden.orchid.posts.permalink.PostsPermalinkStrategy;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
@Description("Share your thoughts and interests with blog posts and archives.")
public class PostsGenerator extends OrchidGenerator implements OptionsHolder {

    public static final Pattern pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)");
    private PostsPermalinkStrategy permalinkStrategy;

    private PostsModel postsModel;

    @Option
    @StringDefault(":year/:month/:day/:slug")
    public String permalink;

    @Option
    @StringDefault("<!--more-->")
    public String excerptSeparator;

    @Option
    @ListClass(Author.class)
    public List<Author> authors;

    @Option("categories")
    public String[] categoryNames;

    @Option
    public PostsPaginator pagination;

    @Option
    public String disqusShortname;

    @Inject
    public PostsGenerator(OrchidContext context, PostsPermalinkStrategy permalinkStrategy, PostsModel postsModel) {
        super(context, "posts", 700);
        this.permalinkStrategy = permalinkStrategy;
        this.postsModel = postsModel;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        postsModel.initialize(permalink, layout, excerptSeparator, authors);

        if(EdenUtils.isEmpty(categoryNames)) {
            List<PostPage> posts = getPostsList(null);
            List<PostArchivePage> archive = buildArchive(null, posts);
            postsModel.getCategories().put(null, new EdenPair<>(posts, archive));
            tagPosts(posts);
        }
        else {
            for (String category : categoryNames) {
                List<PostPage> posts = getPostsList(category);
                List<PostArchivePage> archive = buildArchive(category, posts);
                postsModel.getCategories().put(category, new EdenPair<>(posts, archive));
                tagPosts(posts);
            }
        }

        for (String tag : postsModel.getTagNames()) {
            List<PostPage> posts = postsModel.getPostsTagged(tag);
            List<PostTagArchivePage> archive = buildTagArchive(tag, posts);
            postsModel.getTags().get(tag).second.addAll(archive);
        }

        List<OrchidPage> allPages = new ArrayList<>();
        for(String key : postsModel.getCategories().keySet()) {
            allPages.addAll(postsModel.getCategories().get(key).first);
            allPages.addAll(postsModel.getCategories().get(key).second);
        }
        for(String key : postsModel.getTags().keySet()) {
            allPages.addAll(postsModel.getTags().get(key).second);
        }

        return allPages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> posts) {
        try {
            posts.forEach(context::renderTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        String baseCategoryPath;

        if(EdenUtils.isEmpty(category)) {
            baseCategoryPath = "posts";
        }
        else {
            baseCategoryPath = "posts/" + category;
        }

        resourcesList = context.getLocalResourceEntries(baseCategoryPath, null, true);

        List<PostPage> posts = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            entry.getReference().setUsePrettyUrl(false);

            String formattedFilename = PostsUtils.getPostFilename(entry, baseCategoryPath);

            Matcher matcher = pageTitleRegex.matcher(formattedFilename);

            if (matcher.matches()) {
                PostPage post = new PostPage(entry, postsModel);

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
                post.setTitle(title);

                permalinkStrategy.applyPermalink(post);

                if(!EdenUtils.isEmpty(disqusShortname)) {
                    if(post.getComponents().hasComponent("disqus")) {
                        JSONObject disqusComponent = post.getComponents().getComponentData("disqus");
                        if(disqusComponent != null && !disqusComponent.has("shortname")) {
                            disqusComponent.put("shortname", disqusShortname);
                            post.getComponents().invalidateComponents();
                        }
                    }
                    else {
                        JSONObject disqusComponent = new JSONObject();
                        disqusComponent.put("type", "disqus");
                        disqusComponent.put("shortname", disqusShortname);
                        post.getComponents().addComponent(disqusComponent);
                    }
                }

                posts.add(post);
            }
        }

        posts.sort(PostsModel.postPageComparator);

        int i = 0;
        for (PostPage post : posts) {
            if (next(posts, i) != null) { post.setPrevious(next(posts, i)); }
            if (previous(posts, i) != null) { post.setNext(previous(posts, i)); }
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

    private List<PostTagArchivePage> buildTagArchive(String tag, List<PostPage> posts) {
        List<PostTagArchivePage> tagArchivePages = new ArrayList<>();

        int pages = (int) Math.ceil(posts.size() / pagination.pageSize);

        for (int i = 0; i <= pages; i++) {
            String pageName = "tags/" + tag + "/" + (i + 1) + ".html";
            OrchidReference pageRef = new OrchidReference(context, pageName);

            if(i == 0) {
                pageRef.setTitle(Clog.format("Posts tagged '#{$1}'", StringUtils.capitalize(tag)));
            }
            else {
                pageRef.setTitle(Clog.format("Posts tagged '#{$1}' (Page #{$2})", StringUtils.capitalize(tag), (i + 1)));
            }

            PostTagArchivePage page = new PostTagArchivePage(new StringResource("", pageRef));
            page.setPostList(posts.subList((i * pagination.pageSize), Math.min(((i+1) * pagination.pageSize), posts.size())));
            page.setTag(tag);
            tagArchivePages.add(page);
        }

        int i = 0;
        for (PostTagArchivePage post : tagArchivePages) {
            if (next(posts, i) != null) { post.setNext(next(tagArchivePages, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(tagArchivePages, i)); }
            i++;
        }

        return tagArchivePages;
    }

    private void tagPosts(List<PostPage> posts) {
        for (PostPage post : posts) {
            if(!EdenUtils.isEmpty(post.getTags())) {
                for (String tag : post.getTags()) {
                    postsModel.tagPost(tag, post);
                }
            }
        }
    }
}
