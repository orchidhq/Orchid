package com.eden.orchid.posts;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class PostsGenerator extends OrchidGenerator {

    private OrchidResources resources;
    public static final Pattern pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)");

    @Inject
    public PostsGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
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

                entry.getReference().setBasePath("blog");
                entry.getReference().setPath(Clog.format("#{$1}/#{$2}/#{$3}", year, month, day));
                entry.getReference().setTitle(title);
                entry.getReference().setFileName(title.replaceAll(" ", "-").toLowerCase());

                entry.getReference().setUsePrettyUrl(true);

                OrchidPage post = new OrchidPage(entry);
                post.setType("post");
                post.setData(pageData);

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
        for (OrchidPage post : posts) {
            if (next(posts, i) != null) { post.setNext(next(posts, i)); }
            if (previous(posts, i) != null) { post.setPrevious(previous(posts, i)); }
            i++;
        }

        return posts;
    }

    @Override
    public void startGeneration(List<OrchidPage> posts) {
        for (OrchidPage post : posts) {
            post.renderTemplate();
        }
    }

    public OrchidPage previous(List<OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i != 0) {
                return posts.get(i - 1);
            }
        }

        return null;
    }

    public OrchidPage next(List<OrchidPage> posts, int i) {
        if (posts.size() > 1) {
            if (i < posts.size() - 1) {
                return posts.get(i + 1);
            }
        }

        return null;
    }
}
