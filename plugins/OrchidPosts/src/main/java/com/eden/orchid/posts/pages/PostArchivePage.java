package com.eden.orchid.posts.pages;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostArchivePage extends OrchidPage {

    protected List<PostPage> postList;
    protected String category;

    public PostArchivePage(OrchidResource resource) {
        super(resource, "postArchive");
    }

    public List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();

        for(PostPage post : postList) {
            if(!years.contains(post.getYear())) {
                years.add(post.getYear());
            }
        }

        years.sort(Collections.reverseOrder());
        return years;
    }

    public List<Integer> getMonths(int year) {
        List<Integer> months = new ArrayList<>();

        for(PostPage post : postsInYear(year)) {
            if(!months.contains(post.getMonth())) {
                months.add(post.getMonth());
            }
        }

        months.sort(null);
        return months;
    }

    public List<PostPage> postsInYear(int year) {
        return postList.stream()
                       .filter(postPage -> postPage.getYear() == year)
                       .collect(Collectors.toList());
    }

    public List<PostPage> postsInMonth(int year, int month) {
        return postList.stream()
                       .filter(postPage -> ((postPage.getYear() == year) && postPage.getMonth() == month))
                       .collect(Collectors.toList());
    }

    public String getMonthName(int month) {
        return Month.of(month).toString();
    }
}
