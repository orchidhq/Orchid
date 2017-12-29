package com.eden.orchid.posts;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.annotations.Option;
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
import java.util.stream.Stream;

@Singleton
@Description("Share your thoughts and interests with blog posts and archives.")
public class PostsGenerator extends OrchidGenerator implements OptionsHolder {

    public static final Pattern pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)");
    private PostsPermalinkStrategy permalinkStrategy;

    private PostsModel postsModel;

    @Option
    @StringDefault(":category/:year/:month/:day/:slug")
    public String permalink;

    @Option
    @StringDefault(":category/archive/:archiveIndex")
    public String archivePermalink;

    @Option
    @StringDefault("tags/:tag/:archiveIndex")
    public String tagArchivePermalink;

    @Option
    @StringDefault("<!--more-->")
    public String excerptSeparator;

    @Option
    @ListClass(Author.class)
    public List<Author> authors;

    @Option("categories")
    public String[] categoryNames;

    @Option("pagination")
    public PostsPaginator defaultPagination;

    @Option
    public String disqusShortname;

    @Option("baseDir")
    @StringDefault("posts")
    public String postsBaseDir;

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
            List<PostArchivePage> archive = buildArchive(null, posts, defaultPagination);
            postsModel.getCategories().put(null, new EdenPair<>(posts, archive));
            tagPosts(posts);
        }
        else {
            for (String category : categoryNames) {
                List<PostPage> posts = getPostsList(category);
                List<PostArchivePage> archive = buildArchive(category, posts, defaultPagination);
                postsModel.getCategories().put(category, new EdenPair<>(posts, archive));
                tagPosts(posts);
            }
        }

        for (String tag : postsModel.getTagNames()) {
            List<PostPage> posts = postsModel.getPostsTagged(tag);
            List<PostTagArchivePage> archive = buildTagArchive(tag, posts, defaultPagination);
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
    public void startGeneration(Stream<? extends OrchidPage> posts) {
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
            baseCategoryPath = postsBaseDir;
        }
        else {
            baseCategoryPath = postsBaseDir + "/" + category;
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

    private List<PostArchivePage> buildArchive(String category, List<PostPage> posts, PostsPaginator paginator) {
        List<PostArchivePage> archivePages = new ArrayList<>();

        int pages = (int) Math.ceil(posts.size() / paginator.pageSize);

        for (int i = 0; i <= pages; i++) {
            List<PostPage> postPageList = posts.subList((i * paginator.pageSize), Math.min(((i+1) * paginator.pageSize), posts.size()));

            if(postPageList.size() > 0) {
                OrchidReference pageRef = new OrchidReference(context, "archive.html");

                if (i == 0) {
                    if (!EdenUtils.isEmpty(category)) {
                        pageRef.setTitle(StringUtils.capitalize(category));
                    }
                    else {
                        pageRef.setTitle("Blog Archive");
                    }
                }
                else {
                    if (!EdenUtils.isEmpty(category)) {
                        pageRef.setTitle(StringUtils.capitalize(category) + " Archive (Page " + (i + 1) + ")");
                    }
                    else {
                        pageRef.setTitle("Blog Archive (Page " + (i + 1) + ")");
                    }
                }

                String permalink = (!EdenUtils.isEmpty(paginator.permalink)) ? paginator.permalink : archivePermalink;

                PostArchivePage page = new PostArchivePage(new StringResource("", pageRef), i + 1, permalink);
                page.setPostList(postPageList);

                if (!EdenUtils.isEmpty(category)) {
                    page.setCategory(category);
                }

                permalinkStrategy.applyPermalink(page);

                archivePages.add(page);
            }
        }

        int i = 0;
        for (PostArchivePage post : archivePages) {
            if (next(posts, i) != null) { post.setNext(next(archivePages, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(archivePages, i)); }
            i++;
        }

        return archivePages;
    }

    private List<PostTagArchivePage> buildTagArchive(String tag, List<PostPage> posts, PostsPaginator paginator) {
        List<PostTagArchivePage> tagArchivePages = new ArrayList<>();

        int pages = (int) Math.ceil(posts.size() / paginator.pageSize);

        for (int i = 0; i <= pages; i++) {
            List<PostPage> postPageList = posts.subList((i * paginator.pageSize), Math.min(((i+1) * paginator.pageSize), posts.size()));
            if(postPageList.size() > 0) {
                OrchidReference pageRef = new OrchidReference(context, "tag.html");

                if (i == 0) {
                    pageRef.setTitle(Clog.format("Posts tagged '#{$1}'", StringUtils.capitalize(tag)));
                }
                else {
                    pageRef.setTitle(Clog.format("Posts tagged '#{$1}' (Page #{$2})", StringUtils.capitalize(tag), (i + 1)));
                }

                String permalink = (!EdenUtils.isEmpty(paginator.permalink)) ? paginator.permalink : tagArchivePermalink;

                PostTagArchivePage page = new PostTagArchivePage(new StringResource("", pageRef), i + 1, permalink);
                page.setPostList(postPageList);
                page.setTag(tag);

                permalinkStrategy.applyPermalink(page);

                tagArchivePages.add(page);
            }
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
