package com.eden.orchid.posts;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class PostsGenerator extends OrchidGenerator {

    private OrchidResources resources;
    public static final Pattern pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)");
    private PostsPermalinkStrategy permalinkStrategy;
    private PostsExcerptStrategy excerptStrategy;

    @Inject
    public PostsGenerator(OrchidContext context,
                          OrchidResources resources,
                          PostsPermalinkStrategy permalinkStrategy,
                          PostsExcerptStrategy excerptStrategy) {
        super(context);
        this.resources = resources;
        this.permalinkStrategy = permalinkStrategy;
        this.excerptStrategy = excerptStrategy;
        setPriority(700);
    }

    @Override
    public String getKey() {
        return "posts";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {

        // Map category names to pairs of PostPages and PostArchivePages
        Map<String, EdenPair<List<PostPage>, List<PostArchivePage>>> categories = new HashMap<>();

        List<String> categoryNames = new ArrayList<>();
        if(OrchidUtils.elementIsArray(context.query("options.posts.categories"))) {
            JSONArray categoriesArray = (JSONArray) context.query("options.posts.categories").getElement();

            for (int i = 0; i < categoriesArray.length(); i++) {
                if(!EdenUtils.isEmpty(categoriesArray.getString(i))) {
                    categoryNames.add(categoriesArray.getString(i));
                }
            }
        }
        else {
            categoryNames.add(null);
        }

        for (String category : categoryNames) {
            List<PostPage> posts = getPostsList(category);
            List<PostArchivePage> archive = buildArchive(category, posts);
            categories.put(category, new EdenPair<>(posts, archive));
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
        posts.stream().forEach(OrchidPage::renderTemplate);
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
            Matcher matcher = pageTitleRegex.matcher(entry.getReference().getFileName());

            if (matcher.matches()) {
                JSONObject pageData =
                        (OrchidUtils.elementIsObject(entry.getEmbeddedData()))
                                ? (JSONObject) entry.getEmbeddedData().getElement()
                                : new JSONObject();

                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int day = Integer.parseInt(matcher.group(3));

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                String title = matcher.group(4);

                if (!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                    title = entry.queryEmbeddedData("title").toString();
                }
                else {
                    title = Arrays.stream(title.split("-"))
                                  .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                                  .collect(Collectors.joining(" "));
                }

                if (entry.queryEmbeddedData("date") != null) {
                    String date = entry.queryEmbeddedData("date").toString();

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
                        Date dateTime = formatter.parse(date);

                        calendar = Calendar.getInstance();
                        calendar.setTime(dateTime);

                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.YEAR);
                        day = calendar.get(Calendar.YEAR);

                        hour = calendar.get(Calendar.HOUR_OF_DAY);
                        minute = calendar.get(Calendar.MINUTE);
                        second = calendar.get(Calendar.SECOND);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                pageData.put("year", year);
                pageData.put("month", month);
                pageData.put("day", day);

                pageData.put("hour", hour);
                pageData.put("minute", minute);
                pageData.put("second", second);

                if(EdenUtils.isEmpty(category)) {
                    entry.getReference().stripFromPath("blog");
                }
                else {
                    pageData.put("category", category);
                    entry.getReference().stripFromPath("blog/" + category);
                }

                pageData.put("title", title);

                setPermalink(entry.getReference(), pageData);

                PostPage post = new PostPage(entry);
                post.setType("post");
                post.setData(pageData);

                String excerpt = excerptStrategy.getExcerpt(post);

                post.getData().put("excerpt", excerpt);

                posts.add(post);
            }
        }

        posts.sort((o1, o2) -> {
            String[] criteria = new String[]{"year", "month", "day", "hour", "minute", "second"};

            for (String item : criteria) {
                if (o1.getData().has(item) && o1.getData().has(item)) {
                    int result = o2.getData().optInt(item) - o1.getData().optInt(item);

                    if (result != 0) {
                        return result;
                    }
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

        int pageSize = 4;
        int pages = (int) Math.ceil(posts.size() / pageSize);

        for (int i = 0; i <= pages; i++) {
            String pageName = (!EdenUtils.isEmpty(category))
                ? "archive/" + category + "/" + (i + 1) + ".html"
                : "archive/" + (i + 1) + ".html";

            OrchidReference pageRef = new OrchidReference(context, pageName);
            pageRef.setUsePrettyUrl(true);
            pageRef.setTitle("Archives (Page " + (i + 1) + ")");

            PostArchivePage page = new PostArchivePage(new StringResource("", pageRef));
            page.setPage(i + 1);
            page.setPageSize(pageSize);
            page.setTotalPages(posts.size());

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

    private void setPermalink(OrchidReference reference, JSONObject pageData) {
        String permalink = permalinkStrategy.getPermalinkTemplate(pageData);

        String[] pieces = permalink.split("/");

        String resultingUrl = permalinkStrategy.applyPermalinkTemplate(permalink, pageData);
        String title = permalinkStrategy.applyPermalinkTemplatePiece(pieces[pieces.length - 1], pageData);

        reference.setTitle(pageData.getString("title"));
        reference.setFileName(title);
        reference.setUsePrettyUrl(true);
        reference.setPath(OrchidUtils.normalizePath(resultingUrl));
    }
}
